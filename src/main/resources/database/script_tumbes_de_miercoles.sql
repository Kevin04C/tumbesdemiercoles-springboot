--
-- PostgreSQL database dump
--

\restrict D4XnV7NDynAtd48y4uwfgLuounQQYNamOr27fTyujmISBxOWFfb7WSnaRNl6Xa0

-- Dumped from database version 18.3 (Debian 18.3-1.pgdg13+1)
-- Dumped by pg_dump version 18.3 (Debian 18.3-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: fn_audit_logger(); Type: FUNCTION; Schema: public; Owner: user_wqswkhkwi8
--

CREATE FUNCTION public.fn_audit_logger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_old_data JSONB;
    v_new_data JSONB;
    v_key_value TEXT;
BEGIN
    -- Capturamos datos según la operación
    IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        v_new_data := to_jsonb(NEW);
    END IF;
    
    IF (TG_OP = 'DELETE' OR TG_OP = 'UPDATE') THEN
        v_old_data := to_jsonb(OLD);
    END IF;

    -- Extraemos el valor de la columna 'id' (estandarizado en todas las tablas)
    IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        v_key_value := v_new_data->>'id';
    ELSE
        v_key_value := v_old_data->>'id';
    END IF;

    -- Inserciones en la tabla Audit
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO public.audit (table_name, operation, key_value, new_value, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, v_key_value, v_new_data::TEXT, current_user);
        RETURN NEW;
        
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO public.audit (table_name, operation, key_value, old_value, new_value, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, v_key_value, v_old_data::TEXT, v_new_data::TEXT, current_user);
        RETURN NEW;
        
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO public.audit (table_name, operation, key_value, old_value, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, v_key_value, v_old_data::TEXT, current_user);
        RETURN OLD;
    END IF;
    
    RETURN NULL;
END;
$$;


ALTER FUNCTION public.fn_audit_logger() OWNER TO user_wqswkhkwi8;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: audit; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.audit (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    table_name character varying(100) NOT NULL,
    operation character varying(10) NOT NULL,
    key_value character varying(255) NOT NULL,
    old_value text,
    new_value text,
    changed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    changed_by character varying(100),
    status_registry character varying(20),
    status_updated_at timestamp without time zone
);


ALTER TABLE public.audit OWNER TO user_wqswkhkwi8;

--
-- Name: category; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.category (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    description character varying(255),
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone,
    category_id uuid
);


ALTER TABLE public.category OWNER TO user_wqswkhkwi8;

--
-- Name: columnist; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.columnist (
    is_active boolean,
    content text NOT NULL,
    author character varying,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    title character varying,
    headline character varying,
    status_registry character varying,
    status_updated_at timestamp with time zone,
    author_image_url character varying,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE public.columnist OWNER TO user_wqswkhkwi8;

--
-- Name: digital_weekly; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.digital_weekly (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    pdf_url character varying,
    front_page_image_url character varying,
    descripcion character varying NOT NULL,
    is_active boolean,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    status_registry character varying,
    status_updated_at timestamp with time zone,
    is_premium boolean,
    url character varying
);


ALTER TABLE public.digital_weekly OWNER TO user_wqswkhkwi8;

--
-- Name: news; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.news (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    content text NOT NULL,
    is_carousel boolean,
    headline character varying,
    slug character varying NOT NULL,
    is_premium boolean,
    category_id uuid NOT NULL,
    title character varying,
    is_active boolean,
    updated_at character varying,
    created_at timestamp with time zone,
    status_registry character varying,
    status_updated_at timestamp with time zone,
    image_url text,
    is_peru_daily_news boolean,
    is_latest_news boolean
);


ALTER TABLE public.news OWNER TO user_wqswkhkwi8;

--
-- Name: permission; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.permission (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(255),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone
);


ALTER TABLE public.permission OWNER TO user_wqswkhkwi8;

--
-- Name: role; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.role (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(32) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone
);


ALTER TABLE public.role OWNER TO user_wqswkhkwi8;

--
-- Name: role_permission; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.role_permission (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    role_id uuid NOT NULL,
    permission_id uuid NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone
);


ALTER TABLE public.role_permission OWNER TO user_wqswkhkwi8;

--
-- Name: user; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public."user" (
    id uuid DEFAULT gen_random_uuid() CONSTRAINT account_id_not_null NOT NULL,
    first_name character varying(50) CONSTRAINT account_first_name_not_null NOT NULL,
    last_name character varying(50) CONSTRAINT account_last_name_not_null NOT NULL,
    email character varying(100) CONSTRAINT account_email_not_null NOT NULL,
    image_url character varying(255),
    is_email_verified boolean DEFAULT false,
    password_hash character varying(255) CONSTRAINT account_password_hash_not_null NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20),
    status_updated_at timestamp with time zone,
    is_active boolean DEFAULT true
);


ALTER TABLE public."user" OWNER TO user_wqswkhkwi8;

--
-- Name: user_permission; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.user_permission (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    permission_id uuid NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.user_permission OWNER TO user_wqswkhkwi8;

--
-- Name: user_role; Type: TABLE; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TABLE public.user_role (
    id uuid DEFAULT gen_random_uuid() CONSTRAINT account_role_id_not_null NOT NULL,
    user_id uuid CONSTRAINT account_role_user_id_not_null NOT NULL,
    role_id uuid CONSTRAINT account_role_role_id_not_null NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone,
    status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
    status_updated_at timestamp with time zone
);


ALTER TABLE public.user_role OWNER TO user_wqswkhkwi8;

--
-- Name: vw_audit_recent; Type: VIEW; Schema: public; Owner: user_wqswkhkwi8
--

CREATE VIEW public.vw_audit_recent AS
 SELECT id,
    table_name,
    operation,
    key_value,
    old_value,
    new_value,
    changed_at,
    changed_by,
    status_registry,
    status_updated_at
   FROM public.audit
  ORDER BY changed_at DESC;


ALTER VIEW public.vw_audit_recent OWNER TO user_wqswkhkwi8;

--
-- Data for Name: audit; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.audit (id, table_name, operation, key_value, old_value, new_value, changed_at, changed_by, status_registry, status_updated_at) FROM stdin;
528a71b4-0271-4b57-a09f-23bf762cbbfa	permission	INSERT	d8a8f288-69be-4e33-b1cb-1a416dd50b3d	\N	{"id": "d8a8f288-69be-4e33-b1cb-1a416dd50b3d", "name": "CREATE_ROLE", "created_at": "2026-05-16T23:01:07.167585-05:00", "updated_at": "2026-05-16T23:01:07.167585-05:00", "description": "Permite registrar y definir nuevos roles en el sistema", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:01:07.388171	user_wqswkhkwi8	\N	\N
306d452a-f996-4a63-bf7e-8c3a13cce410	permission	INSERT	b910ca79-a1f2-4e97-b1da-d168a0e41715	\N	{"id": "b910ca79-a1f2-4e97-b1da-d168a0e41715", "name": "READ_ROLES", "created_at": "2026-05-16T23:01:20.129934-05:00", "updated_at": "2026-05-16T23:01:20.129934-05:00", "description": "Permite listar y ver el detalle de los roles existentes", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:01:20.353685	user_wqswkhkwi8	\N	\N
80432843-8e7a-46ac-94b2-948ebc6df9c8	permission	INSERT	4fd7819c-9423-4779-ac90-3ecf6d3c5d26	\N	{"id": "4fd7819c-9423-4779-ac90-3ecf6d3c5d26", "name": "UPDATE_ROLE", "created_at": "2026-05-16T23:01:39.12186-05:00", "updated_at": "2026-05-16T23:01:39.12186-05:00", "description": "Permite modificar el nombre o la descripción de un rol existente", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:01:39.346709	user_wqswkhkwi8	\N	\N
54df34a8-f29e-4aca-9b2a-1a2fa66cb6e6	permission	INSERT	e65c3647-fcd1-4b92-9e34-f2cb83f22192	\N	{"id": "e65c3647-fcd1-4b92-9e34-f2cb83f22192", "name": "DELETE_ROLE", "created_at": "2026-05-16T23:01:46.300441-05:00", "updated_at": "2026-05-16T23:01:46.300441-05:00", "description": "Permite eliminar un rol (siempre que no esté asignado a ningún usuario)", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:01:46.52094	user_wqswkhkwi8	\N	\N
16fbcf24-3c1e-499f-821f-cc0d75f59016	user	DELETE	b8f5a740-d793-4e63-9da8-4ef6e2187de3	{"id": "b8f5a740-d793-4e63-9da8-4ef6e2187de3", "email": "gavino_12@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:45:19.60193+00:00", "first_name": "aaaa feliz", "updated_at": "2026-05-12T17:45:19.60193+00:00", "password_hash": "$2a$10$ElEMhwoVAVsQNYXLXjHxe.tUlKvRngv4AAVYVfk7Dlj25IoyAhq7y", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
7c4eb792-b671-49c6-94e4-47cb805b1257	user	DELETE	4397843a-0127-4b74-94c9-b787dcc5cad3	{"id": "4397843a-0127-4b74-94c9-b787dcc5cad3", "email": "gavino_16@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T22:37:05.636642+00:00", "first_name": "ffffff feliz", "updated_at": "2026-05-12T22:37:05.636642+00:00", "password_hash": "$2a$10$Dit7UaxdeZTQrxfP4MP5K.DtNFqHUstm.WHN9ftH5P66kUoEY8Y3K", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
4f956d0c-8d30-46a0-ae88-aaed6fdc2cf1	user	DELETE	1a63a6c0-6207-47c5-88d5-d815bc5a32b5	{"id": "1a63a6c0-6207-47c5-88d5-d815bc5a32b5", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T01:36:27.013318+00:00", "first_name": "chanchito feliz", "updated_at": "2026-05-12T01:36:27.013318+00:00", "password_hash": "$2a$10$6ZZC3pIiH4.lhHsvzvgmCeCwDcpCRNRGLQ1XY826LFSW1iGxubVsG", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 01:38:40.428268	user_wqswkhkwi8	\N	\N
16cee9b4-efc5-4348-9513-584dad71b4fd	user	INSERT	6b28b118-6a66-4bf5-a909-f3fb0510a58f	\N	{"id": "6b28b118-6a66-4bf5-a909-f3fb0510a58f", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-11T20:38:54.030664-05:00", "first_name": "chanchito feliz", "updated_at": "2026-05-11T20:38:54.030664-05:00", "password_hash": "$2a$10$oopHKYHUZIE3a4padhG04OpgthMfsd7OFC07YiofSzBV86lRoNzZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-11 20:38:54.337619	user_wqswkhkwi8	\N	\N
0cba1eb0-e2e7-45d0-b806-da832c7cb024	user	UPDATE	6b28b118-6a66-4bf5-a909-f3fb0510a58f	{"id": "6b28b118-6a66-4bf5-a909-f3fb0510a58f", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-11T20:38:54.030664-05:00", "first_name": "chanchito feliz", "updated_at": "2026-05-11T20:38:54.030664-05:00", "password_hash": "$2a$10$oopHKYHUZIE3a4padhG04OpgthMfsd7OFC07YiofSzBV86lRoNzZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	{"id": "6b28b118-6a66-4bf5-a909-f3fb0510a58f", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-11T20:38:54.030664-05:00", "first_name": "aea manito ", "updated_at": "2026-05-11T20:40:42.682092-05:00", "password_hash": "$2a$10$oopHKYHUZIE3a4padhG04OpgthMfsd7OFC07YiofSzBV86lRoNzZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-11 20:40:42.865358	user_wqswkhkwi8	\N	\N
6e7e62ea-8e47-49f7-942e-4b452dae2883	user	DELETE	6b28b118-6a66-4bf5-a909-f3fb0510a58f	{"id": "6b28b118-6a66-4bf5-a909-f3fb0510a58f", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T01:38:54.030664+00:00", "first_name": "aea manito ", "updated_at": "2026-05-12T01:40:42.682092+00:00", "password_hash": "$2a$10$oopHKYHUZIE3a4padhG04OpgthMfsd7OFC07YiofSzBV86lRoNzZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 04:16:18.861987	user_wqswkhkwi8	\N	\N
f71c5188-46bf-4e76-9f53-3f43245941c2	user	INSERT	721956b8-1ac0-4204-91bc-fff857e55750	\N	{"id": "721956b8-1ac0-4204-91bc-fff857e55750", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-11T23:18:15.923042-05:00", "first_name": "chanchito feliz", "updated_at": "2026-05-11T23:18:15.923042-05:00", "password_hash": "$2a$10$F6YQt/1CofluTrxEwZZ0KekPHWaD79ou/5M88tTa6zp3yTX7rxd..", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-11 23:18:16.114741	user_wqswkhkwi8	\N	\N
28b6e98f-dff7-4f71-b086-3d792f6dad8a	user	INSERT	b8f5a740-d793-4e63-9da8-4ef6e2187de3	\N	{"id": "b8f5a740-d793-4e63-9da8-4ef6e2187de3", "email": "gavino_12@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T12:45:19.60193-05:00", "first_name": "aaaa feliz", "updated_at": "2026-05-12T12:45:19.60193-05:00", "password_hash": "$2a$10$ElEMhwoVAVsQNYXLXjHxe.tUlKvRngv4AAVYVfk7Dlj25IoyAhq7y", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 12:45:21.505514	user_wqswkhkwi8	\N	\N
d5d72134-e5cf-4211-b482-e301dbbe2f40	user	INSERT	4397843a-0127-4b74-94c9-b787dcc5cad3	\N	{"id": "4397843a-0127-4b74-94c9-b787dcc5cad3", "email": "gavino_16@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:37:05.636642-05:00", "first_name": "ffffff feliz", "updated_at": "2026-05-12T17:37:05.636642-05:00", "password_hash": "$2a$10$Dit7UaxdeZTQrxfP4MP5K.DtNFqHUstm.WHN9ftH5P66kUoEY8Y3K", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 17:37:07.558675	user_wqswkhkwi8	\N	\N
9bf16ecc-c451-4306-a4b7-81d2218db80e	user	INSERT	de5ed0e8-cc46-4f67-bdba-635a9515ce49	\N	{"id": "de5ed0e8-cc46-4f67-bdba-635a9515ce49", "email": "gavino_18@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:38:24.43164-05:00", "first_name": "uuuuuu feliz", "updated_at": "2026-05-12T17:38:24.43164-05:00", "password_hash": "$2a$10$QWxLDy2nVXCK/VWHqSX7PeLGxP/HgMoF99BBX2BlEMrIqM5km07H6", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 17:38:26.321342	user_wqswkhkwi8	\N	\N
5cd74a33-73ba-42be-8f0a-4768884fa833	user	INSERT	2f609b36-15f3-46fc-8dc8-90d20edfc7d0	\N	{"id": "2f609b36-15f3-46fc-8dc8-90d20edfc7d0", "email": "loco_18@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:51:57.978196-05:00", "first_name": "uuuuuu feliz", "updated_at": "2026-05-12T17:51:57.978196-05:00", "password_hash": "$2a$10$aHyZ4SbXeYBsedZUS8O8l.REKDZptPtKReZeHciYdDf2ISdsQBu4G", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 17:51:58.016105	user_wqswkhkwi8	\N	\N
9a640148-3ef5-49f5-8b82-2cff8660a4d1	user	DELETE	721956b8-1ac0-4204-91bc-fff857e55750	{"id": "721956b8-1ac0-4204-91bc-fff857e55750", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T04:18:15.923042+00:00", "first_name": "chanchito feliz", "updated_at": "2026-05-12T04:18:15.923042+00:00", "password_hash": "$2a$10$F6YQt/1CofluTrxEwZZ0KekPHWaD79ou/5M88tTa6zp3yTX7rxd..", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 22:52:44.729772	user_wqswkhkwi8	\N	\N
007ac88b-a2fb-4436-9025-37e37707d30f	user	INSERT	de8bb0b6-efc0-4603-8783-6cfbf9c5c043	\N	{"id": "de8bb0b6-efc0-4603-8783-6cfbf9c5c043", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:52:59.424668-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T17:52:59.424668-05:00", "password_hash": "$2a$10$oCMdWjVd7QKjcW3H1EdrA.YGbh/4aYt.wyzVyLccsRnpGNpcnYY66", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 17:52:59.436967	user_wqswkhkwi8	\N	\N
f0042302-9de0-4545-81ef-9ec8d15228eb	user	UPDATE	de8bb0b6-efc0-4603-8783-6cfbf9c5c043	{"id": "de8bb0b6-efc0-4603-8783-6cfbf9c5c043", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:52:59.424668-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T17:52:59.424668-05:00", "password_hash": "$2a$10$oCMdWjVd7QKjcW3H1EdrA.YGbh/4aYt.wyzVyLccsRnpGNpcnYY66", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	{"id": "de8bb0b6-efc0-4603-8783-6cfbf9c5c043", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T17:52:59.424668-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T17:52:59.424668-05:00", "password_hash": "$2a$10$oCMdWjVd7QKjcW3H1EdrA.YGbh/4aYt.wyzVyLccsRnpGNpcnYY66", "status_registry": "ACTIVE", "is_email_verified": true, "status_updated_at": null}	2026-05-12 17:53:47.809047	user_wqswkhkwi8	\N	\N
daedcda2-3b2c-4ffa-9b11-41d2af019166	user	INSERT	c5279328-b2a0-4c54-8c14-0a2324833853	\N	{"id": "c5279328-b2a0-4c54-8c14-0a2324833853", "email": "gavino_18990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:02:36.166236-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:02:36.166236-05:00", "password_hash": "$2a$10$dmAq0is9W1iUN92Rcq6GHeR7z.d/VIRzmYmcSNF6qbKZOqrzsHFRK", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:02:36.200674	user_wqswkhkwi8	\N	\N
8e1de634-653a-4de6-bf44-0092adfb9799	user	INSERT	788f1b2f-c4ca-418f-afa7-9b0598b6ef64	\N	{"id": "788f1b2f-c4ca-418f-afa7-9b0598b6ef64", "email": "gavino_28990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:05:31.279796-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:05:31.279796-05:00", "password_hash": "$2a$10$0DhIEBmbeQFKH31akVRGHezrjBv2snQ4mgxzKGvEnaECrUOap/Qja", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:05:31.325762	user_wqswkhkwi8	\N	\N
1ad33ab5-ecd7-49f3-853e-3daae4577ba6	user	DELETE	de5ed0e8-cc46-4f67-bdba-635a9515ce49	{"id": "de5ed0e8-cc46-4f67-bdba-635a9515ce49", "email": "gavino_18@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T22:38:24.43164+00:00", "first_name": "uuuuuu feliz", "updated_at": "2026-05-12T22:38:24.43164+00:00", "password_hash": "$2a$10$QWxLDy2nVXCK/VWHqSX7PeLGxP/HgMoF99BBX2BlEMrIqM5km07H6", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
94c115ee-d5f2-46b3-acdc-3bc21a75728f	user	DELETE	2f609b36-15f3-46fc-8dc8-90d20edfc7d0	{"id": "2f609b36-15f3-46fc-8dc8-90d20edfc7d0", "email": "loco_18@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T22:51:57.978196+00:00", "first_name": "uuuuuu feliz", "updated_at": "2026-05-12T22:51:57.978196+00:00", "password_hash": "$2a$10$aHyZ4SbXeYBsedZUS8O8l.REKDZptPtKReZeHciYdDf2ISdsQBu4G", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
00b8110a-2ce4-41a6-962c-69870e5297b2	user	DELETE	6b1b814b-5f86-471f-aa93-5984556ea63f	{"id": "6b1b814b-5f86-471f-aa93-5984556ea63f", "email": "chanchitoFeliz1645_15@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": false, "last_name": "popop", "created_at": "2026-05-09T23:35:55.688726+00:00", "first_name": "chanchito feliz", "updated_at": "2026-05-12T01:14:17.30958+00:00", "password_hash": "$2a$10$FwipOIPgO.t9LrE4jmf5/O6tVd/PPTPQX1HPuPLUK4Az3/2FbVsqy", "status_registry": "DELETE", "is_email_verified": false, "status_updated_at": "2026-05-12T01:14:17.306776+00:00"}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
8aa8008e-ff07-4ca4-8ca7-f914c8084e16	user	DELETE	de8bb0b6-efc0-4603-8783-6cfbf9c5c043	{"id": "de8bb0b6-efc0-4603-8783-6cfbf9c5c043", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T22:52:59.424668+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T22:52:59.424668+00:00", "password_hash": "$2a$10$oCMdWjVd7QKjcW3H1EdrA.YGbh/4aYt.wyzVyLccsRnpGNpcnYY66", "status_registry": "ACTIVE", "is_email_verified": true, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
7f82d933-927a-4e8a-b29c-77c9c7db13fe	user	DELETE	c5279328-b2a0-4c54-8c14-0a2324833853	{"id": "c5279328-b2a0-4c54-8c14-0a2324833853", "email": "gavino_18990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:02:36.166236+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:02:36.166236+00:00", "password_hash": "$2a$10$dmAq0is9W1iUN92Rcq6GHeR7z.d/VIRzmYmcSNF6qbKZOqrzsHFRK", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
b45380a7-7330-4ef7-96f3-cd33e19f5966	user	DELETE	788f1b2f-c4ca-418f-afa7-9b0598b6ef64	{"id": "788f1b2f-c4ca-418f-afa7-9b0598b6ef64", "email": "gavino_28990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:05:31.279796+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:05:31.279796+00:00", "password_hash": "$2a$10$0DhIEBmbeQFKH31akVRGHezrjBv2snQ4mgxzKGvEnaECrUOap/Qja", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-12 23:05:47.72844	user_wqswkhkwi8	\N	\N
068b6228-4326-41ac-89a0-def54ce1fe1b	user	INSERT	b38c8ed3-13d7-4980-8937-a3a46c49093a	\N	{"id": "b38c8ed3-13d7-4980-8937-a3a46c49093a", "email": "gavino_28990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:05:52.382643-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:05:52.382643-05:00", "password_hash": "$2a$10$vvXAWe9rFvT0kuUotDbcMeDuDePS7bAAfW8HDa835qcw3kLHe15PW", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:05:52.389142	user_wqswkhkwi8	\N	\N
b2f65164-8fb4-4511-b5a7-45b618cf6170	user	INSERT	e3b25b0b-de3d-4067-864a-518b9310b360	\N	{"id": "e3b25b0b-de3d-4067-864a-518b9310b360", "email": "gavino_200@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:11:25.706836-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:11:25.706836-05:00", "password_hash": "$2a$10$I0VcT121Le6Udqi1NS5MtOvkljN7wcf5covPkNaCmtzdBTwQZzhue", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:11:25.738397	user_wqswkhkwi8	\N	\N
d25bb19e-1569-4dc7-8a5c-42af01cf93cb	user	INSERT	9d12b621-eee6-404e-a0af-72bec59b4b18	\N	{"id": "9d12b621-eee6-404e-a0af-72bec59b4b18", "email": "chicho_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:17:40.544801-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:17:40.544801-05:00", "password_hash": "$2a$10$DlD3sk7UbkJ.QhmLSyHNoOeSsMCG8OrVAO372xzFgDcDhJheizwZe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:17:40.585159	user_wqswkhkwi8	\N	\N
af1856a2-a196-4dbc-bde7-b0ed95cb44b1	user	INSERT	1d3df91f-c370-43c6-918d-614fe7af9288	\N	{"id": "1d3df91f-c370-43c6-918d-614fe7af9288", "email": "carlonchito_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:23:43.133793-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:23:43.133793-05:00", "password_hash": "$2a$10$qhKpclV503by1f620hBx9.ycj.z3TQUvaPDbP.uFQ6r9S2UTGvan6", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:23:43.168018	user_wqswkhkwi8	\N	\N
81f6a39c-daf3-49ab-9ebd-3a1095322a04	user	INSERT	ca97893a-9779-4d48-9159-ab73ede1667a	\N	{"id": "ca97893a-9779-4d48-9159-ab73ede1667a", "email": "fedelobo_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:28:50.500514-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:28:50.500514-05:00", "password_hash": "$2a$10$HOjP8401Fk229H4SwrnK/.yZWZkho6MYPb2yX95snNTm8xeMDgn32", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:28:50.536506	user_wqswkhkwi8	\N	\N
0bd16559-22a5-44a4-9592-0202f728b9be	user	INSERT	aff9ea97-7147-4368-8a0b-1b8df4564fa7	\N	{"id": "aff9ea97-7147-4368-8a0b-1b8df4564fa7", "email": "claramoyano_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:31:18.020977-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:31:18.020977-05:00", "password_hash": "$2a$10$M.Jfc5QmRJkvCg9nZr2jNuRKjh0hspeAYjRj.PceJB8sb72jTX4Hu", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:31:18.056391	user_wqswkhkwi8	\N	\N
204e9d91-8a5e-4b62-9edf-e9978741a268	user	INSERT	02f7e5ed-a5bc-45ba-b77f-e4ca16785380	\N	{"id": "02f7e5ed-a5bc-45ba-b77f-e4ca16785380", "email": "kamisamamidios@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T18:33:38.204881-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T18:33:38.204881-05:00", "password_hash": "$2a$10$RG9GIxCvVw7wqSuiRviMBu5bP4p7Z4iqmq9QdWykdrFvNOwqT8CJe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 18:33:38.241863	user_wqswkhkwi8	\N	\N
68587962-07df-4d36-800d-14e908e58765	user	DELETE	b38c8ed3-13d7-4980-8937-a3a46c49093a	{"id": "b38c8ed3-13d7-4980-8937-a3a46c49093a", "email": "gavino_28990@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:05:52.382643+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:05:52.382643+00:00", "password_hash": "$2a$10$vvXAWe9rFvT0kuUotDbcMeDuDePS7bAAfW8HDa835qcw3kLHe15PW", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
31d85798-273e-44f6-aa0a-f7cf0531d8ab	user	DELETE	e3b25b0b-de3d-4067-864a-518b9310b360	{"id": "e3b25b0b-de3d-4067-864a-518b9310b360", "email": "gavino_200@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:11:25.706836+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:11:25.706836+00:00", "password_hash": "$2a$10$I0VcT121Le6Udqi1NS5MtOvkljN7wcf5covPkNaCmtzdBTwQZzhue", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
01edd63b-e22d-4365-97f2-b0ab651ec819	user	DELETE	9d12b621-eee6-404e-a0af-72bec59b4b18	{"id": "9d12b621-eee6-404e-a0af-72bec59b4b18", "email": "chicho_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:17:40.544801+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:17:40.544801+00:00", "password_hash": "$2a$10$DlD3sk7UbkJ.QhmLSyHNoOeSsMCG8OrVAO372xzFgDcDhJheizwZe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
d76d6d7f-ec55-4c7d-894f-6711d73b105b	user	DELETE	1d3df91f-c370-43c6-918d-614fe7af9288	{"id": "1d3df91f-c370-43c6-918d-614fe7af9288", "email": "carlonchito_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:23:43.133793+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:23:43.133793+00:00", "password_hash": "$2a$10$qhKpclV503by1f620hBx9.ycj.z3TQUvaPDbP.uFQ6r9S2UTGvan6", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
d60bec0f-5071-4f6e-955c-c910f3e2887a	user	DELETE	ca97893a-9779-4d48-9159-ab73ede1667a	{"id": "ca97893a-9779-4d48-9159-ab73ede1667a", "email": "fedelobo_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:28:50.500514+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:28:50.500514+00:00", "password_hash": "$2a$10$HOjP8401Fk229H4SwrnK/.yZWZkho6MYPb2yX95snNTm8xeMDgn32", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
9bb7194d-730a-4408-bc0d-b76e2b354619	user	DELETE	aff9ea97-7147-4368-8a0b-1b8df4564fa7	{"id": "aff9ea97-7147-4368-8a0b-1b8df4564fa7", "email": "claramoyano_sdsa0@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:31:18.020977+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:31:18.020977+00:00", "password_hash": "$2a$10$M.Jfc5QmRJkvCg9nZr2jNuRKjh0hspeAYjRj.PceJB8sb72jTX4Hu", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
2193ce9a-0565-4c91-a05c-b1111e4a0875	user	DELETE	02f7e5ed-a5bc-45ba-b77f-e4ca16785380	{"id": "02f7e5ed-a5bc-45ba-b77f-e4ca16785380", "email": "kamisamamidios@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T23:33:38.204881+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T23:33:38.204881+00:00", "password_hash": "$2a$10$RG9GIxCvVw7wqSuiRviMBu5bP4p7Z4iqmq9QdWykdrFvNOwqT8CJe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	\N	2026-05-13 00:51:47.004164	user_wqswkhkwi8	\N	\N
77df8cbb-2068-4c6b-b582-40a2af8e9ee9	user	INSERT	48b3965b-9ca9-4c18-a936-0126754e52bd	\N	{"id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-12T19:52:02.89036-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-12T19:52:02.89036-05:00", "password_hash": "$2a$10$2zCsbwb4/lxIPtQyJ2UecuHn8eQ2VVmsrx6jy42MgNzZMkQ1ddHbe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-12 19:52:02.922372	user_wqswkhkwi8	\N	\N
3977fefc-e906-485e-82d7-91793f794843	user	INSERT	2416f619-c9e5-442b-8cbb-04f26c028ef5	\N	{"id": "2416f619-c9e5-442b-8cbb-04f26c028ef5", "email": "kevincespedes2004@gmail.com", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-15T23:15:42.498384-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-15T23:15:42.498384-05:00", "password_hash": "$2a$10$Yf/zofVC8/pBnV/SfGWoY.iUCpB6FCkjqHIvy9ySMBIo0cyd8OEZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}	2026-05-15 23:15:42.597527	user_wqswkhkwi8	\N	\N
d119129d-fad4-4cdd-a8f9-982b1fd09b05	permission	INSERT	4b02b9c5-0135-47c2-86c9-4ba3d29773d8	\N	{"id": "4b02b9c5-0135-47c2-86c9-4ba3d29773d8", "name": "CREATE_USER", "created_at": "2026-05-16T22:58:03.319374-05:00", "updated_at": "2026-05-16T22:58:03.319374-05:00", "description": "Permite crear usuarios", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 22:58:03.547866	user_wqswkhkwi8	\N	\N
a1b36543-90f4-4ef4-bc5c-cb79b0cdbf70	permission	INSERT	717ca265-3bd0-4185-8921-a7d3e6d1a30f	\N	{"id": "717ca265-3bd0-4185-8921-a7d3e6d1a30f", "name": "READ_USERS", "created_at": "2026-05-16T22:59:34.488997-05:00", "updated_at": "2026-05-16T22:59:34.488997-05:00", "description": "Permite listar y ver el detalle de los usuarios", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 22:59:34.70874	user_wqswkhkwi8	\N	\N
02205bbf-35b3-46fe-b0f8-f384a9b3120d	permission	INSERT	44d4263a-d64c-45b4-98fb-48e91242b2cd	\N	{"id": "44d4263a-d64c-45b4-98fb-48e91242b2cd", "name": "UPDATE_USER", "created_at": "2026-05-16T22:59:44.702366-05:00", "updated_at": "2026-05-16T22:59:44.702366-05:00", "description": "Permite modificar la información de los usuarios", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 22:59:44.921988	user_wqswkhkwi8	\N	\N
2783305e-3a1d-4b8c-a6ff-7fcca82788a8	permission	INSERT	364c4112-4126-4d10-8c72-4da6b994b83f	\N	{"id": "364c4112-4126-4d10-8c72-4da6b994b83f", "name": "DELETE_USER", "created_at": "2026-05-16T23:00:02.068917-05:00", "updated_at": "2026-05-16T23:00:02.068917-05:00", "description": "Permite eliminar o inactivar usuarios del sistema", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:00:02.304336	user_wqswkhkwi8	\N	\N
c88963bd-d816-4a52-9948-ce14d54e05f7	permission	INSERT	f75d4497-4a8e-48c3-8dea-963dc0cbbfa3	\N	{"id": "f75d4497-4a8e-48c3-8dea-963dc0cbbfa3", "name": "CREATE_PERMISSION", "created_at": "2026-05-16T23:02:58.470507-05:00", "updated_at": "2026-05-16T23:02:58.470507-05:00", "description": "Permite registrar un nuevo permiso en el sistema para futuras asignaciones", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:02:58.69376	user_wqswkhkwi8	\N	\N
f6dc63cd-703c-4d3b-8837-7ac8c87b20df	permission	INSERT	2da562c7-0f15-4fab-b602-e16cfee48845	\N	{"id": "2da562c7-0f15-4fab-b602-e16cfee48845", "name": "READ_PERMISSIONS", "created_at": "2026-05-16T23:03:08.133411-05:00", "updated_at": "2026-05-16T23:03:08.133411-05:00", "description": "Permite listar todos los permisos disponibles en la plataforma", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:03:08.357297	user_wqswkhkwi8	\N	\N
b2baa43d-3d35-4eb3-8257-3ce1b474cfdb	permission	INSERT	f113bc70-885e-4196-ac18-d51bd0e56acd	\N	{"id": "f113bc70-885e-4196-ac18-d51bd0e56acd", "name": "UPDATE_PERMISSION", "created_at": "2026-05-16T23:03:17.894917-05:00", "updated_at": "2026-05-16T23:03:17.894917-05:00", "description": "Permite editar el nombre o la descripción técnica de un permiso", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:03:18.117418	user_wqswkhkwi8	\N	\N
d77c4364-d65d-4441-8d0b-d48396ad4cbe	permission	INSERT	2484ff9c-ca22-4855-ab1b-a096134dc146	\N	{"id": "2484ff9c-ca22-4855-ab1b-a096134dc146", "name": "DELETE_PERMISSION", "created_at": "2026-05-16T23:03:24.719535-05:00", "updated_at": "2026-05-16T23:03:24.719535-05:00", "description": "Permite eliminar un permiso del catálogo general del sistema", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:03:24.942537	user_wqswkhkwi8	\N	\N
ba0b09ed-159f-467b-8bda-fd6a02466b21	permission	INSERT	afe8c3c3-ba75-4bfd-9bbc-e6ebf4dc5774	\N	{"id": "afe8c3c3-ba75-4bfd-9bbc-e6ebf4dc5774", "name": "ASSIGN_PERMISSION_TO_ROLE", "created_at": "2026-05-16T23:03:49.161673-05:00", "updated_at": "2026-05-16T23:03:49.161673-05:00", "description": "Permite asociar uno o varios permisos a un rol específico", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:03:49.384307	user_wqswkhkwi8	\N	\N
1c75ecf1-b1ac-48b0-a3f9-db29aa4a3715	permission	INSERT	53f2a64f-3f56-4616-85e1-0c481b217296	\N	{"id": "53f2a64f-3f56-4616-85e1-0c481b217296", "name": "REVOKE_PERMISSION_FROM_ROLE", "created_at": "2026-05-16T23:03:59.760039-05:00", "updated_at": "2026-05-16T23:03:59.760039-05:00", "description": "Permite remover permisos previamente asignados a un rol", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:03:59.982302	user_wqswkhkwi8	\N	\N
aff96172-aa48-4dfc-b191-441a19fcb856	permission	INSERT	e273e58e-7d08-4da1-be25-f931057f3651	\N	{"id": "e273e58e-7d08-4da1-be25-f931057f3651", "name": "READ_ROLE_PERMISSIONS", "created_at": "2026-05-16T23:04:08.991909-05:00", "updated_at": "2026-05-16T23:04:08.991909-05:00", "description": "Permite consultar la lista de permisos que tiene asignados un rol en particular", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:04:09.214433	user_wqswkhkwi8	\N	\N
c126a52f-e886-4130-800d-e4fa69c8faae	permission	INSERT	ddd9bc87-a5e8-4b50-8415-befb3ed726c3	\N	{"id": "ddd9bc87-a5e8-4b50-8415-befb3ed726c3", "name": "ASSIGN_ROLE_TO_USER", "created_at": "2026-05-16T23:04:38.254572-05:00", "updated_at": "2026-05-16T23:04:38.254572-05:00", "description": "Permite otorgarle un rol a un usuario (ej. promover a un usuario a Administrador)", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:04:38.480321	user_wqswkhkwi8	\N	\N
8e2acc0b-48b1-45c0-97d3-8d4e0681db0d	permission	INSERT	cd8b5b69-5074-462b-81fe-6c56548f50f6	\N	{"id": "cd8b5b69-5074-462b-81fe-6c56548f50f6", "name": "REVOKE_ROLE_FROM_USER", "created_at": "2026-05-16T23:04:47.431665-05:00", "updated_at": "2026-05-16T23:04:47.431665-05:00", "description": "Permite quitarle un rol a un usuario del sistema", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:04:47.654404	user_wqswkhkwi8	\N	\N
ae46c251-5087-4a2e-a953-b6b71ba8e2d5	permission	INSERT	57b6cecb-4d2c-4284-8d79-94846baffbd5	\N	{"id": "57b6cecb-4d2c-4284-8d79-94846baffbd5", "name": "READ_USER_ROLES", "created_at": "2026-05-16T23:04:53.529115-05:00", "updated_at": "2026-05-16T23:04:53.529115-05:00", "description": "Permite ver qué roles tiene asignados un usuario específico", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-16 23:04:53.752657	user_wqswkhkwi8	\N	\N
4f658047-a564-4288-9482-93eda16e76fe	role	INSERT	b3863900-2933-449b-90b3-665e29b8f45b	\N	{"id": "b3863900-2933-449b-90b3-665e29b8f45b", "name": "ADMIN", "created_at": "2026-05-17T00:06:02.166173-05:00", "updated_at": "2026-05-17T00:06:02.166173-05:00", "status_registry": "ACTIVE", "status_updated_at": null}	2026-05-17 00:06:02.431346	user_wqswkhkwi8	\N	\N
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.category (id, description, is_active, created_at, updated_at, status_registry, status_updated_at, category_id) FROM stdin;
5330b79b-c9be-4cf8-a04f-6d96a9892646	Regional	t	2026-05-09 07:01:26.029587+00	\N	ACTIVE	\N	\N
5b258f62-db37-4040-8076-e8b97721bb03	Política	f	\N	2026-05-09 07:43:59.621793+00	\N	\N	\N
\.


--
-- Data for Name: columnist; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.columnist (is_active, content, author, created_at, updated_at, title, headline, status_registry, status_updated_at, author_image_url, id) FROM stdin;
\.


--
-- Data for Name: digital_weekly; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.digital_weekly (id, pdf_url, front_page_image_url, descripcion, is_active, created_at, updated_at, status_registry, status_updated_at, is_premium, url) FROM stdin;
\.


--
-- Data for Name: news; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.news (id, content, is_carousel, headline, slug, is_premium, category_id, title, is_active, updated_at, created_at, status_registry, status_updated_at, image_url, is_peru_daily_news, is_latest_news) FROM stdin;
\.


--
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.permission (id, name, description, created_at, updated_at, status_registry, status_updated_at) FROM stdin;
4b02b9c5-0135-47c2-86c9-4ba3d29773d8	CREATE_USER	Permite crear usuarios	2026-05-17 03:58:03.319374+00	2026-05-17 03:58:03.319374+00	ACTIVE	\N
717ca265-3bd0-4185-8921-a7d3e6d1a30f	READ_USERS	Permite listar y ver el detalle de los usuarios	2026-05-17 03:59:34.488997+00	2026-05-17 03:59:34.488997+00	ACTIVE	\N
44d4263a-d64c-45b4-98fb-48e91242b2cd	UPDATE_USER	Permite modificar la información de los usuarios	2026-05-17 03:59:44.702366+00	2026-05-17 03:59:44.702366+00	ACTIVE	\N
364c4112-4126-4d10-8c72-4da6b994b83f	DELETE_USER	Permite eliminar o inactivar usuarios del sistema	2026-05-17 04:00:02.068917+00	2026-05-17 04:00:02.068917+00	ACTIVE	\N
d8a8f288-69be-4e33-b1cb-1a416dd50b3d	CREATE_ROLE	Permite registrar y definir nuevos roles en el sistema	2026-05-17 04:01:07.167585+00	2026-05-17 04:01:07.167585+00	ACTIVE	\N
b910ca79-a1f2-4e97-b1da-d168a0e41715	READ_ROLES	Permite listar y ver el detalle de los roles existentes	2026-05-17 04:01:20.129934+00	2026-05-17 04:01:20.129934+00	ACTIVE	\N
4fd7819c-9423-4779-ac90-3ecf6d3c5d26	UPDATE_ROLE	Permite modificar el nombre o la descripción de un rol existente	2026-05-17 04:01:39.12186+00	2026-05-17 04:01:39.12186+00	ACTIVE	\N
e65c3647-fcd1-4b92-9e34-f2cb83f22192	DELETE_ROLE	Permite eliminar un rol (siempre que no esté asignado a ningún usuario)	2026-05-17 04:01:46.300441+00	2026-05-17 04:01:46.300441+00	ACTIVE	\N
f75d4497-4a8e-48c3-8dea-963dc0cbbfa3	CREATE_PERMISSION	Permite registrar un nuevo permiso en el sistema para futuras asignaciones	2026-05-17 04:02:58.470507+00	2026-05-17 04:02:58.470507+00	ACTIVE	\N
2da562c7-0f15-4fab-b602-e16cfee48845	READ_PERMISSIONS	Permite listar todos los permisos disponibles en la plataforma	2026-05-17 04:03:08.133411+00	2026-05-17 04:03:08.133411+00	ACTIVE	\N
f113bc70-885e-4196-ac18-d51bd0e56acd	UPDATE_PERMISSION	Permite editar el nombre o la descripción técnica de un permiso	2026-05-17 04:03:17.894917+00	2026-05-17 04:03:17.894917+00	ACTIVE	\N
2484ff9c-ca22-4855-ab1b-a096134dc146	DELETE_PERMISSION	Permite eliminar un permiso del catálogo general del sistema	2026-05-17 04:03:24.719535+00	2026-05-17 04:03:24.719535+00	ACTIVE	\N
afe8c3c3-ba75-4bfd-9bbc-e6ebf4dc5774	ASSIGN_PERMISSION_TO_ROLE	Permite asociar uno o varios permisos a un rol específico	2026-05-17 04:03:49.161673+00	2026-05-17 04:03:49.161673+00	ACTIVE	\N
53f2a64f-3f56-4616-85e1-0c481b217296	REVOKE_PERMISSION_FROM_ROLE	Permite remover permisos previamente asignados a un rol	2026-05-17 04:03:59.760039+00	2026-05-17 04:03:59.760039+00	ACTIVE	\N
e273e58e-7d08-4da1-be25-f931057f3651	READ_ROLE_PERMISSIONS	Permite consultar la lista de permisos que tiene asignados un rol en particular	2026-05-17 04:04:08.991909+00	2026-05-17 04:04:08.991909+00	ACTIVE	\N
ddd9bc87-a5e8-4b50-8415-befb3ed726c3	ASSIGN_ROLE_TO_USER	Permite otorgarle un rol a un usuario (ej. promover a un usuario a Administrador)	2026-05-17 04:04:38.254572+00	2026-05-17 04:04:38.254572+00	ACTIVE	\N
cd8b5b69-5074-462b-81fe-6c56548f50f6	REVOKE_ROLE_FROM_USER	Permite quitarle un rol a un usuario del sistema	2026-05-17 04:04:47.431665+00	2026-05-17 04:04:47.431665+00	ACTIVE	\N
57b6cecb-4d2c-4284-8d79-94846baffbd5	READ_USER_ROLES	Permite ver qué roles tiene asignados un usuario específico	2026-05-17 04:04:53.529115+00	2026-05-17 04:04:53.529115+00	ACTIVE	\N
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.role (id, name, created_at, updated_at, status_registry, status_updated_at) FROM stdin;
b3863900-2933-449b-90b3-665e29b8f45b	ADMIN	2026-05-17 05:06:02.166173+00	2026-05-17 05:06:02.166173+00	ACTIVE	\N
\.


--
-- Data for Name: role_permission; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.role_permission (id, role_id, permission_id, created_at, updated_at, status_registry, status_updated_at) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public."user" (id, first_name, last_name, email, image_url, is_email_verified, password_hash, created_at, updated_at, status_registry, status_updated_at, is_active) FROM stdin;
48b3965b-9ca9-4c18-a936-0126754e52bd	asdasdasdsad	popop	gavino_10@hotmail.es	http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg	f	$2a$10$2zCsbwb4/lxIPtQyJ2UecuHn8eQ2VVmsrx6jy42MgNzZMkQ1ddHbe	2026-05-13 00:52:02.89036+00	2026-05-13 00:52:02.89036+00	ACTIVE	\N	t
2416f619-c9e5-442b-8cbb-04f26c028ef5	asdasdasdsad	popop	kevincespedes2004@gmail.com	http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg	f	$2a$10$Yf/zofVC8/pBnV/SfGWoY.iUCpB6FCkjqHIvy9ySMBIo0cyd8OEZ2	2026-05-16 04:15:42.498384+00	2026-05-16 04:15:42.498384+00	ACTIVE	\N	t
\.


--
-- Data for Name: user_permission; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.user_permission (id, user_id, permission_id, is_active, created_at, updated_at, status_registry, status_updated_at) FROM stdin;
\.


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: user_wqswkhkwi8
--

COPY public.user_role (id, user_id, role_id, created_at, updated_at, status_registry, status_updated_at) FROM stdin;
\.


--
-- Name: audit audit_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.audit
    ADD CONSTRAINT audit_pkey PRIMARY KEY (id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: columnist columnist_pk; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.columnist
    ADD CONSTRAINT columnist_pk PRIMARY KEY (id);


--
-- Name: digital_weekly digital_weekly_pk; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.digital_weekly
    ADD CONSTRAINT digital_weekly_pk PRIMARY KEY (id);


--
-- Name: news news_pk; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pk PRIMARY KEY (id);


--
-- Name: permission permission_name_key; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_name_key UNIQUE (name);


--
-- Name: permission permission_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- Name: role role_name_key; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_name_key UNIQUE (name);


--
-- Name: role_permission role_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT role_permission_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: role_permission uq_role_permission; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT uq_role_permission UNIQUE (role_id, permission_id);


--
-- Name: user_permission uq_user_permission; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT uq_user_permission UNIQUE (user_id, permission_id);


--
-- Name: user_role uq_user_role; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT uq_user_role UNIQUE (user_id, role_id);


--
-- Name: user user_email_key; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- Name: user_permission user_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT user_permission_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- Name: idx_user_status; Type: INDEX; Schema: public; Owner: user_wqswkhkwi8
--

CREATE INDEX idx_user_status ON public."user" USING btree (status_registry);


--
-- Name: news_category_id_idx; Type: INDEX; Schema: public; Owner: user_wqswkhkwi8
--

CREATE INDEX news_category_id_idx ON public.news USING btree (category_id);


--
-- Name: category trg_audit_category; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_category AFTER INSERT OR DELETE OR UPDATE ON public.category FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: permission trg_audit_permission; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_permission AFTER INSERT OR DELETE OR UPDATE ON public.permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: role trg_audit_role; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_role AFTER INSERT OR DELETE OR UPDATE ON public.role FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: role_permission trg_audit_role_permission; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_role_permission AFTER INSERT OR DELETE OR UPDATE ON public.role_permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: user trg_audit_user; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_user AFTER INSERT OR DELETE OR UPDATE ON public."user" FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: user_permission trg_audit_user_permission; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_user_permission BEFORE UPDATE ON public.user_permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: user_role trg_audit_user_role; Type: TRIGGER; Schema: public; Owner: user_wqswkhkwi8
--

CREATE TRIGGER trg_audit_user_role AFTER INSERT OR DELETE OR UPDATE ON public.user_role FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- Name: category category_category_fk; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: role_permission fk_rp_permission; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES public.permission(id) ON DELETE CASCADE;


--
-- Name: role_permission fk_rp_role; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;


--
-- Name: user_permission fk_up_permission; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_up_permission FOREIGN KEY (permission_id) REFERENCES public.permission(id) ON DELETE CASCADE;


--
-- Name: user_permission fk_up_user; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_up_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE;


--
-- Name: user_role fk_user_role_role; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;


--
-- Name: user_role fk_user_role_user; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE;


--
-- Name: news news_category_fk; Type: FK CONSTRAINT; Schema: public; Owner: user_wqswkhkwi8
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- PostgreSQL database dump complete
--

\unrestrict D4XnV7NDynAtd48y4uwfgLuounQQYNamOr27fTyujmISBxOWFfb7WSnaRNl6Xa0

