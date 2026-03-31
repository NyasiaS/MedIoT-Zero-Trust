ALTER TABLE devices
    ADD COLUMN IF NOT EXISTS api_key_hash VARCHAR(255),
    ADD COLUMN IF NOT EXISTS api_key_created_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS api_key_last_rotated_at TIMESTAMPTZ;

-- Optional: track failed auth attempts / lockout windows (nice for zero-trust realism)
ALTER TABLE devices
    ADD COLUMN IF NOT EXISTS failed_auth_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS last_failed_auth_at TIMESTAMPTZ;

-- Optional: ensure device_name uniqueness (uncomment if you want it)
-- CREATE UNIQUE INDEX IF NOT EXISTS ux_devices_device_name ON devices(device_name);

-- Optional: speed up auth verification lookup patterns (device key hash is checked after finding the device)
CREATE INDEX IF NOT EXISTS idx_devices_api_key_hash ON devices(api_key_hash);