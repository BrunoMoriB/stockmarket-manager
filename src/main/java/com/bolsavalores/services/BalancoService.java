package com.bolsavalores.services;

import java.text.ParseException;
import java.util.List;

import com.bolsavalores.models.Balanco;

public interface BalancoService {
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException;
	public Balanco salvaBalanco(Balanco balanco) throws ParseException;
}
