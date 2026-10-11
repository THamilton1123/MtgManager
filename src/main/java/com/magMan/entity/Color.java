package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * The type Color.
 */
@Entity(name="Color")
@Table(name="colors")

public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @Column(name = "color_name")
    private String colorName;

    @OneToMany(mappedBy = "color", fetch = FetchType.EAGER)
    private Set<CardColor> cards = new HashSet<>();

    /**
     * Instantiates a new Color.
     */
    public Color() {
    }

    /**
     * Instantiates a new Color.
     *
     * @param colorName the color name
     */
    public Color(String colorName) {
        this.colorName = colorName;
    }

    /**
     * Add card
     *
     * @param card the card to add to the color
     */
    public void addCard(Card card) {
        CardColor cardColor = new CardColor(card, this);
        cards.add(cardColor);
        card.getColors().add(cardColor);
    }

    /**
     * Remove card.
     *
     * @param card the card to be removed from the color
     */
    public void removeCard(Card card) {
        for (Iterator<CardColor> iterator = cards.iterator();
             iterator.hasNext();) {
            CardColor cardColor = iterator.next();

            if (cardColor.getColor().equals(this) &&
                    cardColor.getCard().equals(card)) {
                iterator.remove();
                cardColor.getCard().getColors().remove(cardColor);
                cardColor.setCard(null);
                cardColor.setColor(null);
            }
        }
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
     * Gets color name.
     *
     * @return the color name
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * Sets color name.
     *
     * @param colorName the color name
     */
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    /**
     * Gets cards.
     *
     * @return the cards
     */
    public Set<CardColor> getCards() {
        return cards;
    }

    /**
     * Sets cards.
     *
     * @param cards the cards
     */
    public void setCards(Set<CardColor> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", colorName='" + colorName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Color)) {
            return false;
        }

        Color other = (Color) o;
        return Objects.equals(this.getColorName(), other.getColorName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getColorName());
    }
}
