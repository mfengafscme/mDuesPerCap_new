package org.afscme.enterprise.reporting.base;

import java.util.Collection;
import java.util.Comparator;

import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.util.CollectionUtil;

public class BRUtil
{

    public static final String[] OP_CODES =
        { "BT", "EQ", "LT", "LE", "GT", "GE", "NE", "IN", "NL", "NN" };

    public static final String[] OP_DB_CODES =
        {
            "BETWEEN",
            "=",
            "<",
            "<=",
            ">",
            ">=",
            "<>",
            "IN",
            "IS NULL",
            "IS NOT NULL" };

    public static final String[] INT_OP_NAMES =
        {
            "between",
            "equals",
            "less than",
            "less than or equal to",
            "greater than",
            "greater than or equal to",
            "not equal to",
            null,
            "is null",
            "is not null" };

    public static final String[] DATE_OP_NAMES =
        {
            "between",
            "on",
            "before",
            "on or before",
            "after",
            "on or after",
            "not on",
            null,
            "is null",
            "is not null" };

    public static final String[] ENTITY_KEYS =
        new String[] { "P", "M", "O", "A" };

    public static final String[] ENTITY_NAMES =
        new String[] { "Person", "Member", "Officer", "Affiliate" };

    public static final String AFFILIATE_VIEW = "V_Affiliate";
    public static final String MEMBER_VIEW = "V_Member";
    public static final String OFFICER_VIEW = "V_Officer";
    public static final String PERSON_VIEW = "V_Person";
    public static final String PERSON_ADDRESS_VIEW = "V_Person_Address";
    public static final String PERSON_PHONE_VIEW = "V_Person_Phone";
    public static final String PERSON_RELATION_VIEW = "V_Person_Relation";
    public static final String AFFILIATE_ADDRESS_VIEW = "V_Affiliate_Address";
    public static final String AFFILIATE_PHONE_VIEW = "V_Affiliate_Phone";
    public static final String[] VIEW_NAMES =
        new String[] {
            MEMBER_VIEW,
            OFFICER_VIEW,
            PERSON_VIEW,
            AFFILIATE_VIEW,
            PERSON_ADDRESS_VIEW,
            PERSON_PHONE_VIEW,
            PERSON_RELATION_VIEW,
            AFFILIATE_ADDRESS_VIEW,
            AFFILIATE_PHONE_VIEW };

    public static final String[] ONE_TO_MANY_VIEWS =
        new String[] {
            PERSON_ADDRESS_VIEW,
            PERSON_PHONE_VIEW,
            PERSON_RELATION_VIEW,
            AFFILIATE_ADDRESS_VIEW,
            AFFILIATE_PHONE_VIEW };

    public static final String[] PERSON_VIEWS =
        new String[] {
            PERSON_VIEW,
            PERSON_ADDRESS_VIEW,
            PERSON_PHONE_VIEW,
            PERSON_RELATION_VIEW };

    public static final String[] AFFILIATE_VIEWS =
        new String[] {
            AFFILIATE_VIEW,
            AFFILIATE_ADDRESS_VIEW,
            AFFILIATE_PHONE_VIEW };

    public static final String getEntityName(Character entityType)
    {
        return getEntityName(entityType.charValue());
    }

    public static final String getEntityName(char entityType)
    {
        return (String) CollectionUtil.transliterate(
            String.valueOf(entityType),
            ENTITY_KEYS,
            ENTITY_NAMES);
    }

    public static final String getOperatorDBString(String opCode)
    {
        return (String) CollectionUtil.transliterate(
            opCode,
            OP_CODES,
            OP_DB_CODES);
    }

    /**
     * Orders by field pk
     */
    public static final Comparator fieldOrder()
    {
        return new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                int v1 = ((ReportField) o1).getPk().intValue();
                int v2 = ((ReportField) o2).getPk().intValue();
                return v1 - v2;
            }
        };
    }

    /**
     * Orders by field pk
     */
    public static final Comparator entityOrder()
    {
        return CollectionUtil.orderAs(ENTITY_KEYS);
    }

    /**
     * Puts 'Detail' first.
     */
    public static final Comparator categoryOrder()
    {
        return CollectionUtil.orderAs(new String[] { "Detail" });
    }

    /**
     * Tests if a specific object is contained in an array of objects
     * @param array the array to search
     * @param value the value to test for
     * @return <code>true</code> if the array contains value.  <code>false</code> otherwise.
     */
    public static final boolean arrayContains(Object[] array, Object value)
    {
        for (int i = 0, max = array.length; i < max; i++)
        {
            if (value.equals(array[i]))
                return true;
        }

        return false;
    }

    /**
     * Tests if a collection contains any of the elements in a given array
     * @param collection the collection being tested 
     * @param array the array of values being searched for
     * @return <code>true</code> if the collection contains ANY element in the array
     * <code>false</code> otherwise.
     */
    public static final boolean collectionContainsAny(
        Collection collection,
        Object[] array)
    {
        for (int i = 0, max = array.length; i < max; i++)
        {
            if (collection.contains(array[i]))
                return true;
        }
        return false;
    }

    public static final boolean isPersonView(String tableName)
    {
        return arrayContains(PERSON_VIEWS, tableName);
    }

    public static final boolean isAffiliateView(String tableName)
    {
        return arrayContains(AFFILIATE_VIEWS, tableName);
    }

    public static final boolean isOfficerView(String tableName)
    {
        return OFFICER_VIEW.equals(tableName);
    }

    public static final boolean isMemberView(String tableName)
    {
        return MEMBER_VIEW.equals(tableName);
    }
    
    public static final boolean isOneToManyView(String tableName)
    {
        return arrayContains(ONE_TO_MANY_VIEWS, tableName);
    }

}
