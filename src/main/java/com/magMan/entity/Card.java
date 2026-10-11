package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

/**
 * A class to represent a MTG card.
 *
 * @author thamilton12
 */
@Entity
@Table(name="cards")
public class Card {

    //Instance Variables
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native",strategy="native")
    private int id;
    @Column(name="cardName")
    private String cardName;
    @Column(name="cardCmc")
    private Integer cardCmc;
    @Column(name="cardType")
    private String cardType;
    @Column(name="cardQuantity")
    private int cardQuantity;
    @OneToMany(mappedBy="card", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    private List<Ruling> rulings = new ArrayList<>();
    @OneToMany(mappedBy="card", fetch=FetchType.EAGER)
    private Set<CardColor> colors = new HashSet<CardColor>();

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
    public Card(String cardName, Integer cardCmc, String cardType, int cardQuantity) {
        this.cardName = cardName;
        this.cardCmc = cardCmc;
        this.cardType = cardType;
        this.cardQuantity = cardQuantity;
    }

    /**
     * Add ruling.
     *
     * @param ruling the ruling
     */
    public void addRuling(Ruling ruling) {
        rulings.add(ruling);
        ruling.setCard(this);
    }

    /**
     * Remove ruling.
     *
     * @param ruling the ruling
     */
    public void removeRuling(Ruling ruling) {
        rulings.remove(ruling);
        ruling.setCard(null);
    }

    /**
     * Add color.
     *
     * @param color the color to add to the card
     */
    public void addColor(Color color) {
        CardColor cardColor = new CardColor(this, color);
        colors.add(cardColor);
        color.getCards().add(cardColor);
    }

    /**
     * Remove color.
     *
     * @param color the color to be removed from the card
     */
    public void removeColor(Color color) {
        for (Iterator<CardColor> iterator = colors.iterator();
             iterator.hasNext();) {
            CardColor cardColor = iterator.next();

            if (cardColor.getCard().equals(this) &&
                    cardColor.getColor().equals(color)) {
                iterator.remove();
                cardColor.getColor().getCards().remove(cardColor);
                cardColor.setColor(null);
                cardColor.setCard(null);
            }
        }
    }

    /**
     * Gets card id.
     *
     * @return the card id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets card id.
     *
     * @param id the card id
     */
    public void setId(int id) {
        this.id = Card.this.id;
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
    public Integer getCardCmc() {
        return cardCmc;
    }

    /**
     * Sets card cmc.
     *
     * @param cardCmc the card cmc
     */
    public void setCardCmc(Integer cardCmc) {
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

    /**
     * Gets rulings.
     *
     * @return the rulings
     */
    public List<Ruling> getRulings() {
        return rulings;
    }

    /**
     * Sets rulings.
     *
     * @param rulings the rulings
     */
    public void setRulings(List<Ruling> rulings) {
        this.rulings = rulings;
    }

    /**
     * Gets colors.
     *
     * @return the colors
     */
    public Set<CardColor> getColors() {
        return colors;
    }

    /**
     * Sets colors.
     *
     * @param colors the colors
     */
    public void setColors(Set<CardColor> colors) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardName='" + cardName + '\'' +
                ", cardCmc=" + cardCmc +
                ", cardType='" + cardType + '\'' +
                ", cardQuantity=" + cardQuantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }

        Card other = (Card) o;
        return Objects.equals(this.getCardName(), other.getCardName()) &&
                Objects.equals(this.getCardCmc(), other.getCardCmc()) &&
                Objects.equals(this.getCardType(), other.getCardType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getCardName(), this.getCardCmc(), this.getCardType());
    }

}
