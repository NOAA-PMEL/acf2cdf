/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author derek
 */
public class ACFFileReader {

    private File acfFile;
    private MaskFileReader maskFile = null;
    private ACFHeader header = new ACFHeader();
    private ACFDataset dataset = null;
    private List<ACFDimension> dimensions = new ArrayList<ACFDimension>();
    private List<ACFVariable> parameters = new ArrayList<ACFVariable>();
    private boolean timeHasSeconds = false;
    protected boolean evenTimeStep = false;

    public ACFFileReader(String fileName) throws IOException {
        if (!setFileName(fileName)) {
            throw new IOException("ACF File: " + fileName + " does not exist!");
        }
    }

    public boolean setFileName(String fileName) {
        acfFile = new File(fileName);
        return acfFile.exists();

    }

    public void setMaskFile(MaskFileReader maskReader) {
        maskFile = maskReader;
        try {
            maskFile.parse();
        } catch (IOException ex) {
            Logger.getLogger(ACFFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNextLine(BufferedReader reader) throws IOException {
        if (!reader.ready()) {
            return new String("");
        }
        return reader.readLine().trim();
    }

    public ACFDataset parse() throws IOException {
        BufferedReader acfReader = new BufferedReader(new FileReader(acfFile));
        String line;

        line = getNextLine(acfReader);
        while (!line.isEmpty()) {
            //line = line.trim();
            if (isSectionName(line)) {
                String section = getSectionName(line);

                if (section.equals("HEADER")) {
                    line = parseHeader(acfReader);
                    if (header.getEntryValueByName("DATA_TYPE").equals("TIMESERIES-1D")) {
                        //create Dataset
                        dataset = new ACFTimeSeriesDataset(header);
                        //addACFDimensions();
                    }
                } else if (section.equals("TIME PARAMETERS")) {
                    // check which time style we are using
                    line = getNextLine(acfReader);
                    if (line.equals("year month day hour minute second")) {
                        timeHasSeconds = true;
                    }
                    line = getNextLine(acfReader);
                } else if (section.equals("PARAMETER")) {
                    // add parameter
                    line = parseParameter(acfReader);

                    //} else if (section.equals("DATA X HEADINGS")) {
                } else if (section.startsWith("DATA") && section.endsWith("HEADINGS")) {
                    Scanner input = new Scanner(getSectionName(line));
                    input.skip("DATA");
                    int headings = input.nextInt();

                    for (int i = 0; i < headings; i += 1) {
                        line = getNextLine(acfReader);
                    }

                    line = parseData(acfReader);
                    break;
                }

            } else {
                line = getNextLine(acfReader);
            }
        }

        return dataset;
    }

    private boolean isSectionName(String line) {
        //line.trim();
        if (line.startsWith("#") && line.endsWith("#")) {
            return true;
        }
        return false;
    }

    private String getSectionName(String line) {
        return line.substring(1, line.length() - 1);
    }

    /**
     * Get the value of evenTimeStep
     *
     * @return the value of evenTimeStep
     */
    public boolean isEvenTimeStep() {
        return evenTimeStep;
    }

    /**
     * Set the value of evenTimeStep
     *
     * @param evenTimeStep new value of evenTimeStep
     */
    public void setEvenTimeStep(boolean evenTimeStep) {
        this.evenTimeStep = evenTimeStep;
    }

    private String parseHeader(BufferedReader reader) throws IOException {
        String line;
        String section;

        line = getNextLine(reader);
        while (!line.isEmpty()) {
            //line.trim();
            if (isSectionName(line)) {
                section = getSectionName(line);
                if (section.equals("REMARKS")) {
                    // process remarks
                    line = processHeaderRemarks(reader);
                } else {
                    return line;
                }
            } else {
                header.addEntry(line);
                line = getNextLine(reader);
            }

        }

        return line;
    }

    private String processHeaderRemarks(BufferedReader reader) throws IOException {
        String line;
        line = getNextLine(reader);
        while (!line.isEmpty()) {
            if (isSectionName(line)) {
                return line;
            } else {
                header.addRemark(line);
                line = getNextLine(reader);
            }
        }
        return line;
    }

    private void addACFDimensions() {

        // time
        //ACFDimension timeDim = new ACFDimension("time", true, true);
        //timeDim.setTimeData(true);
        //dimensions.add(timeDim);
        dimensions.add(new ACFDimension("time", true, true));
        //dataset.addDimension(new ACFDimension("time", true, true));
        // depth (dep)
        dimensions.add(new ACFDimension("dep", false, false));

        // latitude (lat)
        dimensions.add(new ACFDimension("lat", false, false));

        //longitude (lon)
        dimensions.add(new ACFDimension("lon", false, false));




    }

    private String parseParameter(BufferedReader reader) throws IOException {
        String line;
        String section;
        int cnt = 1;

        line = getNextLine(reader);
        if (line.isEmpty()) {
            throw new IOException("error reading Parameter info");
        }
        // first line is name
        String varName = line;
        //dataset.addVariable(varName);
        //ACFVariable par = new ACFVariable(line, "TIMESERIES-1D");
        ACFVariableHeader varHeader = new ACFVariableHeader();
        varHeader.setHeaderEntry("name", line);
        cnt++;
        line = getNextLine(reader);
        while (!line.isEmpty()) {
            //line.trim();
            if (isSectionName(line)) {
                section = getSectionName(line);
                if (section.equals("PARAMETER NOTES")) {
                    // process remarks
                    //line = processParameterNotes(reader, par);
                    line = processParameterNotes(reader, varHeader);
                } else {
                    break;
                    //return line;
                }
            } else {
                String key = "";

                switch (cnt) {
                    case 2:
                        key = "units";
                        break;
                    case 3:
                        key = "instr_id";
                        break;
                    case 4:
                        key = "min_value";
                        break;
                    case 5:
                        key = "max_value";
                        break;
                    case 6:
                        key = "gap_value";
                        break;
                    case 7:
                        key = "epic_code";
                        break;
                    default:
                        break;
                }
                if (cnt >= 8) {
                    throw new IOException("error reading Parameter info");
                }

                //par.getHeader().setHeaderEntry(key, line);
                varHeader.setHeaderEntry(key, line);
                cnt++;
                line = getNextLine(reader);

            }

        }

        //parameters.add(par);
        dataset.addVariable(varHeader);

        return line;

    }

    private String processParameterNotes(BufferedReader reader, ACFVariableHeader varHead) throws IOException {
        String line;
        line = getNextLine(reader);
        while (!line.isEmpty()) {
            if (isSectionName(line)) {
                return line;
            } else {
                //varHead.getHeader().addNote(line);
                varHead.addNote(line);
                line = getNextLine(reader);
            }
        }
        return line;
    }

    /**
     * This method is to reads the data from the ACF file and parses the time only.
     * It then passes an array of Strings on to the dataset to parse. This will have
     * to be modified if ACF ever describes anything other than a N-d dataset.
     */
    private String parseData(BufferedReader reader) throws IOException {
        String line;

        while (reader.ready()) {
            line = reader.readLine();
            Scanner input = new Scanner(line);

            String date_str = input.next() + "-" + input.next() + "-" + input.next()
                    + " " + input.next() + ":" + input.next() + ":";

            String secs = "00";
            if (timeHasSeconds) {
                secs = input.next();
            }
            date_str += secs;
            try {
                Date dat = new SimpleDateFormat(ACFTimeData.rfc3339Format).parse(date_str);
                dataset.appendTime(dat);
            } catch (ParseException ex) {
                Logger.getLogger(ACFFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<String> values = new ArrayList<String>();
            while (input.hasNext()) {
                values.add(input.next());
            }
            dataset.parseDataAsStrings(values.toArray(new String[0]));

        }

        return null;
    }

// <editor-fold defaultstate="collapsed" desc="comment">
//    public void process() {
//        // check for even time steps, gaps, etc
//        ACFDimension timeDim = getTimeDimension();
//        if (timeDim == null) {
//            throw new UnsupportedOperationException("Can't process data...no Time Dimension");
//        }
//
//        ACFTimeUtil timeUtil = new ACFTimeUtil(timeDim.getData().getTime());
//
//        timeUtil.isMonotonic();
//        if (timeUtil.hasGaps()) {
//            for (ACFTimeGap gap : timeUtil.getGaps()) {
//                SimpleDateFormat fmt = new SimpleDateFormat(ACFTimeData.rfc3339Format);
//                System.out.println("Time gap at data point # " + gap.getDataPoint()
//                        + " from " + fmt.format(gap.getStart())
//                        + " to " + fmt.format(gap.getEnd()));
//            }
//        }
//
//        if (evenTimeStep) {
//            timeUtil.hasEvenTimeStep();
//        }
//
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
//
//
//    }// </editor-fold>

    public void writeToNetCDF() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private ACFDimension getTimeDimension() {
        for (ACFDimension dim : dimensions) {
            if (dim.getName().equals("time")) {
                return dim;
            }
        }
        return null;
    }
}
