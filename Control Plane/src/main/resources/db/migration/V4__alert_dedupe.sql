
ALTER TABLE alerts
    ADD COLUMN IF NOT EXISTS status VARCHAR(16) NOT NULL DEFAULT 'OPEN',
    ADD COLUMN IF NOT EXISTS first_seen TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS last_seen TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS count INTEGER NOT NULL DEFAULT 1;

-- Backfill for existing rows
UPDATE alerts
SET first_seen = COALESCE(first_seen, ts),
    last_seen  = COALESCE(last_seen, ts)
WHERE first_seen IS NULL OR last_seen IS NULL;

CREATE INDEX IF NOT EXISTS idx_alerts_device_type_status_ts
    ON alerts(device_id, type, status, ts);