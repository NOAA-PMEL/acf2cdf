/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import gov.noaa.pmel.eps.Key;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriteable;

/**
 *
 * @author derek
 */
public class ACFTimeSeriesDataset extends ACFDataset {

    public ACFTimeSeriesDataset(ACFHeader header) {
        super(header);
    }

    @Override
    void initDimensions() {
        // TODO if format changes, use header to define dimensions

        // time
        ACFDimension dim;
        dim = new ACFDimension("time", true, true);
        dim.getHeader().setHeaderEntry("FORTRAN_format", "");
        dim.getHeader().setHeaderEntry("units", "");
//        if (getMetadata("evenTimeStep").equals("true")) {
//            dim.getHeader().setHeaderEntry("type", "EVEN");
//        }
        dim.getHeader().setHeaderEntry("epic_code", "");
        addDimension(dim);
        //addDimension(new ACFDimension("time", true, true));


        // depth (dep)
        dim = new ACFDimension("dep", false, false);
        dim.getHeader().setHeaderEntry("FORTRAN_format", "f10.1");
        dim.getHeader().setHeaderEntry("units", "m");
        dim.getHeader().setHeaderEntry("type", "EVEN");
        dim.getHeader().setHeaderEntry("epic_code", "3");
        addDimension(dim);
        //addDimension(new ACFDimension("dep", false, false));


        // latitude (lat)
        dim = new ACFDimension("lat", false, false);
        dim.getHeader().setHeaderEntry("FORTRAN_format", "f10.4");
        dim.getHeader().setHeaderEntry("units", "degree_north");
        dim.getHeader().setHeaderEntry("type", "EVEN");
        dim.getHeader().setHeaderEntry("epic_code", "500");
        addDimension(dim);
        //addDimension(new ACFDimension("lat", false, false));

        //longitude (lon)
        dim = new ACFDimension("lon", false, false);
        dim.getHeader().setHeaderEntry("FORTRAN_format", "f10.4");
        dim.getHeader().setHeaderEntry("units", "degree_east");
        dim.getHeader().setHeaderEntry("type", "EVEN");
        dim.getHeader().setHeaderEntry("epic_code", "502");
        addDimension(dim);
        //addDimension(new ACFDimension("lon", false, false));

    }

    @Override
    void addVariable(ACFVariableHeader header) {
        // TODO allow for multiple dimensions at some point
        ACFVariable var = new ACFVariable(header, ACFDATASET_TIMESERIES_1D);
        variables.add(var);
    }

    @Override
    void parseDataAsStrings(String[] dataStr) {
        int cnt = 0;
        for (ACFVariable var : variables) {
            if (var.getDimensionNames().size() > 1) {
                // deal with multiple dimensions
            } else {
//                var.getData().append(new Double(dataStr[cnt]));

                var.getData().append(new Double(Double.parseDouble(dataStr[cnt])));
                cnt++;
            }
        }
    }

    @Override
    void verify() {
        // check for even time steps, gaps, etc
        ACFDimension timeDim = getTimeDimension();
        if (timeDim == null) {
            throw new UnsupportedOperationException("Can't process data...no Time Dimension");
        }

        ACFTimeUtil timeUtil = new ACFTimeUtil(timeDim.getData().getTime());

        timeUtil.isMonotonic();
        if (timeUtil.hasGaps()) {
            String msg = "";
            for (ACFTimeGap gap : timeUtil.getGaps()) {
                SimpleDateFormat fmt = new SimpleDateFormat(ACFTimeData.rfc3339Format);
                System.out.println("Time gap at data point # " + gap.getDataPoint()
                        + " from " + fmt.format(gap.getStart())
                        + " to " + fmt.format(gap.getEnd()));
                msg += "Time gap at data point # " + gap.getDataPoint()
                        + " from " + fmt.format(gap.getStart())
                        + " to " + fmt.format(gap.getEnd()) + "\n";
            }
            JOptionPane.showMessageDialog(Acf2cdfApp.getApplication().getMainFrame(),
                        msg, "Time Gaps", JOptionPane.INFORMATION_MESSAGE);
        }

        if (getMetadata("evenTimeStep").equals("true")) {
            getTimeDimension().getHeader().setHeaderEntry("type", "EVEN");
            timeUtil.hasEvenTimeStep();
        }

//        // mask data of needed
//        if (maskFile != null) {
//            Date t[] = timeDim.getData().getTime();
//            for (int i = 0; i < t.length; i += 1) {
//                if (maskFile.isMasked(t[i])) {
//                    for (ACFVariable param : parameters) {
//                        param.setBadAtIndex(i);
//                    }
//                }
//            }
//        }


    }

    private ACFDimension getTimeDimension() {
        return getDimensionByName("time");
    }

    @Override
    void appendTime(Date dat) {
        getTimeDimension().getData().appendTime(dat);
    }

    @Override
    void maskData(MaskFileReader mfReader) {
        try {
            mfReader.parse();

            Date t[] = getTimeDimension().getData().getTime();
            for (int i = 0; i < t.length; i += 1) {
                if (mfReader.isMasked(t[i])) {
                    for (ACFVariable var : variables) {
                        var.setBadAtIndex(new ACFIndex(i));
                    }
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(ACFTimeSeriesDataset.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    void writeToEpicCDF(String cdfFilename) {

        NetcdfFileWriteable ncFile = NetcdfFileWriteable.createNew(cdfFilename);
        //NetcdfFileWriteable ncFile = new NetcdfFileWriteable();
        //ncFile.setName(cdfFilename);

        // add dimensions
        Dimension[] epicDims = new Dimension[4];

        // TIME
        Dimension timeDim = ncFile.addDimension(getTimeDimension().getName(),
                getTimeDimension().getData().get().length);
        // add epic style time variables
        ncFile.addVariable("time", DataType.INT, new Dimension[]{timeDim});
        ncFile.addVariableAttribute("time", "format", "");
        ncFile.addVariableAttribute("time", "units", "True Julian Day");
        ncFile.addVariableAttribute("time", "type", getTimeDimension().getHeader().getHeaderEntry("type"));
        ncFile.addVariableAttribute("time", "epic_code", 624);

        ncFile.addVariable("time2", DataType.INT, new Dimension[]{timeDim});
        ncFile.addVariableAttribute("time2", "format", "");
        ncFile.addVariableAttribute("time2", "units", "msec since 0:00 GMT");
        ncFile.addVariableAttribute("time2", "type", getTimeDimension().getHeader().getHeaderEntry("type"));
        ncFile.addVariableAttribute("time2", "epic_code", 624);
        epicDims[0] = timeDim;

        String[] otherDims = {"dep", "lat", "lon"}; // this ensures the proper order
        for (int i = 0; i < otherDims.length; i += 1) {
            Dimension ncDim = ncFile.addDimension(otherDims[i], 1);
//                    getDimensionByName(otherDims[i]).getData().get().length);

            epicDims[i + 1] = ncDim;

            ncFile.addVariable(otherDims[i], DataType.FLOAT, new Dimension[]{ncDim});
            ACFDimensionHeader dimHead = getDimensionByName(otherDims[i]).getHeader();
            for (ACFHeaderEntry entry : dimHead.getEntries()) {
                if (entry.getName().equals("epic_code")) {
                    ncFile.addVariableAttribute(otherDims[i], entry.getName(), new Integer(entry.getValue()));
                } else {
                    ncFile.addVariableAttribute(otherDims[i], entry.getName(), entry.getValue());
                }
            }

        }


        //EPIC_Key_DB keyDB = new EPIC_Key_DB("/home/lorax/derek/Software/ACF/acf2cdf/epic.key");

        //EPIC_Key_DB keyDB = new EPIC_Key_DB("resources/epic.key");
        //EPIC_Key_DB keyDB = new EPIC_Key_DB("epic.key");


        // this is done in EPIC_Key_DB now
        // get epic.key file from embedded resources directory
//        BufferedReader keyDB_reader = null;
//        keyDB_reader = new BufferedReader(new InputStreamReader(Acf2cdfApp.class.getResourceAsStream("resources/epic.key")));

        EPIC_Key_DB keyDB = new EPIC_Key_DB();
        
        // add variables
        for (ACFVariable var : variables) {

            int epic_code = new Integer(var.getHeader().getHeaderEntry("epic_code"));
            Key epicKey = keyDB.findKey(epic_code);
            //var.getHeader().setHeaderEntry("name", epicKey.getSname());
            var.setName(epicKey.getSname());
            var.getHeader().setHeaderEntry("long_name", epicKey.getLname());
            var.getHeader().setHeaderEntry("generic_name", epicKey.getGname());
            var.getHeader().setHeaderEntry("FORTRAN_format", epicKey.getFrmt());
            var.getHeader().setHeaderEntry("units", epicKey.getUnits());

            String varName = var.getEpicName();
            ncFile.addVariable(varName, DataType.FLOAT, epicDims);
            for (ACFHeaderEntry entry : var.getHeader().getEntries()) {
                if (entry.getName().equals("epic_code")) {
                    ncFile.addVariableAttribute(varName, entry.getName(), epic_code);
                } else {
                    ncFile.addVariableAttribute(varName, entry.getName(), entry.getValue());
                }
            }

            int cnt = 1;
            for (String note : var.getHeader().getNotes()) {
                String name = "note_" + String.format("%02d", cnt);
                ncFile.addVariableAttribute(varName, name, note);
                cnt++;
            }

        }

        // gloabal attributes
        for (ACFHeaderEntry entry : header.getEntries()) {
            ncFile.addGlobalAttribute(entry.getName(), entry.getValue());
        }
        int remCnt = 1;
        for (String remark : header.getRemarks()) {
            String name = "REMARK_" + String.format("%02d", remCnt);
            ncFile.addGlobalAttribute(name, remark);
            remCnt++;
        }
        String createFormat = "HH:mm d-MMMM-yyyy";
        SimpleDateFormat fmt = new SimpleDateFormat(createFormat);
        Date now = new Date();
        String creation_date = fmt.format(now);
        ncFile.addGlobalAttribute("CREATION_DATE", creation_date);

        try {
            ncFile.create();
        } catch (IOException ex) {
            Logger.getLogger(ACFTimeSeriesDataset.class.getName()).log(Level.SEVERE, null, ex);
        }


        // write dim values
        ACFTimeUtil util = new ACFTimeUtil();

        Date[] d = getTimeDimension().getData().getTime();
        long[] timeVal = new long[d.length];
        long[] time2Val = new long[d.length];
        for (int i = 0; i < d.length; i += 1) {
            long[] epsDate = util.getTimeAsEPSTime(d[i]);
            timeVal[i] = epsDate[0];
            time2Val[i] = epsDate[1];
        }
        try {
            ncFile.write("time", Array.factory(timeVal));
            ncFile.write("time2", Array.factory(time2Val));

            ncFile.write("dep", Array.factory(new int[]{0}));

            float firstval = (float) getVariableByName("LAT").getData().getAtIndex(new ACFIndex(0));
            ncFile.write("lat", Array.factory(new float[]{firstval}));

            firstval = (float) getVariableByName("LON").getData().getAtIndex(new ACFIndex(0));
            ncFile.write("lon", Array.factory(new float[]{firstval}));

            for (ACFVariable var : variables) {
                double[] test = var.getEpicData().get();
                ncFile.write(var.getEpicName(), Array.factory(var.getEpicData().get4d()));
            }

        } catch (IOException ex) {
            Logger.getLogger(ACFTimeSeriesDataset.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRangeException ex) {
            Logger.getLogger(ACFTimeSeriesDataset.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            ncFile.close();
        } catch (IOException ex) {
            Logger.getLogger(ACFTimeSeriesDataset.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
