CREATE SCHEMA my_schema;

-- ------------------------------------------------------------

DROP TABLE IF EXISTS "my_schema"."client";

CREATE TABLE my_schema.client (
    id integer NOT NULL,
    client_id varchar(255) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(255) DEFAULT NULL::character varying,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(255) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL::character varying,
    post_logout_redirect_uris varchar(1000) DEFAULT NULL::character varying,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NULL,
    token_settings varchar(2000) NULL,
    PRIMARY KEY (id)
);

INSERT INTO my_schema.client ("id", "client_id", "client_id_issued_at", "client_secret", "client_secret_expires_at", "client_name", "client_authentication_methods", "authorization_grant_types", "redirect_uris", "post_logout_redirect_uris", "scopes", "client_settings", "token_settings") VALUES (1, 'client_id_1', CURRENT_TIMESTAMP, '$2a$12$jObAjzMihhjDq2c83H2Cr.5kG62hi9u9a18msiMDVbmSi5FYoK0TO', NULL, 'client_name', 'client_secret_basic client_secret_post', 'authorization_code client_credentials', 'http://localhost:8011/oauth2/code/my-registration', NULL, 'openid profile email', NULL, NULL);

-- ------------------------------------------------------------

DROP TABLE IF EXISTS "my_schema"."users";

CREATE SEQUENCE "my_schema"."users_user_id_seq"
   START WITH 1
   INCREMENT BY 1
   MINVALUE 1
   MAXVALUE 2147483647
   CACHE 1;

CREATE TABLE "my_schema"."users"(
   id integer NOT NULL,
   username varchar(255) NOT NULL,
   password varchar(255) NOT NULL,
   email varchar(255) NOT NULL,
   created_at timestamp DEFAULT CURRENT_TIMESTAMP,
   updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (id)
);

CREATE UNIQUE INDEX "users_username_key" ON "my_schema"."users" ("username");
CREATE UNIQUE INDEX "users_email_key" ON "my_schema"."users" ("email");

INSERT INTO "my_schema"."users" ("id", "username", "password", "email", "created_at", "updated_at") VALUES (1, 'username', '$2a$10$qsgJQJk3Rt5NwWcKubE38ersxcur7bNvPPvdGmFA4hkds2LuehUiq', 'email@example.com', current_timestamp, current_timestamp);

-- ------------------------------------------------------------
