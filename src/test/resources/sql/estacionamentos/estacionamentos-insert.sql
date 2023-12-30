INSERT INTO usuarios (id, username, password, role) VALUES (100, 'ana@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_ADMIN');
INSERT INTO usuarios (id, username, password, role) VALUES (101, 'bia@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (102, 'bob@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (103, 'toby@email.com', '$2a$10$Y65t19is4Gp0IWDkdcX2/eIyjr9kkarf/H/NO.F33DeEk29db7gdm', 'ROLE_CLIENTE');

INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (10, 'Bianca Silva', '56196897004', 100);
INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (11, 'Roberto Gomes', '14848076005', 101);

INSERT INTO vagas (id, codigo, status) VALUES (10, 'A-01', 'OCUPADA');
INSERT INTO vagas (id, codigo, status) VALUES (11, 'A-02', 'LIVRE');
INSERT INTO vagas (id, codigo, status) VALUES (12, 'A-03', 'OCUPADA');
INSERT INTO vagas (id, codigo, status) VALUES (13, 'A-04', 'OCUPADA');
INSERT INTO vagas (id, codigo, status) VALUES (14, 'A-05', 'LIVRE');

INSERT INTO clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 10, 10);
INSERT INTO clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 11, 12);
INSERT INTO clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 10, 13);

