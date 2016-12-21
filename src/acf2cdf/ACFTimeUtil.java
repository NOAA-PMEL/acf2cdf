/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author derek
 */
public class ACFTimeUtil {

    List<Date> dates = null;
    List<ACFTimeGap> gaps = null;
    double delta = 0;
//    private Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    private Calendar cal = Calendar.getInstance();
    static int[] max_day = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static long GREGORIAN = (15 + 31 * (10 + 12 * 1582));
    public static long JULGREG = 299161;

    public ACFTimeUtil() {
    }

    public ACFTimeUtil(Date[] d) {
        if (d != null) {
            dates = new ArrayList<Date>();
            for (int i = 0; i < d.length; i += 1) {
                dates.add(d[i]);
            }
        }

    }

    public Calendar getCal(Date d) {
        cal.setTime(d);
        return cal;
    }

    public List<ACFTimeGap> getGaps() {
        return gaps;
    }

    public double getDelta() {
        return delta;
    }

    public boolean isMonotonic() {
        for (int i = 1; i < dates.size(); i += 1) {
            if (calcDelta(i - 1, i) <= 0) {
                String msg = "Time is not monotonic at data point # " + i;
                JOptionPane.showMessageDialog(Acf2cdfApp.getApplication().getMainFrame(), 
                        msg, "Non-monotonic Alert", JOptionPane.ERROR_MESSAGE);
                throw new UnsupportedOperationException(msg);
            }
        }
        return true;
    }

    public boolean hasGaps() {

        gaps = new ArrayList<ACFTimeGap>();

        if (delta == 0) {
            initDelta();
        }

        for (int i = 1; i < dates.size(); i += 1) {
            if (calcDelta(i - 1, i) > delta) {
                gaps.add(new ACFTimeGap(i, dates.get(i - 1), dates.get(i)));
            }
        }

        return !gaps.isEmpty();
    }

    public boolean hasEvenTimeStep() {
        for (int i = 1; i < dates.size(); i += 1) {
            if ((calcDelta(i - 1, i) % delta) != 0) {
                String msg = "Time step is not even at data point # " + i;
                JOptionPane.showMessageDialog(Acf2cdfApp.getApplication().getMainFrame(),
                        msg, "Uneven Timestep Alert", JOptionPane.ERROR_MESSAGE);
                throw new UnsupportedOperationException(msg);
            }
        }
        return true;
    }

    private void initDelta() {
        delta = calcDelta(0, 1);
    }

    private double calcDelta(int start, int end) {
        return dates.get(end).getTime() - dates.get(start).getTime();
    }

    public long[][] getEpicTime() {
        long[][] epsDate = new long[dates.size()][2];
        int cnt = 0;
        for (Date date : dates) {
            long[] d = getTimeAsEPSTime(date);
            epsDate[cnt] = d;
        }
        return epsDate;
    }

    public long[] getTimeAsEPSTime(Date d) {
        long[] epstime = new long[2];

        Calendar startCal = getCal(d);
        int mon = startCal.get(Calendar.MONTH)+1;
        int day = startCal.get(Calendar.DAY_OF_MONTH);
        int yr = startCal.get(Calendar.YEAR);
        int hour = startCal.get(Calendar.HOUR_OF_DAY);
        int min = startCal.get(Calendar.MINUTE);
        int sec = startCal.get(Calendar.SECOND);
        try {
            epstime = mdyhmsToEpicTime(mon, day, yr, hour, min, sec);
        } catch (TimeConversionException ex) {
            Logger.getLogger(Mask.class.getName()).log(Level.SEVERE, null, ex);
        }

        return epstime;



    }

    /**
     * Convert mdy hms to eps time.

     * @param mon Month
     * @param day Day
     * @param yr Year
     * @param hour Hour
     * @param min Minute
     * @param sec Seconds
     *
     * @return EPIC format time.
     *
     * @exception TimeConversionException An error occurred converting to EPIC time.
     *
     * Blatently stolen from EPS_Util because of netcdf version mismatch
     *
     **/
    public static long[] mdyhmsToEpicTime(int mon, int day, int yr, int hour, int min, double sec) throws TimeConversionException {
        long[] time = new long[2];
        long jul, ja, jy, jm;

        // check the valid month input */
        if (mon > 12 || mon < 1) {
            throw new TimeConversionException("mdyhmsToEpicTime: invalid month input: " + mon);
        }

        // check the valid day input */
        int leap = (yr % 4 != 0 ? 0 : (yr % 400 == 0 ? 1 : (yr % 100 == 0 ? 0 : 1)));
        max_day[1] = 28 + leap;

        if (day > max_day[mon - 1] || day < 1) {
            throw new TimeConversionException("mdyhmsToEpicTime: invalid day input: " + day);
        }

        /* check the valid hour minute second input */
        if (hour >= 24 || hour < 0) {
            throw new TimeConversionException("mdyhmsToEpicTime: invalid hour input: " + hour);
        }

        if (min >= 60 || min < 0) {
            throw new TimeConversionException("mdyhmsToEpicTime: invalid minute input: " + min);
        }

        if (sec >= 60 || sec < 0) {
            throw new TimeConversionException("mdyhmsToEpicTime: invalid second input: " + sec);
        }

        if (yr < 0) {
            ++yr;
        }
        if (mon > 2) {
            jy = yr;
            jm = mon + 1;
        } else {
            jy = yr - 1;
            jm = mon + 13;
        }
        jul = (long) (Math.floor(365.25 * jy) + Math.floor(30.6001 * jm) + day + 1720995);
        if (day + 31 * (mon + 12L * yr) >= GREGORIAN) {
            ja = (long) (0.01 * jy);
            jul += 2 - ja + (long) (0.25 * ja);
        }
        time[0] = jul;
        time[1] = (long) ((hour * 3600 + min * 60) * 1000 + (double) (sec * 1000));
        return time;
    }
}
