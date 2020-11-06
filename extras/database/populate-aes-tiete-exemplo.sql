
--MASSA DE DADOS MODELO - AES TIETE ENERGIA

insert into Empresa (razao_social, nome_pregao, cnpj) values ('AES TIETE ENERGIA SA', 'AES TIETE E', '04128563000110');
update empresa set quantidade_papeis = 1995530000 where cnpj = '04128563000110';

insert into EmpresaSetor (id_empresa, id_setor)
            values ((select id from Empresa where cnpj = '04128563000110'),
                    (select id from Setor where nome = 'Utilidade Pública'));
insert into EmpresaSetor (id_empresa, id_setor)
            values ((select id from Empresa where cnpj = '04128563000110'),
                    (select id from Setor where nome = 'Energia Elétrica'));

insert into Acao (codigo, id_empresa)
            values ('TIET11', (select id from Empresa where cnpj = '04128563000110'));
insert into Acao (codigo, id_empresa)
            values ('TIET3', (select id from Empresa where cnpj = '04128563000110'));
insert into Acao (codigo, id_empresa)
            values ('TIET4', (select id from Empresa where cnpj = '04128563000110'));				
			
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (75000000, 1, 2020, 1527000000, 4259000000, 1396000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (118000000, 2, 2020, 1556000000, 4738000000, 2054000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (62000000, 1, 2019, 1578000000, 4160000000, 1159000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (35000000, 2, 2019, 1467000000, 4222000000, 1397000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (97000000, 3, 2019, 1529000000, 4225000000, 1285000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (106000000, 4, 2019, 1451000000, 4255000000, 1363000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (55000000, 1, 2018, 1612000000, 3926000000, 1739000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (93000000, 2, 2018, 1587000000, 4108000000, 2049000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (35000000, 3, 2018, 1520000000, 4144000000, 1274000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (105000000, 4, 2018, 1523000000, 4128000000, 1034000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (126000000, 1, 2017, 1706000000, 1475000000, 732000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (91000000, 2, 2017, 1628000000, 2413000000, 1584000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (38000000, 3, 2017, 1563000000, 3604000000, 1030000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (169000000, 4, 2017, 1558000000, 3590000000, 1205000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (74000000, 1, 2016, 2093000000, 1423000000, 734000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (103000000, 2, 2016, 1678000000, 1407000000, 347000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (98000000, 3, 2016, 1658000000, 1439000000, 466000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (83000000, 4, 2016, 1578000000, 1447000000, 578000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (110000000, 1, 2015, 6138000000, 4983000000, 1822000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (86000000, 2, 2015, 6170000000, 4573000000, 1405000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (44000000, 3, 2015, 6158000000, 5050000000, 1657000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (153000000, 4, 2015, 2018000000, 1392000000, 747000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (94000000, 1, 2014, 6683000000, 4261000000, 1490000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (-11000000, 2, 2014, 6253000000, 3854000000, 1093000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (-32000000, 3, 2014, 5880000000, 4517000000, 1562000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (30000000, 4, 2014, 5903000000, 4696000000, 1825000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (79000000, 1, 2013, 5640000000, 4239000000, 1818000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (291000000, 2, 2013, 6491000000, 3860000000, 1673000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (95000000, 3, 2013, 6310000000, 3948000000, 1910000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values ( 53000000, 4, 2013, 6538000000, 3866000000, 1640000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (140000000, 1, 2012, 8288000000, 4094000000, 2767000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (83000000, 2, 2012, 7553000000, 3961000000, 1569000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (94000000, 3, 2012, 7538000000, 3815000000, 1539000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (24000000, 4, 2012, 7344000000, 3719000000, 1443000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (162000000, 1, 2011, 7902000000, 4558000000, 2484000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (124000000, 2, 2011, 7188000000, 4432000000, 1723000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (179000000, 3, 2011, 7134000000, 4331000000, 1626000000, (select id from Empresa where cnpj = '04128563000110'), false);
insert into balanco
                (lucroliq_trimestral, trimestre, ano, patrimonioliquido, dividabruta, caixadisponivel, id_empresa, isdailyupdated)
                values (664000000, 4, 2011, 8059000000, 3420000000, 2246000000, (select id from Empresa where cnpj = '04128563000110'), false);
	
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-01-07', 15.48, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-02-03', 15.4, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-03-02', 18.8, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-04-06', 13.24, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-05-04', 14.45, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-06-08', 14.36, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-07-07', 15.85, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-01-07', 10.31, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-02-05', 11.51, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-03-08', 11.44, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-04-08', 11.15, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-05-08', 11.1, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-06-03', 11.34, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-07-08', 12.48, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-08-08', 12.51, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-09-02', 12.25, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-10-08', 11.83, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-11-08', 12.12, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-12-03', 13.09, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-01-12', 13.06, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-02-06', 12.11, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-03-08', 11.92, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-04-02', 11.95, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-05-07', 10.93, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-06-06', 10.2, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-07-02', 9.91, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-08-06', 10.19, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-09-04', 9.49, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-10-09', 10.19, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-11-05', 10.55, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-12-03', 10.4, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-01-12', 14.75, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-02-06', 14.99, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-03-08', 15.27, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-04-03', 13.87, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-05-08', 14.0, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-06-06', 13.38, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-07-06', 14.1, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-08-10', 14.06, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-09-04', 14.19, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-10-04', 14.32, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-11-08', 13.07, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-12-04', 13.08, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-01-08', 13.18, (select id from acao where codigo = 'TIET11'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-02-10', 13.77, (select id from acao where codigo = 'TIET11'), false);		
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-01-06', 3.98, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-02-03', 3.7, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-03-03', 4.28, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-04-06', 3.01, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-05-04', 3.29, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-06-08', 3.26, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-07-07', 3.41, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-01-15', 2.17, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-02-04', 2.35, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-03-06', 2.35, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-04-09', 2.23, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-05-09', 2.21, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-06-03', 2.29, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-07-08', 2.58, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-08-06', 2.77, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-09-05', 2.75, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-10-10', 2.58, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-11-04', 2.61, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-12-04', 2.79, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-01-11', 2.6, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-02-05', 2.42, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-03-08', 2.39, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-04-02', 2.42, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-05-08', 2.19, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-06-08', 2.01, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-07-03', 2.03, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-08-08', 2.09, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-09-03', 1.95, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-10-08', 2.05, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-11-08', 2.1, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-12-03', 2.12, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-01-10', 3.13, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-02-09', 3.07, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-03-06', 3.11, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-04-05', 2.78, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-05-11', 2.7, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-06-05', 2.76, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-07-05', 2.72, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-08-09', 2.91, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-09-04', 2.82, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-10-04', 2.84, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-11-08', 2.6, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-12-04', 2.58, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-01-14', 3.1, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-02-23', 3.3, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-03-08', 3.3, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-04-06', 3.3, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-05-03', 2.6, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-06-07', 2.8, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-07-06', 3.08, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-08-10', 3.21, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-09-05', 3.7, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-10-04', 3.15, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-11-08', 3.4, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-12-08', 3.73, (select id from acao where codigo = 'TIET3'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-01-06', 2.99, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-02-03', 2.96, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-03-03', 3.55, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-04-06', 2.57, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-05-04', 2.79, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-06-08', 2.8, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2020-07-07', 3.1, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-01-10', 2.17, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-02-04', 2.34, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-03-06', 2.28, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-04-10', 2.2, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-05-06', 2.15, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-06-05', 2.26, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-07-10', 2.45, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-08-05', 2.47, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-09-04', 2.37, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-10-09', 2.32, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-11-04', 2.35, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2019-12-03', 2.6, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-01-11', 2.61, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-02-05', 2.46, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-03-08', 2.38, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-04-02', 2.4, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-05-08', 2.14, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-06-08', 2.0, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-07-03', 2.02, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-08-08', 2.09, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-09-03', 1.95, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-10-08', 2.02, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-11-08', 2.11, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2018-12-03', 2.09, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-01-10', 2.78, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-02-09', 2.97, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-03-06', 3.1, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-04-05', 2.73, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-05-11', 2.7, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-06-05', 2.73, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-07-05', 2.75, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-08-09', 2.9, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-09-04', 2.83, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-10-04', 2.84, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-11-08', 2.59, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2017-12-04', 2.62, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-01-12', 2.57, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-02-11', 3.06, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-03-07', 2.7, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-04-07', 3.67, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-05-02', 3.1, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-06-07', 2.8, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-07-06', 3.0, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-08-10', 3.36, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-09-05', 3.6, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-10-04', 3.25, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-11-08', 3.24, (select id from acao where codigo = 'TIET4'), false);
insert into cotacao (data, valor, id_acao,  isdailyupdated)
                    values ('2016-12-08', 2.58, (select id from acao where codigo = 'TIET4'), false);			
			
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.22352356, '2020-05-11', '2020-05-20', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.11128889, '2019-12-10', '2020-07-29', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.26667388, '2019-11-08', '2019-11-22', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.08947567, '2019-08-08', '2019-10-23', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.16137538, '2019-05-09', '2019-07-24', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.19979852, '2019-04-25', '2019-07-24', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.12036997, '2018-12-11', '2019-07-24', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.1001031, '2018-11-08', '2018-11-22', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.26354901, '2018-08-09', '2018-10-10', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.17484835, '2018-05-10', '2018-07-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.12116923, '2018-04-27', '2018-07-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.03157859, '2017-12-12', '2018-01-10', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.01123158, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.09465645, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.26026697, '2017-08-10', '2017-09-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.3388253, '2017-05-10', '2017-05-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.08774986, '2017-04-20', '2017-05-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.15244255, '2016-12-09', '2017-05-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.2476903, '2016-11-08', '2016-11-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.31124422, '2016-08-09', '2016-09-27', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.23444324, '2016-05-06', '2016-06-27', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 1.21655619, '2016-04-28', '2016-05-25', (select id from acao where codigo = 'TIET11'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04470471, '2020-05-11', '2020-05-20', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.02225778, '2019-12-10', '2020-07-29', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.05333478, '2019-11-08', '2019-11-22', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.01789513, '2019-08-08', '2019-10-23', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.03227508, '2019-05-09', '2019-07-24', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.0399597, '2019-04-25', '2019-07-24', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.02407399, '2018-12-11', '2019-07-24', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.02002062, '2018-11-08', '2018-11-22', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.0527098, '2018-08-09', '2018-10-10', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.03496967, '2018-05-10', '2018-07-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.02423385, '2018-04-27', '2018-07-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.00631572, '2017-12-12', '2018-01-10', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.00224632, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.01893129, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.05205339, '2017-08-10', '2017-09-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.06776506, '2017-05-10', '2017-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.01754997, '2017-04-20', '2017-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.03048851, '2016-12-09', '2017-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04953806, '2016-11-08', '2016-11-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.06224884, '2016-08-09', '2016-09-27', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04688865, '2016-05-06', '2016-06-27', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.24331124, '2016-04-28', '2016-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.33743139, '2015-08-07', '2015-09-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.31, '2015-05-08', '2015-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.9751095, '2014-08-06', '2014-09-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.63584133, '2014-05-07', '2014-05-26', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.5657581, '2014-04-25', '2014-04-28', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.05595792, '2013-12-10', '2014-05-15', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.606545, '2013-11-06', '2013-11-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.65, '2013-08-07', '2013-09-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.71, '2013-08-07', '2013-09-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.51, '2013-05-08', '2013-05-27', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.45510623, '2013-02-27', '2013-05-07', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.06531992, '2012-12-20', '2013-05-07', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.63502094, '2012-11-06', '2012-11-22', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.62744247, '2012-08-03', '2012-08-27', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.66069945, '2012-05-03', '2012-05-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.63338259, '2012-04-16', '2012-05-15', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.07522438, '2011-12-07', '2012-05-15', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.61864906, '2011-11-10', '2011-11-25', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.44906749, '2011-08-11', '2011-09-22', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.53122893, '2011-05-12', '2011-05-26', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.51188001, '2011-03-24', '2011-05-17', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.07522458, '2010-12-03', '2011-05-17', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.54151797, '2010-11-05', '2010-12-07', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.43127652, '2010-08-06', '2010-09-15', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.59763003, '2010-05-14', '2010-06-08', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.28439446, '2010-04-30', '2010-05-19', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08446987, '2009-12-15', '2010-05-19', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.55400919, '2009-11-12', '2009-12-10', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.49845553, '2009-08-13', '2009-09-15', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.5387319, '2009-05-14', '2009-05-29', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.59260509, '2009-05-14', '2009-05-29', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.4166562, '2009-04-27', '2009-05-07', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08619483, '2008-12-19', '2009-05-07', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.46978054, '2008-11-13', '2008-11-27', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.33550335, '2008-08-13', '2008-08-28', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.43244764, '2008-05-15', '2008-05-29', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.33893866, '2008-04-23', '2008-05-08', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.07583877, '2007-12-20', '2008-05-08', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.35315311, '2007-11-21', '2007-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.35545364, '2007-08-24', '2007-09-03', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.40156501, '2007-05-28', '2007-06-06', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.41, '2007-04-19', '2007-05-03', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.09, '2006-11-17', '2006-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.27, '2006-11-17', '2006-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.29, '2006-11-17', '2006-11-30', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.76, '2006-08-17', '2006-08-29', (select id from acao where codigo = 'TIET3'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04470471, '2020-05-11', '2020-05-20', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.02225778, '2019-12-10', '2020-07-29', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.05333478, '2019-11-08', '2019-11-22', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.01789513, '2019-08-08', '2019-10-23', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.03227508, '2019-05-09', '2019-07-24', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.0399597, '2019-04-25', '2019-07-24', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.02407399, '2018-12-11', '2019-07-24', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.02002062, '2018-11-08', '2018-11-22', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.0527098, '2018-08-09', '2018-10-10', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.03496967, '2018-05-10', '2018-07-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.02423385, '2018-04-27', '2018-07-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.00631572, '2017-12-12', '2018-01-10', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.01893129, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.00224632, '2017-11-07', '2017-11-30', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.05205339, '2017-08-10', '2017-09-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.06776506, '2017-05-10', '2017-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.01754997, '2017-04-20', '2017-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.03048851, '2016-12-09', '2017-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04953806, '2016-11-08', '2016-11-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.06224884, '2016-08-09', '2016-09-27', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.04688865, '2016-05-06', '2016-06-27', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.24331124, '2016-04-28', '2016-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.37117453, '2015-08-07', '2015-09-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.34, '2015-05-08', '2015-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 1.07262045, '2014-08-06', '2014-09-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.69942546, '2014-05-07', '2014-05-26', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.6223339, '2014-04-25', '2014-04-28', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.06155371, '2013-12-10', '2014-05-15', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.6671995, '2013-11-06', '2013-11-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.56, '2013-05-08', '2013-05-27', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.50061685, '2013-02-27', '2013-05-07', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.07185191, '2012-12-20', '2013-05-07', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.69852304, '2012-11-06', '2012-11-22', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.69018672, '2012-08-03', '2012-08-27', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.7267694, '2012-05-03', '2012-05-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.69672085, '2012-04-16', '2012-05-15', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08274681, '2011-12-07', '2012-05-15', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.68051397, '2011-11-10', '2011-11-25', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.49397423, '2011-08-11', '2011-09-22', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.58435182, '2011-05-12', '2011-05-26', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.56306801, '2011-03-24', '2011-05-17', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08274704, '2010-12-03', '2011-05-17', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.59566977, '2010-11-05', '2010-12-07', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.47440418, '2010-08-06', '2010-09-15', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.65739304, '2010-05-14', '2010-06-08', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.3128339, '2010-04-30', '2010-05-19', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08446987, '2009-12-15', '2010-05-19', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.60941011, '2009-11-12', '2009-12-10', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.54830108, '2009-08-13', '2009-09-15', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.45832182, '2009-04-27', '2009-05-07', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08619483, '2008-12-19', '2009-05-07', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.51675859, '2008-11-13', '2008-11-27', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.36905369, '2008-08-13', '2008-08-28', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.47569241, '2008-05-15', '2008-05-29', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.37283253, '2008-04-23', '2008-05-08', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.08342265, '2007-12-20', '2008-05-08', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.38846843, '2007-11-21', '2007-11-30', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.390999, '2007-08-24', '2007-09-03', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.44172151, '2007-05-28', '2007-06-06', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.45, '2007-04-19', '2007-05-03', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('JCP', 0.1, '2006-11-17', '2006-11-30', (select id from acao where codigo = 'TIET4'));
insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    values ('DIVIDENDO', 0.84, '2006-08-17', '2006-08-29', (select id from acao where codigo = 'TIET4'));
												 
												 
 