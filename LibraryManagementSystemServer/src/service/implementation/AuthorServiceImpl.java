/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.AuthorDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Author;
import service.AuthorService;

/**
 *
 * @author GeekNest
 */
public class AuthorServiceImpl extends UnicastRemoteObject implements AuthorService{
    AuthorDao dao = new AuthorDao();
    public AuthorServiceImpl() throws RemoteException{
        
    }

    @Override
    public Author registerAuthorRecord(Author theAuthor) throws RemoteException {
        return dao.registerAuthor(theAuthor);
    }

    @Override
    public Author updateAuthorRecord(Author theAuthor) throws RemoteException {
        return dao.updateAuthor(theAuthor);
    }

    @Override
    public Author deleteAuthorRecord(Author theAuthor) throws RemoteException {
        return dao.deleteAuthor(theAuthor);
    }

    @Override
    public Author findAuthorRecordById(Author theAuthor) throws RemoteException {
        return dao.findAuthorById(theAuthor);
    }

    @Override
    public List<Author> findAllAuthorRecords() throws RemoteException {
        return dao.findAllAuthors();
    }
    
}
