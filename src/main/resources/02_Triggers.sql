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


CREATE OR REPLACE FUNCTION func_effectivedated_enddate_on_insert()
RETURNS TRIGGER AS E'
DECLARE
    tbl_name text := TG_TABLE_NAME;
BEGIN
    -- Set end_date for the new inserted record
    EXECUTE format(''
        UPDATE %I
        SET end_date = COALESCE(
            (SELECT start_date - INTERVAL ''''1 day''''
             FROM %I
             WHERE parent = $1
             AND start_date > $2
             ORDER BY start_date ASC
             LIMIT 1),
            DATE ''''9999-12-31''''
        )
        WHERE parent = $1
        AND start_date = $2
    '', tbl_name, tbl_name)
    USING NEW.parent, NEW.start_date;

    -- Adjust end_date of the previous record
    EXECUTE format(''
        UPDATE %I
        SET end_date = $2 - INTERVAL ''''1 day''''
        WHERE parent = $1
        AND start_date = (
            SELECT MAX(start_date)
            FROM %I
            WHERE parent = $1
            AND start_date < $2
        )
    '', tbl_name, tbl_name)
    USING NEW.parent, NEW.start_date;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_company_history_func_effectivedated_enddate_on_insert
AFTER INSERT
ON company_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_insert();

CREATE TRIGGER trg_orgunit_history_func_effectivedated_enddate_on_insert
AFTER INSERT
ON cost_center_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_insert();

CREATE TRIGGER trg_cost_center_history_func_effectivedated_enddate_on_insert
AFTER INSERT
ON orgunit_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_insert();

CREATE TRIGGER trg_person_history_func_effectivedated_enddate_on_insert
AFTER INSERT
ON person_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_insert();

CREATE TRIGGER trg_employment_history_func_effectivedated_enddate_on_insert
AFTER INSERT
ON employment_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_insert();

CREATE OR REPLACE FUNCTION func_effectivedated_enddate_on_delete()
RETURNS TRIGGER AS E'
DECLARE
    tbl_name text := TG_TABLE_NAME;
BEGIN
    -- Update the previous record’s end_date if a record is deleted
    EXECUTE format(''
        UPDATE %I
        SET end_date = COALESCE(
            (SELECT start_date - INTERVAL ''''1 day''''
             FROM %I
             WHERE parent = $1
             AND start_date > $2
             ORDER BY start_date ASC
             LIMIT 1),
            DATE ''''9999-12-31''''
        )
        WHERE parent = $1
        AND start_date = (
            SELECT MAX(start_date)
            FROM %I
            WHERE parent = $1
            AND start_date < $2
        )
    '', tbl_name, tbl_name, tbl_name)
    USING OLD.parent, OLD.start_date;

    RETURN OLD;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_company_history_func_effectivedated_enddate_on_delete
AFTER DELETE
ON company_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_delete();

CREATE TRIGGER trg_cost_center_history_func_effectivedated_enddate_on_delete
AFTER DELETE
ON cost_center_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_delete();

CREATE TRIGGER trg_orgunit_history_func_effectivedated_enddate_on_delete
AFTER DELETE
ON orgunit_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_delete();

CREATE TRIGGER trg_person_history_func_effectivedated_enddate_on_delete
AFTER DELETE
ON person_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_delete();

CREATE TRIGGER trg_employment_history_func_effectivedated_enddate_on_delete
AFTER DELETE
ON employment_history
FOR EACH ROW
EXECUTE FUNCTION func_effectivedated_enddate_on_delete();

CREATE OR REPLACE FUNCTION func_effectivedated_enddate_on_update()
RETURNS TRIGGER AS E'
DECLARE
    tbl_name text := TG_TABLE_NAME;
BEGIN
    -- Update end_date for the current record
    EXECUTE format(''
        UPDATE %I
        SET end_date = COALESCE(
            (SELECT start_date - INTERVAL ''''1 day''''
             FROM %I
             WHERE parent = $1
             AND start_date > $2
             ORDER BY start_date ASC
             LIMIT 1),
            DATE ''''9999-12-31''''
        )
        WHERE parent = $1
        AND start_date = $2
    '', tbl_name, tbl_name)
    USING NEW.parent, NEW.start_date;

    -- Adjust end_date of the previous record
    EXECUTE format(''
        UPDATE %I
        SET end_date = $2 - INTERVAL ''''1 day''''
        WHERE parent = $1
        AND start_date = (
            SELECT MAX(start_date)
            FROM %I
            WHERE parent = $1
            AND start_date < $2
        )
    '', tbl_name, tbl_name)
    USING NEW.parent, NEW.start_date;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_company_history_func_effectivedated_enddate_on_update
AFTER UPDATE
ON company_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)
EXECUTE FUNCTION func_effectivedated_enddate_on_update();

CREATE TRIGGER trg_cost_center_history_func_effectivedated_enddate_on_update
AFTER UPDATE
ON cost_center_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)
EXECUTE FUNCTION func_effectivedated_enddate_on_update();

CREATE TRIGGER trg_orgunit_history_func_effectivedated_enddate_on_update
AFTER UPDATE
ON orgunit_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)
EXECUTE FUNCTION func_effectivedated_enddate_on_update();

CREATE TRIGGER trg_person_history_func_effectivedated_enddate_on_update
AFTER UPDATE
ON person_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)
EXECUTE FUNCTION func_effectivedated_enddate_on_update();

CREATE TRIGGER trg_employment_history_func_effectivedated_enddate_on_update
AFTER UPDATE
ON employment_history
FOR EACH ROW
WHEN (OLD.start_date IS DISTINCT FROM NEW.start_date)
EXECUTE FUNCTION func_effectivedated_enddate_on_update();
