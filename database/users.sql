CREATE TABLE IF NOT EXISTS users (
    user_id                        BIGSERIAL PRIMARY KEY,
    username                  VARCHAR(50)  NOT NULL UNIQUE,
    password                  VARCHAR(100) NOT NULL,
    role                      VARCHAR(20)  NOT NULL DEFAULT 'USER'
                              CHECK (role IN ('USER', 'ADMIN')),
    enabled                   BOOLEAN      NOT NULL DEFAULT TRUE,
    password_change_required  BOOLEAN      NOT NULL DEFAULT FALSE
);