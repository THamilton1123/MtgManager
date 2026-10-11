package com.magMan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Objects;

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
    @Column(name="ruling_date")
    private LocalDate rulingDate;
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
     * @param rulingDate the ruling date
     * @param rulingText the ruling text
     * @param card       the card
     */
    public Ruling(LocalDate rulingDate, String rulingText, Card card) {
        this.rulingDate = rulingDate;
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
     * Gets ruling date.
     *
     * @return the ruling date
     */
    public LocalDate getRulingDate() {
        return rulingDate;
    }

    /**
     * Sets ruling date.
     *
     * @param rulingDate the ruling date
     */
    public void setRulingDate(LocalDate rulingDate) {
        this.rulingDate = rulingDate;
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
                ", rulingDate='" + rulingDate + '\'' +
                ", rulingText='" + rulingText + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ruling)) {
            return false;
        }

        Ruling other = (Ruling) o;
        return Objects.equals(this.getRulingDate(), other.getRulingDate()) &&
                Objects.equals(this.getRulingText(), other.getRulingText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRulingDate(), this.getRulingText());
    }

}
