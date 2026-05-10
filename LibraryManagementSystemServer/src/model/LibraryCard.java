/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author GeekNest
 */
@Entity
public class LibraryCard implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Enumerated(EnumType.STRING)
    private ECardStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
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
