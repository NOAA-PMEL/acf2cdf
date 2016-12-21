/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import gov.noaa.pmel.eps.EPS_Util;
import gov.noaa.pmel.eps.TimeConversionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author derek
 */
public class Mask {

    private Date startTime;
    private Date endTime;
    public final static String rfc3339Format = "yyyy-MM-dd HH:mm:ss";
    private Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    public Calendar getCal(Date d) {
        cal.setTime(d);
        return cal;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(int mon, int day, int yr, int hour, int min, int sec) {
        String date_str = yr+"-"+mon+"-"+day+" "+hour+":"+min+":"+sec;
        try {
            startTime = new SimpleDateFormat(rfc3339Format).parse(date_str);
        } catch (ParseException ex) {
            Logger.getLogger(Mask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setEndTime(int mon, int day, int yr, int hour, int min, int sec) {
        String date_str = yr+"-"+mon+"-"+day+" "+hour+":"+min+":"+sec;
        try {
            endTime = new SimpleDateFormat(rfc3339Format).parse(date_str);
        } catch (ParseException ex) {
            Logger.getLogger(Mask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private long[] getTimeAsEPSTime(Date d) {
        long[] epstime = new long[2];

        Calendar startCal = getCal(d);
        int mon = startCal.get(Calendar.MONTH);
        int day = startCal.get(Calendar.DAY_OF_MONTH);
        int yr = startCal.get(Calendar.YEAR);
        int hour = startCal.get(Calendar.HOUR_OF_DAY);
        int min = startCal.get(Calendar.MINUTE);
        int sec = startCal.get(Calendar.SECOND);
        try {
            epstime = EPS_Util.mdyhmsToEpicTime(mon, day, yr, hour, min, sec);
        } catch (TimeConversionException ex) {
            Logger.getLogger(Mask.class.getName()).log(Level.SEVERE, null, ex);
        }

        return epstime;
    }

    public long[] getStartTimeAsEPSTime() {
        return getTimeAsEPSTime(startTime);
    }

    public long[] getEndTimeAsEPSTime() {
        return getTimeAsEPSTime(endTime);
    }

    public boolean isMasked(Date d) {
        return ( (d.compareTo(startTime) >= 0) && (d.compareTo(endTime) <= 0) );
    }
}
