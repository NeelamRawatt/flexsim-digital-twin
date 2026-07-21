CREATE SEQUENCE exp_sequence START WITH 1001 INCREMENT BY 1;

CREATE TABLE experiment (
    experiment_id            BIGINT PRIMARY KEY DEFAULT nextval('exp_sequence'),
    experiment_name          VARCHAR(200) NOT NULL,
    terminal                 VARCHAR(100),
    sorting_type             VARCHAR(100),
    use_case_id              INTEGER NOT NULL,
    selected_date            DATE,
    start_time               TIMESTAMP,
    end_time                 TIMESTAMP,
    parcel_count             INTEGER,
    new_parcel_count         INTEGER,
    parcel_change_value      INTEGER,
    parcel_change_mode       VARCHAR(50),
    max_recirculation_count  INTEGER,
    username                 VARCHAR(100) NOT NULL,
    status                   VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    created_at               TIMESTAMP NOT NULL DEFAULT now()
);

ALTER SEQUENCE exp_sequence OWNED BY experiment.experiment_id;

CREATE INDEX idx_experiment_username ON experiment (username);
CREATE INDEX idx_experiment_status ON experiment (status);