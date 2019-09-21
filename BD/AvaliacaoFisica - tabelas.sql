
CREATE SEQUENCE public."elevacao-seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 28
  CACHE 1;
ALTER TABLE public."elevacao-seq"
  OWNER TO postgres;


CREATE SEQUENCE public."exercicio-seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 40
  CACHE 1;
ALTER TABLE public."exercicio-seq"
  OWNER TO postgres;


CREATE SEQUENCE public."ritmo-detalhado-seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 158
  CACHE 1;
ALTER TABLE public."ritmo-detalhado-seq"
  OWNER TO postgres;


CREATE SEQUENCE public."ritmo-seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 26
  CACHE 1;
ALTER TABLE public."ritmo-seq"
  OWNER TO postgres;



CREATE SEQUENCE public."velocidade-seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 26
  CACHE 1;
ALTER TABLE public."velocidade-seq"
  OWNER TO postgres;



CREATE TABLE public.cliente
(
  email character varying NOT NULL,
  nome character varying,
  sexo character varying,
  peso double precision,
  altura double precision,
  data_de_nascimento bigint,
  CONSTRAINT "cliente_email_PK" PRIMARY KEY (email)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.cliente
  OWNER TO postgres;



CREATE TABLE public.exercicio
(
  email character varying NOT NULL,
  tempo_inicio bigint NOT NULL,
  tempo_fim bigint NOT NULL,
  distancia double precision,
  calorias double precision,
  passos bigint,
  data bigint NOT NULL,
  exercicio character varying,
  codigo bigint NOT NULL DEFAULT nextval('"exercicio-seq"'::regclass),
  duracao bigint,
  CONSTRAINT "exercicio_PK" PRIMARY KEY (email, tempo_inicio, tempo_fim, data),
  CONSTRAINT "cliente_email_FK" FOREIGN KEY (email)
      REFERENCES public.cliente (email) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT "codigo_UC" UNIQUE (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.exercicio
  OWNER TO postgres;



CREATE TABLE public.elevacao
(
  codigo bigint NOT NULL DEFAULT nextval('"elevacao-seq"'::regclass),
  maior_elevacao bigint,
  menor_elevacao bigint,
  CONSTRAINT "codigoElevacao" PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.elevacao
  OWNER TO postgres;



CREATE TABLE public.velocidade
(
  codigo bigint NOT NULL DEFAULT nextval('"velocidade-seq"'::regclass),
  velocidade_maxima double precision,
  velocidade_media double precision,
  CONSTRAINT "codigoVelocidade" PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.velocidade
  OWNER TO postgres;



CREATE TABLE public.ritmo
(
  codigo bigint NOT NULL DEFAULT nextval('"ritmo-seq"'::regclass),
  ritmo_maximo bigint,
  ritmo_medio bigint,
  CONSTRAINT "codigoRitmo" PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ritmo
  OWNER TO postgres;



CREATE TABLE public.ritmo_detalhado
(
  codigo bigint NOT NULL DEFAULT nextval('"ritmo-detalhado-seq"'::regclass),
  quilometro double precision,
  ritmo bigint,
  CONSTRAINT "codigoRitmoDetalhado" PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ritmo_detalhado
  OWNER TO postgres;




CREATE TABLE public.ritmo_ritmo_detalhado
(
  codigo_ritmo bigint NOT NULL,
  codigo_ritmo_detalhado bigint NOT NULL,
  CONSTRAINT "codigoRitmoRitmoDetalhado" PRIMARY KEY (codigo_ritmo, codigo_ritmo_detalhado),
  CONSTRAINT "codigoRitmoDetalhado_FK" FOREIGN KEY (codigo_ritmo_detalhado)
      REFERENCES public.ritmo_detalhado (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT "codigoRitmo_FK" FOREIGN KEY (codigo_ritmo)
      REFERENCES public.ritmo (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ritmo_ritmo_detalhado
  OWNER TO postgres;




CREATE TABLE public.exercicio_detalhado
(
  codigo bigint NOT NULL,
  codigo_velocidade bigint,
  codigo_ritmo bigint,
  codigo_elevacao bigint,
  CONSTRAINT "exercicio_detatlhado_PK" PRIMARY KEY (codigo),
  CONSTRAINT "exercicio_codigo_FK" FOREIGN KEY (codigo)
      REFERENCES public.exercicio (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT "exercicio_elevacao_FK" FOREIGN KEY (codigo_elevacao)
      REFERENCES public.elevacao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "exercicio_ritmo_FK" FOREIGN KEY (codigo_ritmo)
      REFERENCES public.ritmo (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "exercicio_velocidade_FK" FOREIGN KEY (codigo_velocidade)
      REFERENCES public.velocidade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.exercicio_detalhado
  OWNER TO postgres;
