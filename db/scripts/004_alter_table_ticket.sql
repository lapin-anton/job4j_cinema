ALTER TABLE ticket
    ADD CONSTRAINT constraint_ticket UNIQUE (session_id, pos_row, cell);