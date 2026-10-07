package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * The type Ruling.
 */
@Entity(name="Ruling")
@Table(name="rulings")
public class Ruling {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private int id;
    @Column(name="ruling_text")
    private String rulingText;
    @ManyToOne
    private Card card;

    /**
     * Instantiates a new Ruling.
     */
    public Ruling() {
    }

    /**
     * Instantiates a new Ruling.
     *
     * @param rulingText the ruling text
     * @param card       the card
     */
    public Ruling(String rulingText, Card card) {
        this.rulingText = rulingText;
        this.card = card;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets ruling text.
     *
     * @return the ruling text
     */
    public String getRulingText() {
        return rulingText;
    }

    /**
     * Sets ruling text.
     *
     * @param rulingText the ruling text
     */
    public void setRulingText(String rulingText) {
        this.rulingText = rulingText;
    }

    /**
     * Gets card.
     *
     * @return the card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets card.
     *
     * @param card the card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Ruling{" +
                "id=" + id +
                ", rulingText='" + rulingText + '\'' +
                ", card=" + card.getCardName() +
                '}';
    }
}