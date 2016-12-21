/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author derek
 */
public class ACFVariable {
    private ACFVariableHeader header = new ACFVariableHeader();
    private List<String> dimensions = new ArrayList<String>();
    private ACFVariableData data = null;

    private String dataType;
    private String name;

    public ACFVariable(String name, String type) {

        setName(name);
        setDataType(type);
        //header.setHeaderEntry("name", name);
        createData();
    }

    public ACFVariable(ACFVariableHeader header, String type) {

        setName(header.getHeaderEntry("name"));
        setDataType(type);
        setHeader(header);
        //header.setHeaderEntry("name", name);
        createData();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        header.setHeaderEntry("name", name);
        this.name = name;
    }

    public void addDimensionName(String name) {
        dimensions.add(name);
    }

    public List<String> getDimensionNames() {
        return dimensions;
    }


    public String getDataType() {
        return dataType;
    }

    private void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ACFVariableData getData() {
        return data;
    }

    public ACFVariableHeader getHeader() {
        return header;
    }

    public void setHeader(ACFVariableHeader header) {
        this.header = header;
    }


    private void createData() {
        if (getDataType().equals("TIMESERIES-1D")) {
            data = new ACFData1d();
        } else if (getDataType().equals("TIMEDATA")) {
            data = new ACFTimeData();
        }
    }

    public void setBadAtIndex(ACFIndex index) {
        double bad = new Double(header.getHeaderEntry("gap_value"));
        //data.set(i,bad);
        data.setAtIndex(index, bad);
    }

    public ACFVariableData getEpicData() {
        double bad = new Double(header.getHeaderEntry("gap_value"));
        data.findAndReplace(bad, ACFDataset.EPS_GAP);
        return data;
    }

    public String getEpicName() {
        String epicName = getName() + "_" + header.getHeaderEntry("epic_code");
        return epicName;
    }
}
