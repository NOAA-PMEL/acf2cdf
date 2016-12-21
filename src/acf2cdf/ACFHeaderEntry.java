/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

/**
 *
 * @author derek
 */
public class ACFHeaderEntry {

    protected String name;
    protected String value;

    public ACFHeaderEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    ACFHeaderEntry() {
    }

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(String value) {
        this.value = value;
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

    public boolean parse(String input) {
        int equals;

        if ((equals = input.indexOf("=")) > 0) {
            String[] parsed = input.split("=");
            setName(parsed[0]);
            setValue(parsed[1]);
            return true;
        }
        // illegal entry syntax...no "="
        return false;
    }
}
