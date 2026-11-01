package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RelatedUris{

	@JsonProperty("tcgplayer_infinite_decks")
	private String tcgplayerInfiniteDecks;

	@JsonProperty("gatherer")
	private String gatherer;

	@JsonProperty("edhrec")
	private String edhrec;

	@JsonProperty("tcgplayer_infinite_articles")
	private String tcgplayerInfiniteArticles;

	public void setTcgplayerInfiniteDecks(String tcgplayerInfiniteDecks){
		this.tcgplayerInfiniteDecks = tcgplayerInfiniteDecks;
	}

	public String getTcgplayerInfiniteDecks(){
		return tcgplayerInfiniteDecks;
	}

	public void setGatherer(String gatherer){
		this.gatherer = gatherer;
	}

	public String getGatherer(){
		return gatherer;
	}

	public void setEdhrec(String edhrec){
		this.edhrec = edhrec;
	}

	public String getEdhrec(){
		return edhrec;
	}

	public void setTcgplayerInfiniteArticles(String tcgplayerInfiniteArticles){
		this.tcgplayerInfiniteArticles = tcgplayerInfiniteArticles;
	}

	public String getTcgplayerInfiniteArticles(){
		return tcgplayerInfiniteArticles;
	}

	@Override
 	public String toString(){
		return 
			"RelatedUris{" + 
			"tcgplayer_infinite_decks = '" + tcgplayerInfiniteDecks + '\'' + 
			",gatherer = '" + gatherer + '\'' + 
			",edhrec = '" + edhrec + '\'' + 
			",tcgplayer_infinite_articles = '" + tcgplayerInfiniteArticles + '\'' + 
			"}";
		}
}