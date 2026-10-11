package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Card color.
 */
@Entity(name="CardColor")
@Table(name="cards_colors")

public class CardColor implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private int id;

    @ManyToOne
    @JoinColumn(name="card_id", referencedColumnName = "id")
    private Card card;

    @ManyToOne
    @JoinColumn(name="color_id", referencedColumnName = "id")
    private Color color;

    /**
     * Instantiates a new Card color.
     */
    public CardColor() {
    }

    /**
     * Instantiates a new Card color.
     *
     * @param card  the card
     * @param color the color
     */
    public CardColor(Card card, Color color) {
        this.card = card;
        this.color = color;
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

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CardColor{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardColor)) {
            return false;
        }

        CardColor other = (CardColor) o;
        return Objects.equals(this.getCard(), other.getCard()) &&
                Objects.equals(this.getColor(), other.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getCard(), this.getColor());
    }
}
