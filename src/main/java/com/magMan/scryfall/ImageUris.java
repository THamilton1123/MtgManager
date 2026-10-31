package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageUris{

	@JsonProperty("small")
	private String small;

	@JsonProperty("normal")
	private String normal;

	@JsonProperty("art")
	private String art;

	@JsonProperty("large")
	private String large;

	@JsonProperty("thumb")
	private String thumb;

	@JsonProperty("grid")
	private String grid;

	@JsonProperty("display")
	private String display;

	@JsonProperty("png")
	private String png;

	@JsonProperty("border_crop")
	private String borderCrop;

	@JsonProperty("jpeg")
	private String jpeg;

	@JsonProperty("crop")
	private String crop;

	@JsonProperty("art_crop")
	private String artCrop;

	public void setSmall(String small){
		this.small = small;
	}

	public String getSmall(){
		return small;
	}

	public void setNormal(String normal){
		this.normal = normal;
	}

	public String getNormal(){
		return normal;
	}

	public void setArt(String art){
		this.art = art;
	}

	public String getArt(){
		return art;
	}

	public void setLarge(String large){
		this.large = large;
	}

	public String getLarge(){
		return large;
	}

	public void setThumb(String thumb){
		this.thumb = thumb;
	}

	public String getThumb(){
		return thumb;
	}

	public void setGrid(String grid){
		this.grid = grid;
	}

	public String getGrid(){
		return grid;
	}

	public void setDisplay(String display){
		this.display = display;
	}

	public String getDisplay(){
		return display;
	}

	public void setPng(String png){
		this.png = png;
	}

	public String getPng(){
		return png;
	}

	public void setBorderCrop(String borderCrop){
		this.borderCrop = borderCrop;
	}

	public String getBorderCrop(){
		return borderCrop;
	}

	public void setJpeg(String jpeg){
		this.jpeg = jpeg;
	}

	public String getJpeg(){
		return jpeg;
	}

	public void setCrop(String crop){
		this.crop = crop;
	}

	public String getCrop(){
		return crop;
	}

	public void setArtCrop(String artCrop){
		this.artCrop = artCrop;
	}

	public String getArtCrop(){
		return artCrop;
	}

	@Override
 	public String toString(){
		return 
			"ImageUris{" + 
			"small = '" + small + '\'' + 
			",normal = '" + normal + '\'' + 
			",art = '" + art + '\'' + 
			",large = '" + large + '\'' + 
			",thumb = '" + thumb + '\'' + 
			",grid = '" + grid + '\'' + 
			",display = '" + display + '\'' + 
			",png = '" + png + '\'' + 
			",border_crop = '" + borderCrop + '\'' + 
			",jpeg = '" + jpeg + '\'' + 
			",crop = '" + crop + '\'' + 
			",art_crop = '" + artCrop + '\'' + 
			"}";
		}
}