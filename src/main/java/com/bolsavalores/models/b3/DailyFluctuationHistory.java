package com.bolsavalores.models.b3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyFluctuationHistory {

	private BizSts BizSts;
	private Msg Msg;
	private TradgFlr TradgFlr;
	
	@JsonProperty("BizSts")
	public BizSts getBizSts() {
		return BizSts;
	}
	public void setBizSts(BizSts bizSts) {
		BizSts = bizSts;
	}
	
	@JsonProperty("Msg")
	public Msg getMsg() {
		return Msg;
	}
	public void setMsg(Msg msg) {
		Msg = msg;
	}
	
	@JsonProperty("TradgFlr")
	public TradgFlr getTradgFlr() {
		return TradgFlr;
	}
	public void setTradgFlr(TradgFlr tradgFlr) {
		TradgFlr = tradgFlr;
	}
	
}
