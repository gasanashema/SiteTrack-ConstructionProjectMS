/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Member;

/**
 *
 * @author GeekNest
 */
public interface MemberService extends Remote{
     Member registerMemberRecord(Member theMember) throws RemoteException;
     Member updateMemberRecord(Member theMember) throws RemoteException;
     Member deleteMemberRecord(Member theMember) throws RemoteException;
     Member findMemberRecordById(Member theMember) throws RemoteException;
     List<Member> findAllMemberRecords() throws RemoteException;
}
