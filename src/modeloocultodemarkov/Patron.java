/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**
 * Cada patrón tendrà asociado una secuencia de observación
 * @author usuario
 * @version 0.1
 * 
 */

public class Patron {
    double datos[][];

    /**
     * Constructor
     * @param double[][]datos Construye un patrón ingresandole los datos asociados
     */
    public Patron(double[][] datos) {
        this.datos = datos;
    }

    /**
     * Obtiene el numero de frames
     * @return número de frames
     */
    public int getnroFrames(){
        return datos[0].length;
    }
    //
    /**
     * Retorna la dimension "d" del patron
     * @return Retorna la dimensión del patron
     */
    public int getDimension(){
        return datos.length;
    }
    /**
     * Retorna los datos asociados al patrón
     * @return Retorna los datos asociados al patrón
     */

    public double[][] getDatos() {
        return datos;
    }

    /**
     * Establece los datos asociados al patrón
     * @param double[][]datos  Datos que se asociarán al patrón
     * @return Retorna los datos asociados al patrón
     */
    public void setDatos(double[][] datos) {
        this.datos = datos;
    }



}
