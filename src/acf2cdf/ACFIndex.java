/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acf2cdf;

/**
 *
 * @author derek
 */
public class ACFIndex {

    private int[] index = new int[5];

    public ACFIndex(int i) {
        this.index[0] = i;
    }

    public ACFIndex(int i, int j) {
        this.index[0] = i;
        this.index[1] = j;
    }

    public ACFIndex(int i, int j, int k) {
        this.index[0] = i;
        this.index[1] = j;
        this.index[2] = k;
    }

    public int[] getIndex() {
        return index;
    }
    public int get(int i) {
        return index[i];
    }
}
