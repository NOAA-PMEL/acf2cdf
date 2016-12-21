/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author derek
 */
public abstract class ACFDataset {

    protected ACFHeader header = new ACFHeader();
    protected List<ACFDimension> dimensions = new ArrayList<ACFDimension>();
    protected List<ACFVariable> variables = new ArrayList<ACFVariable>();
    protected Map<String, String> metadata = new HashMap<String, String>();
    public static final String ACFDATASET_TIMESERIES_1D = "TIMESERIES-1D";

    public static final double EPS_GAP = 1.e35;

    public ACFDataset(ACFHeader header) {
        setHeader(header);
        initDimensions();
    }

    public ACFHeader getHeader() {
        return header;
    }

    public void setHeader(ACFHeader header) {
        this.header = header;
    }

    public void addDimension(ACFDimension dim) {
        dimensions.add(dim);
    }

    public void putMetadata(String key, String value) {
        metadata.put(key, value);
        String val = metadata.get(key);
    }

    public String getMetadata(String key) {
        String val = metadata.get(key);
        return metadata.get(key);
    }

    protected ACFDimension getDimensionByName(String name) {
        for (ACFDimension dim : dimensions) {
            if (dim.getName().equals(name)) {
                return dim;
            }
        }
        return null;
    }

    protected ACFVariable getVariableByName(String name) {
        for (ACFVariable var : variables) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return null;
    }

    abstract void initDimensions();

    abstract void addVariable(ACFVariableHeader header);

    abstract void appendTime(Date dat);

    abstract void parseDataAsStrings(String[] dataStr);

    abstract void verify();

    abstract void maskData(MaskFileReader mfReader);

    abstract void writeToEpicCDF(String cdfFilename);
}
