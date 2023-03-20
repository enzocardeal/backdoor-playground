INSERT INTO app_user(user_id, username, password, role) VALUES (
    '017e2808-c712-11ed-afa1-0242ac120002',
    'admin',
    '\$argon2id\$v=19\$m=1048576,t=4,p=8\$sDTUwLXcJU5SH40UD1wPFg\$TYG2P1RqoTLIayicU+MNKujGIqqF+0shrqnufqMg8J4',
    'ADMIN'
    ) ON CONFLICT DO NOTHING;
