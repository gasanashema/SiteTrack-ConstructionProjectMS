/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author jeremie
 */

public class LibraryCard implements Serializable{
    
    public static final long serialVersionUID=1L;
    private int id;
    private String cardNumber;
    private LocalDate issueDate;
    
    private LocalDate expirationDate;
    
    private ECardStatus status;
    
    private Member member;

    public LibraryCard() {
    }

    public LibraryCard(int id, String cardNumber, LocalDate issueDate, LocalDate expirationDate, ECardStatus status, Member member) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ECardStatus getStatus() {
        return status;
    }

    public void setStatus(ECardStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    
}
