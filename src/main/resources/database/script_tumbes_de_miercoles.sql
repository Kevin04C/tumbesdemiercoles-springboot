--
-- PostgreSQL database dump
--

-- Dumped from database version 17.10
-- Dumped by pg_dump version 17.0

-- Started on 2026-06-01 23:04:07

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
-- TOC entry 240 (class 1255 OID 17416)
-- Name: fn_audit_logger(); Type: FUNCTION; Schema: public; Owner: -
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


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 17417)
-- Name: audit; Type: TABLE; Schema: public; Owner: -
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


--
-- TOC entry 218 (class 1259 OID 17424)
-- Name: category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.category (
                                 id uuid DEFAULT gen_random_uuid() NOT NULL,
                                 description character varying(255),
                                 is_active boolean DEFAULT true,
                                 created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                                 updated_at timestamp with time zone,
                                 status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
                                 status_updated_at timestamp with time zone,
                                 category_id uuid,
                                 slug character varying(50)
);


--
-- TOC entry 219 (class 1259 OID 17431)
-- Name: columnist; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.columnist (
                                  is_active boolean,
                                  content text NOT NULL,
                                  author character varying,
                                  created_at timestamp with time zone DEFAULT now(),
                                  updated_at timestamp with time zone,
                                  title character varying,
                                  headline character varying,
                                  status_registry character varying,
                                  status_updated_at timestamp with time zone,
                                  author_image_url character varying,
                                  id uuid DEFAULT gen_random_uuid() NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 17437)
-- Name: digital_weekly; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.digital_weekly (
                                       id uuid DEFAULT gen_random_uuid() NOT NULL,
                                       pdf_url character varying,
                                       front_page_image_url character varying,
                                       descripcion character varying NOT NULL,
                                       is_active boolean,
                                       created_at timestamp with time zone DEFAULT now(),
                                       updated_at timestamp with time zone,
                                       status_registry character varying,
                                       status_updated_at timestamp with time zone,
                                       is_premium boolean,
                                       url character varying
);


--
-- TOC entry 221 (class 1259 OID 17443)
-- Name: news; Type: TABLE; Schema: public; Owner: -
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
                             created_at timestamp with time zone DEFAULT now(),
                             status_registry character varying,
                             status_updated_at timestamp with time zone,
                             image_url text,
                             is_peru_daily_news boolean,
                             is_latest_news boolean
);


--
-- TOC entry 222 (class 1259 OID 17449)
-- Name: permission; Type: TABLE; Schema: public; Owner: -
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


--
-- TOC entry 223 (class 1259 OID 17455)
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role (
                             id uuid DEFAULT gen_random_uuid() NOT NULL,
                             name character varying(32) NOT NULL,
                             created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                             updated_at timestamp with time zone,
                             status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
                             status_updated_at timestamp with time zone
);


--
-- TOC entry 224 (class 1259 OID 17461)
-- Name: role_permission; Type: TABLE; Schema: public; Owner: -
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


--
-- TOC entry 225 (class 1259 OID 17467)
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."user" (
                               id uuid DEFAULT gen_random_uuid() NOT NULL,
                               first_name character varying(50) NOT NULL,
                               last_name character varying(50) NOT NULL,
                               email character varying(100) NOT NULL,
                               image_url character varying(255),
                               is_email_verified boolean DEFAULT false,
                               password_hash character varying(255) NOT NULL,
                               created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                               updated_at timestamp with time zone,
                               status_registry character varying(20),
                               status_updated_at timestamp with time zone,
                               is_active boolean DEFAULT true,
                               user_name character varying(50)
);


--
-- TOC entry 226 (class 1259 OID 17476)
-- Name: user_permission; Type: TABLE; Schema: public; Owner: -
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


--
-- TOC entry 227 (class 1259 OID 17485)
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_role (
                                  id uuid DEFAULT gen_random_uuid() NOT NULL,
                                  user_id uuid NOT NULL,
                                  role_id uuid NOT NULL,
                                  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                                  updated_at timestamp with time zone,
                                  status_registry character varying(20) DEFAULT 'ACTIVE'::character varying,
                                  status_updated_at timestamp with time zone
);


--
-- TOC entry 228 (class 1259 OID 17491)
-- Name: vw_audit_recent; Type: VIEW; Schema: public; Owner: -
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


--
-- TOC entry 3548 (class 0 OID 17417)
-- Dependencies: 217
-- Data for Name: audit; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.audit VALUES ('763ee6db-9dcb-48a5-8666-d4cd869f5e7b', 'category', 'DELETE', '5330b79b-c9be-4cf8-a04f-6d96a9892646', '{"id": "5330b79b-c9be-4cf8-a04f-6d96a9892646", "is_active": true, "created_at": "2026-05-09T02:01:26.029587-05:00", "updated_at": null, "category_id": null, "description": "Regional", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-19 14:11:50.176499', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('cc4686fc-d783-45dd-bcc3-6d065f25549d', 'category', 'DELETE', '5b258f62-db37-4040-8076-e8b97721bb03', '{"id": "5b258f62-db37-4040-8076-e8b97721bb03", "is_active": false, "created_at": null, "updated_at": "2026-05-09T02:43:59.621793-05:00", "category_id": null, "description": "Política", "status_registry": null, "status_updated_at": null}', NULL, '2026-05-19 14:11:50.212524', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('7592f3e4-2ff2-40a1-9bdf-6eae289bca94', 'category', 'INSERT', '690cc6fb-cdcc-46b8-8ed7-908956a97ad3', NULL, '{"id": "690cc6fb-cdcc-46b8-8ed7-908956a97ad3", "slug": "region", "is_active": true, "created_at": "2026-05-19T14:20:02.768725-05:00", "updated_at": null, "category_id": null, "description": "Región", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:20:02.768725', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('ed383b33-8275-4961-9f08-dee5befc0b0c', 'category', 'INSERT', 'ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed', NULL, '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": null, "is_active": true, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:22:36.728154', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('a89ee08b-78db-4e1d-93cc-6a8c9e3d973b', 'category', 'UPDATE', 'ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": null, "is_active": true, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": "policiales", "is_active": true, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:24:24.67378', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('35cd908f-f6d3-499c-8563-83b2ee9c1ea1', 'category', 'INSERT', 'f4667840-f16a-468b-b047-44c224268383', NULL, '{"id": "f4667840-f16a-468b-b047-44c224268383", "slug": null, "is_active": true, "created_at": "2026-05-19T14:24:43.102746-05:00", "updated_at": null, "category_id": null, "description": "Perú", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:24:43.102746', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('2061bf7f-7e2d-4247-b6a3-b4e79ef1d7c3', 'category', 'UPDATE', 'f4667840-f16a-468b-b047-44c224268383', '{"id": "f4667840-f16a-468b-b047-44c224268383", "slug": null, "is_active": true, "created_at": "2026-05-19T14:24:43.102746-05:00", "updated_at": null, "category_id": null, "description": "Perú", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "f4667840-f16a-468b-b047-44c224268383", "slug": "peru", "is_active": true, "created_at": "2026-05-19T14:24:43.102746-05:00", "updated_at": null, "category_id": null, "description": "Perú", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:26:19.668722', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('97e6031c-386a-422d-ad4b-40fac4fe8201', 'category', 'INSERT', '8bfcfb68-3bca-4075-8d33-ccfe5fa22880', NULL, '{"id": "8bfcfb68-3bca-4075-8d33-ccfe5fa22880", "slug": null, "is_active": true, "created_at": "2026-05-19T14:27:26.340197-05:00", "updated_at": null, "category_id": null, "description": "Deportivas", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:27:26.340197', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('11129a91-3b2e-45d2-bdf3-81816acddbe7', 'category', 'INSERT', '989c990c-0db4-496c-a2d7-84244edb8a17', NULL, '{"id": "989c990c-0db4-496c-a2d7-84244edb8a17", "slug": null, "is_active": true, "created_at": "2026-05-19T14:27:26.371892-05:00", "updated_at": null, "category_id": null, "description": "Frontera", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:27:26.371892', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('5cdaf0eb-f695-4de7-9595-bcd4d9108451', 'category', 'INSERT', 'b38740e3-c409-4916-9a28-090d220256ab', NULL, '{"id": "b38740e3-c409-4916-9a28-090d220256ab", "slug": "locales", "is_active": true, "created_at": "2026-05-19T14:28:12.733739-05:00", "updated_at": null, "category_id": null, "description": "Locales", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:28:12.733739', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('1c85b43b-ff6a-4e2f-bda9-fe4e1ab54820', 'category', 'UPDATE', '8bfcfb68-3bca-4075-8d33-ccfe5fa22880', '{"id": "8bfcfb68-3bca-4075-8d33-ccfe5fa22880", "slug": null, "is_active": true, "created_at": "2026-05-19T14:27:26.340197-05:00", "updated_at": null, "category_id": null, "description": "Deportivas", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "8bfcfb68-3bca-4075-8d33-ccfe5fa22880", "slug": "deportivas", "is_active": true, "created_at": "2026-05-19T14:27:26.340197-05:00", "updated_at": null, "category_id": null, "description": "Deportivas", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:28:12.777633', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('762f6238-82fe-4ba5-8404-2078dc3b83a7', 'category', 'UPDATE', '989c990c-0db4-496c-a2d7-84244edb8a17', '{"id": "989c990c-0db4-496c-a2d7-84244edb8a17", "slug": null, "is_active": true, "created_at": "2026-05-19T14:27:26.371892-05:00", "updated_at": null, "category_id": null, "description": "Frontera", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "989c990c-0db4-496c-a2d7-84244edb8a17", "slug": "frontera", "is_active": true, "created_at": "2026-05-19T14:27:26.371892-05:00", "updated_at": null, "category_id": null, "description": "Frontera", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:28:12.801682', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('35f75e5e-773b-4c77-94a9-78984b0d4a75', 'category', 'INSERT', 'a95b42fe-c3df-4831-8ec4-0d964b8414f5', NULL, '{"id": "a95b42fe-c3df-4831-8ec4-0d964b8414f5", "slug": "tumbes", "is_active": true, "created_at": "2026-05-19T14:28:46.228527-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Tumbes", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:28:46.228527', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('d7c96141-71e6-4bd9-8127-3a388d068d6d', 'category', 'INSERT', '583d9f0c-de8c-4c1a-a52f-1b52ea3e346d', NULL, '{"id": "583d9f0c-de8c-4c1a-a52f-1b52ea3e346d", "slug": "contralmirante-villar", "is_active": true, "created_at": "2026-05-19T14:29:47.449055-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Contralmirante villar", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:29:47.449055', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('ff1cbfda-a9af-4217-9a40-2f61a6e4c4f9', 'category', 'INSERT', '5e72926c-2dff-420d-b288-240f8e8e3a9d', NULL, '{"id": "5e72926c-2dff-420d-b288-240f8e8e3a9d", "slug": "zorritos", "is_active": true, "created_at": "2026-05-19T14:29:47.480883-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Zorritos", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 14:29:47.480883', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('e3ebe692-efe9-4491-a583-7c0b1fa7b7dd', 'user_role', 'INSERT', '5095a113-6780-457c-807e-65454739188c', NULL, '{"id": "5095a113-6780-457c-807e-65454739188c", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "user_id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "created_at": "2026-05-19T16:38:42.306387-05:00", "updated_at": "2026-05-19T16:38:42.306387-05:00", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 16:38:42.327486', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('653e6e0e-738c-4ddf-a6d4-a6748688667e', 'role_permission', 'INSERT', 'd2ab71c7-44f1-4560-aa1c-e0e92b0cacff', NULL, '{"id": "d2ab71c7-44f1-4560-aa1c-e0e92b0cacff", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T16:41:36.426141-05:00", "updated_at": "2026-05-19T16:41:36.426141-05:00", "permission_id": "717ca265-3bd0-4185-8921-a7d3e6d1a30f", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 16:41:36.420733', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('4f6baef6-1d8c-4665-9920-c3dcc0bbac4e', 'role_permission', 'INSERT', '5834a99e-62fc-4d5d-94bd-f208c50cfc93', NULL, '{"id": "5834a99e-62fc-4d5d-94bd-f208c50cfc93", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T16:41:36.425639-05:00", "updated_at": "2026-05-19T16:41:36.425639-05:00", "permission_id": "4b02b9c5-0135-47c2-86c9-4ba3d29773d8", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 16:41:36.415542', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('3c6bee6e-29c1-4559-8b64-0fb8e7c76a25', 'role_permission', 'INSERT', '04dfa97e-955c-40e4-b6e7-13099c66f031', NULL, '{"id": "04dfa97e-955c-40e4-b6e7-13099c66f031", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T16:41:36.425639-05:00", "updated_at": "2026-05-19T16:41:36.425639-05:00", "permission_id": "44d4263a-d64c-45b4-98fb-48e91242b2cd", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 16:41:36.41966', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('203efb7b-8cc9-481d-be76-958736415acc', 'role_permission', 'INSERT', '1e836386-ff0f-44fb-96d7-222f84e29d42', NULL, '{"id": "1e836386-ff0f-44fb-96d7-222f84e29d42", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T16:41:36.426141-05:00", "updated_at": "2026-05-19T16:41:36.426141-05:00", "permission_id": "364c4112-4126-4d10-8c72-4da6b994b83f", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 16:41:36.419649', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('d8c64d89-1702-461e-b302-1c89a6f99bf0', 'user_role', 'INSERT', 'a3fc23c0-0241-45c9-9d2a-8e39bfadb632', NULL, '{"id": "a3fc23c0-0241-45c9-9d2a-8e39bfadb632", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "user_id": "2416f619-c9e5-442b-8cbb-04f26c028ef5", "created_at": "2026-05-19T22:41:13.317007-05:00", "updated_at": "2026-05-19T22:41:13.317007-05:00", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 22:41:13.273138', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('177e5f2f-0696-481a-92c9-425636f7a4bc', 'category', 'INSERT', 'dcebfd82-684f-4c69-899a-44167eb5d427', NULL, '{"id": "dcebfd82-684f-4c69-899a-44167eb5d427", "slug": "politica", "is_active": true, "created_at": "2026-05-19T23:04:05.905628-05:00", "updated_at": null, "category_id": null, "description": "Politica", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 23:04:05.905628', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('63b4e8c2-f13d-463e-bcd0-8433c771fe86', 'role_permission', 'INSERT', 'e7803844-4616-454b-a902-1498cf24fdf1', NULL, '{"id": "e7803844-4616-454b-a902-1498cf24fdf1", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T23:28:19.939081-05:00", "updated_at": "2026-05-19T23:28:19.939081-05:00", "permission_id": "e273e58e-7d08-4da1-be25-f931057f3651", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 23:28:19.555272', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('05eb17c7-306f-4079-a9a7-5df78fcddef4', 'role_permission', 'INSERT', '46c5e976-85a8-4c29-bfa3-0dee640f185d', NULL, '{"id": "46c5e976-85a8-4c29-bfa3-0dee640f185d", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-19T23:28:19.994076-05:00", "updated_at": "2026-05-19T23:28:19.994076-05:00", "permission_id": "ddd9bc87-a5e8-4b50-8415-befb3ed726c3", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-19 23:28:19.555272', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('888157d1-38eb-4b1c-8473-73d72bd52490', 'category', 'UPDATE', 'a95b42fe-c3df-4831-8ec4-0d964b8414f5', '{"id": "a95b42fe-c3df-4831-8ec4-0d964b8414f5", "slug": "tumbes", "is_active": true, "created_at": "2026-05-19T14:28:46.228527-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Tumbes", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "a95b42fe-c3df-4831-8ec4-0d964b8414f5", "slug": "tumbes", "is_active": true, "created_at": "2026-05-19T14:28:46.228527-05:00", "updated_at": null, "category_id": "b38740e3-c409-4916-9a28-090d220256ab", "description": "Tumbes", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 01:34:23.293224', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('34313971-0966-4fc8-8d32-9da07310c7b9', 'category', 'UPDATE', '583d9f0c-de8c-4c1a-a52f-1b52ea3e346d', '{"id": "583d9f0c-de8c-4c1a-a52f-1b52ea3e346d", "slug": "contralmirante-villar", "is_active": true, "created_at": "2026-05-19T14:29:47.449055-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Contralmirante villar", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "583d9f0c-de8c-4c1a-a52f-1b52ea3e346d", "slug": "contralmirante-villar", "is_active": true, "created_at": "2026-05-19T14:29:47.449055-05:00", "updated_at": null, "category_id": "b38740e3-c409-4916-9a28-090d220256ab", "description": "Contralmirante villar", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 01:34:23.342653', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('b3501ee1-7a7e-4a50-8236-05104ba99918', 'category', 'UPDATE', '5e72926c-2dff-420d-b288-240f8e8e3a9d', '{"id": "5e72926c-2dff-420d-b288-240f8e8e3a9d", "slug": "zorritos", "is_active": true, "created_at": "2026-05-19T14:29:47.480883-05:00", "updated_at": null, "category_id": "989c990c-0db4-496c-a2d7-84244edb8a17", "description": "Zorritos", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "5e72926c-2dff-420d-b288-240f8e8e3a9d", "slug": "zorritos", "is_active": true, "created_at": "2026-05-19T14:29:47.480883-05:00", "updated_at": null, "category_id": "b38740e3-c409-4916-9a28-090d220256ab", "description": "Zorritos", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 01:34:23.376076', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('146bf1ab-19a2-49e0-9269-2b25737d6ae8', 'category', 'INSERT', '3646ca00-566b-4b46-9d80-afcc80dccf82', NULL, '{"id": "3646ca00-566b-4b46-9d80-afcc80dccf82", "slug": null, "is_active": true, "created_at": "2026-05-20T01:35:19.157166-05:00", "updated_at": null, "category_id": "a95b42fe-c3df-4831-8ec4-0d964b8414f5", "description": "test", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 01:35:19.157166', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('de470461-0a49-407b-82f3-cadec2e7fbfd', 'category', 'UPDATE', 'ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": "policiales", "is_active": true, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": "policiales", "is_active": false, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 02:08:13.689133', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('83601e8c-cda8-4a18-bcbb-5d1b6c421faf', 'category', 'UPDATE', 'ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": "policiales", "is_active": false, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed", "slug": "policiales", "is_active": true, "created_at": "2026-05-19T14:22:36.728154-05:00", "updated_at": null, "category_id": null, "description": "Policiales", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 02:08:30.187184', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('b12d588a-5886-4089-81b2-8a7a60628ac1', 'role', 'INSERT', '30ecc70b-533d-41a9-917f-1c8e1bda6f03', NULL, '{"id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "name": "USER", "created_at": "2026-05-20T22:59:46.458872-05:00", "updated_at": "2026-05-20T22:59:46.458872-05:00", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 22:59:46.727842', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('ec365718-6dff-4371-859b-3a17612f8062', 'permission', 'INSERT', 'ff12028d-7e3d-431b-b0b2-3b5c6acf798f', NULL, '{"id": "ff12028d-7e3d-431b-b0b2-3b5c6acf798f", "name": "READ_ALL_CATEGORY", "created_at": "2026-05-20T23:00:58.358927-05:00", "updated_at": "2026-05-20T23:00:58.358927-05:00", "description": "Permite ver la lista de todas las categorias", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 23:00:58.627469', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('6107c657-230d-410e-9424-f2296c7e9e9c', 'role_permission', 'INSERT', '84099f19-32e4-4cb2-bacc-6a53614a7cc3', NULL, '{"id": "84099f19-32e4-4cb2-bacc-6a53614a7cc3", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-20T23:02:08.607733-05:00", "updated_at": "2026-05-20T23:02:08.607733-05:00", "permission_id": "717ca265-3bd0-4185-8921-a7d3e6d1a30f", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-20 23:02:08.793399', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('0f8c1fbf-c682-4379-b494-56d1e1c05f62', 'user', 'INSERT', '67708d45-2eca-4afd-922a-bc8b01e644f1', NULL, '{"id": "67708d45-2eca-4afd-922a-bc8b01e644f1", "email": "gavino_15@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "created_at": "2026-05-20T23:13:04.358279-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-20T23:13:04.358279-05:00", "password_hash": "$2a$10$ViGdljDQWfJg0bEjYULx2el9cEkUTHNnP5ABvDQ/qi/ehnDD0NV7O", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-20 23:13:04.644523', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('b6621e17-7b79-47a9-bc47-244553d5f91f', 'user_role', 'INSERT', '91232203-63fa-4e98-a0ef-0eeb924e8f02', NULL, '{"id": "91232203-63fa-4e98-a0ef-0eeb924e8f02", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "user_id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "created_at": "2026-05-24T20:18:17.357074-05:00", "updated_at": "2026-05-24T20:18:17.357074-05:00", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 20:18:17.531564', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('aea14263-4f1d-4944-bd43-ffe07250fbeb', 'user_role', 'DELETE', '5095a113-6780-457c-807e-65454739188c', '{"id": "5095a113-6780-457c-807e-65454739188c", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "user_id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "created_at": "2026-05-19T21:38:42.306387+00:00", "updated_at": "2026-05-19T21:38:42.306387+00:00", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-25 02:28:51.625157', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('33bc592e-524f-463d-996a-9073ef13db9c', 'permission', 'INSERT', '92960200-6e95-4f49-bfcf-7456641111b2', NULL, '{"id": "92960200-6e95-4f49-bfcf-7456641111b2", "name": "READ_EFFECTIVE_PERMISSIONS", "created_at": "2026-05-24T21:40:25.993324-05:00", "updated_at": "2026-05-24T21:40:25.993324-05:00", "description": "Permite ver la lista de todas los permisos habilitados del usuario", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 21:40:26.232386', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('4f4b88ac-319f-4d38-a2ac-b43ae97619d2', 'role_permission', 'INSERT', '5579a7a7-b3c6-44b2-8b68-22bdfa7b2b73', NULL, '{"id": "5579a7a7-b3c6-44b2-8b68-22bdfa7b2b73", "role_id": "b3863900-2933-449b-90b3-665e29b8f45b", "created_at": "2026-05-24T21:41:36.119063-05:00", "updated_at": "2026-05-24T21:41:36.119063-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 21:41:36.27098', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('cb42fa31-8450-4b15-a571-96845d4c36d3', 'role_permission', 'INSERT', 'efe9b82a-a45a-4de2-8215-77b3dc2ef244', NULL, '{"id": "efe9b82a-a45a-4de2-8215-77b3dc2ef244", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T22:18:51.942447-05:00", "updated_at": "2026-05-24T22:18:51.942447-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 22:18:51.994696', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('c9d2c71e-54eb-46e3-82e4-6a8c28483ec1', 'role_permission', 'UPDATE', 'efe9b82a-a45a-4de2-8215-77b3dc2ef244', '{"id": "efe9b82a-a45a-4de2-8215-77b3dc2ef244", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T22:18:51.942447-05:00", "updated_at": "2026-05-24T22:18:51.942447-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '{"id": "efe9b82a-a45a-4de2-8215-77b3dc2ef244", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T22:18:51.942447-05:00", "updated_at": "2026-05-24T22:20:56.511054-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "DELETE", "status_updated_at": "2026-05-24T22:20:56.505268-05:00"}', '2026-05-24 22:20:56.611366', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('d1493254-5d2e-4173-8959-860c5ca32370', 'role_permission', 'DELETE', 'efe9b82a-a45a-4de2-8215-77b3dc2ef244', '{"id": "efe9b82a-a45a-4de2-8215-77b3dc2ef244", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-25T03:18:51.942447+00:00", "updated_at": "2026-05-25T03:20:56.511054+00:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "DELETE", "status_updated_at": "2026-05-25T03:20:56.505268+00:00"}', NULL, '2026-05-25 04:17:33.870895', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('bd762360-a51f-4a79-b7bb-a021736258b0', 'role_permission', 'INSERT', '296b5b92-bc18-49b3-8de7-ae46e61056d0', NULL, '{"id": "296b5b92-bc18-49b3-8de7-ae46e61056d0", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:17:58.004431-05:00", "updated_at": "2026-05-24T23:17:58.004431-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 23:17:58.033282', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('272c36da-d052-469f-b302-f8a7b3b4909c', 'role_permission', 'DELETE', '296b5b92-bc18-49b3-8de7-ae46e61056d0', '{"id": "296b5b92-bc18-49b3-8de7-ae46e61056d0", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:17:58.004431-05:00", "updated_at": "2026-05-24T23:17:58.004431-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-24 23:18:07.456311', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('25f5e2fe-d1dd-418b-ba29-7637b4e3d60d', 'role_permission', 'INSERT', '68de319d-218a-42a3-aefb-7e881d0d1062', NULL, '{"id": "68de319d-218a-42a3-aefb-7e881d0d1062", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:18:23.690254-05:00", "updated_at": "2026-05-24T23:18:23.690254-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 23:18:23.720867', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('a1980bc4-934f-488a-9745-434a0c4da3d3', 'role_permission', 'DELETE', '68de319d-218a-42a3-aefb-7e881d0d1062', '{"id": "68de319d-218a-42a3-aefb-7e881d0d1062", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:18:23.690254-05:00", "updated_at": "2026-05-24T23:18:23.690254-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-24 23:18:30.342088', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('103a933e-3c6a-43f9-93ac-a2cf702bd01d', 'role_permission', 'INSERT', '4d7413dd-bead-458f-8428-af08e550a15f', NULL, '{"id": "4d7413dd-bead-458f-8428-af08e550a15f", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:18:55.358597-05:00", "updated_at": "2026-05-24T23:18:55.358597-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 23:18:55.390612', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('f4ef77b1-de40-4268-9f41-efd2700eb93c', 'role_permission', 'DELETE', '4d7413dd-bead-458f-8428-af08e550a15f', '{"id": "4d7413dd-bead-458f-8428-af08e550a15f", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:18:55.358597-05:00", "updated_at": "2026-05-24T23:18:55.358597-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-24 23:19:02.903372', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('3e33149b-282e-4ddc-838d-49c8770c69a1', 'role_permission', 'INSERT', 'd9d9c951-1c12-4226-b0c7-13c1d273783f', NULL, '{"id": "d9d9c951-1c12-4226-b0c7-13c1d273783f", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:27:17.88995-05:00", "updated_at": "2026-05-24T23:27:17.88995-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 23:27:17.9131', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('3b023c40-da78-41aa-a2f2-f8f91cd44d6e', 'role_permission', 'DELETE', 'd9d9c951-1c12-4226-b0c7-13c1d273783f', '{"id": "d9d9c951-1c12-4226-b0c7-13c1d273783f", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:27:17.88995-05:00", "updated_at": "2026-05-24T23:27:17.88995-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-24 23:27:24.447158', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('a65a6608-bf5d-44e2-b76e-cbcbba7f4e1c', 'role_permission', 'INSERT', '6ace9dbd-8761-41da-a9c8-35af6e7c198b', NULL, '{"id": "6ace9dbd-8761-41da-a9c8-35af6e7c198b", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:27:41.139192-05:00", "updated_at": "2026-05-24T23:27:41.139192-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-24 23:27:41.169935', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('125ae3e0-e6fb-417a-8817-bd3c31cd4c4a', 'user_role', 'INSERT', 'd14e4546-d87e-428d-923f-77d2441d80e3', NULL, '{"id": "d14e4546-d87e-428d-923f-77d2441d80e3", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "user_id": "67708d45-2eca-4afd-922a-bc8b01e644f1", "created_at": "2026-05-25T00:05:45.437799-05:00", "updated_at": "2026-05-25T00:05:45.437799-05:00", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-25 00:05:45.442199', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('3f8d93c2-342b-4f41-bf67-c0bbffb029a9', 'role_permission', 'DELETE', '6ace9dbd-8761-41da-a9c8-35af6e7c198b', '{"id": "6ace9dbd-8761-41da-a9c8-35af6e7c198b", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-24T23:27:41.139192-05:00", "updated_at": "2026-05-24T23:27:41.139192-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-25 00:10:32.128637', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('d6244a87-caf1-4950-ad68-992e8ab049dc', 'role_permission', 'INSERT', '803f7f2b-687f-424b-b25d-b10ffa0e4443', NULL, '{"id": "803f7f2b-687f-424b-b25d-b10ffa0e4443", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-25T00:11:11.858725-05:00", "updated_at": "2026-05-25T00:11:11.858725-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', '2026-05-25 00:11:11.890612', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('02ddfada-e1f1-43eb-b27d-830d3a2ac350', 'role_permission', 'DELETE', '803f7f2b-687f-424b-b25d-b10ffa0e4443', '{"id": "803f7f2b-687f-424b-b25d-b10ffa0e4443", "role_id": "30ecc70b-533d-41a9-917f-1c8e1bda6f03", "created_at": "2026-05-25T00:11:11.858725-05:00", "updated_at": "2026-05-25T00:11:11.858725-05:00", "permission_id": "92960200-6e95-4f49-bfcf-7456641111b2", "status_registry": "ACTIVE", "status_updated_at": null}', NULL, '2026-05-25 00:11:16.745511', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('abd24fa3-d296-4e0d-9713-ef769bf0f506', 'user', 'INSERT', '49819b26-c0bd-4ef1-9770-1fafc290c12f', NULL, '{"id": "49819b26-c0bd-4ef1-9770-1fafc290c12f", "email": "orealy@gmail.com", "image_url": "string", "is_active": true, "last_name": "cespedes", "created_at": "2026-05-25T13:52:14.575596-05:00", "first_name": "orealy", "updated_at": "2026-05-25T13:52:14.575596-05:00", "password_hash": "$2a$10$iT5QRGFK9T46Q0tJ2NvbJO.1umJs4eziYAGAYhKOgk0ju5cU.NiU2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-25 13:52:14.627597', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('ec14db17-5606-4f1c-93d1-2df180e33d84', 'user', 'DELETE', '49819b26-c0bd-4ef1-9770-1fafc290c12f', '{"id": "49819b26-c0bd-4ef1-9770-1fafc290c12f", "email": "orealy@gmail.com", "image_url": "string", "is_active": true, "last_name": "cespedes", "created_at": "2026-05-25T13:52:14.575596-05:00", "first_name": "orealy", "updated_at": "2026-05-25T13:52:14.575596-05:00", "password_hash": "$2a$10$iT5QRGFK9T46Q0tJ2NvbJO.1umJs4eziYAGAYhKOgk0ju5cU.NiU2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', NULL, '2026-05-26 14:31:23.39276', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('1e29e78c-3732-4711-8c28-79c549c41e26', 'user', 'INSERT', '5f1bbb83-720d-47f5-8625-e02fc3adb3b8', NULL, '{"id": "5f1bbb83-720d-47f5-8625-e02fc3adb3b8", "email": "orealy@gmail.com", "image_url": "https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a", "is_active": true, "last_name": "cespedes", "created_at": "2026-05-26T14:32:04.154373-05:00", "first_name": "orealy", "updated_at": "2026-05-26T14:32:04.154373-05:00", "password_hash": "$2a$10$m5P.njTo9ZqWAyRMfbRHteTeYWJzNJSGMw17K6cSmGBxooYMVFvNy", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-26 14:32:04.161677', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('f4f5cde8-51b6-4ce5-844b-8bade16ba620', 'user', 'UPDATE', '2416f619-c9e5-442b-8cbb-04f26c028ef5', '{"id": "2416f619-c9e5-442b-8cbb-04f26c028ef5", "email": "kevincespedes2004@gmail.com", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": null, "created_at": "2026-05-16T04:15:42.498384+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-16T04:15:42.498384+00:00", "password_hash": "$2a$10$Yf/zofVC8/pBnV/SfGWoY.iUCpB6FCkjqHIvy9ySMBIo0cyd8OEZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '{"id": "2416f619-c9e5-442b-8cbb-04f26c028ef5", "email": "kevincespedes2004@gmail.com", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": "pancrasio", "created_at": "2026-05-16T04:15:42.498384+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-16T04:15:42.498384+00:00", "password_hash": "$2a$10$Yf/zofVC8/pBnV/SfGWoY.iUCpB6FCkjqHIvy9ySMBIo0cyd8OEZ2", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-31 20:37:54.211962', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('596d2b8c-a177-49ba-ba10-3a2d176cb6f3', 'user', 'UPDATE', '48b3965b-9ca9-4c18-a936-0126754e52bd', '{"id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": null, "created_at": "2026-05-13T00:52:02.89036+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-13T00:52:02.89036+00:00", "password_hash": "$2a$10$2zCsbwb4/lxIPtQyJ2UecuHn8eQ2VVmsrx6jy42MgNzZMkQ1ddHbe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '{"id": "48b3965b-9ca9-4c18-a936-0126754e52bd", "email": "gavino_10@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": "fedelobo", "created_at": "2026-05-13T00:52:02.89036+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-13T00:52:02.89036+00:00", "password_hash": "$2a$10$2zCsbwb4/lxIPtQyJ2UecuHn8eQ2VVmsrx6jy42MgNzZMkQ1ddHbe", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-31 20:37:54.211962', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('9c1a44a0-1a2f-4168-aedd-1ea3d9800fdb', 'user', 'UPDATE', '5f1bbb83-720d-47f5-8625-e02fc3adb3b8', '{"id": "5f1bbb83-720d-47f5-8625-e02fc3adb3b8", "email": "orealy@gmail.com", "image_url": "https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a", "is_active": true, "last_name": "cespedes", "user_name": null, "created_at": "2026-05-26T19:32:04.154373+00:00", "first_name": "orealy", "updated_at": "2026-05-26T19:32:04.154373+00:00", "password_hash": "$2a$10$m5P.njTo9ZqWAyRMfbRHteTeYWJzNJSGMw17K6cSmGBxooYMVFvNy", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '{"id": "5f1bbb83-720d-47f5-8625-e02fc3adb3b8", "email": "orealy@gmail.com", "image_url": "https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a", "is_active": true, "last_name": "cespedes", "user_name": "chicharito", "created_at": "2026-05-26T19:32:04.154373+00:00", "first_name": "orealy", "updated_at": "2026-05-26T19:32:04.154373+00:00", "password_hash": "$2a$10$m5P.njTo9ZqWAyRMfbRHteTeYWJzNJSGMw17K6cSmGBxooYMVFvNy", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-31 20:37:54.211962', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('8204a38e-be62-4af5-b6c9-a493eee61ba2', 'user', 'UPDATE', '67708d45-2eca-4afd-922a-bc8b01e644f1', '{"id": "67708d45-2eca-4afd-922a-bc8b01e644f1", "email": "gavino_15@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": null, "created_at": "2026-05-21T04:13:04.358279+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-21T04:13:04.358279+00:00", "password_hash": "$2a$10$ViGdljDQWfJg0bEjYULx2el9cEkUTHNnP5ABvDQ/qi/ehnDD0NV7O", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '{"id": "67708d45-2eca-4afd-922a-bc8b01e644f1", "email": "gavino_15@hotmail.es", "image_url": "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg", "is_active": true, "last_name": "popop", "user_name": "tilso", "created_at": "2026-05-21T04:13:04.358279+00:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-21T04:13:04.358279+00:00", "password_hash": "$2a$10$ViGdljDQWfJg0bEjYULx2el9cEkUTHNnP5ABvDQ/qi/ehnDD0NV7O", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-31 20:37:54.211962', 'user_wqswkhkwi8', NULL, NULL);
INSERT INTO public.audit VALUES ('f36e66ac-9db9-439b-95df-746b88694b56', 'user', 'INSERT', '77eb97fc-b5b4-4888-b323-960b4941e963', NULL, '{"id": "77eb97fc-b5b4-4888-b323-960b4941e963", "email": "gavino_21@hotmail.es", "image_url": "https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a", "is_active": true, "last_name": "popop", "user_name": "carlito ancheloti", "created_at": "2026-05-31T15:46:22.552699-05:00", "first_name": "asdasdasdsad", "updated_at": "2026-05-31T15:46:22.552699-05:00", "password_hash": "$2a$10$cRxnPzrgJ1NbY2B0Tt2NPe7k1hvJWsdC/cVb66XqpHIxs7Y5lzuWa", "status_registry": "ACTIVE", "is_email_verified": false, "status_updated_at": null}', '2026-05-31 15:46:23.238143', 'user_wqswkhkwi8', NULL, NULL);


--
-- TOC entry 3549 (class 0 OID 17424)
-- Dependencies: 218
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.category VALUES ('690cc6fb-cdcc-46b8-8ed7-908956a97ad3', 'Región', true, '2026-05-19 19:20:02.768725+00', NULL, 'ACTIVE', NULL, NULL, 'region');
INSERT INTO public.category VALUES ('f4667840-f16a-468b-b047-44c224268383', 'Perú', true, '2026-05-19 19:24:43.102746+00', NULL, 'ACTIVE', NULL, NULL, 'peru');
INSERT INTO public.category VALUES ('b38740e3-c409-4916-9a28-090d220256ab', 'Locales', true, '2026-05-19 19:28:12.733739+00', NULL, 'ACTIVE', NULL, NULL, 'locales');
INSERT INTO public.category VALUES ('8bfcfb68-3bca-4075-8d33-ccfe5fa22880', 'Deportivas', true, '2026-05-19 19:27:26.340197+00', NULL, 'ACTIVE', NULL, NULL, 'deportivas');
INSERT INTO public.category VALUES ('989c990c-0db4-496c-a2d7-84244edb8a17', 'Frontera', true, '2026-05-19 19:27:26.371892+00', NULL, 'ACTIVE', NULL, NULL, 'frontera');
INSERT INTO public.category VALUES ('dcebfd82-684f-4c69-899a-44167eb5d427', 'Politica', true, '2026-05-20 04:04:05.905628+00', NULL, 'ACTIVE', NULL, NULL, 'politica');
INSERT INTO public.category VALUES ('a95b42fe-c3df-4831-8ec4-0d964b8414f5', 'Tumbes', true, '2026-05-19 19:28:46.228527+00', NULL, 'ACTIVE', NULL, 'b38740e3-c409-4916-9a28-090d220256ab', 'tumbes');
INSERT INTO public.category VALUES ('583d9f0c-de8c-4c1a-a52f-1b52ea3e346d', 'Contralmirante villar', true, '2026-05-19 19:29:47.449055+00', NULL, 'ACTIVE', NULL, 'b38740e3-c409-4916-9a28-090d220256ab', 'contralmirante-villar');
INSERT INTO public.category VALUES ('5e72926c-2dff-420d-b288-240f8e8e3a9d', 'Zorritos', true, '2026-05-19 19:29:47.480883+00', NULL, 'ACTIVE', NULL, 'b38740e3-c409-4916-9a28-090d220256ab', 'zorritos');
INSERT INTO public.category VALUES ('3646ca00-566b-4b46-9d80-afcc80dccf82', 'test', true, '2026-05-20 06:35:19.157166+00', NULL, 'ACTIVE', NULL, 'a95b42fe-c3df-4831-8ec4-0d964b8414f5', NULL);
INSERT INTO public.category VALUES ('ac21f7c0-3f71-4e3c-bf76-530ddd5ec8ed', 'Policiales', true, '2026-05-19 19:22:36.728154+00', NULL, 'ACTIVE', NULL, NULL, 'policiales');


--
-- TOC entry 3550 (class 0 OID 17431)
-- Dependencies: 219
-- Data for Name: columnist; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.columnist VALUES (true, '<p><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">El Instituto Nacional de Defensa Civil (Indeci) recomendó adoptar medidas de preparación ante el Aviso Meteorológico N° 201 (nivel naranja) del Senamhi que anuncia el incremento de la temperatura diurna –de moderada a fuerte intensidad– desde el domingo 24 al martes 26 de mayo.</span></p><p><br></p><p><strong style="color: rgb(17, 24, 39); background-color: rgb(255, 255, 255);">Este incremento se presentará en la sierra de las regiones Áncash, Arequipa, Ayacucho, Cajamarca, Huancavelica, Ica, La Libertad, Lambayeque, Lima, Moquegua, Piura, Tacna y Tumbes, detalló Defensa Civil.</strong></p><p><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">De acuerdo con el pronóstico, se prevén temperaturas máximas </span><strong style="color: rgb(17, 24, 39); background-color: rgb(255, 255, 255);">28 °C y 36 °C e</strong><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">n la costa norte; entre </span><strong style="color: rgb(17, 24, 39); background-color: rgb(255, 255, 255);">25 °C y 31 °C</strong><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);"> en la costa centro; y valores entre </span><strong style="color: rgb(17, 24, 39); background-color: rgb(255, 255, 255);">24 °C y 31 °</strong><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">C en la costa sur.</span></p><p><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">Además, se esperan registros entre 20 °C y 31 °C la sierra norte y centro, y temperaturas máximas entre 18 °C y 30 °C en la sierra sur.</span></p><p><span style="color: rgb(55, 65, 81); background-color: rgb(255, 255, 255);">Se prevé también escasa nubosidad hacia el mediodía, lo que favorecerá el incremento de los niveles de radiación ultravioleta (UV). Asimismo, se presentarían ráfagas de viento con velocidades cercanas a los 35 km/h, principalmente en horas de la tarde.</span></p><p><img src="https://storage-app.orealy.xyz/f/b6683b8e-9013-44bd-9af1-f9361bf126d9"></p>', 'El que se pica pierde', '2026-05-26 05:35:58.177223+00', '2026-05-26 06:03:17.498131+00', 'algo', 'algo tmb', NULL, NULL, '/f/dc761990-f994-4547-a408-83eca6a5271c', 'a82288d6-01c0-458d-a729-9a1e5cf907e4');
INSERT INTO public.columnist VALUES (true, '**LA “SUPER LAPTOP”**

Hay un alcalde distrital de peso pesado, que adquirido cuatro laptops y cuatro impresoras a un precio exorbitante, por un monto de 24 mil 600 soles. Lo más sorprendente es que el proveedor, luego de cobrar le dio de baja a su RUC, en el momento de contratar con el Estado no contaba con RNP, requisito indispensable para pactar con el Estado. Además, no habría cumplido con todos los requisitos que pide la Sunat, para la adquisición de estos equipos tecnológicos.

**"SOY SU HERMANO, PERO NO SE NADA"**

De Ripley esto solo sucede en el Perú y sobre todo en una municipalidad provincial playera, plagada de presuntos actos de corrupción, en donde todo está de cabeza y ahora todo se justificaría.
Todas las denuncias no prosperan ni dan resultado toda vez que, en un claro conflicto de intereses, pues la autoridad edil quien predica la palabra de Dios, no tuvo la mejor idea que darle trabajo al hermano de un funcionario de la Oficina de Control Interno (OCI) de su propia comuna.

**"SOY SU HERMANO, PERO NO SE NADA" II**

Hasta nuestra sala de redacción nos llegó la información que el supervisor de limpieza pública sería hermano del funcionario de Control Interno de la muni, que funge de supervisor en diversas comisiones de control, si esto fuera así, sería un escándalo que colinda con un delito ya que esa plaza de supervisor no ha sido cubierta bajo los términos de ley sino "A DEDOCRACIA” y lógicamente ante este hecho serían razones suficientes para que toda denuncia sea encarpetada y no sigan los procedimientos de acuerdo a ley.

**“MANZANO” AL CONGRESO"**


Nos comentan que el popular “Manzano”, estaría pensando postular al Congreso como candidato a Diputado, por el partido político “País para Todos”, del humorista Carlos Alvarez, sino regresa a la municipalidad Inca, ya que este fin de mes estaría cumpliendo su condena.

**“MATACOJUDO”**

**Este árbol lo vemos desde Tumbes a Piura, tiene unos frutos no comestibles que llegan a pesar 5 ó 6 kilos. La razón de este nombre coloquial se debe a la forma y el peso de sus frutos, que pueden causar daño si caen sobre alguien.**', '¡EL QUE SE PICA PIERDE!', '2026-05-20 03:37:26.421141+00', '2026-05-26 06:13:30.519153+00', 'CAMBIO DE CHIP', '', NULL, NULL, '/f/ceb8f30e-7b93-4198-956f-e7e335b991cc', '1dd3d2f7-db99-4f97-bf1a-eae265b10cb7');


--
-- TOC entry 3551 (class 0 OID 17437)
-- Dependencies: 220
-- Data for Name: digital_weekly; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.digital_weekly VALUES ('25dec80c-c531-47d5-96aa-f7397bf933d7', '/f/a1f6292a-1a02-4255-a9cf-03937ec68ee5', '/f/826588a8-e3f5-45cb-8995-5a1aa77ee262', 'Edición 28', true, '2026-05-26 02:47:51.636054+00', '2026-05-26 02:57:35.219275+00', NULL, NULL, false, '');
INSERT INTO public.digital_weekly VALUES ('bd6aef6c-f4a1-415f-8bf3-3cdf6d9439d0', '/f/913cd401-1025-4e3c-8964-35ff76fd19f9', '/f/8d86c93c-ea71-4641-b41b-21c4e97ed2e8', 'Edición 20', true, '2026-05-26 03:01:14.582572+00', '2026-05-26 03:01:14.582572+00', NULL, NULL, false, '');


--
-- TOC entry 3552 (class 0 OID 17443)
-- Dependencies: 221
-- Data for Name: news; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.news VALUES ('2f27c649-cfe6-4999-a187-a0f9cda923b5', 'El aumento de casos de dengue tiene alarmado a las autoridades de la Dirección Regional de Salud en Tumbes. Esto ha encendido las alarmas en el Cantón Huaquillas, en Ecuador, que está muy cerca y cuyos funcionarios de salud ya han empleado un plan para prevenir un posible brote de dengue en la frontera.', true, 'EN EL VECINO PAÍS ASEGURAN QUE LA ENFERMEDAD ESTÁ CONTROLADA', 'en-el-vecino-país-aseguran-que-la-enfermedad-está-controlada', false, 'a95b42fe-c3df-4831-8ec4-0d964b8414f5', 'Pobladores de Huaquillas están en alerta por emergencia de dengue en Tumbes', true, '2026-05-26 15:13:41.437505', NULL, NULL, NULL, '', true, true);


--
-- TOC entry 3553 (class 0 OID 17449)
-- Dependencies: 222
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.permission VALUES ('4b02b9c5-0135-47c2-86c9-4ba3d29773d8', 'CREATE_USER', 'Permite crear usuarios', '2026-05-17 03:58:03.319374+00', '2026-05-17 03:58:03.319374+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('717ca265-3bd0-4185-8921-a7d3e6d1a30f', 'READ_USERS', 'Permite listar y ver el detalle de los usuarios', '2026-05-17 03:59:34.488997+00', '2026-05-17 03:59:34.488997+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('44d4263a-d64c-45b4-98fb-48e91242b2cd', 'UPDATE_USER', 'Permite modificar la información de los usuarios', '2026-05-17 03:59:44.702366+00', '2026-05-17 03:59:44.702366+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('364c4112-4126-4d10-8c72-4da6b994b83f', 'DELETE_USER', 'Permite eliminar o inactivar usuarios del sistema', '2026-05-17 04:00:02.068917+00', '2026-05-17 04:00:02.068917+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('d8a8f288-69be-4e33-b1cb-1a416dd50b3d', 'CREATE_ROLE', 'Permite registrar y definir nuevos roles en el sistema', '2026-05-17 04:01:07.167585+00', '2026-05-17 04:01:07.167585+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('b910ca79-a1f2-4e97-b1da-d168a0e41715', 'READ_ROLES', 'Permite listar y ver el detalle de los roles existentes', '2026-05-17 04:01:20.129934+00', '2026-05-17 04:01:20.129934+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('4fd7819c-9423-4779-ac90-3ecf6d3c5d26', 'UPDATE_ROLE', 'Permite modificar el nombre o la descripción de un rol existente', '2026-05-17 04:01:39.12186+00', '2026-05-17 04:01:39.12186+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('e65c3647-fcd1-4b92-9e34-f2cb83f22192', 'DELETE_ROLE', 'Permite eliminar un rol (siempre que no esté asignado a ningún usuario)', '2026-05-17 04:01:46.300441+00', '2026-05-17 04:01:46.300441+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('f75d4497-4a8e-48c3-8dea-963dc0cbbfa3', 'CREATE_PERMISSION', 'Permite registrar un nuevo permiso en el sistema para futuras asignaciones', '2026-05-17 04:02:58.470507+00', '2026-05-17 04:02:58.470507+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('2da562c7-0f15-4fab-b602-e16cfee48845', 'READ_PERMISSIONS', 'Permite listar todos los permisos disponibles en la plataforma', '2026-05-17 04:03:08.133411+00', '2026-05-17 04:03:08.133411+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('f113bc70-885e-4196-ac18-d51bd0e56acd', 'UPDATE_PERMISSION', 'Permite editar el nombre o la descripción técnica de un permiso', '2026-05-17 04:03:17.894917+00', '2026-05-17 04:03:17.894917+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('2484ff9c-ca22-4855-ab1b-a096134dc146', 'DELETE_PERMISSION', 'Permite eliminar un permiso del catálogo general del sistema', '2026-05-17 04:03:24.719535+00', '2026-05-17 04:03:24.719535+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('afe8c3c3-ba75-4bfd-9bbc-e6ebf4dc5774', 'ASSIGN_PERMISSION_TO_ROLE', 'Permite asociar uno o varios permisos a un rol específico', '2026-05-17 04:03:49.161673+00', '2026-05-17 04:03:49.161673+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('53f2a64f-3f56-4616-85e1-0c481b217296', 'REVOKE_PERMISSION_FROM_ROLE', 'Permite remover permisos previamente asignados a un rol', '2026-05-17 04:03:59.760039+00', '2026-05-17 04:03:59.760039+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('e273e58e-7d08-4da1-be25-f931057f3651', 'READ_ROLE_PERMISSIONS', 'Permite consultar la lista de permisos que tiene asignados un rol en particular', '2026-05-17 04:04:08.991909+00', '2026-05-17 04:04:08.991909+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('ddd9bc87-a5e8-4b50-8415-befb3ed726c3', 'ASSIGN_ROLE_TO_USER', 'Permite otorgarle un rol a un usuario (ej. promover a un usuario a Administrador)', '2026-05-17 04:04:38.254572+00', '2026-05-17 04:04:38.254572+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('cd8b5b69-5074-462b-81fe-6c56548f50f6', 'REVOKE_ROLE_FROM_USER', 'Permite quitarle un rol a un usuario del sistema', '2026-05-17 04:04:47.431665+00', '2026-05-17 04:04:47.431665+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('57b6cecb-4d2c-4284-8d79-94846baffbd5', 'READ_USER_ROLES', 'Permite ver qué roles tiene asignados un usuario específico', '2026-05-17 04:04:53.529115+00', '2026-05-17 04:04:53.529115+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('ff12028d-7e3d-431b-b0b2-3b5c6acf798f', 'READ_ALL_CATEGORY', 'Permite ver la lista de todas las categorias', '2026-05-21 04:00:58.358927+00', '2026-05-21 04:00:58.358927+00', 'ACTIVE', NULL);
INSERT INTO public.permission VALUES ('92960200-6e95-4f49-bfcf-7456641111b2', 'READ_EFFECTIVE_PERMISSIONS', 'Permite ver la lista de todas los permisos habilitados del usuario', '2026-05-25 02:40:25.993324+00', '2026-05-25 02:40:25.993324+00', 'ACTIVE', NULL);


--
-- TOC entry 3554 (class 0 OID 17455)
-- Dependencies: 223
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.role VALUES ('b3863900-2933-449b-90b3-665e29b8f45b', 'ADMIN', '2026-05-17 05:06:02.166173+00', '2026-05-17 05:06:02.166173+00', 'ACTIVE', NULL);
INSERT INTO public.role VALUES ('30ecc70b-533d-41a9-917f-1c8e1bda6f03', 'USER', '2026-05-21 03:59:46.458872+00', '2026-05-21 03:59:46.458872+00', 'ACTIVE', NULL);


--
-- TOC entry 3555 (class 0 OID 17461)
-- Dependencies: 224
-- Data for Name: role_permission; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.role_permission VALUES ('5834a99e-62fc-4d5d-94bd-f208c50cfc93', 'b3863900-2933-449b-90b3-665e29b8f45b', '4b02b9c5-0135-47c2-86c9-4ba3d29773d8', '2026-05-19 21:41:36.425639+00', '2026-05-19 21:41:36.425639+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('1e836386-ff0f-44fb-96d7-222f84e29d42', 'b3863900-2933-449b-90b3-665e29b8f45b', '364c4112-4126-4d10-8c72-4da6b994b83f', '2026-05-19 21:41:36.426141+00', '2026-05-19 21:41:36.426141+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('04dfa97e-955c-40e4-b6e7-13099c66f031', 'b3863900-2933-449b-90b3-665e29b8f45b', '44d4263a-d64c-45b4-98fb-48e91242b2cd', '2026-05-19 21:41:36.425639+00', '2026-05-19 21:41:36.425639+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('d2ab71c7-44f1-4560-aa1c-e0e92b0cacff', 'b3863900-2933-449b-90b3-665e29b8f45b', '717ca265-3bd0-4185-8921-a7d3e6d1a30f', '2026-05-19 21:41:36.426141+00', '2026-05-19 21:41:36.426141+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('e7803844-4616-454b-a902-1498cf24fdf1', 'b3863900-2933-449b-90b3-665e29b8f45b', 'e273e58e-7d08-4da1-be25-f931057f3651', '2026-05-20 04:28:19.939081+00', '2026-05-20 04:28:19.939081+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('46c5e976-85a8-4c29-bfa3-0dee640f185d', 'b3863900-2933-449b-90b3-665e29b8f45b', 'ddd9bc87-a5e8-4b50-8415-befb3ed726c3', '2026-05-20 04:28:19.994076+00', '2026-05-20 04:28:19.994076+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('84099f19-32e4-4cb2-bacc-6a53614a7cc3', '30ecc70b-533d-41a9-917f-1c8e1bda6f03', '717ca265-3bd0-4185-8921-a7d3e6d1a30f', '2026-05-21 04:02:08.607733+00', '2026-05-21 04:02:08.607733+00', 'ACTIVE', NULL);
INSERT INTO public.role_permission VALUES ('5579a7a7-b3c6-44b2-8b68-22bdfa7b2b73', 'b3863900-2933-449b-90b3-665e29b8f45b', '92960200-6e95-4f49-bfcf-7456641111b2', '2026-05-25 02:41:36.119063+00', '2026-05-25 02:41:36.119063+00', 'ACTIVE', NULL);


--
-- TOC entry 3556 (class 0 OID 17467)
-- Dependencies: 225
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public."user" VALUES ('2416f619-c9e5-442b-8cbb-04f26c028ef5', 'asdasdasdsad', 'popop', 'kevincespedes2004@gmail.com', 'http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg', false, '$2a$10$Yf/zofVC8/pBnV/SfGWoY.iUCpB6FCkjqHIvy9ySMBIo0cyd8OEZ2', '2026-05-16 04:15:42.498384+00', '2026-05-16 04:15:42.498384+00', 'ACTIVE', NULL, true, 'pancrasio');
INSERT INTO public."user" VALUES ('48b3965b-9ca9-4c18-a936-0126754e52bd', 'asdasdasdsad', 'popop', 'gavino_10@hotmail.es', 'http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg', false, '$2a$10$2zCsbwb4/lxIPtQyJ2UecuHn8eQ2VVmsrx6jy42MgNzZMkQ1ddHbe', '2026-05-13 00:52:02.89036+00', '2026-05-13 00:52:02.89036+00', 'ACTIVE', NULL, true, 'fedelobo');
INSERT INTO public."user" VALUES ('5f1bbb83-720d-47f5-8625-e02fc3adb3b8', 'orealy', 'cespedes', 'orealy@gmail.com', 'https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a', false, '$2a$10$m5P.njTo9ZqWAyRMfbRHteTeYWJzNJSGMw17K6cSmGBxooYMVFvNy', '2026-05-26 19:32:04.154373+00', '2026-05-26 19:32:04.154373+00', 'ACTIVE', NULL, true, 'chicharito');
INSERT INTO public."user" VALUES ('67708d45-2eca-4afd-922a-bc8b01e644f1', 'asdasdasdsad', 'popop', 'gavino_15@hotmail.es', 'http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg', false, '$2a$10$ViGdljDQWfJg0bEjYULx2el9cEkUTHNnP5ABvDQ/qi/ehnDD0NV7O', '2026-05-21 04:13:04.358279+00', '2026-05-21 04:13:04.358279+00', 'ACTIVE', NULL, true, 'tilso');
INSERT INTO public."user" VALUES ('77eb97fc-b5b4-4888-b323-960b4941e963', 'asdasdasdsad', 'popop', 'gavino_21@hotmail.es', 'https://storage-app.orealy.xyz/f/63a31969-d6b5-40cb-a81d-2b0820fff32a', false, '$2a$10$cRxnPzrgJ1NbY2B0Tt2NPe7k1hvJWsdC/cVb66XqpHIxs7Y5lzuWa', '2026-05-31 20:46:22.552699+00', '2026-05-31 20:46:22.552699+00', 'ACTIVE', NULL, true, 'carlito ancheloti');


--
-- TOC entry 3557 (class 0 OID 17476)
-- Dependencies: 226
-- Data for Name: user_permission; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3558 (class 0 OID 17485)
-- Dependencies: 227
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.user_role VALUES ('a3fc23c0-0241-45c9-9d2a-8e39bfadb632', '2416f619-c9e5-442b-8cbb-04f26c028ef5', 'b3863900-2933-449b-90b3-665e29b8f45b', '2026-05-20 03:41:13.317007+00', '2026-05-20 03:41:13.317007+00', 'ACTIVE', NULL);
INSERT INTO public.user_role VALUES ('91232203-63fa-4e98-a0ef-0eeb924e8f02', '48b3965b-9ca9-4c18-a936-0126754e52bd', '30ecc70b-533d-41a9-917f-1c8e1bda6f03', '2026-05-25 01:18:17.357074+00', '2026-05-25 01:18:17.357074+00', 'ACTIVE', NULL);
INSERT INTO public.user_role VALUES ('d14e4546-d87e-428d-923f-77d2441d80e3', '67708d45-2eca-4afd-922a-bc8b01e644f1', '30ecc70b-533d-41a9-917f-1c8e1bda6f03', '2026-05-25 05:05:45.437799+00', '2026-05-25 05:05:45.437799+00', 'ACTIVE', NULL);


--
-- TOC entry 3350 (class 2606 OID 17496)
-- Name: audit audit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.audit
    ADD CONSTRAINT audit_pkey PRIMARY KEY (id);


--
-- TOC entry 3352 (class 2606 OID 17498)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 3354 (class 2606 OID 17579)
-- Name: category category_slug_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_slug_key UNIQUE (slug);


--
-- TOC entry 3356 (class 2606 OID 17500)
-- Name: columnist columnist_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.columnist
    ADD CONSTRAINT columnist_pk PRIMARY KEY (id);


--
-- TOC entry 3358 (class 2606 OID 17502)
-- Name: digital_weekly digital_weekly_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.digital_weekly
    ADD CONSTRAINT digital_weekly_pk PRIMARY KEY (id);


--
-- TOC entry 3361 (class 2606 OID 17504)
-- Name: news news_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pk PRIMARY KEY (id);


--
-- TOC entry 3363 (class 2606 OID 17506)
-- Name: permission permission_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_name_key UNIQUE (name);


--
-- TOC entry 3365 (class 2606 OID 17508)
-- Name: permission permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- TOC entry 3367 (class 2606 OID 17510)
-- Name: role role_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_name_key UNIQUE (name);


--
-- TOC entry 3371 (class 2606 OID 17512)
-- Name: role_permission role_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT role_permission_pkey PRIMARY KEY (id);


--
-- TOC entry 3369 (class 2606 OID 17514)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 3373 (class 2606 OID 17516)
-- Name: role_permission uq_role_permission; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT uq_role_permission UNIQUE (role_id, permission_id);


--
-- TOC entry 3380 (class 2606 OID 17518)
-- Name: user_permission uq_user_permission; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT uq_user_permission UNIQUE (user_id, permission_id);


--
-- TOC entry 3384 (class 2606 OID 17520)
-- Name: user_role uq_user_role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT uq_user_role UNIQUE (user_id, role_id);


--
-- TOC entry 3376 (class 2606 OID 17522)
-- Name: user user_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- TOC entry 3382 (class 2606 OID 17524)
-- Name: user_permission user_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT user_permission_pkey PRIMARY KEY (id);


--
-- TOC entry 3378 (class 2606 OID 17526)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 3386 (class 2606 OID 17528)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- TOC entry 3374 (class 1259 OID 17529)
-- Name: idx_user_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_status ON public."user" USING btree (status_registry);


--
-- TOC entry 3359 (class 1259 OID 17530)
-- Name: news_category_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX news_category_id_idx ON public.news USING btree (category_id);


--
-- TOC entry 3395 (class 2620 OID 17531)
-- Name: category trg_audit_category; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_category AFTER INSERT OR DELETE OR UPDATE ON public.category FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3396 (class 2620 OID 17532)
-- Name: permission trg_audit_permission; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_permission AFTER INSERT OR DELETE OR UPDATE ON public.permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3397 (class 2620 OID 17533)
-- Name: role trg_audit_role; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_role AFTER INSERT OR DELETE OR UPDATE ON public.role FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3398 (class 2620 OID 17534)
-- Name: role_permission trg_audit_role_permission; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_role_permission AFTER INSERT OR DELETE OR UPDATE ON public.role_permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3399 (class 2620 OID 17535)
-- Name: user trg_audit_user; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_user AFTER INSERT OR DELETE OR UPDATE ON public."user" FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3400 (class 2620 OID 17536)
-- Name: user_permission trg_audit_user_permission; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_user_permission BEFORE UPDATE ON public.user_permission FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3401 (class 2620 OID 17537)
-- Name: user_role trg_audit_user_role; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER trg_audit_user_role AFTER INSERT OR DELETE OR UPDATE ON public.user_role FOR EACH ROW EXECUTE FUNCTION public.fn_audit_logger();


--
-- TOC entry 3387 (class 2606 OID 17538)
-- Name: category category_category_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 3389 (class 2606 OID 17543)
-- Name: role_permission fk_rp_permission; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES public.permission(id) ON DELETE CASCADE;


--
-- TOC entry 3390 (class 2606 OID 17548)
-- Name: role_permission fk_rp_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;


--
-- TOC entry 3391 (class 2606 OID 17553)
-- Name: user_permission fk_up_permission; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_up_permission FOREIGN KEY (permission_id) REFERENCES public.permission(id) ON DELETE CASCADE;


--
-- TOC entry 3392 (class 2606 OID 17558)
-- Name: user_permission fk_up_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_up_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE;


--
-- TOC entry 3393 (class 2606 OID 17563)
-- Name: user_role fk_user_role_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;


--
-- TOC entry 3394 (class 2606 OID 17568)
-- Name: user_role fk_user_role_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE;


--
-- TOC entry 3388 (class 2606 OID 17573)
-- Name: news news_category_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id);


-- Completed on 2026-06-01 23:04:11

--
-- PostgreSQL database dump complete
--

