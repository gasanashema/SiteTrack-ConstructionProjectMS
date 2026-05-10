/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collections;
import java.util.List;
import model.Member;
import org.hibernate.*;

/**
 *
 * @author GeekNest
 */
public class MemberDao {

    public Member registerMember(Member memberObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(memberObj);
            tr.commit();
            ss.close();
            return memberObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member updateMember(Member memberObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(memberObj);
            tr.commit();
            ss.close();
            return memberObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member deleteMember(Member memberObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(memberObj);
            tr.commit();
            ss.close();
            return memberObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member findMemberById(Member memberObj){
        try {
          Session ss = HibernateUtil.getSessionFactory().openSession();
          Member found = (Member)ss.get(Member.class, memberObj.getId());  
          ss.close();
          return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Member> findAllMembers(){
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            List<Member> members = ss.createQuery("SELECT member From Member member").list();
            ss.close();
            return members;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
