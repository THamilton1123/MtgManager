package com.magMan.scryfall;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScryfallSet {
    @JsonProperty("id")
    private String setUuid;
    @JsonProperty("name")
    private String setName;
    @JsonProperty("code")
    private String setCode;
    @JsonProperty("released_at")
    private String setRelease;
    @JsonProperty("set_type")
    private String setType;
    @JsonProperty("block")
    private String setBlock;
    @JsonProperty("block_code")
    private String setBlockCode;
    @JsonProperty("icon_svg_uri")
    private String setIconUri;
    @JsonProperty("search_uri")
    private String setSearchUri;
    @JsonProperty("digital")
    private boolean setDigital; // true -> ONLY digital, filter out 'true'

    public String getSetUuid() {
        return setUuid;
    }

    public void setSetUuid(String setUuid) {
        this.setUuid = setUuid;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public String getSetRelease() {
        return setRelease;
    }

    public void setSetRelease(String setRelease) {
        this.setRelease = setRelease;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public String getSetBlock() {
        return setBlock;
    }

    public void setSetBlock(String setBlock) {
        this.setBlock = setBlock;
    }

    public String getSetBlockCode() {
        return setBlockCode;
    }

    public void setSetBlockCode(String setBlockCode) {
        this.setBlockCode = setBlockCode;
    }

    public String getSetIconUri() {
        return setIconUri;
    }

    public void setSetIconUri(String setIconUri) {
        this.setIconUri = setIconUri;
    }

    public String getSetSearchUri() {
        return setSearchUri;
    }

    public void setSetSearchUri(String setSearchUri) {
        this.setSearchUri = setSearchUri;
    }

    public boolean isSetDigital() {
        return setDigital;
    }

    public void setSetDigital(boolean setDigital) {
        this.setDigital = setDigital;
    }
}
