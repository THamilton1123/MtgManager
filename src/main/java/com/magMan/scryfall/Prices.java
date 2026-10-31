package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prices{

	@JsonProperty("usd_foil")
	private Object usdFoil;

	@JsonProperty("usd_etched")
	private Object usdEtched;

	@JsonProperty("tix")
	private Object tix;

	@JsonProperty("eur")
	private String eur;

	@JsonProperty("usd")
	private String usd;

	@JsonProperty("eur_foil")
	private Object eurFoil;

	public void setUsdFoil(Object usdFoil){
		this.usdFoil = usdFoil;
	}

	public Object getUsdFoil(){
		return usdFoil;
	}

	public void setUsdEtched(Object usdEtched){
		this.usdEtched = usdEtched;
	}

	public Object getUsdEtched(){
		return usdEtched;
	}

	public void setTix(Object tix){
		this.tix = tix;
	}

	public Object getTix(){
		return tix;
	}

	public void setEur(String eur){
		this.eur = eur;
	}

	public String getEur(){
		return eur;
	}

	public void setUsd(String usd){
		this.usd = usd;
	}

	public String getUsd(){
		return usd;
	}

	public void setEurFoil(Object eurFoil){
		this.eurFoil = eurFoil;
	}

	public Object getEurFoil(){
		return eurFoil;
	}

	@Override
 	public String toString(){
		return 
			"Prices{" + 
			"usd_foil = '" + usdFoil + '\'' + 
			",usd_etched = '" + usdEtched + '\'' + 
			",tix = '" + tix + '\'' + 
			",eur = '" + eur + '\'' + 
			",usd = '" + usd + '\'' + 
			",eur_foil = '" + eurFoil + '\'' + 
			"}";
		}
}