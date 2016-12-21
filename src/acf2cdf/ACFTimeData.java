/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author derek
 */
public class ACFTimeData implements ACFVariableData {
    List<Date> times = new ArrayList<Date>();
    public final static String rfc3339Format = "yyyy-MM-dd HH:mm:ss";

    public void append(double dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void appendTime(Date dat) {
        times.add(dat);
    }

    public void append(double[] dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void append(double[][] dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] get() {
        Date[] dat = getTime();
        double[] d = new double[dat.length];
        for (int i=0; i<dat.length; i+=1) {
            d[i] = dat[i].getTime();
        }
        return d;
    }

    public double[][] get2d() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[][][] get3d() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date[] getTime() {
        Date[] time_array = new Date[times.size()];
        for (int i=0; i<times.size(); i+=1) {
            time_array[i] = times.get(i);
        }
        return time_array;
    }

    public double getAtIndex(ACFIndex index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getTimeAtIndex(ACFIndex index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAtIndex(ACFIndex index, double dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTimeAtIndex(ACFIndex index, Date dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void set(int i, double val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void findAndReplace(double find, double replace) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[][][][] get4d() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
