/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author derek
 */
public class ACFData1d implements ACFVariableData {

    ArrayList<Double> data = new ArrayList<Double>();

    public void append(double dat) {
        data.add(dat);
    }

    public void append(double[] dat) {
        throw new UnsupportedOperationException("Wrong type...only supports 1d.");
    }

    public void append(double[][] dat) {
        throw new UnsupportedOperationException("Wrong type...only supports 1d.");
    }

    public double[] get() {
        double[] data_array = new double[data.size()];
        for (int i=0; i<data.size(); i+=1) {
            data_array[i] = data.get(i);
        }
        return data_array;
    }

    public double[][] get2d() {
        throw new UnsupportedOperationException("Wrong type...only supports 1d.");
    }

    public double[][][] get3d() {
        throw new UnsupportedOperationException("Wrong type...only supports 1d.");
    }

    public double[][][][] get4d() {
        double[][][][] dat4 = new double[data.size()][1][1][1];
        for (int i=0; i<data.size(); i+=1) {
            dat4[i][0][0][0] = data.get(i);
        }
        return dat4;
    }

    public void appendTime(Date dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date[] getTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getAtIndex(ACFIndex index) {
        return data.get(index.get(0));
    }

    public Date getTimeAtIndex(ACFIndex index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAtIndex(ACFIndex index, double dat) {
        data.set(index.get(0), dat);
    }

    public void setTimeAtIndex(ACFIndex index, Date dat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void set(int i, double val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void findAndReplace(double find, double replace) {
        //for (Double dat : data) {
        for (int i=0; i<data.size();i++) {
            if (data.get(i) == find) {
                //System.out.println("find / dat / replace  " + find + " / " + data.get(i) + " / " + replace);
                data.set(i, replace);
            }
            //System.out.println("data = " + data.get(i));
        }
    }

}
