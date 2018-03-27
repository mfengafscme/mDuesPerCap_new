/*
 * CardsBeanTest.java
 * JUnit based test
 *
 * Created on July 14, 2003, 3:06 PM
 */

//package org.afscme.enterprise.cards.ejb;
//
//import java.util.*;
//import java.util.Iterator;
//import java.util.LinkedList;
//import javax.ejb.CreateException;
//import javax.naming.NamingException;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//import org.afscme.enterprise.cards.RunSummary;
//import org.afscme.enterprise.util.JNDIUtil;
//import javax.ejb.RemoveException;
//
///**
// *
// * @author vjain
// */
//public class CardsBeanTest extends TestCase {
//    
//    public CardsBeanTest(java.lang.String testName) {
//        super(testName);
//    }
//    
//    public static void main(java.lang.String[] args) {
//        TestRunner.run(suite());
//    }
//    
//    public static Test suite() {
//        TestSuite suite = new TestSuite(CardsBeanTest.class);
//        
//        return suite;
//    }
//    
//    /** Test of getScheduled method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    public void testGetScheduled()throws NamingException, CreateException {
//        System.out.println("testGetScheduled");
//        Cards cardsBean = JNDIUtil.getCardsHome().create();
//        LinkedList resultlist = cardsBean.getScheduled();
//        
//         Iterator resultiterator = resultlist.listIterator();
//         while (resultiterator.hasNext()) {
//            RunSummary runsummaryresult = (RunSummary)resultiterator.next();
//            System.out.println("This is the Run Summary Result = "+ runsummaryresult.toString());
//         }
//    }
//
//
//    /** Test of getByPassed method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    public void testGetByPassed()throws NamingException, CreateException {
//        System.out.println("testGetByPassed");
//        Cards cardsBean = JNDIUtil.getCardsHome().create();
//        LinkedList resultlist = cardsBean.getByPassed();
//        
//         Iterator resultiterator = resultlist.listIterator();
//         while (resultiterator.hasNext()) {
//            RunSummary runsummaryresult = (RunSummary)resultiterator.next();
//            System.out.println("This is the Run Summary Result = "+ runsummaryresult.toString());
//         }
//    }
//   
//    /** Test of getCompleted method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    public void testGetCompleted()throws NamingException, CreateException {
//        System.out.println("testGetCompleted");
//        Cards cardsBean = JNDIUtil.getCardsHome().create();
//        LinkedList resultlist = cardsBean.getCompleted();
//        
//         Iterator resultiterator = resultlist.listIterator();
//         while (resultiterator.hasNext()) {
//            RunSummary runsummaryresult = (RunSummary)resultiterator.next();
//            System.out.println("This is the Run Summary Result = "+ runsummaryresult.toString());
//         }
//    }
//
//    
//    /** Test of removeFromBypass method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    public void testremoveFromBypass()throws NamingException, CreateException {
//        System.out.println("testremoveFromBypass");
//        Cards cardsBean = JNDIUtil.getCardsHome().create();
//        LinkedList resultlist = cardsBean.removeFromBypass();
//        
//         Iterator resultiterator = resultlist.listIterator();
//         while (resultiterator.hasNext()) {
//            RunSummary runsummaryresult = (RunSummary)resultiterator.next();
//            System.out.println("This is the Run Summary Result = "+ runsummaryresult.toString());
//   
//           }
//        }
//      
//      /** Test of addToBypass method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//  /**   public void testAddToBYpass() throws NamingException, CreateException, RemoveException
//    {
//        System.out.println("testAddToBypass : starting");
//        System.out.println();
//        System.out.println();
//        
//        Cards cardsBean = JNDIUtil.getaddToBypass().create();
//        Integer personPk = new Integer(10000310);
//             
//        cardsBean.addToBypass(personPk);
//        
//        System.out.println();
//        System.out.println("testAddToBypass : ending");
//        
//        
//   
//   *
//   }
//   */
//    /**Test of performRun method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    public void testPerformRun() {
//        System.out.println("testPerformRun");
//        
//        Cards cardsBean = JNDIUtil.performRun().create();
//        
//        
//        Integer personPk = newInteger(1223);
//        
//     
//        cardsBean.performRun(personPk);
//        
//      
//        System.out.println();
//        System.out.println("testPerformRun");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
//    /** Test of perfomRunSync method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    /*public void testPerfomRunSync() {
//        System.out.println("testPerfomRunSync");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    */
//    /** Test of initializeNewRun method, of class org.afscme.enterprise.cards.ejb.CardsBean. */
//    /*public void testInitializeNewRun() {
//        System.out.println("testInitializeNewRun");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    */
//    // Add test methods here, they have to start with 'test' name.
//    // for example:
//    // public void testHello() {}
//    
//    
//}
