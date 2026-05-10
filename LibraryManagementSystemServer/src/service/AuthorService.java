/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Author;

/**
 *
 * @author GeekNest
 */
public interface AuthorService extends Remote{
     Author registerAuthorRecord(Author theAuthor) throws RemoteException;
     Author updateAuthorRecord(Author theAuthor) throws RemoteException;
     Author deleteAuthorRecord(Author theAuthor) throws RemoteException;
     Author findAuthorRecordById(Author theAuthor) throws RemoteException;
     List<Author> findAllAuthorRecords() throws RemoteException;
}
