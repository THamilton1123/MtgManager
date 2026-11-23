package com.magMan.scryfall;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Scryfall search criteria.
 */
public class ScryfallSearchCriteria {
    private String name;          // Card name
    private boolean exactName = false; // true -> !"name", false -> name
    private String setCode;       // Set abbreviation (s:lea)
    private String rarity;        // common, uncommon, rare, mythic (r:...)
    private String oracleText;    // Rules text (o:...)
    private String superType;     // Supertype (t:...)
    private String type;          // Type (t:...)
    private String subType;       // Subtype (t:...)
    // Scryfall combines format and legality into a single keyword
    // (legal:commander, banned:modern, restricted:vintage).
    // If legality is "Not Legal", Scryfall uses -legal:format
    private String legality;      // Legal, Not Legal, Banned, Restricted (paired with format)
    private String format;        // Standard, Commander, Vintage, Legacy, etc. (paired with legality)
    // Because manaCost uses {2}{U}, JAX-RS WebTarget.queryParam() will throw an IllegalArgumentException
    // if { and } aren't escaped or if the URI template parser sees them. Replacing { with %7B and } with %7D
    // (or passing a pre-encoded URI) prevents that crash.
    private String manaCost;      // Specific mana symbols (m:{2}{U})
    private Integer cmc;          // Mana value
    private boolean noManaCost = false; // true -> -has:manacost
    private List<String> colors = new ArrayList<>(); // e.g., ["U", "R"]
    private String colorSearchType; // Exactly (c=UR), Include (c:UR), or Exclude (-c:UR), Within (c<=UR)
    private List<String> colorIdentity = new ArrayList<>(); // e.g., ["W", "B", "G"]
    private String colorIdentitySearchType; // Exactly (identity=WBG), Include (identity:WBG), or Exclude (-identity:WBG), Within (identity<=WBG)
    private String power;        // Creature's starting power value
    private String toughness;    // Creature's starting toughness value
    private String loyalty;      // Planeswalker's starting Loyalty value
    private String flavorText;    // Printing's flavor text
    private String artistName;    // Name of artist credited on Printing(s)
    private String language;      // Language of Printing
    private String cmcOperator;   // "=", "<", "<=", ">", ">="
    private String powerOperator; // "=", "<", "<=", ">", ">="
    private String toughnessOperator; // "=", "<", "<=", ">", ">="
    private String loyaltyOperator; // "=", "<", "<=", ">", ">="
    private String uniqueMode = "cards"; // "cards", "art", or "prints"
    private boolean includeExtras = false;
    private boolean includeVariations = false;
    private boolean includeMultilingual = false;

    public ScryfallSearchCriteria() {}

    /**
     *
     * @param req
     * @return
     */
    public static ScryfallSearchCriteria buildCriteriaFromRequest(HttpServletRequest req) {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();

        // Query filter: Card Name
        String searchName = req.getParameter("searchName");
        if (searchName != null && !searchName.trim().isEmpty()) {
            criteria.setName(searchName);
        }
        criteria.setExactName("true".equals(req.getParameter("exactName")));

        // Query filter: Set
        String setCode = req.getParameter("setCode");
        if (setCode != null && !setCode.trim().isEmpty()) {
            criteria.setSetCode(setCode.trim());
        }

        // Query filter: Rarity
        String rarity = req.getParameter("rarity");
        if (rarity != null && !rarity.trim().isEmpty()) {
            criteria.setRarity(rarity.trim());
        }

        // Query parameter: unique mode ("cards", "art", or "prints")
        String uniqueMode = req.getParameter("uniqueMode");
        if (uniqueMode != null && !uniqueMode.trim().isEmpty()) {
            criteria.setUniqueMode(uniqueMode.trim());
        }

        // Query parameters: include extras, variations, and/or multilingual
        criteria.setIncludeExtras("true".equals(req.getParameter("includeExtras")));
        criteria.setIncludeVariations("true".equals(req.getParameter("includeVariations")));
        criteria.setIncludeMultilingual("true".equals(req.getParameter("includeMultilingual")));

        return criteria;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExactName() {
        return exactName;
    }

    public void setExactName(boolean exactName) {
        this.exactName = exactName;
    }

    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getOracleText() {
        return oracleText;
    }

    public void setOracleText(String oracleText) {
        this.oracleText = oracleText;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getLegality() {
        return legality;
    }

    public void setLegality(String legality) {
        this.legality = legality;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public Integer getCmc() {
        return cmc;
    }

    public void setCmc(Integer cmc) {
        this.cmc = cmc;
    }

    public boolean isNoManaCost() {
        return noManaCost;
    }

    public void setNoManaCost(boolean noManaCost) {
        this.noManaCost = noManaCost;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getColorSearchType() {
        return colorSearchType;
    }

    public void setColorSearchType(String colorSearchType) {
        this.colorSearchType = colorSearchType;
    }

    public List<String> getColorIdentity() {
        return colorIdentity;
    }

    public void setColorIdentity(List<String> colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public String getColorIdentitySearchType() {
        return colorIdentitySearchType;
    }

    public void setColorIdentitySearchType(String colorIdentitySearchType) {
        this.colorIdentitySearchType = colorIdentitySearchType;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public String getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCmcOperator() {
        return cmcOperator;
    }

    public void setCmcOperator(String cmcOperator) {
        this.cmcOperator = cmcOperator;
    }

    public String getPowerOperator() {
        return powerOperator;
    }

    public void setPowerOperator(String powerOperator) {
        this.powerOperator = powerOperator;
    }

    public String getToughnessOperator() {
        return toughnessOperator;
    }

    public void setToughnessOperator(String toughnessOperator) {
        this.toughnessOperator = toughnessOperator;
    }

    public String getLoyaltyOperator() {
        return loyaltyOperator;
    }

    public void setLoyaltyOperator(String loyaltyOperator) {
        this.loyaltyOperator = loyaltyOperator;
    }

    public String getUniqueMode() {
        return uniqueMode;
    }

    public void setUniqueMode(String uniqueMode) {
        this.uniqueMode = uniqueMode;
    }

    public boolean isIncludeExtras() {
        return includeExtras;
    }

    public void setIncludeExtras(boolean includeExtras) {
        this.includeExtras = includeExtras;
    }

    public boolean isIncludeVariations() {
        return includeVariations;
    }

    public void setIncludeVariations(boolean includeVariations) {
        this.includeVariations = includeVariations;
    }

    public boolean isIncludeMultilingual() {
        return includeMultilingual;
    }

    public void setIncludeMultilingual(boolean includeMultilingual) {
        this.includeMultilingual = includeMultilingual;
    }
}
