/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.LibraryCard;

/**
 *
 * @author GeekNest
 */
public interface LibraryCardService extends Remote{
     LibraryCard registerLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException;
     LibraryCard updateLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException;
     LibraryCard deleteLibraryCardRecord(LibraryCard theLibraryCard) throws RemoteException;
     LibraryCard findLibraryCardRecordById(LibraryCard theLibraryCard) throws RemoteException;
     List<LibraryCard> findAllLibraryCardRecords() throws RemoteException;
}
