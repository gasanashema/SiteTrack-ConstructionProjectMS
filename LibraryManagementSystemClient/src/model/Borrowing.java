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

public class Borrowing implements Serializable{
    
    public static final long serialVersionUID=1L;
    private int id;
    private LocalDate borrowingDate;
    private LocalDate returningDate;
    
    private Set<Book> books;
    
    private Member member;

    public Borrowing() {
    }

    public Borrowing(int id) {
        this.id = id;
    }

    public Borrowing(int id, LocalDate borrowingDate, LocalDate returningDate, Set<Book> books, Member member) {
        this.id = id;
        this.borrowingDate = borrowingDate;
        this.returningDate = returningDate;
        this.books = books;
        this.member = member;
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

    public LocalDate getReturningDate() {
        return returningDate;
    }

    public void setReturningDate(LocalDate returningDate) {
        this.returningDate = returningDate;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    
}
