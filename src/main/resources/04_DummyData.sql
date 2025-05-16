INSERT INTO customer (customer_name, record_Status)
VALUES
    ('Salling',1),
    ('DSV', 1);
INSERT INTO tenant (customer, tenant_ID, record_Status, tenant_Name, tenant_Type, admin_Email, communication_language)
VALUES
    ('10000000','SALLINGTEST',1,'Salling TEST',3,'test@egemen.dk','en_GB'),
	('10000000','SALLING',1,'Salling Production',2,'test@egemen.dk','en_GB'),
	('10000001','DSVTEST',1,'DSV Test',1,'test@egemen.dk','en_GB'),
	('10000001','DSV',1,'DSV Production',3,'test@egemen.dk','da_DK');

INSERT INTO tm_employee (full_name, username, password)
VALUES ('Tone Asik', 'tonton', 'yeterdugungelsin112');

INSERT INTO orgunit (tenant,external_id)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'10002345'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'10002345'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'10002345'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'10002345');
INSERT INTO orgunit (tenant,external_id)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'10002346'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'10002346'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'10002346'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'10002346'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'10002347'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'10002347'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'10002347'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'10002347');
INSERT INTO orgunit (tenant,external_id)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'10002348'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'10002348'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'10002348'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'10002348'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'10002349'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'10002349'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'10002349'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'10002349');


INSERT INTO orgunit_history (parent,start_date,name,record_status,parent_unit)
VALUES
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002345'),'1900-01-01','Chief Executive Office',1,null),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002345'),'1900-01-01','Chief Executive Office',1,null),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002345'),'1900-01-01','Chief Executive Office',1,null),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002345'),'1900-01-01','Chief Executive Office',1,null),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002346'),'1900-01-01','Finance',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002346'),'1900-01-01','Finance',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002346'),'1900-01-01','Finance',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002346'),'1900-01-01','Finance',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002347'),'1900-01-01','Logistic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002347'),'1900-01-01','Logistic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002347'),'1900-01-01','Logistic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002347'),'1900-01-01','Logistic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002345')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002348'),'1900-01-01','Denmark International',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002348'),'1900-01-01','Denmark International',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002348'),'1900-01-01','Denmark International',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002348'),'1900-01-01','Denmark International',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002349'),'1900-01-01','Denmark Domestic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002349'),'1900-01-01','Denmark Domestic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002349'),'1900-01-01','Denmark Domestic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002347')),
	((SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002349'),'1900-01-01','Denmark Domestic',1,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002347'));

INSERT INTO cost_center (tenant,external_id)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'98123456'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'98123456'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'98123456'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'98123456');
INSERT INTO cost_center_history (parent,start_date,name,record_status,parent_unit)
VALUES
	((SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='98123456'),'1900-01-01','General CC',1,null),
	((SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='98123456'),'1900-01-01','General CC',1,null),
	((SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='98123456'),'1900-01-01','General CC',1,null),
	((SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='98123456'),'1900-01-01','General CC',1,null);

INSERT INTO Company (tenant,external_id)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'SS01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'SS02'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'SS03'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'SS04');

INSERT INTO company_history (parent,start_date,name,default_language_pack,record_status,default_timezone)
VALUES
	((SELECT internal_id FROM company WHERE external_id='SS01'),'1900-01-01','Company A A/S','en_GB',1,'Europe/Copenhagen'),
	((SELECT internal_id FROM company WHERE external_id='SS02'),'1900-01-01','Company B A/S','en_GB',1,'Europe/Copenhagen'),
	((SELECT internal_id FROM company WHERE external_id='SS03'),'1900-01-01','Company C A/S','en_GB',1,'Europe/Copenhagen'),
	((SELECT internal_id FROM company WHERE external_id='SS04'),'1900-01-01','Company D A/S','en_GB',1,'Europe/Copenhagen');
 INSERT INTO Country_To_Company
VALUES
    ('DNK',(SELECT internal_id FROM Company where external_id = 'SS01')),
    ('DNK',(SELECT internal_id FROM Company WHERE external_id = 'SS02')),
    ('DNK',(SELECT internal_id FROM Company WHERE external_id = 'SS03')),
    ('DNK',(SELECT internal_id FROM Company WHERE external_id = 'SS04'));

 INSERT INTO Person (tenant,person_Id,language_pack)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'12345678','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'12345678','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'12345678','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'12345678','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'12345676','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'12345676','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'12345676','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'12345676','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),'12345675','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),'12345675','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),'12345675','en_GB'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),'12345675','en_GB');

 INSERT INTO Person_History (parent,start_date,end_date,first_name,middle_name,last_name,display_name)
VALUES
    ((SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01','9999-12-31','John','Allen','Smith','John Smith'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01','9999-12-31','John','Allen','Smith','John Smith'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01','9999-12-31','John','Allen','Smith','John Smith'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01','9999-12-31','John','Allen','Smith','John Smith'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01','9999-12-31','Carla','','Grant','Carla Grant'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01','9999-12-31','Carla','','Grant','Carla Grant'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01','9999-12-31','Carla','','Grant','Carla Grant'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01','9999-12-31','Carla','','Grant','Carla Grant'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01','9999-12-31','System','','Admin','System Admin'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01','9999-12-31','System','','Admin','System Admin'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01','9999-12-31','System','','Admin','System Admin'),
    ((SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01','9999-12-31','System','','Admin','System Admin');

 --Password of users ILoveWFM123?
 INSERT INTO Employment (tenant,person,employee_Id,username,password,email,primary_Employment,hire_Date)
VALUES
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'12345678','jsmith','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','jsmith@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),(SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'12345678','jsmith','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','jsmith@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'12345678','jsmith','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','jsmith@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),(SELECT internal_id FROM Person WHERE person_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'12345678','jsmith','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','jsmith@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'12345676','cgrant','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','cgrant@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),(SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'12345676','cgrant','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','cgrant@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'12345676','cgrant','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','cgrant@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),(SELECT internal_id FROM Person WHERE person_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'12345676','cgrant','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','cgrant@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'12345675','admin','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','admin@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING'),(SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'12345675','admin','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','admin@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST'),(SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'12345675','admin','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','admin@company.com',true,'2025-01-01'),
    ((SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV'),(SELECT internal_id FROM Person WHERE person_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'12345675','admin','$2a$10$Qz0K9e/pO99pvSVxzHFtmuyxdr7xd/4EfRFSUDDFXLvY5s6E6TdGe','admin@company.com',true,'2025-01-01');

INSERT INTO Employment_History (parent,start_Date,event,employee_Status,company,manager,hr,orgunit,cost_center,timezone)
VALUES
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS01'),null,null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002347'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS02'),null,null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002347'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS03'),null,null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002347'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS04'),null,null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002347'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS01'),(SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='10002348'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS02'),(SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='10002348'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS03'),(SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='10002348'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345676' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS04'),(SELECT internal_id FROM Employment WHERE employee_Id = '12345678' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),null,(SELECT internal_id FROM orgunit WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='10002348'),(SELECT internal_id FROM cost_center WHERE tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV') and external_id='98123456'),'Europe/Copenhagen'),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLINGTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS01'),null,null,null,null,null),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'SALLING')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS02'),null,null,null,null,null),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSVTEST')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS03'),null,null,null,null,null),
	((SELECT internal_id FROM Employment WHERE employee_Id = '12345675' and tenant = (SELECT internal_id FROM Tenant WHERE tenant_id = 'DSV')),'2025-01-01',1,1,(SELECT internal_id FROM Company WHERE external_id = 'SS04'),null,null,null,null,null);
