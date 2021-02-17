package com.bolsavalores.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DataBalancoUtils {

	private static final int DIA_PRIMEIRO = 1;
	private static final int DIA_CINCO = 5;
	
	private static final int PRIMEIRO_TRI = 1;
	private static final int SEGUNDO_TRI = 2;
	private static final int TERCEIRO_TRI = 3;
	
//	public Date getDataDiaUmByTrimestreAndAno(int trimestre, int ano) {
//		Month mes = getMonthByTrimestre(trimestre);
//		LocalDate data = LocalDate.of(ano, mes, DIA_PRIMEIRO);
//		return java.sql.Date.valueOf(data);
//	}
	
//	public Date getDataDiaUtilByTrimestreAndAno(int trimestre, int ano) {
//		Month mes = getMonthByTrimestre(trimestre);
//		LocalDate data = verificaFimDeSemanas(LocalDate.of(ano, mes, DIA_CINCO));
//		return java.sql.Date.valueOf(data);
//	}
	
//	public Month getMonthByTrimestre(int trimestre) {
//		if(trimestre == PRIMEIRO_TRI)
//			return Month.MAY;
//		else if(trimestre == SEGUNDO_TRI)
//			return Month.AUGUST;
//		else if(trimestre == TERCEIRO_TRI)
//			return Month.NOVEMBER;
//		else 
//			return Month.FEBRUARY;
//	}
	
//	private LocalDate verificaFimDeSemanas(LocalDate data) {
//		if(data.getDayOfWeek() == DayOfWeek.SATURDAY)
//			return data.plusDays(2);
//		if(data.getDayOfWeek() == DayOfWeek.SUNDAY)
//			return data.plusDays(1);
//		
//		return data;
//	}
}
