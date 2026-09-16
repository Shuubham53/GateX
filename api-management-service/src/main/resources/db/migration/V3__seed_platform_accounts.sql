INSERT INTO management_users (
    organization_id, role_id, username, password_hash, enabled, created_at, updated_at
)
SELECT
    NULL,
    roles.id,
    'platform-admin',
    '$2a$12$EYdIOY4K2q3uVWxSbg.mIe2Unk86JoPo8wldMscmjNHkAxB5kDxUO',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM roles
WHERE roles.name = 'PLATFORM_ADMIN';


INSERT INTO management_users (
    organization_id, role_id, username, password_hash, enabled, created_at, updated_at
)
SELECT
    NULL,
    roles.id,
    'gateway-service',
    '$2a$12$EYdIOY4K2q3uVWxSbg.mIe2Unk86JoPo8wldMscmjNHkAxB5kDxUO',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM roles
WHERE roles.name = 'GATEWAY_SERVICE';