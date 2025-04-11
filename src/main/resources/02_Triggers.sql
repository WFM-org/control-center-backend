CREATE OR REPLACE FUNCTION insert_employmenthistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Set end_date for the new record
    UPDATE employment_history
    SET "end_date" = COALESCE(
        (SELECT start_date - INTERVAL  ''1 day''
         FROM employment_history
         WHERE employment = NEW.employment
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE \'9999-12-31\'
    )
    WHERE employment = NEW.employment
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE employment_history
    SET end_date = NEW.start_date - INTERVAL  ''1 day''
    WHERE employment = NEW.employment
    AND start_date = (
        SELECT MAX(start_date)
        FROM employment_history
        WHERE employment = NEW.employment
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_insert_employmenthistory_end_date
AFTER INSERT
ON employment_history
FOR EACH ROW
EXECUTE FUNCTION insert_employmenthistory_end_date();




CREATE OR REPLACE FUNCTION update_employmenthistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update end_date for the current row
    UPDATE employment_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL  ''1 day''
         FROM employment_history
         WHERE employment = NEW.employment
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE employment = NEW."employment"
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE employment_history
    SET end_date = NEW.start_date - INTERVAL  ''1 day''
    WHERE employment = NEW.employment
    AND start_date = (
        SELECT MAX(start_date)
        FROM employmenthistory
        WHERE employment = NEW.employment
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_update_employmenthistory_end_date
AFTER UPDATE
ON employment_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)  -- Corrected column name
EXECUTE FUNCTION update_employmenthistory_end_date();



CREATE OR REPLACE FUNCTION delete_employmenthistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update the previous record’s end_date if a record is deleted
    UPDATE employment_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL  ''1 day''
         FROM employment_history
         WHERE employment = OLD.employment
         AND start_date > OLD.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE employment = OLD.employment
    AND start_date = (
        SELECT MAX(start_date)
        FROM employment_history
        WHERE employment = OLD.employment
        AND start_date < OLD.start_date
    );

    RETURN OLD;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_delete_employmenthistory_end_date
AFTER DELETE
ON employment_history
FOR EACH ROW
EXECUTE FUNCTION delete_employmenthistory_end_date();



CREATE OR REPLACE FUNCTION insert_personhistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Set end_date for the new record
    UPDATE person_history
    SET "end_date" = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM person_history
         WHERE person = NEW.person
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE person = NEW.person
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE person_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE person = NEW.person
    AND start_date = (
        SELECT MAX(start_date)
        FROM person_history
        WHERE person = NEW.person
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_insert_personhistory_end_date
AFTER INSERT
ON person_history
FOR EACH ROW
EXECUTE FUNCTION insert_personhistory_end_date();



CREATE OR REPLACE FUNCTION update_personhistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update end_date for the current row
    UPDATE person_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM person_history
         WHERE person = NEW.person
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE person = NEW."person"
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE person_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE person = NEW.person
    AND start_date = (
        SELECT MAX(start_date)
        FROM person_history
        WHERE person = NEW.person
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_update_personhistory_end_date
AFTER UPDATE
ON person_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)  -- Corrected column name
EXECUTE FUNCTION update_personhistory_end_date();



CREATE OR REPLACE FUNCTION delete_personhistory_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update the previous record’s end_date if a record is deleted
    UPDATE person_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM person_history
         WHERE person = OLD.person
         AND start_date > OLD.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE person = OLD.person
    AND start_date = (
        SELECT MAX(start_date)
        FROM person_history
        WHERE person = OLD.person
        AND start_date < OLD.start_date
    );

    RETURN OLD;
END;
' LANGUAGE plpgsql;


CREATE TRIGGER trg_delete_personhistory_end_date
AFTER DELETE
ON person_history
FOR EACH ROW
EXECUTE FUNCTION delete_personhistory_end_date();

CREATE OR REPLACE FUNCTION insert_orgunit_history_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Set end_date for the new record
    UPDATE orgunit_history
    SET "end_date" = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM orgunit_history
         WHERE orgunit = NEW.orgunit
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE orgunit = NEW.orgunit
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE orgunit_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE orgunit = NEW.orgunit
    AND start_date = (
        SELECT MAX(start_date)
        FROM orgunit_history
        WHERE orgunit = NEW.orgunit
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_insert_orgunit_history_end_date
AFTER INSERT
ON orgunit_history
FOR EACH ROW
EXECUTE FUNCTION insert_orgunit_history_end_date();



CREATE OR REPLACE FUNCTION update_orgunit_history_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update end_date for the current row
    UPDATE orgunit_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM orgunit_history
         WHERE orgunit = NEW.orgunit
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE orgunit = NEW."orgunit"
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE orgunit_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE orgunit = NEW.orgunit
    AND start_date = (
        SELECT MAX(start_date)
        FROM orgunit_history
        WHERE orgunit = NEW.orgunit
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_update_orgunit_history_end_date
AFTER UPDATE
ON orgunit_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)  -- Corrected column name
EXECUTE FUNCTION update_orgunit_history_end_date();



CREATE OR REPLACE FUNCTION func_orghistory_set_end_date_ondelete()
RETURNS TRIGGER AS E'
BEGIN
    -- Update the previous record’s end_date if a record is deleted
    UPDATE orgunit_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM orgunit_history
         WHERE orgunit = OLD.orgunit
         AND start_date > OLD.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE orgunit = OLD.orgunit
    AND start_date = (
        SELECT MAX(start_date)
        FROM orgunit_history
        WHERE orgunit = OLD.orgunit
        AND start_date < OLD.start_date
    );

    RETURN OLD;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_orghistory_set_end_date_ondelete
AFTER DELETE
ON orgunit_history
FOR EACH ROW
EXECUTE FUNCTION func_orghistory_set_end_date_ondelete();

CREATE OR REPLACE FUNCTION get_sequencenext(p_internalid UUID)
RETURNS BIGINT AS E'
DECLARE
    new_value BIGINT;
BEGIN
    UPDATE tenantsequence
    SET currentvalue = currentvalue + incrementby
    WHERE internalid = p_internalid
    RETURNING currentvalue INTO new_value;

    RETURN new_value;
END;
' LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION func_tenant_default_values()
RETURNS TRIGGER AS E'
DECLARE
    seq_id UUID;
BEGIN
    -- Insert into language_pack_enabled
    INSERT INTO language_pack_enabled (tenant, language_pack)
    VALUES (NEW.internal_id, NEW.communication_language);

    -- Insert into tenant_sequence and retrieve its internal_id
    INSERT INTO tenant_sequence (tenant, start_value, current_value)
    VALUES (NEW.internal_id, 10000000, 10000000)
    RETURNING internal_id INTO seq_id;  -- Capture the primary key

    -- Insert into tenant_general_config using the retrieved seq_id
    INSERT INTO tenant_general_config (tenant, employee_id_sequence)
    VALUES (NEW.internal_id, seq_id);

    RETURN NEW;
END;
' LANGUAGE plpgsql;


-- Step 2: Create the trigger
CREATE TRIGGER trg_tenant_default_values
AFTER INSERT ON tenant
FOR EACH ROW
EXECUTE FUNCTION func_tenant_default_values();

CREATE OR REPLACE FUNCTION insert_cost_center_history_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Set end_date for the new record
    UPDATE cost_center_history
    SET "end_date" = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM cost_center_history
         WHERE cost_center = NEW.cost_center
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE cost_center = NEW.cost_center
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE cost_center_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE cost_center = NEW.cost_center
    AND start_date = (
        SELECT MAX(start_date)
        FROM cost_center_history
        WHERE cost_center = NEW.cost_center
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_insert_cost_center_history_end_date
AFTER INSERT
ON cost_center_history
FOR EACH ROW
EXECUTE FUNCTION insert_cost_center_history_end_date();



CREATE OR REPLACE FUNCTION update_cost_center_history_end_date()
RETURNS TRIGGER AS E'
BEGIN
    -- Update end_date for the current row
    UPDATE cost_center_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM cost_center_history
         WHERE cost_center = NEW.cost_center
         AND start_date > NEW.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE cost_center = NEW."cost_center"
    AND start_date = NEW.start_date;

    -- Adjust end_date of the previous record
    UPDATE cost_center_history
    SET end_date = NEW.start_date - INTERVAL ''1 day''
    WHERE cost_center = NEW.cost_center
    AND start_date = (
        SELECT MAX(start_date)
        FROM cost_center_history
        WHERE cost_center = NEW.cost_center
        AND start_date < NEW.start_date
    );

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_update_cost_center_history_end_date
AFTER UPDATE
ON cost_center_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)  -- Corrected column name
EXECUTE FUNCTION update_cost_center_history_end_date();



CREATE OR REPLACE FUNCTION func_cost_center_history_set_end_date_ondelete()
RETURNS TRIGGER AS E'
BEGIN
    -- Update the previous record’s end_date if a record is deleted
    UPDATE cost_center_history
    SET end_date = COALESCE(
        (SELECT start_date - INTERVAL ''1 day''
         FROM cost_center_history
         WHERE cost_center = OLD.cost_center
         AND start_date > OLD.start_date
         ORDER BY start_date ASC
         LIMIT 1),
        DATE ''9999-12-31''
    )
    WHERE cost_center = OLD.cost_center
    AND start_date = (
        SELECT MAX(start_date)
        FROM cost_center_history
        WHERE cost_center = OLD.cost_center
        AND start_date < OLD.start_date
    );

    RETURN OLD;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_cost_center_history_set_end_date_ondelete
AFTER DELETE
ON cost_center_history
FOR EACH ROW
EXECUTE FUNCTION func_cost_center_history_set_end_date_ondelete();
