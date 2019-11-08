DO $$
    DECLARE
        vals integer := 0;
    BEGIN
        SELECT COUNT(*) FROM column_headers INTO vals;
        IF vals = 0 THEN
            INSERT INTO column_headers (name) VALUES ('Defined');
            INSERT INTO column_headers (name) VALUES ('In Progress');
            INSERT INTO column_headers (name) VALUES ('Completed');
        END IF;
    END $$;
