/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.BookDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Book;
import service.BookService;

/**
 *
 * @author GeekNest
 */
public class BookServiceImpl extends UnicastRemoteObject implements BookService{
    BookDao dao = new BookDao();
    public BookServiceImpl() throws RemoteException{
        
    }

    @Override
    public Book registerBookRecord(Book theBook) throws RemoteException {
        return dao.registerBook(theBook);
    }

    @Override
    public Book updateBookRecord(Book theBook) throws RemoteException {
        return dao.updateBook(theBook);
    }

    @Override
    public Book deleteBookRecord(Book theBook) throws RemoteException {
        return dao.deleteBook(theBook);
    }

    @Override
    public Book findBookRecordById(Book theBook) throws RemoteException {
        return dao.findBookById(theBook);
    }

    @Override
    public List<Book> findAllBookRecords() throws RemoteException {
        return dao.findAllBooks();
    }
    
}
