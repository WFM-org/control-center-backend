
-- START DROP
drop table IF EXISTS employment_history cascade;
drop table IF EXISTS employment cascade;
drop table IF EXISTS person_History cascade;
drop table IF EXISTS person cascade;
drop table IF EXISTS country_to_company cascade;
drop table IF EXISTS company_history cascade;
drop table IF EXISTS company cascade;
drop table IF EXISTS language_pack_enabled cascade;
drop table IF EXISTS orgunit_history cascade;
drop table IF EXISTS orgunit cascade;
drop table IF EXISTS cost_center cascade;
drop table IF EXISTS cost_center_history cascade;
drop table IF EXISTS tenant_sequence cascade;
drop table IF EXISTS field_override cascade;
drop table IF EXISTS allowed_field_overrides cascade;
drop table IF EXISTS tenant_general_config cascade;
drop table IF EXISTS tenant_custom_code cascade;
drop table IF EXISTS tenant cascade;
drop table IF EXISTS customer cascade;
drop table IF EXISTS language_pack cascade;
drop table IF EXISTS country cascade;
drop table IF EXISTS timezone cascade;
drop table If EXISTS tm_employee cascade;

drop extension if exists pgcrypto;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE tm_employee (
    employee_id UUID PRIMARY key DEFAULT gen_random_uuid(),
	full_name VARCHAR(64) NOT NULL,
	username VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE allowed_field_overrides (
    id int primary key,
	table_name VARCHAR(64) NOT NULL,
    field_name VARCHAR(64) NOT NULL
);

CREATE SEQUENCE customer_id
    START 10000000
    INCREMENT 1
    MINVALUE 10000000
    NO MAXVALUE
    CACHE 1;

CREATE TABLE timezone (
    tz_name VARCHAR(64) PRIMARY KEY  -- 'Europe/Copenhagen' as unique identifier
);

CREATE TABLE customer
(
    customer_id   BIGINT NOT NULL DEFAULT nextval('customer_id') PRIMARY KEY,
    customer_name VARCHAR(64) NOT null,
    record_status smallint not null
);

ALTER SEQUENCE customer_id OWNED BY customer.customer_id;


CREATE TABLE language_pack(
    internal_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);


CREATE TABLE country
(
    isocode3 CHAR(3) PRIMARY KEY,
    name VARCHAR(64),
    isocode2 CHAR(2)
);


CREATE TABLE tenant
(
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    customer BIGINT NOT NULL,
    tenant_id VARCHAR(16) UNIQUE NOT NULL,
    record_status smallint not null,
    tenant_name VARCHAR(64) NOT NULL,
    tenant_type smallint NOT NULL,
    admin_email VARCHAR(128) NOT NULL,
    communication_language VARCHAR(10) NOT NULL,
    CONSTRAINT fk_tenant_customer FOREIGN KEY (customer) REFERENCES customer (customer_id),
    CONSTRAINT fk_tenant_language_pack FOREIGN KEY (communication_language) REFERENCES language_pack (internal_id)
    );

CREATE TABLE tenant_sequence (
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    tenant UUID not null,
    name varchar(32) not null default 'default',
    start_value bigint not null default 1,
    increment_by int not null default 1,
    current_value bigint not null default 1,
    CONSTRAINT fk_tenant_sequence_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
);

CREATE TABLE tenant_custom_code (
    tenant UUID not null,
    code_location smallint not null,
    code text not null

);


CREATE TABLE tenant_general_config (
    tenant UUID primary key,
    employee_id_sequence uuid not null,
    CONSTRAINT fk_tenantgeneralconfig_employeeidsequence FOREIGN KEY (employee_id_sequence) REFERENCES tenant_sequence (internal_id),
    CONSTRAINT fk_tenantgeneralconfig_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
);

CREATE TABLE field_override (
    tenant UUID,
    allowed_field_overrides int,
	visible boolean NOT NULL,
    mandatory boolean NOT NULL,
    editable boolean NOT NULL,
    PRIMARY KEY (tenant, allowed_field_overrides),
    CONSTRAINT fk_fieldoverride_allowedfieldoverride FOREIGN KEY (allowed_field_overrides) REFERENCES allowed_field_overrides (id)
);

CREATE TABLE orgunit
(
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    external_id VARCHAR(16) NOT NULL,
    CONSTRAINT unique_orgunit_external_id UNIQUE (tenant, external_id),
    CONSTRAINT fk_orgunit_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
);

CREATE TABLE orgunit_history
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent UUID not NUll,
    start_date DATE not NUll,
    end_date DATE,
    name VARCHAR(64) NOT NULL,
    record_status smallint not null,
    parent_unit UUID,
    CONSTRAINT unique_orgunitHistory_parentStart UNIQUE (parent, start_date),
    CONSTRAINT fk_orgunit_ref FOREIGN KEY (parent) REFERENCES orgunit (internal_id) ON DELETE cascade,
    CONSTRAINT fk_orgunit_parentunit FOREIGN KEY (parent_unit) REFERENCES orgunit (internal_id) ON DELETE CASCADE
);

CREATE TABLE cost_center
(
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    external_id VARCHAR(16) NOT NULL,
    CONSTRAINT unique_costcenter_external_id UNIQUE (tenant, external_id),
    CONSTRAINT fk_costcenter_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
);

CREATE TABLE  cost_center_history
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent UUID not NUll,
    start_date DATE not NUll,
    end_date DATE,
    name VARCHAR(64) NOT NULL,
    record_status smallint not null,
    parent_unit UUID,
    CONSTRAINT unique_costcenterHistory_parentStart UNIQUE (parent, start_date),
    CONSTRAINT fk_costcenter_ref FOREIGN KEY (parent) REFERENCES cost_center (internal_id) ON DELETE cascade,
    CONSTRAINT fk_costcenter_parentunit FOREIGN KEY (parent_unit) REFERENCES cost_center (internal_id) ON DELETE CASCADE
);

CREATE TABLE language_pack_enabled
(
    language_pack  VARCHAR(10),
    tenant UUID ,
    PRIMARY KEY (language_pack, tenant),
    CONSTRAINT fk_language_packenabled_language FOREIGN KEY (language_pack) REFERENCES language_pack (internal_id),
    CONSTRAINT fk_language_packenabled_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
    );

CREATE TABLE company
(
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    external_id VARCHAR(16) NOT NULL,
    CONSTRAINT unique_company_external_id UNIQUE (tenant, external_id),
    CONSTRAINT fk_company_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE
);

CREATE TABLE company_history
(
    internal_id UUID PRIMARY key DEFAULT gen_random_uuid(),
    parent UUID not NUll,
    start_date DATE not NUll,
    end_date DATE,
    name VARCHAR(64) NOT NULL,
    default_language_pack VARCHAR(10),
    default_timezone varChar(64),
    record_status smallint not null,
    country CHAR(3) not null,
    CONSTRAINT unique_companyHistory_parentStart UNIQUE (parent, start_date),
    CONSTRAINT fk_company_ref FOREIGN KEY (parent) REFERENCES company (internal_id) ON DELETE cascade,
    CONSTRAINT fk_company_language_pack FOREIGN KEY (default_language_pack) REFERENCES language_pack (internal_id),
    CONSTRAINT fk_company_timezone FOREIGN KEY (default_timezone) REFERENCES timezone (tz_name),
    CONSTRAINT fk_company_country FOREIGN KEY (country) REFERENCES country (isocode3)
);

CREATE TABLE person
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    person_id VARCHAR(16) NOT NULL,
    language_pack VARCHAR(10),
    CONSTRAINT unique_person_personid UNIQUE (tenant, person_id),
    CONSTRAINT fk_person_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_person_language_pack FOREIGN KEY (language_pack) REFERENCES language_pack (internal_id)
);


CREATE TABLE person_history
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent UUID not NUll,
    start_date DATE not NUll,
    end_date DATE,
    first_name VARCHAR(64),
    middle_name VARCHAR(64),
    last_name VARCHAR(64),
    display_name VARCHAR(128),
    CONSTRAINT unique_personHistory_parentStart UNIQUE (parent, start_date),
    CONSTRAINT fk_personhistory_parent FOREIGN KEY (parent) REFERENCES person (internal_id) ON DELETE CASCADE
);


CREATE TABLE employment
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant UUID NOT null,
    person UUID NOT NULL,
    employee_id VARCHAR(16) NOT NULL,
    username VARCHAR(16) NOT NULL,
    email VARCHAR(126),
    password varchar(256),
    login_method int default 0 check (login_method in(0,1,2)),--0 = Both, 1 = password, 2 = SSO
    primary_employment BOOLEAN NOT NULL,
    hire_date DATE NOT NULL,
    termination_date DATE,
    freeze_access_from DATE,
    CONSTRAINT unique_employment_employeeID UNIQUE (tenant, employee_id),
    CONSTRAINT unique_employment_username UNIQUE (tenant, username),
    CONSTRAINT fk_employment_person FOREIGN KEY (person) REFERENCES person (internal_id),
    CONSTRAINT fk_employment_tenant FOREIGN KEY (tenant) REFERENCES tenant (internal_id)

);

CREATE TABLE employment_history
(
    internal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent UUID not NUll,
    start_date DATE not NUll,
    end_date DATE,
    event smallint NOT NULL,
    employee_status smallint not null,
    company UUID NOT NULL,
    timezone varchar(64),
    manager UUID,
    hr UUID,
    orgunit UUID,
    cost_center UUID,
    CONSTRAINT unique_employmentHistory_parentStart UNIQUE (parent, start_date),
    CONSTRAINT fk_employmenthistory_orgunit FOREIGN KEY (orgunit) REFERENCES orgunit (internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_employmenthistory_cost_center FOREIGN KEY (cost_center) REFERENCES cost_center (internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_employmenthistory_id FOREIGN KEY (parent) REFERENCES employment (internal_id),
    CONSTRAINT fk_employmenthistory_company FOREIGN KEY (company) REFERENCES company (internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_employmenthistory_manager FOREIGN KEY (manager) REFERENCES employment (internal_id),
    CONSTRAINT fk_employmenthistory_hr FOREIGN KEY (hr) REFERENCES employment (internal_id),
    CONSTRAINT fk_employmenthistory_timezone FOREIGN KEY (timezone) REFERENCES timezone (tz_name)
);
