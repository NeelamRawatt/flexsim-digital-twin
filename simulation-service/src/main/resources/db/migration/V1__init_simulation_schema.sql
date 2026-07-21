CREATE TABLE experiment_run_progress (
    run_id BIGSERIAL PRIMARY KEY, experiment_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL, stage VARCHAR(50) NOT NULL,
    message VARCHAR(300), error_message VARCHAR(1000),
    started_at TIMESTAMP NOT NULL, completed_at TIMESTAMP, updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_run_experiment_id ON experiment_run_progress (experiment_id);
CREATE INDEX idx_run_status ON experiment_run_progress (status);

CREATE TABLE simulation_context (
    experiment_id BIGINT PRIMARY KEY, selected_date DATE,
    start_time TIMESTAMP, end_time TIMESTAMP, max_recirculation_count INTEGER
);

CREATE TABLE parcel_details (
    id BIGSERIAL PRIMARY KEY, parcel_id VARCHAR(50), parcel_time_slot TIMESTAMP NOT NULL,
    parcel_date DATE, parcel_infeed_name VARCHAR(100), sort_name VARCHAR(100), reject_code VARCHAR(50),
    parcel_weight DOUBLE PRECISION, parcel_length DOUBLE PRECISION, parcel_width DOUBLE PRECISION,
    parcel_height DOUBLE PRECISION, parcel_volume DOUBLE PRECISION,
    parcel_edt_date DATE NOT NULL, terminal_id INTEGER
);
CREATE INDEX idx_parcel_edt_date_time_slot ON parcel_details (parcel_edt_date, parcel_time_slot);

CREATE TABLE zone_resource_detail (
    id BIGSERIAL PRIMARY KEY, shift_id INTEGER, zone_id VARCHAR(50),
    resource_id INTEGER, chutes VARCHAR(500), experiment_id BIGINT NOT NULL
);
CREATE INDEX idx_zone_resource_experiment_id ON zone_resource_detail (experiment_id);

CREATE TABLE infeed_resource_detail (
    id BIGSERIAL PRIMARY KEY, shift_id INTEGER, tc VARCHAR(50), infeed VARCHAR(50),
    zone_id VARCHAR(50), no_of_resources INTEGER, active BOOLEAN, experiment_id BIGINT NOT NULL
);
CREATE INDEX idx_infeed_resource_experiment_id ON infeed_resource_detail (experiment_id);

CREATE TABLE chute_details_kpi (
    seq_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL, chute_id INTEGER, type VARCHAR(50),
    containers_filled INTEGER, parcel_filled_in_cage INTEGER, parcel_throughput INTEGER,
    parcel_blocked INTEGER, parcel_handled_resource INTEGER, created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_chute_details_sim_exp_id ON chute_details_kpi (sim_exp_id);

CREATE TABLE exp_insights (
    insight_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL,
    total_parcels_scanned INTEGER, total_parcels_blocked INTEGER, total_parcel_throughput INTEGER,
    total_parcels_unloaded INTEGER, total_parcels_rejected INTEGER, max_recirculation_count INTEGER,
    total_parcels_throughput_without_rejected INTEGER, total_recirculation_count INTEGER,
    total_parcel_not_sorted INTEGER, created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_exp_insights_sim_exp_id UNIQUE (sim_exp_id)
);

CREATE TABLE resource_weight_handled_kpi (
    seq_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL, resource_id INTEGER,
    zone_name VARCHAR(100), chute_id VARCHAR(50), total_parcels_handled INTEGER,
    total_weight_handled INTEGER, tick_value INTEGER, shift_id INTEGER, created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_resource_weight_sim_exp_id ON resource_weight_handled_kpi (sim_exp_id);

CREATE TABLE infeed_parcels_unloaded_kpi_per_tick (
    seq_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL, tick_value INTEGER,
    infeed_name VARCHAR(100), parcel_unloaded INTEGER, shift_id INTEGER, created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_infeed_unloaded_sim_exp_id ON infeed_parcels_unloaded_kpi_per_tick (sim_exp_id);

CREATE TABLE parcel_insights_kpi_per_tick (
    seq_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL, tick_value INTEGER,
    parcel_scanned INTEGER, parcel_throughput INTEGER, parcel_rejected INTEGER,
    parcel_throughput_without_rejected INTEGER, parcel_blocked INTEGER, shift_id INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_parcel_insights_sim_exp_id ON parcel_insights_kpi_per_tick (sim_exp_id);

CREATE TABLE chute_insights_kpi_per_tick (
    seq_id BIGSERIAL PRIMARY KEY, sim_exp_id INTEGER NOT NULL, tick_value INTEGER,
    chute_id INTEGER, chute_type INTEGER, parcel_scanned INTEGER, parcel_throughput INTEGER,
    parcel_blocked INTEGER, shift_id INTEGER, parcel_weight INTEGER, created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_chute_insights_sim_exp_id ON chute_insights_kpi_per_tick (sim_exp_id);

CREATE TABLE acceptable_parcel_unit (
    acceptable_parcel_unit_id BIGSERIAL PRIMARY KEY,
    min_height DOUBLE PRECISION, max_height DOUBLE PRECISION,
    min_length DOUBLE PRECISION, max_length DOUBLE PRECISION,
    min_width DOUBLE PRECISION, max_width DOUBLE PRECISION,
    min_weight DOUBLE PRECISION, max_weight DOUBLE PRECISION,
    experiment_id BIGINT NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_acceptable_parcel_unit_experiment_id ON acceptable_parcel_unit (experiment_id);