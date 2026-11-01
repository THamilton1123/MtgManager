package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseUris{

	@JsonProperty("cardmarket")
	private String cardmarket;

	@JsonProperty("cardhoarder")
	private String cardhoarder;

	@JsonProperty("tcgplayer")
	private String tcgplayer;

	public void setCardmarket(String cardmarket){
		this.cardmarket = cardmarket;
	}

	public String getCardmarket(){
		return cardmarket;
	}

	public void setCardhoarder(String cardhoarder){
		this.cardhoarder = cardhoarder;
	}

	public String getCardhoarder(){
		return cardhoarder;
	}

	public void setTcgplayer(String tcgplayer){
		this.tcgplayer = tcgplayer;
	}

	public String getTcgplayer(){
		return tcgplayer;
	}

	@Override
 	public String toString(){
		return 
			"PurchaseUris{" + 
			"cardmarket = '" + cardmarket + '\'' + 
			",cardhoarder = '" + cardhoarder + '\'' + 
			",tcgplayer = '" + tcgplayer + '\'' + 
			"}";
		}
}