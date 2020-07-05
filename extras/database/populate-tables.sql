\connect bolsa_valores;

copy Setor(id, nome, subsetor)
    from '/bd-massdata/setor.csv' WITH (FORMAT csv);
copy Acao(nome,	codigo,	quantidade,	id,	id_setor)
    from '/bd-massdata/acao.csv' WITH (FORMAT csv);
copy Desempenhofinanceiro(id, evolucaolucroliquido_trimestral, evolucaolucroliquido_anual, hascrescimentolucroliquido_tresanos)
    from '/bd-massdata/desempenhofinanceiro.csv' WITH (FORMAT csv);
copy Multiplosfundamentalistas(id, preco_lucro,	preco_valorpatrimonial,	roe, dividabruta_patrimonioliquido,	media_precolucro, media_precovalorpatrimonial, caixadisponivel_dividabruta)
    from '/bd-massdata/multiplosfundamentalistas.csv' WITH (FORMAT csv);
copy Balanco(id, data, lucroliq_trimestral, nota, id_acao, trimestre, id_multiplos, id_desempenho, qtdpapeis, lucroliq_anual, cotacao, patrimonioliquido, dividabruta, caixadisponivel, justificativa_nota, isdailyupdated)
    from '/bd-massdata/balanco.csv' WITH (FORMAT csv);

\disconnect;

