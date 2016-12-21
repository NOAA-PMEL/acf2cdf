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
public class MaskFileReader {

    List<Mask> maskList = new ArrayList<Mask>();
    File maskFile;
    public final static String rfc3339Format = "yyyy-MM-dd HH:mm:ss";

    public MaskFileReader(String maskFileName) throws IOException {
        if (!setFileName(maskFileName))
            throw new IOException("maskFile: " + maskFileName + " does not exist!");
    }

    public boolean setFileName(String maskFileName) {
        maskFile = new File(maskFileName);
        return maskFile.exists();
//        if (!maskFile.exists()) {
//            System.out.println("maskFile: " + maskFileName + " does not exist!");
//        }
    }

    public void parse() throws IOException {
        BufferedReader maskReader = null;
        String line;
        maskReader = new BufferedReader(new FileReader(maskFile));

        while (maskReader.ready()) {

            line = maskReader.readLine();
            Scanner input = new Scanner(line);
            String dateString = input.next() + "-" + input.next() + "-" +
                    input.next() + " " + input.next() + ":" + input.next() + ":00";
            Date d = null;
            try {
                d = new SimpleDateFormat(rfc3339Format).parse(dateString);
            } catch (ParseException ex) {
                Logger.getLogger(MaskFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            Mask mask = new Mask();
            mask.setStartTime(d);

            dateString = input.next() + "-" + input.next() + "-" +
                    input.next() + " " + input.next() + ":" + input.next() + ":00";
            try {
                d = new SimpleDateFormat(rfc3339Format).parse(dateString);
            } catch (ParseException ex) {
                Logger.getLogger(MaskFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            //mask = new Mask();
            mask.setEndTime(d);

            maskList.add(mask);

        }
    }

    public void test() {
        for (Mask mask : maskList) {
            //String start = mask.getStartTime();
            //System.out.println(mask.getStartTime().toString());
            String start = new SimpleDateFormat(rfc3339Format).format(mask.getStartTime());
            String end = new SimpleDateFormat(rfc3339Format).format(mask.getEndTime());
            System.out.println("Start: " + start + "  End: " + end);
        }
    }
    public boolean isMasked(Date d) {

        for (Mask mask : maskList) {
            if (mask.isMasked(d))
                return true;
        }

        return false;
    }
}
