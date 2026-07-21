CREATE TABLE uploaded_file (
    id              BIGSERIAL PRIMARY KEY,
    sim_exp_id      INTEGER NOT NULL,
    file_name       VARCHAR(255) NOT NULL,
    file_extension  VARCHAR(20),
    file_category   VARCHAR(30) NOT NULL,
    file_content    BYTEA,
    upload_date     DATE,
    uploaded_at     TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_uploaded_file_sim_exp_id ON uploaded_file (sim_exp_id);