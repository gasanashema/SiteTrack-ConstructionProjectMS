/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Book;

/**
 *
 * @author GeekNest
 */
public interface BookService extends Remote{
     Book registerBookRecord(Book theBook) throws RemoteException;
     Book updateBookRecord(Book theBook) throws RemoteException;
     Book deleteBookRecord(Book theBook) throws RemoteException;
     Book findBookRecordById(Book theBook) throws RemoteException;
     List<Book> findAllBookRecords() throws RemoteException;
}
