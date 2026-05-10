/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import service.implementation.AuthorServiceImpl;
import service.implementation.BarcodeServiceImpl;
import service.implementation.BookServiceImpl;
import service.implementation.BorrowingServiceImpl;
import service.implementation.LibraryCardServiceImpl;
import service.implementation.MemberServiceImpl;

/**
 *
 * @author GeekNest
 */
public class Server {

    public static void main(String[] args) {
        try {
            //step 1: configure server properties
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            //step 2: create registry
            Registry theRegistry = LocateRegistry.createRegistry(8002);

            theRegistry.rebind("author-service", new AuthorServiceImpl());
            theRegistry.rebind("barcode-service", new BarcodeServiceImpl());
            theRegistry.rebind("book-service", new BookServiceImpl());
            theRegistry.rebind("borrowing-service", new BorrowingServiceImpl());
            theRegistry.rebind("library-card-service", new LibraryCardServiceImpl());
            theRegistry.rebind("member-service", new MemberServiceImpl());
            
            System.out.println("Server is running on port 8002");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
