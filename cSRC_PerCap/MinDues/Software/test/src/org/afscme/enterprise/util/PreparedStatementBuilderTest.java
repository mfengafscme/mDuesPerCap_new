package org.afscme.enterprise.util;

import java.util.Calendar;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import java.text.ParseException;
import java.sql.Timestamp;
import org.afscme.enterprise.util.*;
import java.util.*;

public class PreparedStatementBuilderTest extends TestCase {
    
    public PreparedStatementBuilderTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(PreparedStatementBuilderTest.class);
        
        return suite;
    }
    
    /** Test of format method, of class org.afscme.enterprise.util.TextUtil. */
    public void testEmptyCriterion() {
        String nullString = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        builder.addCriterion("col1", "");
        builder.addCriterion("col2", nullString);
        PreparedStatementBuilder.Criterion criterion = new PreparedStatementBuilder.Criterion();
        criterion.setField("col3");
        criterion.setOperator("BETWEEN");
        criterion.addValue("");
        criterion.addValue("");
        assertEquals("SELECT * FROM TABLE", builder.getPreparedStatementSQL("SELECT * FROM TABLE", true));
        builder.addCriterion("col4", "value");
        assertEquals("SELECT * FROM TABLE WHERE ( col4 = ? )", builder.getPreparedStatementSQL("SELECT * FROM TABLE", true));
    }
}    
