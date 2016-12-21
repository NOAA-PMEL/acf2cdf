/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

/**
 *
 * @author derek
 */
public class ACFDimension {

    private String name;
    private ACFDimensionHeader header = new ACFDimensionHeader();
    private ACFVariableData data = null;

    private boolean timeData = false;
    private boolean unlimited = false;


    public ACFDimension(String name, boolean isTime, boolean unlimited) {
        setName(name);
        setTimeData(isTime);
        setUnlimited(unlimited);
        
        createData();
    }



    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * Get the value of timeData
     *
     * @return the value of timeData
     */
    public boolean isTimeData() {
        return timeData;
    }

    /**
     * Set the value of timeData
     *
     * @param timeData new value of timeData
     */
    public void setTimeData(boolean timeData) {
        this.timeData = timeData;
    }

    public ACFVariableData getData() {
        return data;
    }

    public void setData(ACFVariableData data) {
        this.data = data;
    }

    public ACFDimensionHeader getHeader() {
        return header;
    }

    public void setHeader(ACFDimensionHeader header) {
        this.header = header;
    }

    /**
     * Get the value of unlimited
     *
     * @return the value of unlimited
     */
    public boolean isUnlimited() {
        return unlimited;
    }

    /**
     * Set the value of unlimited
     *
     * @param unlimited new value of unlimited
     */
    public void setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    private void createData() {
        if (isTimeData()) {
            data = new ACFTimeData();
        } else {
            data = new ACFData1d();
        }
    }



}
