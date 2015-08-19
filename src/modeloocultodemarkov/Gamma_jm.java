/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

import java.util.Vector;

/**
 *
 * @author jorjasso
 */
public class Gamma_jm {

    /*El vector ser+a de tamaño M*/
    double [] gamma_jm;

    public Gamma_jm()
    {}
    /*GETTER AND SETTER PERSONALIZADOS*/
    public double getGamma_jm(int i)
    {
        return gamma_jm[i];
    }

    /*GETTER AND SETTER*/

    public double[] getGama_jm() {
        return gamma_jm;
    }

    public void setGama_jm(double[] gama_jm) {
        this.gamma_jm = gama_jm;
    }
    
}
