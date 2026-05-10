/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.MemberDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Member;
import service.MemberService;

/**
 *
 * @author GeekNest
 */
public class MemberServiceImpl extends UnicastRemoteObject implements MemberService{
    MemberDao dao = new MemberDao();
    public MemberServiceImpl() throws RemoteException{
        
    }

    @Override
    public Member registerMemberRecord(Member theMember) throws RemoteException {
        return dao.registerMember(theMember);
    }

    @Override
    public Member updateMemberRecord(Member theMember) throws RemoteException {
        return dao.updateMember(theMember);
    }

    @Override
    public Member deleteMemberRecord(Member theMember) throws RemoteException {
        return dao.deleteMember(theMember);
    }

    @Override
    public Member findMemberRecordById(Member theMember) throws RemoteException {
        return dao.findMemberById(theMember);
    }

    @Override
    public List<Member> findAllMemberRecords() throws RemoteException {
        return dao.findAllMembers();
    }
    
}
