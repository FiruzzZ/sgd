package sgd.jpa.model;

/**
 *
 * @author FiruzzZ
 */
public enum SectorUI {

    USUARIOS(1, "Usuarios"),
    AFILIACION(101, "Afiliacion"),
    APE(102, "APE"),
    CONTABLE(103, "Contable"),
    PSICOFISICO(104, "Psicofisico"),
    FACTURACION(105, "Facturacion"),
    AUDITORIA(106, "Auditoria"),
    GREMIALES(107, "Gremiales"),
    CRONICO(108, "Cronico"),
    DISCAPACIDAD(109, "Discapacidad"),
    AUDITORIAMEDICA(110, "AuditoriaMedica");

    private final Integer code;
    private final String nombre;

    private SectorUI(Integer code, String nombre) {
        this.code = code;
        this.nombre = nombre;
    }

    public Integer getCode() {
        return code;
    }

    public String getNombre() {
        return nombre;
    }

    public static SectorUI getByCode(int code) {
        for (SectorUI sectorUI : values()) {
            if (sectorUI.getCode().equals(code)) {
                return sectorUI;
            }
        }
        throw new IllegalArgumentException("NO EXISTE UN sectorUI.code=" + code);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
