/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.BorrowingDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Borrowing;
import service.BorrowingService;

/**
 *
 * @author GeekNest
 */
public class BorrowingServiceImpl extends UnicastRemoteObject implements BorrowingService{
    BorrowingDao dao = new BorrowingDao();
    public BorrowingServiceImpl() throws RemoteException{
        
    }

    @Override
    public Borrowing registerBorrowingRecord(Borrowing theBorrowing) throws RemoteException {
        return dao.registerBorrowing(theBorrowing);
    }

    @Override
    public Borrowing updateBorrowingRecord(Borrowing theBorrowing) throws RemoteException {
        return dao.updateBorrowing(theBorrowing);
    }

    @Override
    public Borrowing deleteBorrowingRecord(Borrowing theBorrowing) throws RemoteException {
        return dao.deleteBorrowing(theBorrowing);
    }

    @Override
    public Borrowing findBorrowingRecordById(Borrowing theBorrowing) throws RemoteException {
        return dao.findBorrowingById(theBorrowing);
    }

    @Override
    public List<Borrowing> findAllBorrowingRecords() throws RemoteException {
        return dao.findAllBorrowings();
    }
    
}
