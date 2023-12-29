INSERT INTO usuarios (id, username, password, role) VALUES (100, 'ana@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_ADMIN');
INSERT INTO usuarios (id, username, password, role) VALUES (101, 'bia@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (102, 'bob@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (103, 'toby@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');

INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (10, 'Bianca Silva', '56196897004', 100);
INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (11, 'Roberto Gomes', '14848076005', 101);