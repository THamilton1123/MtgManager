package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy="card", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Ruling> rulings = new ArrayList<>();

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
}
