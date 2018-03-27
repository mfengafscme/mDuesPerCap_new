package org.afscme.enterprise.test;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestResult;
import junit.framework.AssertionFailedError;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import java.lang.reflect.InvocationTargetException;
import javax.ejb.EJBException;
import java.io.PrintStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Runs a set of JUnit tests and collects the results.  (used by test.jsp)
 */
public class Tester extends TestResult {

    private long startTime;
    private long overallTime;
    private Map times = new HashMap();
    private Map errors = new HashMap();
    private Map failures = new HashMap();
    private String stdout;
    private String stderr;
	
	private static Object performLock = new Object();

    public void perform(String className, String methodName) throws InstantiationException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {

		synchronized (performLock) {  //needs to be one one of these running at a time
			//create the suite
			TestSuite suite = new TestSuite();
			if (className == null)
				throw new RuntimeException("Class name not specified in Tester.perform()");

            Class c = Class.forName(className);
            if (methodName == null) {
                Test test = (Test)c.getDeclaredMethod("suite", null).invoke(null, null);
                suite.addTest(test);
            } else {
                Constructor constructor = c.getConstructor(new Class[] { String.class });
                suite.addTest((Test)constructor.newInstance(new Object[] { methodName }));
            }

			//hijak stdout and stderr
			ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
			ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
			PrintStream out = System.out;
			PrintStream err = System.err;
			PrintStream teeOut = new PrintStream(new TeeOutputStream(out, outBuffer));
			PrintStream teeErr = new PrintStream(new TeeOutputStream(err, errBuffer));
			try {
				System.setOut(teeOut);
				System.setErr(teeErr);

				//run the tests
				suite.run(this);
			} finally {
				//un-hijak stdout and stderr

				teeOut.flush();
				teeErr.flush();
				System.setOut(out);
				System.setErr(err);
			}
			outBuffer.close();
			errBuffer.close();
			stdout = escapeHTML(outBuffer.toString());
			stderr = escapeHTML(errBuffer.toString());
		}
    }

    public Map getTimes() {
        return times;
    }
    public float getTime() {
        return ((float)overallTime) / 1000f;
    }
    public Map getFailures() {
        return failures;
    }
    public Map getErrors() {
        return errors;
    }
    public String getStdOut() {
        return stdout;
    }
    public String getStdErr() {
        return stderr;
    }

    public static String getStackTrace(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        t.printStackTrace(writer);
        writer.close();
        return escapeHTML(baos.toString());
    }
    
    public static List getTests(File dir) {
        LinkedList classes = new LinkedList();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String name = files[i].getPath();
                if (files[i].getName().endsWith("Test.class") && name.indexOf("AFSCMEWebTest") == -1) {
                    name = name.replace(File.separatorChar, '.');
                    name = name.substring(name.indexOf("org.afscme.enterprise"), name.length() - 6);
                    classes.add(name);
                }
            } else if (files[i].isDirectory() && !files[i].getName().equals("web")) {
                classes.addAll(getTests(files[i]));
            }
        }
        
        return classes;
    }
    
    public static List getTests() throws Exception {
       File entry = new File("../server/default/deploy/AFSCMEEnterprise.ear/test.war/WEB-INF/classes/org/afscme/enterprise");
       return getTests(entry);
   }
    
    public static List getMethods(String className) throws Exception {
        List methodNames = new LinkedList();
        java.lang.reflect.Method[] methods = Class.forName(className).getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith("test"))
                methodNames.add(methods[i].getName());
        }
        
        return methodNames;
    }

    //
    // TestResult overrldes
    //
    public void startTest(Test test) {
        startTime = System.currentTimeMillis();
        super.startTest(test);
    }
    public void endTest(Test test) {
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        overallTime += diff;
        times.put(((TestCase)test).getName(), new Float(diff / 1000f));
        super.endTest(test);
    }        
    public void addError(Test test, Throwable t) {
		while (t instanceof EJBException) {
			EJBException e = (EJBException)t;
			Exception cause = e.getCausedByException();
			if (cause != null)
				t = cause;
			else
				break;
		}
        errors.put(((TestCase)test).getName(), t);
        super.addError(test, t);
        log("Error running '"+((TestCase)test).getName()+"': "+t);
    }
    public void addFailure(Test test, AssertionFailedError e) {
        failures.put(((TestCase)test).getName(), e);
        super.addFailure(test, e);
        log("Failure running '"+((TestCase)test).getName()+"': "+e);
    }
    
	public static String escapeHTML(String s){
		if (s == null) return "";
		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
				case '<': sb.append("&lt;"); break;
				case '>': sb.append("&gt;"); break;
				case '&': sb.append("&amp;"); break;
				case '"': sb.append("&quot;"); break;
				default:  sb.append(c); break;
			}
		}
		return sb.toString();
   }

    private void log(String message) {
        System.out.println(message);
    }
	
}
    
 

class TeeOutputStream extends OutputStream {
    OutputStream ostream1, ostream2;

    TeeOutputStream(OutputStream o1, OutputStream o2) throws IOException {
        ostream1 = o1;
        ostream2 = o2;
    }

    public void flush() throws IOException {
        ostream1.flush();
        ostream2.flush();
    }

    public void write(int b) throws IOException {
        byte[] buf = new byte[1];
        buf[0] = (byte)b;
        write(buf, 0, 1);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        ostream1.write(b, off, len);
        ostream2.write(b, off, len);
    }
}



