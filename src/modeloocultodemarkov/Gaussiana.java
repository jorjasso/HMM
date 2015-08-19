/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

import java.util.Vector;

/**
 *
 * @author usuarioGaussiana
 */
public class Gaussiana {
    
    double []u; //media
    double []E;//matriz de coovarianza diagonal

    Vector info;// contiene toda la informacion del objeto
    
    //establece la media para la gaussiana 
    //ingresan Y=vectores de observacion
    //speech recognition and synthesis ec 9.34
    public Gaussiana(double [][]Y)
    {
        u=Util.promedio(Y);
        E=Util.coovarianzaDiagonal(Y, u);

        /*agrega esta informacion al vector info*/
        info = new Vector();
        info.add(u);
        info.add(E);
    }

    public Gaussiana(double []u, double []E)
    {
        this.u=u;
        this.E=E;

        /*agrega esta informacion al vector info*/
        info = new Vector();
        info.add(Util.doubleArrayToString(u));
        info.add(Util.doubleArrayToString(E));
    }
        
    //ec 9.53
    //retorna el logaritmo de la probabilidad de la observacion

    public double logP(double[] y)
    {

        double k=u.length;
        double var1=0;
        double var2=0;

        for(int i=0;i<k;i++)
        {         
            var1=var1+Util.log(E[i]);         
            var2=var2+Math.pow((y[i]-u[i])/E[i], 2);
            
        }
        
        return -k/2*Util.log(2*Math.PI)-var1-1.0/2.0*var2;
    }


    //establece la media de la observacion en cierto estado
    //speech recognition and synthesis ec 9.34
    public void setU(double []u)
    {
        this.u=u;
    }
    
    public void setE(double []E)
    {
        this.E=E;
    }
    
    public double[] getU()
    {
        return u;
    }
    public double[] getE()
    {
        return E;
    }

    public Vector getInfo() {
        return info;
    }

    public void mostrar()
    {
        Util.mostrar(u, "Media",'H');
        Util.mostrar(E, "Coovarianza",'H');
    }

}



