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

public class Barcode implements Serializable{
    
    public static final long serialVersionUID=1L;
    private int id;
    private String code;
    private LocalDate generateDate;
    
    private Book book;

    public Barcode() {
    }

    public Barcode(int id, String code, LocalDate generateDate, Book book) {
        this.id = id;
        this.code = code;
        this.generateDate = generateDate;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(LocalDate generateDate) {
        this.generateDate = generateDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return code;
    }
    
    
}
