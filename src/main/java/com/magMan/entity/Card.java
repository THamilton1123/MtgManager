package com.magMan.entity;

/**
 * A class to represent a MTG card.
 *
 * @author thamilton12
 */

public class Card {

    //Instance Variables
    private int cardId;
    private String cardName;
    private int cardCmc;
    private String cardType;
    private int cardQuantity;

    /**
     * Instantiates a new Card.
     */
    public Card() {
    }

    /**
     * Instantiates a new Card.
     *
     * @param cardName     the card name
     * @param cardCmc      the card cmc
     * @param cardType     the card type
     * @param cardQuantity the card quantity
     */
    public Card(String cardName, int cardCmc, String cardType, int cardQuantity) {
        this.cardName = cardName;
        this.cardCmc = cardCmc;
        this.cardType = cardType;
        this.cardQuantity = cardQuantity;
    }

    /**
     * Gets card id.
     *
     * @return the card id
     */
    public int getCardId() {
        return cardId;
    }

    /**
     * Sets card id.
     *
     * @param cardId the card id
     */
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    /**
     * Gets card name.
     *
     * @return the card name
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Sets card name.
     *
     * @param cardName the card name
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Gets card cmc.
     *
     * @return the card cmc
     */
    public int getCardCmc() {
        return cardCmc;
    }

    /**
     * Sets card cmc.
     *
     * @param cardCmc the card cmc
     */
    public void setCardCmc(int cardCmc) {
        this.cardCmc = cardCmc;
    }

    /**
     * Gets card type.
     *
     * @return the card type
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets card type.
     *
     * @param cardType the card type
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * Gets card quantity.
     *
     * @return the card quantity
     */
    public int getCardQuantity() {
        return cardQuantity;
    }

    /**
     * Sets card quantity.
     *
     * @param cardQuantity the card quantity
     */
    public void setCardQuantity(int cardQuantity) {
        this.cardQuantity = cardQuantity;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardName='" + cardName + '\'' +
                ", cardCmc=" + cardCmc +
                ", cardType='" + cardType + '\'' +
                ", cardQuantity=" + cardQuantity +
                '}';
    }
}
