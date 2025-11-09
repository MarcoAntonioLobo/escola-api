-- ===============================
-- CLIENTES
-- ===============================

INSERT INTO client (external_id, school_name, cafeteria_name, location, student_count) VALUES
('EXT008','Gluck Haus Cafe Marista Paranaense','CANTINA VLUPT','Curitiba PR',2609),
('EXT009','Cantina Santa Maria Marista','CANTINA VLUPT','Curitiba PR',2428),
('EXT022','Pôla Lanches Positivo Júnior','CANTINA VLUPT','Curitiba PR',1557),
('EXT023','Pôla Lanches Positivo Ângelo Sampaio','CANTINA VLUPT','Curitiba PR',908),
('EXT024','Gluck Haus Colégio Bom Jesus Centro','CANTINA VLUPT','Curitiba PR',1387),
('EXT030','Cantina Bon Bini Colégio Marista Anjo da Guarda','CANTINA VLUPT','Curitiba PR',1123),
('EXT036','Cardapium Bom Jesus Divina Providência','CANTINA VLUPT','Curitiba PR',812),
('EXT038','Cantina Arte & Sabor Colégio Adventista Alto Boqueirão','CANTINA VLUPT','Curitiba PR',1250),
('EXT052','Cantina Sesi Boqueirão','CANTINA VLUPT','Curitiba PR',0),
('EXT053','Cantina da Nona Escola Bambinata','CANTINA VLUPT','Curitiba PR',600),
('EXT056','Oliveiras Foods Colégio Vicentino São José','CANTINA VLUPT','Curitiba PR',613),
('EXT059','Graces Coffee Escola Grace','CANTINA VLUPT','Curitiba PR',620),
('EXT060','Cantinho do Sabor Curso e Colégio Acesso - Boqueirão','CANTINA VLUPT','Curitiba PR',545),
('EXT067','Eskillus Escola Interativa - Bacacheri','CANTINA VLUPT','Curitiba PR',354),
('EXT100','Red House Red House - Campus I','CANTINA VLUPT','Curitiba PR',200),
('EXT101','Cantina Solos Escola Solos','CANTINA VLUPT','Curitiba PR',330),
('EXT110','Coração da Nonna Santa Teresinha Escola Santa Teresinha do Menino Jesus','CANTINA VLUPT','Curitiba PR',130),
('EXT213','Cantina Escolar Cavalcanti Colégio Amplação','CANTINA VLUPT','Curitiba PR',926),
('EXT245','Maaloe Red House - Campus 2','CANTINA VLUPT','Curitiba PR',200),
('EXT251','La Merienda Gastronomia Colegio Assunção','CANTINA VLUPT','Curitiba - Guabirotuba Curitiba PR',1000),
('EXT318','Taki Taki Esic Business & Marketing School -faculdade E Colégio Internacional','CANTINA VLUPT','Curitiba PR',700),
('EXT352','Cantina Lumen Escola Lumen Ltda','CANTINA VLUPT','Curitiba PR',241),
('EXT381','Cantina Cosmos Escola Cosmos','CANTINA VLUPT','Curitiba PR',158),
('EXT388','Cantina Escola Sao Carlos Borromeo','CANTINA VLUPT','Curitiba PR',450);

-- ===============================
-- DADOS DE CLIENTES (Setembro e Outubro)
-- ===============================

INSERT INTO client_data (client_id, month_date, revenue, expenses, order_count, notes) VALUES
-- Cliente 8
(1,'2025-09-01',159611.53,7851.70,14333,'Setembro'),
(1,'2025-10-01',160000.00,8000.00,14500,'Outubro'),

-- Cliente 9
(2,'2025-09-01',65569.50,2668.14,7944,'Setembro'),
(2,'2025-10-01',66000.00,2700.00,8000,'Outubro'),

-- Cliente 22
(3,'2025-09-01',125315.40,4637.75,13478,'Setembro'),
(3,'2025-10-01',126000.00,4700.00,13500,'Outubro'),

-- Cliente 23
(4,'2025-09-01',6975.20,229.54,651,'Setembro'),
(4,'2025-10-01',7100.00,250.00,660,'Outubro'),

-- Cliente 24
(5,'2025-09-01',4596.00,521.85,447,'Setembro'),
(5,'2025-10-01',4700.00,540.00,460,'Outubro'),

-- Cliente 30
(6,'2025-09-01',151551.00,974.38,14497,'Setembro'),
(6,'2025-10-01',153000.00,1000.00,14500,'Outubro'),

-- Cliente 36
(7,'2025-09-01',49408.50,2620.11,5135,'Setembro'),
(7,'2025-10-01',50000.00,2700.00,5200,'Outubro'),

-- Cliente 38
(8,'2025-09-01',21286.29,1403.98,4501,'Setembro'),
(8,'2025-10-01',22000.00,1500.00,4600,'Outubro'),

-- Cliente 52
(9,'2025-09-01',0.00,0.00,0,'Setembro'),
(9,'2025-10-01',0.00,0.00,0,'Outubro'),

-- Cliente 53
(10,'2025-09-01',26702.00,801.14,3161,'Setembro'),
(10,'2025-10-01',27000.00,820.00,3200,'Outubro'),

-- Cliente 56
(11,'2025-09-01',0.00,0.00,0,'Setembro'),
(11,'2025-10-01',0.00,0.00,0,'Outubro'),

-- Cliente 59
(12,'2025-09-01',22596.85,713.36,2822,'Setembro'),
(12,'2025-10-01',23000.00,750.00,2900,'Outubro'),

-- Cliente 60
(13,'2025-09-01',13739.50,180.64,1258,'Setembro'),
(13,'2025-10-01',14000.00,200.00,1300,'Outubro'),

-- Cliente 67
(14,'2025-09-01',5332.70,159.57,733,'Setembro'),
(14,'2025-10-01',5500.00,170.00,750,'Outubro'),

-- Cliente 100
(15,'2025-09-01',9393.50,349.38,695,'Setembro'),
(15,'2025-10-01',9500.00,360.00,700,'Outubro'),

-- Cliente 101
(16,'2025-09-01',69592.36,1026.57,4659,'Setembro'),
(16,'2025-10-01',70000.00,1050.00,4700,'Outubro'),

-- Cliente 110
(17,'2025-09-01',4833.80,522.38,590,'Setembro'),
(17,'2025-10-01',5000.00,540.00,600,'Outubro'),

-- Cliente 213
(18,'2025-09-01',21811.00,1323.34,2715,'Setembro'),
(18,'2025-10-01',22000.00,1350.00,2800,'Outubro'),

-- Cliente 245
(19,'2025-09-01',16040.50,855.11,1386,'Setembro'),
(19,'2025-10-01',16500.00,870.00,1400,'Outubro'),

-- Cliente 251
(20,'2025-09-01',17015.50,1172.66,2516,'Setembro'),
(20,'2025-10-01',17200.00,1200.00,2550,'Outubro'),

-- Cliente 318
(21,'2025-09-01',0.00,239.20,0,'Setembro'),
(21,'2025-10-01',0.00,250.00,0,'Outubro'),

-- Cliente 352
(22,'2025-09-01',15256.40,445.78,1592,'Setembro'),
(22,'2025-10-01',15500.00,460.00,1600,'Outubro'),

-- Cliente 381
(23,'2025-09-01',0.00,200.00,0,'Setembro'),
(23,'2025-10-01',0.00,210.00,0,'Outubro'),

-- Cliente 388
(24,'2025-09-01',0.00,200.00,0,'Setembro'),
(24,'2025-10-01',0.00,210.00,0,'Outubro');
