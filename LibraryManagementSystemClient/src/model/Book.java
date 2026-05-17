/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 *
 * @author jeremie
 */
public class Book implements Serializable{
    
    public static final long serialVersionUID=1L;
    private int id;
    private String title;
    private String ISBN;
    private LocalDate publishedDate;
    private Barcode barcode;
    
    private Set<Author> authors;
    
    /**
     * book_author
     * book_id(FK) | author_id (FK) --> PK
     * --------------------
     * 1       | 1
     * 1       | 2
     * 2       | 1
     * 2       | 1
     */
    
    private Set<Borrowing> borrowings;

    public Book(int id, String title, String ISBN, LocalDate publishedDate, Barcode barcode, Set<Author> authors, Set<Borrowing> borrowings) {
        this.id = id;
        this.title = title;
        this.ISBN = ISBN;
        this.publishedDate = publishedDate;
        this.barcode = barcode;
        this.authors = authors;
        this.borrowings = borrowings;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
    
    
}
