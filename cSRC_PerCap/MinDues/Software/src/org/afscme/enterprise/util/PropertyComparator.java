/*
 * Created on Dec 16, 2003
 *
 */
package org.afscme.enterprise.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Comparator for sorting objects by a bean property.
 * This Comparator can be used to sort a collection of objects based off a Java Beans 
 * style property name.  The property names given to this object should NOT include
 * the get or is portion of the accessor method for the property.  This comparator
 * is capable of getting any property that has an accessor method that has the name
 * "get" + PropertyName or "is"+PropertyName.  The property name should start with a lower 
 * case letter which will be converted for the accessor method to upper case.
 * <p>
 * example:  If there is a class with a method getName() then the property name should be 
 * simply "name".
 * 
 * <p>
 * This class is also capable of handling nested properties of unlimited depth.  At this time
 * the class does not handle properties that are arrays or maps.
 * <p>
 * Example for nested.  If there is a class that has an address property (and the associated
 * getAddress() method) and the result is an Address that has a property of state then
 * a valid property name to get the state would be "address.state" 
 * 
 * @author Clayton J. Kovar (ckovar)
 */
public class PropertyComparator implements Comparator
{
    private static final Logger log =
        Logger.getLogger(PropertyComparator.class);
    private static final Object[] EMPTY_ARGUMENTS = new Object[0];
    private Class baseObjectClass;
    private String sortProperty;
    private boolean sortAscending = true;
    private int greaterThan = 1;
    private int lessThan = -1;
    private int equals = 0;
    private List methodList;

    /**
     * private default constructor.  Preventing its usage.
     *
     */
    private PropertyComparator()
    {

    }

    /**
     * Main constructor
     * @param objectClass the Class that is used as the base of the property given
     * @param property the property the objects should be sorted by
     * @param ascending <code>true</code> to sort in ascending order <code>false</code> otherwise
     */
    public PropertyComparator(
        Class objectClass,
        String property,
        boolean ascending)
    {
        if (objectClass == null)
            throw new NullPointerException("The objectClass argument cannot be null");
        if (property == null)
            throw new NullPointerException("The property argument cannot be null");

        baseObjectClass = objectClass;
        sortProperty = property;
        sortAscending = ascending;
        if (!ascending)
        {
            greaterThan = -1;
            lessThan = 1;
        }
        methodList = buildMethodList(objectClass, property);
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1)
    {
        if (arg0 == null || arg1 == null)
            return nullCompare(arg0, arg1);

        Object value0 = getValue(arg0);
        Object value1 = getValue(arg1);

        if (value0 == null || value1 == null)
            return nullCompare(value0, value1);

        if (value0 instanceof Comparable && value1 instanceof Comparable)
        {
            if (sortAscending)
                return ((Comparable) value0).compareTo(value1);
            else
                return ((Comparable) value1).compareTo(value0);
        }
        else
            throw new RuntimeException(
                "The value for property "
                    + sortProperty
                    + " does not implement the Comparable interface");

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        if (arg0 instanceof PropertyComparator)
        {
            PropertyComparator candidate = (PropertyComparator) arg0;
            return sortProperty.equals(candidate.sortProperty)
                && sortAscending == candidate.sortAscending;

        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append(": sortProperty=").append(sortProperty);
        sb.append(" : sortAscending=").append(sortAscending);
        return sb.toString();
    }

    /**
     * Gets the property off the given object
     * @param obj the object to get the property from.
     * @return the value of the property
     */
    private Object getValue(Object obj)
    {
        Object retVal = obj;

        try
        {
            for (Iterator i = methodList.iterator();
                i.hasNext() && retVal != null;
                )
            {
                retVal = ((Method) i.next()).invoke(retVal, EMPTY_ARGUMENTS);
            }
        }
        catch (Exception e)
        {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }

        if (retVal instanceof Boolean)
        {
            if (((Boolean) retVal).booleanValue())
                retVal = new Integer(1);
            else
                retVal = new Integer(0);
        }
        return retVal;

    }

    /**
     * This method should only be called when one or both of the values are null
     * 
     */
    private int nullCompare(Object o, Object o2)
    {
        if (o != null && o2 != null)
            throw new RuntimeException("expecting one argument to be null");

        if (o != null)
            return lessThan;
        else if (o2 != null)
            return greaterThan;
        else
            return equals;
    }

    /**
     * Builds the stack of methods that need to be called to access the property from
     * the base class.  
     * @param objClass the class to get the property (or nested property) from
     * @param property the identifier of the property being requested.
     * @return A list of Method objects that can be called in sequence to get the value
     * of the property for a given object.
     */
    private List buildMethodList(Class objClass, String property)
    {
        List retVal = new ArrayList();
        Class currentClass = objClass;

        String remainingPropertyInfo = property;

        while (remainingPropertyInfo != null)
        {
            int propertyEndIndex = remainingPropertyInfo.indexOf(".");
            String tempProperty;
            if (propertyEndIndex > 0)
            {
                tempProperty =
                    remainingPropertyInfo.substring(0, propertyEndIndex);
                remainingPropertyInfo =
                    remainingPropertyInfo.substring(propertyEndIndex + 1);
            }
            else
            {
                tempProperty = remainingPropertyInfo;
                remainingPropertyInfo = null;
            }

            tempProperty =
                Character.toUpperCase(tempProperty.charAt(0))
                    + tempProperty.substring(1);
            try
            {
                Method methods[] = currentClass.getMethods();
                Method method = null;
                for (int i = 0, max = methods.length; i < max; i++)
                {
                    String methodName = methods[i].getName();
                    if ((methodName.equals("get" + tempProperty)
                        || methodName.equals("is" + tempProperty))
                        && methods[i].getParameterTypes().length == 0)
                    {
                        method = methods[i];
                        break;
                    }
                }
                if (method != null)
                {
                    retVal.add(method);
                    currentClass = method.getReturnType();
                }
                else
                {
                    throw new RuntimeException(
                        "Unable to process the property "
                            + property
                            + ": unable to find the property segment "
                            + tempProperty);

                }
            }
            catch (Exception e)
            {
                if (e instanceof RuntimeException)
                    throw (RuntimeException) e;
                throw new RuntimeException(e);
            }
        }

        return retVal;
    }

}
