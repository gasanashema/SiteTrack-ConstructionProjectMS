
package model;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author jeremie
 */
public class Author implements Serializable{
    
    public static final long serialVersionUID=1L;
    
    private int id;
    private String names;
    
    private String nationalId;
    private String bio;
    
    private Set<Book> books;

    public Author() {
    }

    public Author(int id, String names, String nationalId, String bio, Set<Book> books) {
        this.id = id;
        this.names = names;
        this.nationalId = nationalId;
        this.bio = bio;
        this.books = books;
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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return ""+id+" - "+names;
        // 1 - Test authors
    }
    
    
    
}
