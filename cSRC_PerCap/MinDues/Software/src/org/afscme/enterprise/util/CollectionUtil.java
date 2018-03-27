package org.afscme.enterprise.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Contains static methods for performing common operations on collections */
public class CollectionUtil {

    /** Copies an array of objects to a collection
     * 
     * @param to The collection to copy to
     * @param from the array of objects to copy
     * @return the collection that was passed in
     */
	public static final Collection copy(Collection to, Object[] from) {
		for (int i=0; i < from.length; i++)
			to.add(from[i]);
		return to;
	}

    /** Makes a String array from a collection
     *
     * @param from the Collection to make the array from
     * @return a String array containing the elments in the given collection
     */
	public static final String[] toStringArray(Collection from) {
		String[] ret = new String[from.size()];
		Iterator it = from.iterator();
		for (int i = 0; it.hasNext(); i++)
			ret[i] = toString(it.next());
		return ret;
	}

    /** Makes an Integer array from a collection of Integers.
     *
     * @param   from the Collection to make the array from
     * @return  an Integer array containing the elments in the given collection. 
     *          Returns null if any of the Collection items were not an Integer.
     */
	public static final Integer[] toIntegerArray(Collection from) {
            Integer[] ret = new Integer[from.size()];
            Iterator it = from.iterator();
            for (int i = 0; it.hasNext(); i++) {
                Object o = it.next();
                if ( !(o instanceof Integer) ) {
                    return null;
                }
                ret[i] = (Integer)o;
            }
            return ret;
	}
        
    /**
     * Returns a string represnentation of the given objects
     */
	public static final String toString(Object[] objects) {
		if (objects == null)
			return "null";
		DelimitedStringBuffer buf = new DelimitedStringBuffer(", ");
		for (int i = 0; i < objects.length; i++)
			buf.append(toString(objects[i]));
		return "{"+buf.toString().trim()+"}";
	}
    
    /**
     * Returns a String representation of the given Collection.
     */
    public static final String toString(Collection c) {
        if (c == null) {
            return "null";
        } else {
            return toString(c.toArray());
        }
    }
    
    public static final String toString(Map m) {
        if (m == null) {
            return "null";
        }
        StringBuffer sb = new StringBuffer("\t[");
        Map.Entry entry;
        for (Iterator it = m.entrySet().iterator(); it.hasNext(); ) {
            entry = (Map.Entry)it.next();
            sb.append("\n\t\tkey=");
            sb.append(entry.getKey());
            sb.append(";value=");
            sb.append(entry.getValue());
        }
        sb.append("\n\t]");
        return sb.toString().trim();
    }
	
    /**
     * Returns a string representation of the given object.
     * If the given object is a string, the object itself is returned
     */
	public static final String toString(Object object) {
		if (object instanceof String)
			return (String)object;
		else
			return object.toString();
	}
    

    /**
     * Transliterates 'value' from the domain of 'from' to the range of 'to'
     *
     * @param value the value to transliterate
     * @param from the vector of values to translate from
     * @param to the vecto of values to translate to
     * @return a value from 'to' that corresponds to the position of 'value' in 'from'
     */
    public static final int transliterate(Object value, Object[] from, int[] to) {
        for (int i = 0; i < from.length; i++)
            if (from[i].equals(value))
                return to[i];
        throw new RuntimeException("CollutionUtil.transliterate() Could not find " + value + " in " + from);
    }

    /**
     * Transliterates 'value' from the domain of 'from' to the range of 'to'
     *
     * @param value the value to transliterate
     * @param from the vector of values to translate from
     * @param to the vecto of values to translate to
     * @return a value from 'to' that corresponds to the position of 'value' in 'from'
     */
    public static final Object transliterate(int value, int[] from, Object[] to) {
        for (int i = 0; i < from.length; i++)
            if (value == from[i])
                return to[i];
        throw new RuntimeException("CollutionUtil.transliterate() Could not find " + value + " in " + from);
    }

    /**
     * Transliterates 'value' from the domain of 'from' to the range of 'to'
     *
     * @param value the value to transliterate
     * @param from the list of values to translate from
     * @param to the list of values to translate to
     * @return a value from 'to' that corresponds to the position of 'value' in 'from'
     */
    public static final Object transliterate(Object value, Object[] from, Object[] to) {
        for (int i = 0; i < from.length; i++)
            if (value.equals(from[i]))
                return to[i];
        throw new RuntimeException("CollutionUtil.transliterate() Could not find " + value + " in " + from);
    }
    
    /**
     * Transliterates 'values' from the domain of 'from' to the range of 'to'
     *
     * @param values collection of values to transliterate
     * @param from the list of values to translate from
     * @param to the list of values to translate to
     * @return a value from 'to' that corresponds to the position of 'value' in 'from'
     */
    public static final List transliterate(Collection values, Object[] from, Object[] to) {
        List list = new LinkedList();
        Iterator it = values.iterator();
        while (it.hasNext())
            list.add(transliterate(it.next(), from, to));
        return list;
    }
    
    /**
     * Checks if a Collection is null or contains no items.
     */
    public static final boolean isEmpty(Collection c) {
        return (c == null || c.size() == 0);
    }
    
    public static final boolean isEmpty(Map m) {
        return (m == null || m.size() == 0);
    }
    
    /**
     * Compares two Collections, either or both of which may be null.
     *
     * If c1 and c2 are both empty, true is returned
     * If c1 is empty and c2 is not, false is returned
     * If c2 is empty and c1 is not, false is returned
     * Otherwise, check if each Collection contains all the items in the other.
     */
    public static final boolean equals(Collection c1, Collection c2) {
        if (isEmpty(c1)) {
            return isEmpty(c2);
        } else if (isEmpty(c2)) {
            return false;
        } else {
            return (c1.containsAll(c2) && c2.containsAll(c1));
        }
    }

    /**
     * Searches for an object in an array.
     *
     * @param in        Object[] to be searched.
     * @param searchFor Object for which to search.
     *
     * @return  true if object is found, false otherwise. Will also return false
     *          if either object passed in is null.
     */
    public static final boolean contains(Object[] in, Object searchFor) {
        if (in == null || searchFor == null) {
            return false;
        }
        for (int i = 0; i < in.length; i++) {
            if (searchFor.equals(in[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of an object in an array.
     *
     * @param in        Object[] to be searched.
     * @param searchFor Object for which to search.
     *
     * @return  index of object if found, or -1 if not found
     */
    public static final int indexOf(Object[] in, Object searchFor) {
        for (int i = 0; i < in.length; i++) {
            if (TextUtil.equals(searchFor, in[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a comparator that orders items in the order of the given array.  Items not showing up
     * in the array are orered after items that are in the array
     */
    public static final Comparator orderAs(final Object[] objects) {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                int i1 = indexOf(objects, o1);
                int i2 = indexOf(objects, o2);
                return
                    i1 == -1 ? 
                        i2 == -1 ?
                            CollectionUtil.compare(o1, o2)
                            : 1
                        : i2 == -1 ?
                            -1
                            : i1 - i2;
            }
        };
    }

    /** 
     * Compares 2 object, uses their Comparable interface if present, otherwise does a normal JDK object compare
     */
    private static final int compare(Object o1, Object o2) {
        if (o1 instanceof Comparable && o2 instanceof Comparable)
            return ((Comparable)o1).compareTo((Comparable)o2);
        else
            return o1 == o2 ? 0 : o1.hashCode() < o2.hashCode() ? -1 : 1;
    }
}
