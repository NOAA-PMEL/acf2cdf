/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

import gov.noaa.pmel.eps.Key;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author derek
 */
public class EPIC_Key_DB {

    private Map<Integer, Key> keyMap = null;
    private BufferedReader reader;
    private String dbFileName = "epic.key"; // default name

    public EPIC_Key_DB() {
        getReader();
        init();
    }


    public EPIC_Key_DB(BufferedReader reader) {
        this.reader = reader;
        init();
    }

    public EPIC_Key_DB(String altFileName) {
        this.dbFileName = altFileName;
        getReader();
        init();
    }


    private void init() {
        keyMap = new HashMap<Integer, Key>();

//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader(dbFile));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(EPIC_Key_DB.class.getName()).log(Level.SEVERE, null, ex);
//        }
        try {
            parse();
        } catch (IOException ex) {
            Logger.getLogger(EPIC_Key_DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parse() throws IOException {

        reader.readLine();
        while (reader.ready()) {
            String line = reader.readLine();
            Scanner input = new Scanner(line).useDelimiter(":");

            //int id = input.nextInt();
            String id_str = input.next().trim();
            // sname
            String sname = input.next().trim();

            // lname
            String lname = input.next().trim();

            // gname
            String gname = input.next().trim();

            // units
            String units = input.next().trim();

            // frmt
            String frmt = input.next().trim();

            // 4 is the EPSREAL type
            int id = new Integer(id_str);
            Key key = new Key(id, sname, lname, gname, units, frmt, 4);
            keyMap.put(id, key);

        }
    }

    public Key findKey(int id) {
        return keyMap.get(id);
    }

    private void getReader() {
        String name = "resources/" + dbFileName;
        reader = new BufferedReader(new InputStreamReader(Acf2cdfApp.class.getResourceAsStream(name)));
    }
}
