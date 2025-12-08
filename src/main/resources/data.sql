-- ===============================
-- RESETAR TABELAS (Evita duplicatas)
-- ===============================

DELETE FROM client_data;
DELETE FROM client;

ALTER TABLE client_data AUTO_INCREMENT = 1;
ALTER TABLE client AUTO_INCREMENT = 1;

-- ===============================
-- CLIENTES
-- ===============================
INSERT INTO client (external_id, school_name, cafeteria_name, location, student_count) VALUES
('EXT001','Gluck Haus Cafe Marista Paranaense','CANTINA VLUPT','Curitiba PR',2609),
('EXT002','Cantina Santa Maria Marista','CANTINA VLUPT','Curitiba PR',2428),
('EXT003','Pôla Lanches Positivo Júnior','CANTINA VLUPT','Curitiba PR',1557),
('EXT004','Pôla Lanches Positivo Ângelo Sampaio','CANTINA VLUPT','Curitiba PR',908),
('EXT005','Gluck Haus Colégio Bom Jesus Centro','CANTINA VLUPT','Curitiba PR',1387),
('EXT006','Cantina Bon Bini Colégio Marista Anjo da Guarda','CANTINA VLUPT','Curitiba PR',1123),
('EXT007','Cardapium Bom Jesus Divina Providência','CANTINA VLUPT','Curitiba PR',812),
('EXT008','Cantina Arte & Sabor Colégio Adventista Alto Boqueirão','CANTINA VLUPT','Curitiba PR',1250),
('EXT009','Cantina Sesi Boqueirão','CANTINA VLUPT','Curitiba PR',0),
('EXT010','Cantina da Nona Escola Bambinata','CANTINA VLUPT','Curitiba PR',600),
('EXT011','Oliveiras Foods Colégio Vicentino São José','CANTINA VLUPT','Curitiba PR',613),
('EXT012','Graces Coffee Escola Grace','CANTINA VLUPT','Curitiba PR',620),
('EXT013','Cantinho do Sabor Curso e Colégio Acesso - Boqueirão','CANTINA VLUPT','Curitiba PR',545),
('EXT014','Eskillus Escola Interativa - Bacacheri','CANTINA VLUPT','Curitiba PR',354),
('EXT015','Red House Red House - Campus I','CANTINA VLUPT','Curitiba PR',200),
('EXT016','Cantina Solos Escola Solos','CANTINA VLUPT','Curitiba PR',330),
('EXT017','Coração da Nonna Santa Teresinha Escola Santa Teresinha do Menino Jesus','CANTINA VLUPT','Curitiba PR',130),
('EXT018','Cantina Escolar Cavalcanti Colégio Amplação','CANTINA VLUPT','Curitiba PR',926),
('EXT019','Maaloe Red House - Campus 2','CANTINA VLUPT','Curitiba PR',200),
('EXT020','La Merienda Gastronomia Colegio Assunção','CANTINA VLUPT','Curitiba PR',1000),
('EXT021','Taki Taki Esic Business & Marketing School -faculdade E Colégio Internacional','CANTINA VLUPT','Curitiba PR',700),
('EXT022','Cantina Lumen Escola Lumen Ltda','CANTINA VLUPT','Curitiba PR',241),
('EXT023','Cantina Cosmos Escola Cosmos','CANTINA VLUPT','Curitiba PR',158),
('EXT024','Cantina Escola Sao Carlos Borromeo','CANTINA VLUPT','Curitiba PR',450),
('EXT025','Cantina Sabor & Arte Colégio Horizonte','CANTINA VLUPT','Curitiba PR',780),
('EXT026','Cantina Bom Paladar Colégio Estrela','CANTINA VLUPT','Curitiba PR',920),
('EXT027','Cantina Delícias do Saber Escola Aurora','CANTINA VLUPT','Curitiba PR',300),
('EXT028','Cantina Pão Quente Escola Pioneira','CANTINA VLUPT','Curitiba PR',1100),
('EXT029','Cantina Vida & Sabor Instituto Progresso','CANTINA VLUPT','Curitiba PR',640),
('EXT030','Cantina NutriMais Colégio Integração','CANTINA VLUPT','Curitiba PR',850),
('EXT031','Cantina Terra Café Centro Educacional Vale Verde','CANTINA VLUPT','Curitiba PR',430),
('EXT032','Cantina Gourmet Kids Colégio Alfa','CANTINA VLUPT','Curitiba PR',720),
('EXT033','Cantina Bom Gosto Escola Prisma','CANTINA VLUPT','Curitiba PR',380),
('EXT034','Cantina Saborosa Escola Vitória','CANTINA VLUPT','Curitiba PR',510),
('EXT035','Cantina MixKids Ensino Brilhante','CANTINA VLUPT','Curitiba PR',610),
('EXT036','Cantina Trigo & Mel Escola Horizonte Azul','CANTINA VLUPT','Curitiba PR',200),
('EXT037','Cantina NutriSaber Colégio Avante','CANTINA VLUPT','Curitiba PR',1000),
('EXT038','Cantina Casa do Pão Escola Nacional','CANTINA VLUPT','Curitiba PR',310),
('EXT039','Cantina Tropical Escola Brasil Futuro','CANTINA VLUPT','Curitiba PR',900),
('EXT040','Cantina Mil Sabores Escola Criativa','CANTINA VLUPT','Curitiba PR',550),
('EXT041','Cantina Planet Café Colégio Alpha Plus','CANTINA VLUPT','Curitiba PR',410),
('EXT042','Cantina Estudante Feliz Escola Conquista','CANTINA VLUPT','Curitiba PR',820),
('EXT043','Cantina Bom Sabor Kids Escola Premium','CANTINA VLUPT','Curitiba PR',760),
('EXT044','Cantina Viva Escola Evolução','CANTINA VLUPT','Curitiba PR',640),
('EXT045','Cantina Sabores do Sol Colégio Diamante','CANTINA VLUPT','Curitiba PR',450),
('EXT046','Cantina NutriVida Escola Horizonte Novo','CANTINA VLUPT','Curitiba PR',280),
('EXT047','Cantina TikTak Kids Colégio Vitória Plus','CANTINA VLUPT','Curitiba PR',1250),
('EXT048','Cantina Delícia Urbana Colégio Expressão','CANTINA VLUPT','Curitiba PR',540),
('EXT049','Cantina Nova Era Escola Caminhos','CANTINA VLUPT','Curitiba PR',600),
('EXT050','Cantina SuperKids Colégio Aprende+','CANTINA VLUPT','Curitiba PR',720),
('EXT051','Cantina Central Kids Colégio Ideal','CANTINA VLUPT','Curitiba PR',310),
('EXT052','Cantina BigCoffee Escola Alpha Kids','CANTINA VLUPT','Curitiba PR',400),
('EXT053','Cantina Estação Sabor Colégio Mater Dei','CANTINA VLUPT','Curitiba PR',900),
('EXT054','Cantina Chef Kids Escola Progresso Júnior','CANTINA EXTRA','Belo Horizonte MG',780),
('EXT055','Cantina Alegria Escola Horizonte Kids','CANTINA EXTRA','Belo Horizonte MG',330),
('EXT056','Cantina Doce Sabor Colégio Majestade','CANTINA EXTRA','Belo Horizonte MG',510),
('EXT057','Cantina Sabor Real Escola UniKids','CANTINA Franchising','São Paulo SP',440),
('EXT058','Cantina Kids & Café Escola Master','CANTINA Franchising','São Paulo SP',810),
('EXT059','Cantina Mundo Verde Escola Alfa Júnior','CANTINA Franchising','São Paulo SP',290),
('EXT060','Cantina MaxKids Colégio Vitória Infantil','CANTINA Franchising','São Paulo SP',500);

INSERT INTO client_data
(data_id, client_id, month_date, revenue, expenses, order_count, notes, registered_students, average_cantina_per_student, average_pedagogical_per_student, average_ticket_app, cantina_percent, orders_outside_vpt, profitability, revenue_loss)
VALUES
(1,2,'2025-08-01',85000.00,0,5400,'Mês de agosto',85,35.00,40.00,14.50,0.08,45,42000.00,1300.00),
(2,2,'2025-09-01',89300.40,0,5600,'Mês de setembro',90,36.00,41.00,15.05,0.09,47,44000.00,1350.00),
(3,2,'2025-10-01',94000.00,0,5800,'Mês de outubro',95,37.00,42.00,15.62,0.10,50,46000.00,1400.00),
(4,2,'2025-11-01',90000.00,0,5500,'Mês de novembro',88,36.00,41.00,15.10,0.11,48,44000.00,1380.00),

(5,3,'2025-08-01',62000.00,0,4000,'Mês de agosto',62,25.50,30.00,15.50,0.08,30,30000.00,1000.00),
(6,3,'2025-09-01',65000.00,0,4200,'Mês de setembro',65,26.00,31.00,15.48,0.09,32,32000.00,1050.00),
(7,3,'2025-10-01',70000.00,0,4400,'Mês de outubro',70,27.50,33.00,15.91,0.10,35,34000.00,1100.00),
(8,3,'2025-11-01',68000.00,0,4300,'Mês de novembro',68,26.80,32.50,15.81,0.11,34,33000.00,1080.00),

(9,4,'2025-08-01',48000.00,0,3200,'Mês de agosto',48,24.00,28.00,15.00,0.08,28,23000.00,900.00),
(10,4,'2025-09-01',51000.00,0,3300,'Mês de setembro',51,25.00,29.00,15.45,0.09,29,24000.00,920.00),
(11,4,'2025-10-01',53000.00,0,3400,'Mês de outubro',53,25.50,30.00,15.59,0.10,30,25000.00,940.00),
(12,4,'2025-11-01',52000.00,0,3350,'Mês de novembro',52,25.00,29.50,15.52,0.11,29,24500.00,930.00),

(13,5,'2025-08-01',70000.00,0,4500,'Mês de agosto',28,28.00,33.00,15.55,0.07,35,35000.00,1100.00),
(14,5,'2025-09-01',73000.00,0,4600,'Mês de setembro',29,29.00,34.00,15.87,0.08,37,36500.00,1150.00),
(15,5,'2025-10-01',76000.00,0,4700,'Mês de outubro',30,30.00,35.00,16.17,0.06,38,38000.00,1200.00),
(16,5,'2025-11-01',75000.00,0,4650,'Mês de novembro',29,29.50,34.50,16.03,0.08,36,37500.00,1180.00);
