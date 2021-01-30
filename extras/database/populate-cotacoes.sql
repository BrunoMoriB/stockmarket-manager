\connect bolsa_valores;

/* Popular a tabela cotações */
insert into cotacao (data, valor, id_acao)
                        values ('2017-03-06', 12.25, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2017-06-05', 18.01, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2017-09-04', 16.19, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2017-12-04', 14.98, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2020-03-02', 19.72, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2020-06-08', 12.51, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2020-09-11', 11.31, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2020-12-07', 12.25, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2018-03-05', 15.7, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2018-06-04', 13.43, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2018-09-03', 12.74, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2018-12-03', 13.14, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2019-03-08', 15.38, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2019-06-03', 13.84, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2019-09-02', 18.96, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2019-12-02', 18.53, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2016-12-05', 14.6, (select id from acao where codigo = 'AALR3'));
insert into cotacao (data, valor, id_acao)
                        values ('2014-03-07', 11.64, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2014-06-02', 13.12, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2014-09-08', 15.15, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2014-12-08', 13.1, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2013-03-04', 15.16, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2013-06-03', 14.55, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2013-09-02', 12.11, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2013-12-02', 12.42, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2012-03-05', 13.67, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2012-06-04', 10.57, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2012-09-03', 12.48, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2012-12-03', 13.11, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2011-03-11', 12.37, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2011-06-06', 11.81, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2011-09-05', 10.4, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2011-12-05', 11.5, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2015-03-02', 12.11, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2015-06-08', 11.48, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2015-09-11', 8.58, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2015-12-07', 8.95, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2016-03-07', 10.02, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2016-06-06', 11.86, (select id from acao where codigo = 'ABCB4'));
insert into cotacao (data, valor, id_acao)
                        values ('2016-09-05', 14.39, (select id from acao where codigo = 'ABCB4'));
\disconnect;