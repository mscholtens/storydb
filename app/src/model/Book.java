package model;

/**
 * Created by Marijn on 23.12.17.
 */
public class Book {

    private String name;
    private Era era;
    boolean isHighlighted;
    boolean isExpanded;

    public Book(String name, Era era) {
        this.name = name;
        this.era = era;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void expand() {
        isExpanded = true;
    }

    public void collapse() {
        isExpanded = false;
    }
}
