/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author GeekNest
 */
@Entity
public class Borrowing implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "borrowing_date")
    private LocalDate borrowingDate;
    @Column(name = "return_date")
    private LocalDate returnDate;

    @ManyToMany
    @JoinTable(
            name = "book_borrowed_transaction",
            joinColumns = @JoinColumn(name = "borrowing_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member members;

    public Borrowing() {
    }

    public Borrowing(int id, LocalDate borrowingDate, LocalDate returnDate, Set<Book> books, Member members) {
        this.id = id;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
        this.books = books;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(LocalDate borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Member getMembers() {
        return members;
    }

    public void setMembers(Member members) {
        this.members = members;
    }

}
