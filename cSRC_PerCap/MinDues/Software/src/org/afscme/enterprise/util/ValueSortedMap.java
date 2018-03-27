
package org.afscme.enterprise.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


/**
 * Implements a map sorted on comparisons of the values of objects, instead of the keys.
 */
public class ValueSortedMap extends TreeMap {

	/** Creates a Map, implemented by ValueSortedMap.  The values
	 * int the provided map object should implement the Comparable interface
	 * for this to work as desired
	 */
	public static Map create(Map map) {
		return Collections.unmodifiableMap(new ValueSortedMap(map));
	}

	/** Private constructor, use the public create() method to create this object */
	private ValueSortedMap(Map map) {
        super(new ValueComparator(map));
		putAll(map);
    }
	
	/** Comparator that used the corresponding map values to compare items */
	private static class ValueComparator implements Serializable, Comparator {

		private Map m_externalMap;
	
		public ValueComparator(Map map) {
			m_externalMap = map;
		}
		
    	public int compare(Object o1, Object o2) {
            Comparable v1 = (Comparable)m_externalMap.get(o1);
            Comparable v2 = (Comparable)m_externalMap.get(o2);
            if (v1 == null) {
                return v2 == null ? 0 : -1;
            } else if (v2 == null) {
    			return 1;
            } else  {
                return v1.compareTo(v2);
            }
	    }
    }
}
