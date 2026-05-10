/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Borrowing;

/**
 *
 * @author GeekNest
 */
public interface BorrowingService extends Remote{
     Borrowing registerBorrowingRecord(Borrowing theBorrowing) throws RemoteException;
     Borrowing updateBorrowingRecord(Borrowing theBorrowing) throws RemoteException;
     Borrowing deleteBorrowingRecord(Borrowing theBorrowing) throws RemoteException;
     Borrowing findBorrowingRecordById(Borrowing theBorrowing) throws RemoteException;
     List<Borrowing> findAllBorrowingRecords() throws RemoteException;
}
