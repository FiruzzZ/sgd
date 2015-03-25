package sgd.jpa.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.*;
import org.apache.log4j.*;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.exceptions.DatabaseException;
import sgd.controller.exception.DatabaseErrorException;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public abstract class DAO implements Runnable {

    private static EntityManagerFactory emf;
    /**
     * Este EntityManager, es una Transaction iniciada. <code>em.getTransaction().begin())</code> NO
     * SE COMMITEA NUNCA! Es usado exclusivamente para conexiones JDBC, no se limpia (.clear()) ni
     * se cierra (.close())
     */
    private static EntityManager entityManagerForJDBC;
    private static Connection connection;
    private static int instanceOfJDBCCreated = 0;
    private static Properties properties = null;
    private static int entityManagerInstanceRequested = 0;
    public static final Logger LOG = Logger.getRootLogger();

    private DAO() {
        //singleton..
    }

    public static void setProperties(Properties p) {
        properties = p;
        if (properties == null) {
            throw new IllegalArgumentException("Archivo de configuración de conexión no válido.\nNull Properties");
        }
        if (properties.isEmpty()) {
            throw new IllegalArgumentException("Archivo de configuración de conexión no válido.\nEmpty Properties");
        }
        if (properties.getProperty("database") == null
                || properties.getProperty("port") == null
                || properties.getProperty("server") == null) {
            throw new IllegalArgumentException("Archivo de configuración de conexión no válido");
        }
        DAO.properties = p;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        //singleton..
        throw new CloneNotSupportedException("This Object can not be cloned, because it's a Singleton Design Class!!! [666]");
    }

    public static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName, Map<?, ?> properties) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        return emf;
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            String persistenceUnitName = "SGDPU";
            Logger.getLogger(DAO.class).trace("Initializing EntityManagerFactory= " + persistenceUnitName);
            String server, port, database;
            server = properties.getProperty("server");
            port = properties.getProperty("port");
            database = properties.getProperty("database");
            if (properties.getProperty("create-tables", "false").equals("true")) {
                properties.setProperty("eclipselink.ddl-generation", "create-tables");
            }
            properties.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://" + server + ":" + port + "/" + database);
            emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
            getJDBCConnection();
        }
        EntityManager em = emf.createEntityManager();
        entityManagerInstanceRequested++;
        Logger.getLogger(DAO.class).trace("EntityManagerInstancesRequested=" + entityManagerInstanceRequested);
        return em;
    }

    static EntityManagerFactory getEntityManagerFactory(String string) {
        return Persistence.createEntityManagerFactory(string);
    }

    public static void closeAllConnections() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class).error(ex.getMessage(), ex);
        }
        if (entityManagerForJDBC != null && entityManagerForJDBC.isOpen()) {
            Logger.getLogger(DAO.class).trace("Closing EntityManager for JDBC conn..");
            entityManagerForJDBC.close();

        }
        if (emf != null && emf.isOpen()) {
            Logger.getLogger(DAO.class).trace("Closing EntityManagerFactory..");
            emf.close();
        }
    }

    /**
     * Devuelve un {@link java.sql.Connection} Este método leave a
     * EntityManager.getTransaction.begin() opened! Which must be closed with
     * {@code closeEntityManager()} manually when the returned Connection oebject will no longer be
     * used
     *
     * @return
     */
    public static Connection getJDBCConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instanceOfJDBCCreated++;
                Logger.getLogger(DAO.class).log(Level.TRACE, "Creating a new JDBC #" + instanceOfJDBCCreated);
                entityManagerForJDBC = emf.createEntityManager();
                entityManagerForJDBC.getTransaction().begin();

                //JPA 1.0
//                UnitOfWorkImpl unitOfWorkImpl = (UnitOfWorkImpl) ((EntityManagerImpl) entityManagerForJDBC.getDelegate()).getActiveSession();
//                unitOfWorkImpl.beginEarlyTransaction();
//                connection = unitOfWorkImpl.getAccessor().getConnection();
                //JPA 2.0
                connection = entityManagerForJDBC.unwrap(java.sql.Connection.class);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class).log(Level.ERROR, ex.getMessage(), ex);
        }
        return connection;
    }

    static void create(Object o) throws Exception {
        EntityManager em = null;
        em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Como no se puede borrar una detached entity,
     *
     * @param classType
     * @param id can't be nul!!
     */
    static void remove(Class classType, Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("id can not be NULL!!");
        }
        EntityManager em = null;
        em = getEntityManager();
        try {
            em.getTransaction().begin();
            Object o = em.find(classType, id);
            em.remove(o);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Merge the state of the given entity into the current persistence context. If any exception
     * occurs, a rollback action on the current transaction is launched.
     *
     * @param o entity instance
     * @return the managed instance that the state was merged to or <code>null</code> if a exception
     * occurs
     */
    static <T extends Object> T doMerge(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T merge = em.merge(o);
            em.getTransaction().commit();
            return merge;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Logger.getLogger(DAO.class).log(Level.ERROR, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    static Object getNativeQuerySingleResult(String sqlString, Class<?> resultClass) throws DatabaseErrorException {
        EntityManager em = getEntityManager();
        try {
            return em.createNativeQuery(sqlString, resultClass).getSingleResult();
        } catch (DatabaseException e) {
            Logger.getLogger(DAO.class).log(Level.FATAL, e);
            throw new DatabaseErrorException();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene una collection de objetos
     *
     * @param sqlString a native SQL statement.
     * @param resultClass the class of the returning List. If is NULL, will be a untyped List.
     * @return a list...
     * @throws DatabaseErrorException
     */
    static List<?> getNativeQueryResultList(String sqlString, Class<?> resultClass) throws DatabaseErrorException {
        EntityManager em = getEntityManager();
        try {
            List l;
            if (resultClass != null) {
                l = em.createNativeQuery(sqlString, resultClass).getResultList();
            } else {
                l = em.createNativeQuery(sqlString).getResultList();
            }
            return l;
        } catch (DatabaseException e) {
            throw new DatabaseErrorException();
        } finally {
            em.close();
        }
    }

    static List<?> getNativeQueryResultList(String sqlString, String resultSetMapping) throws DatabaseErrorException {
        EntityManager em = getEntityManager();
        try {
            return em.createNativeQuery(sqlString, resultSetMapping).setHint(QueryHints.REFRESH, true).getResultList();
        } catch (DatabaseException e) {
            throw new DatabaseErrorException(e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca el estado mas actualizado del objeto en la base de datos. El objeto debe tener un campo
     * llamado id y ser único (UNIQUE CONSTRAINT) ( <code>object.id</code>)
     *
     * @param object La clase de la cual se buscará la instancia
     * @param id valor único identificador
     * @return Una instancia del tipo <code>object</code>
     */
    public static Object findEntity(Class object, Integer id) {
        if (object == null) {
            throw new IllegalArgumentException("El parámetro object can not be NULL");
        }
        if (id == null) {
            throw new IllegalArgumentException("El parámetro id can not be NULL");
        }
        try {
            return getEntityManager().createQuery("SELECT o FROM " + object.getSimpleName() + " o WHERE o.id=" + id).setHint(QueryHints.REFRESH, true).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (NonUniqueResultException ex) {
            throw new RuntimeException("More than one entity " + object.getSimpleName() + " was found!!, id is not a UNIQUE identifier field");
        }
    }

    /**
     * Retorna una collections de objetos object is renamed to o (
     * <code> SELECT o FROM object o</code>).
     *
     * @param object Class type of the object to find and return.
     * @param conditions a String with filters to obtain the collections entities. Example
     * <code>conditions = "o.id > 777 AND o.aField != null"</code>, and this will be contated to
     * <code>"WHERE " + conditions</code>. Must be null is there is no conditions.
     * @return a List of object
     */
    static List<?> findEntities(Class<?> object, String conditions) {
        if (object == null) {
            throw new IllegalArgumentException("El parámetro object can not be NULL");
        }
        conditions = conditions == null ? "" : "WHERE " + conditions;
        return getEntityManager().createQuery("SELECT o FROM " + object.getSimpleName() + " o " + conditions).setHint(QueryHints.REFRESH, true).getResultList();
    }

    /**
     * Crea todos los datos que el sistema necesita inicialmente:
     * <br>*Contribuyentes <br>*Usuario: admin pws: adminadmin (permisos full)<br>Sectores
     * <br>*ya vemos que mas..
     */
    public static void setDefaultData() {
        System.out.println("--SETTING DEFAULT DATA=" + properties.getProperty("populate"));
        if (properties.getProperty("populate", "false").equalsIgnoreCase("true")) {
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                for (SectorUI sectorUI : SectorUI.values()) {
                    Sector found = em.find(Sector.class, sectorUI.getCode());
                    if (found == null) {
                        Sector s = new Sector();
                        s.setId(sectorUI.getCode());
                        s.setNombre(sectorUI.getNombre());
                        s.setTipoDocumentos(new ArrayList<TipoDocumento>());
                        LOG.info("new SectorUI:" + sectorUI);
                        em.persist(s);
                    }
                }
                InstitucionJPAController ic = new InstitucionJPAController();
                if (ic.count() == 0) {
                    Institucion i;
                    i = new Institucion(1, "SINDICATO");
                    LOG.info("creando default institución..");
                    ic.create(i);
                }
                // <editor-fold defaultstate="collapsed" desc="Creación de Usuario: admin Pws: admin">
                Usuario defaultUser = em.find(Usuario.class, 1);
                if (defaultUser == null) {
                    defaultUser = new Usuario();
                    defaultUser.setId(1);
                    defaultUser.setBlocked(false);
                    defaultUser.setNombre("admin");
                    defaultUser.setPwd("admin");
                    defaultUser.setUsuarioSectores(new ArrayList<UsuarioSector>());
                    //asigna permiso a todos los sectores de todas las instituciones
                    for (Institucion institucion : ic.findAll()) {
                        for (SectorUI sectorUI : SectorUI.values()) {
                            Sector s = em.find(Sector.class, sectorUI.getCode());
                            UsuarioSector us = new UsuarioSector(null, defaultUser, institucion, s, 2, true, true);
                            defaultUser.getUsuarioSectores().add(us);
                        }
                    }
                    em.persist(defaultUser);
                } else {
                    List<SectorUI> sectores = Arrays.asList(SectorUI.values());
                    List<SectorUI> yaAsignados = new ArrayList<>(sectores.size());
                    for (UsuarioSector usuarioSector : defaultUser.getUsuarioSectores()) {
                        SectorUI sector = usuarioSector.getSector().getSectorUI();
                        if (!yaAsignados.contains(sector)) {
                            yaAsignados.add(sector);
                        }
                    }
                    for (SectorUI sectorUI : sectores) {
                        if (!yaAsignados.contains(sectorUI)) {
                            System.out.println("adding Sector a Usuario: " + sectorUI);
                            for (Institucion institucion : ic.findAll()) {
                                //agrega el permiso del sector "que se haya agregado"
                                Sector s = em.find(Sector.class, sectorUI.getCode());
                                UsuarioSector uss = new UsuarioSector(null, defaultUser, institucion, s, 2, true, true);
                                defaultUser.getUsuarioSectores().add(uss);
                            }
                        }
                    }
                    em.merge(defaultUser);
                }
                // </editor-fold>
                em.getTransaction().commit();
            } catch (Exception ex) {
                if (em != null && em.getTransaction() != null) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                }
                Logger.getLogger(DAO.class.getName()).log(Level.ERROR, null, ex);
            } finally {
                if (em != null) {
                    em.close();
                }
            }
            System.out.println("finished setDefaultData()..");
        }

    }

    /**
     * Ver {@link EntityManager#createQuery(java.lang.String)}
     *
     * @param query a Java Persistence query string
     * @param withRefreshQueryHint
     * @return
     */
    static Query createQuery(String query, boolean withRefreshQueryHint) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery(query);
        if (withRefreshQueryHint) {
            q.setHint(QueryHints.REFRESH, true);
        }
        return q;
    }

    static Query createNativeQuery(String query, Class aClass, boolean withRefreshQueryHint) {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery(query, aClass);
        if (withRefreshQueryHint) {
            q.setHint(QueryHints.REFRESH, true);
        }
        return q;
    }

    public <T extends Object> List<T> get(T object, String condicion) {
        List<T> l = new ArrayList<T>();
        return l;

    }
}
