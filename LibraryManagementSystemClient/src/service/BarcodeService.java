/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Barcode;

/**
 *
 * @author jeremie
 */
public interface BarcodeService extends Remote{
    // method signature/ definition
    Barcode registerBarcodeRecord(Barcode theBarcode) throws RemoteException;
    Barcode updateBarcodeRecord(Barcode theBarcode) throws RemoteException;
    Barcode deleteBarcodeRecord(Barcode theBarcode) throws RemoteException;
    Barcode findBarcodeRecordById(Barcode theBarcode) throws RemoteException;
    List<Barcode> findAllBarcodeRecords() throws RemoteException; 
    Barcode getNextBarcode() throws RemoteException;
}
