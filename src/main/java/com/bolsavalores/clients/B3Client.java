package com.bolsavalores.clients;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bolsavalores.models.b3.DailyFluctuationHistory;
import com.bolsavalores.models.b3.LstQtn;

@Component
public class B3Client {

	RestTemplate restTemplate = new RestTemplate();
	
	private static final String B3_URI = "http://cotacao.b3.com.br/mds/api/v1/DailyFluctuationHistory";
	
	public DailyFluctuationHistory getDailyFluctuationHistoryByCodigoAcao(String codigoAcao) {
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate.getForObject(B3_URI + "/" + codigoAcao, DailyFluctuationHistory.class);
	}
	
	public List<LstQtn> getListCotacoesDiariaByCodigoAcao(String codigoAcao){
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		DailyFluctuationHistory dailyFluctuation = restTemplate.getForObject(B3_URI + "/" + codigoAcao, DailyFluctuationHistory.class);
		List<LstQtn> listLstQtn = dailyFluctuation.getTradgFlr().getScty().getLstQtn();
		Collections.sort(listLstQtn);
		return listLstQtn;
	}
	
	public LstQtn getCotacaoMaisAtualByCodigoAcao(String codigoAcao) {
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		DailyFluctuationHistory dailyFluctuation = restTemplate.getForObject(B3_URI + "/" + codigoAcao, DailyFluctuationHistory.class);
		List<LstQtn> listLstQtn = dailyFluctuation.getTradgFlr().getScty().getLstQtn();
		Collections.sort(listLstQtn);
		return listLstQtn.get(listLstQtn.size() - 1);
	}
	
}
