/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.LibraryCardDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.LibraryCard;
import service.LibraryCardService;

/**
 *
 * @author GeekNest
 */
public class LibraryCardServiceImpl extends UnicastRemoteObject implements LibraryCardService{
    LibraryCardDao dao = new LibraryCardDao();
    public LibraryCardServiceImpl() throws RemoteException{
        
    }

    @Override
    public LibraryCard registerLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException {
        return dao.registerLibraryCard(theLibraryCard);
    }

    @Override
    public LibraryCard updateLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException {
        return dao.updateLibraryCard(theLibraryCard);
    }

    @Override
    public LibraryCard deleteLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException {
        return dao.deleteLibraryCard(theLibraryCard);
    }

    @Override
    public LibraryCard findLibraryCardRecordById(LibraryCard theLibraryCard) throws RemoteException {
        return dao.findLibraryCardById(theLibraryCard);
    }

    @Override
    public List<LibraryCard> findAllLibraryCardRecords() throws RemoteException {
        return dao.findAllLibraryCards();
    }
    
}
