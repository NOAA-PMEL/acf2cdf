/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acf2cdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author derek
 */
public class ACFHeader {

    List<ACFHeaderEntry> entries = new ArrayList<ACFHeaderEntry>();

    public List<ACFHeaderEntry> getEntries() {
        return entries;
    }

    public List<String> getRemarks() {
        return remarks;
    }
    List<String> remarks = new ArrayList<String>();

    public void addEntry(String entry_str) throws IOException {
        ACFHeaderEntry entry = new ACFHeaderEntry();
        if (entry.parse(entry_str)) {
            entries.add(entry);
        }
        else {
            throw new IOException("Illegal entry syntax: " + entry_str);
        }
    }

    public void addRemark(String rem_string) {
    
        remarks.add(rem_string);

    }

    public String getEntryValueByName(String name) {
        for (ACFHeaderEntry entry : entries) {
            if (name.equals(entry.getName())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
