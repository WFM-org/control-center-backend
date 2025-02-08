-- START DROP
drop table IF EXISTS EmploymentHistory cascade;
drop table IF EXISTS Employment cascade;
drop table IF EXISTS PersonHistory cascade;
drop table IF EXISTS Person cascade;
drop table IF EXISTS CountryToCompany cascade;
drop table IF EXISTS Company cascade;
drop table IF EXISTS LocaleEnabled cascade;
drop table IF EXISTS Tenant cascade;
drop table IF EXISTS AllowedFieldOverrides cascade;
drop table IF EXISTS Customer cascade;
drop table IF EXISTS Locale cascade;
drop table IF EXISTS Country cascade;
drop type IF EXISTS status;
drop type IF EXISTS tenantType;
drop type IF EXISTS eventType;
drop extension if exists pgcrypto;
-- END DROP
-- Extension
CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- End Extension
-- START TYPES
CREATE TYPE status AS ENUM ('active', 'inactive');
CREATE TYPE tenantType AS ENUM ('prod', 'test', 'dev');
CREATE TYPE eventType AS ENUM ('hire', 'rehire', 'termination', 'change');
-- END START TYPES

-- START TABLE AllowedFieldOverrides
CREATE TABLE AllowedFieldOverrides (
                                       id int primary key,
                                       tableName VARCHAR(64) NOT NULL,
                                       fieldName VARCHAR(64) NOT NULL
);
-- END TABLE AllowedFieldOverrides

-- START TABLE Customer
CREATE SEQUENCE CustomerID
    START 10000000
    INCREMENT 1
    MINVALUE 10000000
    NO MAXVALUE
    CACHE 1;

CREATE TABLE Customer
(
    customerId   BIGINT NOT NULL DEFAULT nextval('CustomerID') PRIMARY KEY,
    customerName VARCHAR(64)     NOT null,
    status       status NOT NULL
);

ALTER SEQUENCE CustomerID OWNED BY Customer.customerId;
-- END TABLE Customer

-- START TABLE Locale
CREATE TABLE Locale(
                       localeId VARCHAR(10) PRIMARY KEY,
                       Name VARCHAR(32) NOT NULL
);
-- END TABLE Locale

-- START TABLE Country
CREATE TABLE Country
(
    isoCode3 CHAR(3) PRIMARY KEY,
    name VARCHAR(64),
    isoCode2 CHAR(2)
);
-- END TABLE Country

-- START TABLE Tenant
CREATE TABLE Tenant
(
    internalId UUID PRIMARY key DEFAULT gen_random_uuid(),
    customer BIGINT NOT NULL,
    tenantID VARCHAR(16) UNIQUE NOT NULL,
    status status NOT NULL,
    tenantName VARCHAR(64) NOT NULL,
    tenantType tenantType NOT NULL,
    adminEmail VARCHAR(128) NOT NULL,
    localeDefault VARCHAR(10) NOT NULL,
    CONSTRAINT FK_Customer FOREIGN KEY (customer) REFERENCES Customer (customerId),
    CONSTRAINT FK_Locale FOREIGN KEY (localeDefault) REFERENCES Locale (localeId)
);
-- END TABLE Tenant

-- START TABLE LocaleEnabled
CREATE TABLE LocaleEnabled
(
    locale  VARCHAR(10),
    tenant UUID ,
    PRIMARY KEY (locale, tenant),
    CONSTRAINT FK_Language FOREIGN KEY (Locale) REFERENCES Locale (localeId),
    CONSTRAINT FK_Tenant FOREIGN KEY (tenant) REFERENCES Tenant (internalId)
);
-- END TABLE LocaleEnabled

-- START TABLE Company
CREATE TABLE Company
(
    internalId UUID PRIMARY key DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    externalId VARCHAR(16) NOT NULL,
    name VARCHAR(64) NOT NULL,
    localeDefault VARCHAR(10),
    status status not null,
    CONSTRAINT unique_tenant_externalId UNIQUE (tenant, externalId),
    CONSTRAINT FK_Tenant FOREIGN KEY (tenant) REFERENCES Tenant (internalId),
    CONSTRAINT FK_Locale FOREIGN KEY (localeDefault) REFERENCES Locale (localeId)
);
-- END TABLE Company

-- START TABLE CountryToCompany
CREATE TABLE CountryToCompany
(
    country CHAR(3),
    company UUID,
    PRIMARY KEY (country, company),
    CONSTRAINT FK_Country FOREIGN KEY (country) REFERENCES Country (isoCode3),
    CONSTRAINT FK_Company FOREIGN KEY (company) REFERENCES Company (internalId)
);
-- END TABLE CountryToCompany

-- START TABLE Person
CREATE TABLE Person
(
    internalId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant UUID NOT NULL,
    personId VARCHAR(16) NOT NULL,
    username VARCHAR(16) NOT NULL,
    password VARCHAR(128),
    email VARCHAR(126),
    localeDecision VARCHAR(10),
    CONSTRAINT unique_tenant_personId UNIQUE (tenant, personId),
    CONSTRAINT unique_tenant_username UNIQUE (tenant, username),
    CONSTRAINT FK_Tenant FOREIGN KEY (tenant) REFERENCES Tenant (internalId),
    CONSTRAINT FK_Locale FOREIGN KEY (localeDecision) REFERENCES Locale (localeId)
);
-- END TABLE Person


-- START TABLE PersonHistory
CREATE TABLE PersonHistory
(
    person UUID,
    startDate DATE,
    endDate DATE NOT NULL DEFAULT '9999-12-31',
    firstName VARCHAR(64),
    middleName VARCHAR(64),
    lastName VARCHAR(64),
    displayName VARCHAR(128),
    PRIMARY KEY (person, startDate),
    CONSTRAINT FK_Person_history FOREIGN KEY (person) REFERENCES Person (internalId)
);
-- END TABLE PersonHistory

-- START TABLE Employment
CREATE TABLE Employment
(
    internalId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant UUID not null,
    person UUID NOT NULL,
    employeeID VARCHAR(16) NOT NULL,
    status status NOT NULL,
    primaryEmployment BOOLEAN NOT NULL,
    hireDate DATE NOT NULL,
    terminationDate DATE,
    CONSTRAINT unique_tenant_employeeID UNIQUE (tenant, employeeID),
    CONSTRAINT FK_Person FOREIGN KEY (person) REFERENCES Person (internalId)
);
-- END TABLE Employment

-- START TABLE EmploymentHistory
CREATE TABLE EmploymentHistory
(
    employment UUID,
    startDate DATE,
    endDate DATE NOT NULL DEFAULT '9999-12-31',
    event eventType NOT NULL,
    status status NOT NULL,
    company UUID NOT NULL,
    manager UUID,
    hr UUID,
    PRIMARY KEY (employment, startDate),
    CONSTRAINT FK_EmploymentId FOREIGN KEY (employment) REFERENCES Employment (internalId),
    CONSTRAINT FK_Company FOREIGN KEY (company) REFERENCES Company (internalId),
    CONSTRAINT FK_EmploymentManager FOREIGN KEY (manager) REFERENCES Employment (internalId),
    CONSTRAINT FK_EmploymentHr FOREIGN KEY (hr) REFERENCES Employment (internalId)
);
-- END TABLE EmploymentHistory 
    