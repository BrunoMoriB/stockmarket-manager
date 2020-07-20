package com.bolsavalores.services;

import java.text.ParseException;
import java.util.List;

import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.exceptions.B3ClientInfoException;
import com.bolsavalores.models.exceptions.BalancoNotFoundException;


public interface BalancoService {
	//TODO: Precisa de uma interface?
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException, BalancoNotFoundException, B3ClientInfoException;
	public Balanco salvaBalanco(Balanco balanco) throws ParseException;
	public Balanco salvaBalancoDailyUpdated(Balanco balancoDailyUpdate, long acaoId) throws ParseException, B3ClientInfoException, BalancoNotFoundException;
}
