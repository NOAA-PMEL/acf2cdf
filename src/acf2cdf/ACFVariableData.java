/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

import java.util.Date;

/**
 *
 * @author derek
 */
public interface ACFVariableData {

    public void append(double dat);
    public void appendTime(Date dat);
    public void append(double[] dat);
    public void append(double[][] dat);

    public double[] get();
    public double[][] get2d();
    public double[][][] get3d();
    public double[][][][] get4d();


    public Date[] getTime();

    public double getAtIndex(ACFIndex index);
    public Date getTimeAtIndex(ACFIndex index);
    public void setAtIndex(ACFIndex index, double dat);
    public void setTimeAtIndex(ACFIndex index, Date dat);

    public void set(int i, double val);
    // get as cdf parameter

    public void findAndReplace(double find, double replace);

}
