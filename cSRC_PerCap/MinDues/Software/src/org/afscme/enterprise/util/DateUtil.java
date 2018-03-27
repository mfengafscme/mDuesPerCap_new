package org.afscme.enterprise.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;


/**
 * Contains static helper methods for working with Date/Time type objects.
 */
public class DateUtil {
    /** AFSCME Default Date Format. May want to move this to a config file or table. */
    protected static final String DATE_FORMAT_STRING = "MM/dd/yyyy";

    /** The maximum year, according to the database server */
    public static final int MAX_YEAR = 9999;
	/** The minimum year, according to the database server */
    public static final int MIN_YEAR = 1753;
    
    /** Static reference to the logger for the class */
    static Logger log = Logger.getLogger(DateUtil.class);    

    /** Creates a new instance of DateUtil */
    public DateUtil() {
    }

    /**
     * Converts a date String to a Timestamp object.
     *
     * NOTE: THIS METHOD RETURNS ZERO VALUES FOR TIME FIELDS IN THE TIMESTAMP.
     *
     * @param date String in the format MM/DD/YYYY.
     *
     * @return The Timestamp object. Returns null if dateString cannot be converted.
     */
    public static Timestamp getTimestamp(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            StringTokenizer tok = new StringTokenizer(dateString, "/");
            String mm = tok.nextToken();
            String dd = tok.nextToken();
            String yyyy = tok.nextToken();
            Integer i = new Integer(0);
            if (yyyy.length() < 4)
            {
                try
                {
                   int yy = convert2DigitYear(Integer.parseInt(yyyy));
                   yyyy = Integer.toString(yy);
                }catch(NumberFormatException nfe)
                {
                    log.debug(nfe);
                }
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append(yyyy);
            sb.append("-");
            sb.append(mm);
            sb.append("-");
            sb.append(dd);
            sb.append(" 00:00:00.000000000");
            return Timestamp.valueOf(sb.toString().trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a month/year pair to a Timestamp object. Default day will
     * be 1.
     *
     * NOTE: THIS METHOD RETURNS ZERO VALUES FOR TIME FIELDS IN THE TIMESTAMP.
     *
     * @param month   Number indicating the month. See the isCalendarMonth
     *                param for more info.
     * @param year    The four digit year.
     * @param isCalendarMonth   'TRUE' indicates the month param follows the
     *                  java.util.Calendar numbering of 0-11, and 'FALSE'
     *                  indicate that it follows a 1-12 numbering.
     *
     * @return a Timestamp representing the date values passed in.
     */
    public static Timestamp getTimestamp(int month, int year, boolean isCalendarMonth) {
        return getTimestamp(month, year, 1, isCalendarMonth);
    }

    /**
     * Converts month, year and day values to a Timestamp object.
     *
     * NOTE: THIS METHOD RETURNS ZERO VALUES FOR TIME FIELDS IN THE TIMESTAMP.
     *
     * @param month   Number indicating the month. See the isCalendarMonth
     *                param for more info.
     * @param year    The four digit year.
     * @param day     Number indicating the day of the month.
     * @param isCalendarMonth   'TRUE' indicates the month param follows the
     *                  java.util.Calendar numbering of 0-11, and 'FALSE'
     *                  indicate that it follows a 1-12 numbering.
     *
     * @return a Timestamp representing the date values passed in.
     */
    private static Timestamp getTimestamp(int month, int year, int day, boolean isCalendarMonth) {
        Calendar cal = Calendar.getInstance();
        // Convert a non-Calendar month (1-12) to a Calendar month (0-11).
        if (!isCalendarMonth) {
            month -= 1;
        }
        // Convert year in case it is in format YY.
        year = convert2DigitYear(year);
        cal.set(year, month, day);
        //return new Timestamp(cal.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
        return getTimestamp(format.format(cal.getTime()));
    }

    public static int convert2DigitYear(int yy) {
        if (yy < 100) {
            int cutoff = ConfigUtil.getConfigurationData().getYearConversionCutoff();
            if (yy < cutoff) {
                yy += 2000;
            } else {
                yy += 1900;
            }
        }
        return yy;
    }


    /**
     * Creats a Timestamp representing today's date.
     *
     * NOTE: THIS METHOD RETURNS ZERO VALUES FOR TIME FIELDS IN THE TIMESTAMP.
     *
     * @return a Timestamp object.
     */
    public static Timestamp getCurrentDateAsTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
        return getTimestamp(format.format(new java.util.Date()));
    }

    /**
     * Creats a Timestamp representing today's date at the current time.
     *
     * @return a Timestamp object.
     */
    public static Timestamp getCurrentDateTimeAsTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Increases the date associated with the given parameter by one day.
     *
     * NOTE: THIS METHOD RETURNS ZERO VALUES FOR TIME FIELDS IN THE TIMESTAMP.
     *
     * @param ts The Timestamp to use as the starting date.
     *
     * @return a Timestamp representing the new date.
     */
    public static Timestamp incrementTimestampByDay(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        cal.add(Calendar.DATE, 1);
        return getTimestamp(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), cal.get(Calendar.DATE), true);
    }

    /**
     * Converts a Timestamp to a String in the format defined in the constant
     * DATE_FORMAT_STRING.
     *
     * @param ts Timestamp object.
     *
     * @return a String representing the date in the Timestamp param.
     */
    public static String getSimpleDateString(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
        return format.format(new java.util.Date(ts.getTime()));
    }

    /**
     * Converts a Calendar to a String in the format defined in the constant
     * DATE_FORMAT_STRING.
     *
     * @param cal Calendar object.
     *
     * @return a String representing the date in the Calendar param.
     */
    public static String getSimpleDateString(Calendar cal) {
        if (cal == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
        return format.format(cal.getTime());
    }

    /**
     * Converts a Timestamp to a Calendar, which can then be used to retrieve the
     * month and year values.
     */
    public static Calendar getCalendar(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        return cal;
    }

    /**
	 * Checks whether the year is within the range specified by the database server.
	 *
	 * @param year The year to be checked.
	 * @return Flag indicating whether year is in range.
	 */
	public static boolean isYearInRange(int year) {
		if (year <= MAX_YEAR && year >= MIN_YEAR) {
			return true;
		}
		return false;
    }
}
