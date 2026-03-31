CREATE TABLE IF NOT EXISTS jwt_tokens (
                                          jti        VARCHAR(64) PRIMARY KEY,
    device_id  UUID        NOT NULL,
    role       VARCHAR(64) NOT NULL,
    issued_at  TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ NULL
    );

CREATE INDEX IF NOT EXISTS idx_jwt_tokens_device_id  ON jwt_tokens(device_id);
CREATE INDEX IF NOT EXISTS idx_jwt_tokens_expires_at ON jwt_tokens(expires_at);
CREATE INDEX IF NOT EXISTS idx_jwt_tokens_revoked_at ON jwt_tokens(revoked_at);