CREATE TABLE IF NOT EXISTS devices (
                                       uuid UUID PRIMARY KEY,
                                       device_name VARCHAR(255) NOT NULL,
    role VARCHAR(64) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_devices_role ON devices(role);
CREATE INDEX IF NOT EXISTS idx_devices_enabled ON devices(enabled);

CREATE TABLE IF NOT EXISTS policies (
                                        role VARCHAR(64) PRIMARY KEY,
    policy_json VARCHAR(8192) NOT NULL DEFAULT '{}',
    updated_at TIMESTAMPTZ NOT NULL
    );

CREATE TABLE IF NOT EXISTS events (
                                      id BIGSERIAL PRIMARY KEY,
                                      device_id VARCHAR(128) NOT NULL,
    outcome VARCHAR(32) NOT NULL,
    ts TIMESTAMPTZ NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_events_device_ts ON events(device_id, ts);
CREATE INDEX IF NOT EXISTS idx_events_outcome ON events(outcome);

CREATE TABLE IF NOT EXISTS alerts (
                                      id BIGSERIAL PRIMARY KEY,
                                      device_id VARCHAR(128) NOT NULL,
    type VARCHAR(64) NOT NULL,
    severity VARCHAR(32) NOT NULL,
    message VARCHAR(1024) NULL,
    ts TIMESTAMPTZ NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_alerts_device_ts ON alerts(device_id, ts);
CREATE INDEX IF NOT EXISTS idx_alerts_severity ON alerts(severity);