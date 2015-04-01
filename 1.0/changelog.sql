--20150327
ALTER TABLE auditoriamedicadetalle ADD COLUMN nombre character varying(50);
ALTER TABLE auditoriamedicadetalle ADD COLUMN apellido character varying(50);

--20150326
ALTER TABLE auditoriamedicadetalle ADD COLUMN delegacion character varying(50);

--20150317
CREATE TABLE auditoriamedica(
  id serial NOT NULL,
  baja boolean,
  barcode character varying(50) NOT NULL,
  codigo integer NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(),
  observacion character varying(200),
  institucion_id integer,
  recibo_id integer,
  sector_id integer,
  usuario_id integer,
  CONSTRAINT auditoriamedica_pkey PRIMARY KEY (id),
  CONSTRAINT fk_auditoriamedica_institucion_id FOREIGN KEY (institucion_id) REFERENCES institucion (id),
  CONSTRAINT fk_auditoriamedica_recibo_id FOREIGN KEY (recibo_id) REFERENCES recibo (id),
  CONSTRAINT fk_auditoriamedica_sector_id FOREIGN KEY (sector_id) REFERENCES sector (id),
  CONSTRAINT fk_auditoriamedica_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario (id),
  CONSTRAINT auditoriamedica_barcode_key UNIQUE (barcode),
  CONSTRAINT unq_auditoriamedica_0 UNIQUE (institucion_id, sector_id, codigo)
);
CREATE TABLE auditoriamedicadetalle (
  id serial NOT NULL,
  documentofecha date,
  numeroafiliado bigint,
  observacion character varying(255),
  numerodocumento bigint,
  orderindex integer NOT NULL,
  auditoriamedica_id integer NOT NULL,
  subtipodocumento_id integer,
  tipodocumento_id integer NOT NULL,
  CONSTRAINT auditoriamedicadetalle_pkey PRIMARY KEY (id),
  CONSTRAINT fk_auditoriamedicadetalle_auditoriamedica_id FOREIGN KEY (auditoriamedica_id) REFERENCES auditoriamedica (id),
  CONSTRAINT fk_auditoriamedicadetalle_subtipodocumento_id FOREIGN KEY (subtipodocumento_id) REFERENCES subtipodocumento (id),
  CONSTRAINT fk_auditoriamedicadetalle_tipodocumento_id FOREIGN KEY (tipodocumento_id) REFERENCES tipodocumento (id) 
);
CREATE TABLE auditoriamedicaprecinto (
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  auditoriamedica_id integer NOT NULL,
  CONSTRAINT auditoriamedicaprecinto_pkey PRIMARY KEY (id),
  CONSTRAINT fk_auditoriamedicaprecinto_auditoriamedica_id FOREIGN KEY (auditoriamedica_id) REFERENCES auditoriamedica (id) 
);

--20141128
alter table afiliacion drop column cerrada;
alter table ape drop column cerrada;
alter table auditoria drop column cerrada;
alter table contable drop column cerrada;
alter table facturacion drop column cerrada;
alter table gremiales drop column cerrada;
alter table psicofisico drop column cerrada;
alter table cronico drop column cerrada;
alter table discapacidad drop column cerrada;
--20141125
insert into usuariosector values
(default, TRUE,TRUE,current_timestamp,2,1,109,1),
(default, TRUE,TRUE,current_timestamp,2,2,109,1),
(default, TRUE,TRUE,current_timestamp,2,3,109,1);

CREATE TABLE discapacidad (
  id serial NOT NULL,
  baja boolean NOT NULL DEFAULT false,
  barcode character varying(50) NOT NULL,
  codigo integer NOT NULL,
  cerrada boolean NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(),
  observacion character varying(200),
  institucion_id integer NOT NULL,
  sector_id integer NOT NULL,
  usuario_id integer NOT NULL,
  recibo_id integer,
  CONSTRAINT discapacidad_pkey PRIMARY KEY (id ),
  CONSTRAINT discapacidad_institucion_id_fkey FOREIGN KEY (institucion_id) REFERENCES institucion (id),
  CONSTRAINT discapacidad_recibo_id_fkey FOREIGN KEY (recibo_id) REFERENCES recibo (id),
  CONSTRAINT discapacidad_sector_id_fkey FOREIGN KEY (sector_id) REFERENCES sector (id),
  CONSTRAINT discapacidad_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES usuario (id),
  CONSTRAINT discapacidad_barcode_key UNIQUE (barcode ),
  CONSTRAINT discapacidad_institucion_id_sector_id_codigo_key UNIQUE (institucion_id , sector_id , codigo )
);
CREATE TABLE discapacidaddetalle (
  id serial NOT NULL,
  discapacidad_id integer NOT NULL,
  tipodocumento_id integer NOT NULL,
  subtipodocumento_id integer,
  documentofecha date,
  documentonumero bigint,
  periodo_year numeric(4,0),
  apellido character varying(50),
  nombre character varying(50),
  observacion character varying(255),
  orderindex integer NOT NULL,
  CONSTRAINT discapacidaddetalle_pkey PRIMARY KEY (id ),
  CONSTRAINT discapacidaddetalle_discapacidad_id_fkey FOREIGN KEY (discapacidad_id) REFERENCES discapacidad (id),
  CONSTRAINT discapacidaddetalle_subtipodocumento_id_fkey FOREIGN KEY (subtipodocumento_id) REFERENCES subtipodocumento (id),
  CONSTRAINT discapacidaddetalle_tipodocumento_id_fkey FOREIGN KEY (tipodocumento_id) REFERENCES tipodocumento (id)
);
CREATE TABLE discapacidadprecinto(
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  discapacidad_id integer NOT NULL,
   PRIMARY KEY (id ),
   FOREIGN KEY (discapacidad_id) REFERENCES discapacidad(id)
);
--20141121
CREATE TABLE solicitud (
  id serial NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(),
  numero integer NOT NULL,
  sector_id integer not null,
  usuario_id integer not null,
  CONSTRAINT solicitud_pkey PRIMARY KEY (id),
  CONSTRAINT fk_solicitud_sector_id FOREIGN KEY (sector_id) REFERENCES sector (id),
  CONSTRAINT fk_solicitud_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
CREATE TABLE solicituddetalle (
  id serial NOT NULL,
  archivoid integer NOT NULL,
  barcode character varying(255) NOT NULL,
  orderindex integer NOT NULL,
  solicitud_id integer NOT NULL,
  CONSTRAINT solicituddetalle_pkey PRIMARY KEY (id),
  CONSTRAINT fk_solicituddetalle_solicitud_id FOREIGN KEY (solicitud_id) REFERENCES solicitud (id)
);
--20141120
ALTER TABLE cronicodetalle DROP CONSTRAINT cronicodetalle_archivo_id_fkey;
ALTER TABLE cronicodetalle RENAME archivo_id  TO cronico_id;
ALTER TABLE cronicodetalle ADD FOREIGN KEY (cronico_id) REFERENCES cronico (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE cronicoprecinto DROP CONSTRAINT cronicoprecinto_archivo_id_fkey;
ALTER TABLE cronicoprecinto RENAME archivo_id  TO cronico_id;
ALTER TABLE cronicoprecinto ADD FOREIGN KEY (cronico_id) REFERENCES cronico (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE gremialesdetalle DROP CONSTRAINT gremialesdetalle_archivo_id_fkey;
ALTER TABLE gremialesdetalle RENAME archivo_id  TO gremiales_id;
ALTER TABLE gremialesdetalle ADD FOREIGN KEY (gremiales_id) REFERENCES gremiales (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE gremialesprecinto DROP CONSTRAINT gremialesprecinto_archivo_id_fkey;
ALTER TABLE gremialesprecinto RENAME archivo_id  TO gremiales_id;
ALTER TABLE gremialesprecinto ADD FOREIGN KEY (gremiales_id) REFERENCES cronico (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
--20140902
CREATE TABLE cronico (
  id serial NOT NULL, 
  baja boolean NOT NULL DEFAULT false, 
  barcode character varying(50) NOT NULL, 
  codigo integer NOT NULL, 
  cerrada boolean NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(), 
  observacion character varying(200), 
  institucion_id integer NOT NULL, 
  sector_id integer NOT NULL, 
  usuario_id integer NOT NULL, 
  recibo_id integer, 
  PRIMARY KEY (id ),
  FOREIGN KEY (institucion_id)      REFERENCES institucion (id),
  FOREIGN KEY (recibo_id)      REFERENCES recibo (id),
  FOREIGN KEY (sector_id)      REFERENCES sector (id),
  FOREIGN KEY (usuario_id)      REFERENCES usuario (id),
  UNIQUE (barcode ),
  UNIQUE (institucion_id , sector_id , codigo )
);
CREATE TABLE cronicodetalle (
  id serial NOT NULL,
  archivo_id integer NOT NULL,
  tipodocumento_id integer NOT NULL,
  subtipodocumento_id integer,
  documentofecha date,
  documentonumero bigint,
  numeroafiliado integer not null,
  numerofamiliar integer not null default 0,
  numeroformulario integer,
  observacion character varying(255),
  orderindex integer NOT NULL,
  PRIMARY KEY (id ),
  FOREIGN KEY (archivo_id)      REFERENCES cronico (id),
  FOREIGN KEY (subtipodocumento_id)      REFERENCES subtipodocumento (id),
  FOREIGN KEY (tipodocumento_id)      REFERENCES tipodocumento (id)
);

CREATE TABLE cronicoprecinto(
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  archivo_id integer NOT NULL,
   PRIMARY KEY (id ),
   FOREIGN KEY (archivo_id)      REFERENCES cronico (id)
);
--20140630
CREATE TABLE gremiales (
  id serial NOT NULL, 
  baja boolean NOT NULL DEFAULT false, 
  barcode character varying(50) NOT NULL, 
  codigo integer NOT NULL, 
  cerrada boolean NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(), 
  observacion character varying(200), 
  institucion_id integer NOT NULL, 
  sector_id integer NOT NULL, 
  usuario_id integer NOT NULL, 
  recibo_id integer, 
  PRIMARY KEY (id ),
  FOREIGN KEY (institucion_id)      REFERENCES institucion (id),
  FOREIGN KEY (recibo_id)      REFERENCES recibo (id),
  FOREIGN KEY (sector_id)      REFERENCES sector (id),
  FOREIGN KEY (usuario_id)      REFERENCES usuario (id),
  UNIQUE (barcode ),
  UNIQUE (institucion_id , sector_id , codigo )
);
CREATE TABLE gremialesdetalle (
  id serial NOT NULL,
  archivo_id integer NOT NULL,
  tipodocumento_id integer NOT NULL,
  subtipodocumento_id integer,
  documentofecha date,
  documentonumero bigint,
  empresa varchar(150),
  empleado varchar(150),
  observacion character varying(255),
  orderindex integer NOT NULL,
  PRIMARY KEY (id ),
  FOREIGN KEY (archivo_id)      REFERENCES gremiales (id),
  FOREIGN KEY (subtipodocumento_id)      REFERENCES subtipodocumento (id),
  FOREIGN KEY (tipodocumento_id)      REFERENCES tipodocumento (id)
);

CREATE TABLE gremialesprecinto(
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  archivo_id integer NOT NULL,
   PRIMARY KEY (id ),
   FOREIGN KEY (archivo_id)      REFERENCES gremiales (id)
);
--20140522
CREATE TABLE auditoria (
  id serial NOT NULL, 
  baja boolean NOT NULL DEFAULT false, 
  barcode character varying(50) NOT NULL, 
  codigo integer NOT NULL, 
  cerrada boolean NOT NULL,
  creation timestamp with time zone NOT NULL DEFAULT now(), 
  observacion character varying(200), 
  institucion_id integer NOT NULL, 
  sector_id integer NOT NULL, 
  usuario_id integer NOT NULL, 
  recibo_id integer, 
  PRIMARY KEY (id ),
  FOREIGN KEY (institucion_id)      REFERENCES institucion (id),
  FOREIGN KEY (recibo_id)      REFERENCES recibo (id),
  FOREIGN KEY (sector_id)      REFERENCES sector (id),
  FOREIGN KEY (usuario_id)      REFERENCES usuario (id),
  UNIQUE (barcode ),
  UNIQUE (institucion_id , sector_id , codigo )
);
CREATE TABLE auditoriadetalle (
  id serial NOT NULL,
  auditoria_id integer NOT NULL,
  tipodocumento_id integer NOT NULL,
  subtipodocumento_id integer,
  documentofecha date,
  prestador character varying(50) NOT NULL,
  observacion character varying(255),
  orderindex integer NOT NULL,
  PRIMARY KEY (id ),
  FOREIGN KEY (auditoria_id)      REFERENCES auditoria (id),
  FOREIGN KEY (subtipodocumento_id)      REFERENCES subtipodocumento (id),
  FOREIGN KEY (tipodocumento_id)      REFERENCES tipodocumento (id)
);

CREATE TABLE auditoriaprecinto(
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  auditoria_id integer NOT NULL,
   PRIMARY KEY (id ),
   FOREIGN KEY (auditoria_id)      REFERENCES auditoria (id)
);
--20140404
CREATE TABLE facturacion (
   id serial NOT NULL, 
   baja boolean NOT NULL DEFAULT false, 
   barcode character varying(50) NOT NULL, 
   codigo integer NOT NULL, 
   cerrada boolean NOT NULL,
   creation timestamp with time zone NOT NULL DEFAULT now(), 
   observacion character varying(200), 
   institucion_id integer NOT NULL, 
   sector_id integer NOT NULL, 
   usuario_id integer NOT NULL, 
   recibo_id integer, 
    PRIMARY KEY (id), 
    FOREIGN KEY (institucion_id) REFERENCES institucion (id), 
    FOREIGN KEY (sector_id) REFERENCES sector (id), 
    FOREIGN KEY (usuario_id) REFERENCES usuario (id), 
    FOREIGN KEY (recibo_id) REFERENCES recibo (id), 
    UNIQUE (barcode), 
    UNIQUE (institucion_id, sector_id, codigo)
);
CREATE TABLE facturaciondetalle (
  id serial NOT NULL,
  facturacion_id integer NOT NULL,
  tipodocumento_id integer NOT NULL,
  subtipodocumento_id integer,
  documentofecha date,
  prestador character varying (50),
  observacion character varying(255),
  orderindex integer NOT NULL,
  expediente character varying(20),
  PRIMARY KEY (id),
  FOREIGN KEY (facturacion_id) REFERENCES facturacion (id),
  FOREIGN KEY (subtipodocumento_id) REFERENCES subtipodocumento (id),
  FOREIGN KEY (tipodocumento_id) REFERENCES tipodocumento (id)
); 
CREATE TABLE facturacionprecinto(
  id serial NOT NULL,
  codigo character varying(30) NOT NULL,
  facturacion_id integer NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (facturacion_id) REFERENCES facturacion (id)
);
ALTER TABLE subtipodocumento ADD UNIQUE (nombre, tipodocumento_id);
ALTER TABLE tipodocumento ADD UNIQUE (nombre, sector_id);
INSERT INTO sector VALUES(105, CURRENT_DATE, 'Facturacion');
INSERT INTO usuariosector VALUES 
    (default, true, true, CURRENT_DATE, 2, 1, 105, 1),
    (default, true, true, CURRENT_DATE, 2, 2, 105, 1),
    (default, true, true, CURRENT_DATE, 2, 3, 105, 1);
--20131202
ALTER TABLE apedetalle ADD COLUMN apellido character varying(50);
ALTER TABLE apedetalle ADD COLUMN nombre character varying(50);
--20131122
CREATE ROLE sgd LOGIN;
GRANT ALL ON DATABASE sgd TO sgd;
--20120821
ALTER TABLE afiliaciondetalle ADD COLUMN familiarnumero integer NOT NULL DEFAULT 0;
ALTER TABLE afiliaciondetalle ALTER COLUMN familiarnumero DROP NOT NULL;
