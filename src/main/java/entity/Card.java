package entity;

/**
 * A class to represent a MTG card.
 *
 * @author thamilton12
 */

public class Card {

    //Instance Variables
    private String cardName;
    private int cardCmc;
    private String cardType;

    /**
     * Instantiates a new Card.
     */
    public Card() {
    }

    /**
     * Instantiates a new Card.
     *
     * @param cardName the card name
     * @param cardCmc  the card cmc
     * @param cardType the card type
     */
    public Card(String cardName, int cardCmc, String cardType) {
        this.cardName = cardName;
        this.cardCmc = cardCmc;
        this.cardType = cardType;
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

    @Override
    public String toString() {
        return "Card{" +
                "cardName='" + cardName + '\'' +
                ", cardCmc=" + cardCmc +
                ", cardType='" + cardType + '\'' +
                '}';
    }
}
