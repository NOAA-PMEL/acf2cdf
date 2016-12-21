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
public class ACFDimensionHeader {

    private List<ACFHeaderEntry> entries = new ArrayList<ACFHeaderEntry>();

    public ACFDimensionHeader() {
        entries.add(new ACFHeaderEntry("FORTRAN_format", ""));
        entries.add(new ACFHeaderEntry("units", ""));
        entries.add(new ACFHeaderEntry("type", ""));
        entries.add(new ACFHeaderEntry("epic_code", ""));
    }

    public boolean setHeaderEntry(String name, String value) {
        for (ACFHeaderEntry entry : entries) {
            if (entry.getName().equals(name)) {
                entry.setValue(value);
                return true;
            }
        }
        return false;
    }

    public List<ACFHeaderEntry> getEntries() {
        return entries;
    }

    public String getHeaderEntry(String name) {
         for (ACFHeaderEntry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry.getValue();
            }
        }
        return "";
   }
}
