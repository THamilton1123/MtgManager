package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Legalities{

	@JsonProperty("standard")
	private String standard;

	@JsonProperty("oldschool")
	private String oldschool;

	@JsonProperty("historic")
	private String historic;

	@JsonProperty("legacy")
	private String legacy;

	@JsonProperty("competitivebrawl")
	private String competitivebrawl;

	@JsonProperty("paupercommander")
	private String paupercommander;

	@JsonProperty("oathbreaker")
	private String oathbreaker;

	@JsonProperty("gladiator")
	private String gladiator;

	@JsonProperty("standardbrawl")
	private String standardbrawl;

	@JsonProperty("alchemy")
	private String alchemy;

	@JsonProperty("pioneer")
	private String pioneer;

	@JsonProperty("commander")
	private String commander;

	@JsonProperty("modern")
	private String modern;

	@JsonProperty("pauper")
	private String pauper;

	@JsonProperty("premodern")
	private String premodern;

	@JsonProperty("future")
	private String future;

	@JsonProperty("vintage")
	private String vintage;

	@JsonProperty("duel")
	private String duel;

	@JsonProperty("predh")
	private String predh;

	@JsonProperty("tlr")
	private String tlr;

	@JsonProperty("brawl")
	private String brawl;

	@JsonProperty("penny")
	private String penny;

	@JsonProperty("timeless")
	private String timeless;

	public void setStandard(String standard){
		this.standard = standard;
	}

	public String getStandard(){
		return standard;
	}

	public void setOldschool(String oldschool){
		this.oldschool = oldschool;
	}

	public String getOldschool(){
		return oldschool;
	}

	public void setHistoric(String historic){
		this.historic = historic;
	}

	public String getHistoric(){
		return historic;
	}

	public void setLegacy(String legacy){
		this.legacy = legacy;
	}

	public String getLegacy(){
		return legacy;
	}

	public void setCompetitivebrawl(String competitivebrawl){
		this.competitivebrawl = competitivebrawl;
	}

	public String getCompetitivebrawl(){
		return competitivebrawl;
	}

	public void setPaupercommander(String paupercommander){
		this.paupercommander = paupercommander;
	}

	public String getPaupercommander(){
		return paupercommander;
	}

	public void setOathbreaker(String oathbreaker){
		this.oathbreaker = oathbreaker;
	}

	public String getOathbreaker(){
		return oathbreaker;
	}

	public void setGladiator(String gladiator){
		this.gladiator = gladiator;
	}

	public String getGladiator(){
		return gladiator;
	}

	public void setStandardbrawl(String standardbrawl){
		this.standardbrawl = standardbrawl;
	}

	public String getStandardbrawl(){
		return standardbrawl;
	}

	public void setAlchemy(String alchemy){
		this.alchemy = alchemy;
	}

	public String getAlchemy(){
		return alchemy;
	}

	public void setPioneer(String pioneer){
		this.pioneer = pioneer;
	}

	public String getPioneer(){
		return pioneer;
	}

	public void setCommander(String commander){
		this.commander = commander;
	}

	public String getCommander(){
		return commander;
	}

	public void setModern(String modern){
		this.modern = modern;
	}

	public String getModern(){
		return modern;
	}

	public void setPauper(String pauper){
		this.pauper = pauper;
	}

	public String getPauper(){
		return pauper;
	}

	public void setPremodern(String premodern){
		this.premodern = premodern;
	}

	public String getPremodern(){
		return premodern;
	}

	public void setFuture(String future){
		this.future = future;
	}

	public String getFuture(){
		return future;
	}

	public void setVintage(String vintage){
		this.vintage = vintage;
	}

	public String getVintage(){
		return vintage;
	}

	public void setDuel(String duel){
		this.duel = duel;
	}

	public String getDuel(){
		return duel;
	}

	public void setPredh(String predh){
		this.predh = predh;
	}

	public String getPredh(){
		return predh;
	}

	public void setTlr(String tlr){
		this.tlr = tlr;
	}

	public String getTlr(){
		return tlr;
	}

	public void setBrawl(String brawl){
		this.brawl = brawl;
	}

	public String getBrawl(){
		return brawl;
	}

	public void setPenny(String penny){
		this.penny = penny;
	}

	public String getPenny(){
		return penny;
	}

	public void setTimeless(String timeless){
		this.timeless = timeless;
	}

	public String getTimeless(){
		return timeless;
	}

	@Override
 	public String toString(){
		return 
			"Legalities{" + 
			"standard = '" + standard + '\'' + 
			",oldschool = '" + oldschool + '\'' + 
			",historic = '" + historic + '\'' + 
			",legacy = '" + legacy + '\'' + 
			",competitivebrawl = '" + competitivebrawl + '\'' + 
			",paupercommander = '" + paupercommander + '\'' + 
			",oathbreaker = '" + oathbreaker + '\'' + 
			",gladiator = '" + gladiator + '\'' + 
			",standardbrawl = '" + standardbrawl + '\'' + 
			",alchemy = '" + alchemy + '\'' + 
			",pioneer = '" + pioneer + '\'' + 
			",commander = '" + commander + '\'' + 
			",modern = '" + modern + '\'' + 
			",pauper = '" + pauper + '\'' + 
			",premodern = '" + premodern + '\'' + 
			",future = '" + future + '\'' + 
			",vintage = '" + vintage + '\'' + 
			",duel = '" + duel + '\'' + 
			",predh = '" + predh + '\'' + 
			",tlr = '" + tlr + '\'' + 
			",brawl = '" + brawl + '\'' + 
			",penny = '" + penny + '\'' + 
			",timeless = '" + timeless + '\'' + 
			"}";
		}
}