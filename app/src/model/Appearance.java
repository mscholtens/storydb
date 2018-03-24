package model;

/**
 * Created by Marijn on 23.12.17.
 */
public class Appearance {

    private Person person;
    private Book book;
    private String description;

    public Appearance(Person person, Book book, String description) {
        this.person = person;
        this.book = book;
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
