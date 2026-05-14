/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jeremie
 */

public class Member implements Serializable{
    
    public static final long serialVersionUID=1L;
    
    private int id;
    private String names;
    private String email;
    private String phoneNumber;
    
    private List<Borrowing> borrowings;
    
    private List<LibraryCard> libraryCards;

    public Member() {
    }

    public Member(int id, String names, String email, String phoneNumber, List<Borrowing> borrowings, List<LibraryCard> libraryCards) {
        this.id = id;
        this.names = names;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowings = borrowings;
        this.libraryCards = libraryCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(List<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }

    public List<LibraryCard> getLibraryCards() {
        return libraryCards;
    }

    public void setLibraryCards(List<LibraryCard> libraryCards) {
        this.libraryCards = libraryCards;
    }
    
}
