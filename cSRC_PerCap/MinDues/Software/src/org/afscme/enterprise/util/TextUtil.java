package org.afscme.enterprise.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Collection;
import java.util.Iterator;
import java.text.NumberFormat;
import java.text.DecimalFormat;


/**
 * Contains static methods for common text functions.
 */
public class TextUtil {

    /** regex pattern for a 'word' ([a-zA-Z_0-9]*) */
    private static final Pattern s_wordPattern = Pattern.compile("\\w+");
    /** regex pattern for an integer */
    private static final Pattern s_intPattern = Pattern.compile("\\d+");
    /** regex pattern for an Alpha */
    private static final Pattern s_alphaPattern = Pattern.compile("[a-zA-Z]*");
    /** regex pattern for all zeros */
    private static final Pattern s_zeroPattern = Pattern.compile("[0]*");
    /** regex pattern for an Alpha Numeric */
    private static final Pattern s_alphaNumericPattern = Pattern.compile("[a-zA-Z0-9]*");
    /** regex pattern for a double */
    private static final Pattern s_doublePattern = Pattern.compile("[0-9]*|[0-9?.0-9]*");
    /** regex pattern for a  an email address */
    private static final Pattern s_emailPattern = Pattern.compile("[\\w_~\\-][\\w_~\\-\\.]*@[\\w_~\\-][\\w_~\\-\\.]*");

    private static final SimpleDateFormat s_dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Formats a Timetsamp as a printable string.
     *
     * @param date - The date to be formatted
     * @return String
     */
    public static final String format(Timestamp date) {
        String result;
        synchronized (s_dateFormat) {
            result = s_dateFormat.format(date);
        }

        return result;
    }

    /**
     * Formats a Timetsamp as a printable string, with both the calendar date and time of day
     *
     * @param date - The date to be formatted
     * @return String
     */
    public static final String formatDateTime(Timestamp date) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return dateFormat.format(date);
    }

    /**
     * Parses a date string.
     *
     * @param date - Teh date to be parsed.
     * @return Timestamp
     * @throws java.text.ParseException
     */
    public static final Timestamp parseDate(String date) throws ParseException {

        try {
            return parseDate(date, DateFormat.MEDIUM);
        }
        catch (ParseException e) {}

        return parseDate(date, DateFormat.SHORT);
    }

    /**
     * Parses a date string.
     *
     * @param date - The date to be parsed.
     * @param style - The style the date is in, like DateFormat.SHORT
     * @return Timestamp
     * @throws java.text.ParseException
     */
    public static final Timestamp parseDate(String date, int style) throws ParseException {
        DateFormat dateFormat = DateFormat.getDateInstance(style);
        dateFormat.setLenient(true);
        return new Timestamp(dateFormat.parse(date).getTime());
    }

    /** Returns true iff str is null or zero-length or contains no whitespace */
    public static final boolean isEmptyOrSpaces(String str) {
        return isEmpty(str) || isEmpty(str.trim());
    }

    /** Returns true iff char is null or zero-length or contains no whitespace */
    public static final boolean isEmptyOrSpaces(Character ch) {
        return isEmpty(ch) || isEmpty(ch.toString().trim());
    }

    /** Returns true iff str is null or zero-length */
    public static final boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /** Returns true iff o is null or a zero-length string*/
    public static final boolean isEmpty(Object o) {
        return o == null || ((o instanceof String) && ((String)o).length() == 0);
    }

    /** Returns true iff col is null, empty or contains all empty values */
    public static final boolean isEmpty(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            if (!isEmpty(it.next()))
                return false;
        }
        return true;
    }

    public static final String trimTrailing(String str, char trim) {
        if (str == null) {
            return str;
        }
        char[] chars = str.toCharArray();
        int index = -1;
        for (int i = chars.length-1; i >= 0; i--) {
            if (chars[i] != trim) {
                index = i;
                break;
            }
        }
        if (index == -1) { // entire string was the trim char
            return "";
        }
        return str.substring(0, index+1);
    }

    public static final String trimLeading(String str, char trim) {
        if (str == null || str.trim().equalsIgnoreCase("0")) {
            return str;
        }
        char[] chars = str.toCharArray();
        int index = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != trim) {
                index = i;
                break;
            } // else ignore the trim chars if at the beginning
        }
        if (index == -1) { // entire string was the trim char
            return "";
        }
        return str.substring(index);
    }

    /** Appends <code>str</code> with trailing <code>pad</code> charactes, up to a total length of <code>minLen</code> */
    public static final String padTrailing(String str, int minLen, char pad) {
        if (str.length() < minLen)
            return str.concat(getFill(minLen - str.length(), pad));
        return str;
    }

    /** Prepends <code>str</code> with trailing <code>pad</code> charactes, up to a total length of <code>minLen</code> */
    public static final String padLeading(String str, int minLen, char pad) {
        if (str.length() < minLen)
            return getFill(minLen - str.length(), pad).concat(str);
        return str;
    }

    /** Ued by padLeading/PadTrailing to get the fill string */
    public static final String getFill(int length, char pad) {
        if (pad == '0') {

            //padding of 0's is used very often
            //return premade strings in this case

            switch (length) {
                case 1:
                    return "0";
                case 2:
                    return "00";
                case 3:
                    return "000";
            }
        }
        char[] fill = new char[length];
        Arrays.fill(fill, pad);
        return new String(fill);
    }

    /**
     * Formats a message.  Example:  TextUtil.format("my.id", new Object[] { myparam1, myparam2 });
     *
     * @param messageID The id of the message in the messages.properties file.
     * @param arguments Array of arguments to insert into the message.
     */
    public static final String format(String messageID, Object[] arguments) {
        Map messages = ConfigUtil.getMessages();
        MessageFormat mf = (MessageFormat)messages.get(messageID);
        if (mf == null)
            throw new RuntimeException("Message string with id '" + messageID + "' doesn't exist");

        return mf.format(arguments);
    }

    /** Returns true iff the provided string contains only letters, digits, or underscore */
    public static final boolean isWord(String str) {
        return s_wordPattern.matcher(str).matches();
    }

    /** Returns true iff the provided string contains only letters */
    public static final boolean isAlpha(String str) {
        return s_alphaPattern.matcher(str).matches();
    }

    /** Returns true iff the provided string contains only letters or digits */
    public static final boolean isAlphaNumeric(String str) {
        return s_alphaNumericPattern.matcher(str).matches();
    }

    public static final boolean isAllZeros(String str) {
        return s_zeroPattern.matcher(str).matches();
    }

    /** Returns true iff the provided string contains only digits */
    public static final boolean isInt(String str) {
        return s_intPattern.matcher(str).matches();
    }

    /** Returns true iff the provided string is a double */
    public static final boolean isDouble(String str) {
        return s_doublePattern.matcher(str).matches();
    }

    /** Returns true iff the provided is a well formed email address */
    public static final boolean isEmail(String str) {
        return s_emailPattern.matcher(str).matches();
    }

    /**
     * Compares 2 strings.  If both strings represent integers, a comparison of the
     * integers is used, otherwise a lexicogriphal comparison is used
     */
    public static final int compareAlphanumerics(String s1, String s2) {
        if (s1 == null) {
            return s2 == null ? 0 : -1;
        } else if (s2 == null) {
            return 1;
        } else if (isInt(s1) && isInt(s2)) {
            return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
        }
        else
            return s1.compareTo(s2);
    }

    /**
     * Works like Object.toString() except prints the individual values if the object is an array.
     *
     * @param The object to print
     * @return The String representation of the object
     */
    public static final String toString(Object o) {

        if (o == null) {
            return "null";
        } else if (o.getClass().isArray()) {
            DelimitedStringBuffer buf = new DelimitedStringBuffer(",");
            if (o instanceof String[]) {
                String[] array = (String[])o;
                for (int i = 0; i < array.length; i++)
                    buf.append(toString(array[i]));
            } else {
                buf.append(o.toString());
            }
            return "[" + buf.toString() + "]";
        } else if (o instanceof String) {
            return (String)o;
        } else {
            return o.toString();
        }
    }

    /**
     * Compares two objects, either or both of which may be null.<br>
     *
     * For Strings, null and empty strings are considered equal<br>
     * For other objects:<br>
     * If o1 and o2 are both null, true is returned <br>
     * If o1 is null and o2 is not, false is returned <br>
     * If o2 is null and o1 is not, false is returned <br>
     * Otherwise, the equals() method of o1 is used <br>
     */
    public static final boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null || ((o2 instanceof String) && isEmpty((String)o2));
        } else if (o2 == null) {
            return (o1 instanceof String) && isEmpty((String)o1);
        } else if (o1 instanceof Character && o2 instanceof String) {
            return o1.toString().equals(o2);
        } else if (o1 instanceof String && o2 instanceof Character) {
            return o2.toString().equals(o1);
        } else {
            return o1.equals(o2);
        }
    }

    /**
     * Compares two objects, either or both of which may be null.<br>
     *
     * null and empty strings are considered equal<br>
     * If s1 and s2 are both null or empty, true is returned <br>
     * If s1 is null or emtpy and s2 is not, false is returned <br>
     * If s2 is null or emtpry and s1 is not, false is returned <br>
     * Otherwise, the equalsIgnoreCase() method of s1 is used <br>
     */
    public static final boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == null) {
            return s2 == null || isEmpty(s2);
        } else if (s2 == null) {
            return isEmpty(s1);
        } else {
            return s1.equalsIgnoreCase(s2);
        }
    }

    /**
     * Returns the primitive boolean of the parameter value. Returns false if
     * value is null.
     *
     * @param value
     *
     * @return primitive boolean value of parameter.
     */
    public static final boolean getPrimitiveBoolean(Boolean value) {
        return (value == null) ? false : value.booleanValue();
    }

	/**
	 * Formats a name like Doe II, John Michael
	 */
    public static final String formatName(String prefix, String first, String middle, String last, String suffix) {
        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");
        buf.append(last);
        buf.append(suffix);
        buf.append(",", "");
        buf.append(first);
        buf.append(middle);

        return buf.toString();
    }

	/**
	 * Formats a result set column
	 *
	 * @param rs The result set to get the data from
	 * @param columnIndex Index of the column to get
	 * @param nullValue The value to return if the data is null
	 * @return Formatted value
	 */
    public static final String format(ResultSet rs, int columnIndex, String nullValue) throws SQLException {
        String result;
        switch (rs.getMetaData().getColumnType(columnIndex)) {
            case java.sql.Types.TIMESTAMP:
            case java.sql.Types.TIME:
            case java.sql.Types.DATE:
                Timestamp ts = rs.getTimestamp(columnIndex);
                result = (ts != null) ? format(ts) : nullValue;
                break;
            default:
                String str = rs.getString(columnIndex);
                result = isEmpty(str) ? nullValue : str;
        }

        return result;
    }

	/**
	 * Formats a double
	 *
	 * @param d a double number
	 * @return Formatted String value
	 */
	public static final String formatDouble(double d) {
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		String s = formatter.format(d);

		s = s.replaceAll(".00", "");

		return s;
	}

	public static final String formatDoubleThreeDec(double d) {
		NumberFormat formatter = new DecimalFormat("#,##0.000");
		String s = formatter.format(d);

		s = s.replaceAll(".000", "");

		return s;
	}

	public static final String formatInt(int i) {
		NumberFormat formatter = new DecimalFormat("#,###");
		String s = formatter.format(i);

		return s;
	}

	public static final String formatToNumber(String s) {
		String rtv = s;
		if (s != null)
			rtv = s.replaceAll(",", "");

		return rtv;
	}


	public static final double roundDouble(double d, int places) {
		return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,(double) places);
	}

}
