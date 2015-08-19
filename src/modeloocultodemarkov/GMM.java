/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**
 *
 * @author usuario
 */
public class GMM {
    
    private double []c;//coeficiente de las gaussianas
    private Gaussiana [] N;//varias gaussianas

    private int M;//numero de gaussianas
        
    
    
    public  GMM (int nroGaussianas)
    {
        c=new double[nroGaussianas];
        N=new Gaussiana[nroGaussianas];
        this.M=nroGaussianas;
    }

    /*inicializa los pararmetros u y E de las gaussianas de GMM usando el algoritmo kmeans*/
    /*Y no es un vector de observaciones individual, si no varios vectores de diversos patrones
     resultado de la segmentacion de los patrones por estado del modelo oculto
     */
    public void inicializacionKmeans(double [][] Y)
    {
        Cluster [] cluster = Algoritmos.kMeans(Y, N.length);
        for(int i=0;i<N.length;i++)
        {                       
            N[i]=new Gaussiana(cluster[i].getMedia(),cluster[i].getMatrizCoovarianzaDiagonal());
            c[i]=(double)cluster[i].getNumeroDatosPorCluster()/(double)Y[0].length;            
        }        
    }

   public double getLogProb(double [] y)
    {       
       double []temp=new double[N.length];
       for(int i=0;i<N.length;i++)
           temp[i]=N[i].logP(y)+Util.log(c[i]);

       return Util.sumLog(temp);
    }
    /*GETTER AND SETTER PERSONALIZADOS*/

   public double getC(int i)
   {
       return c[i];
   }

    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }

    public Gaussiana getN(int i)
    {
        return N[i];
    }

    /*GETTER AND SETTER*/
    public Gaussiana[] getN() {
        return N;
    }

    public void setN(Gaussiana[] N) {
        this.N = N;
    }

    public double[] getC() {
        return c;
    }

    public void setC(double[] c) {
        this.c = c;
    }

    public void mostrar()
    {
        Util.mostrar(c, "Coeficientes C",'V');
        Util.mensaje("Gausianas");
        for(int i=0;i<N.length;i++)
        {
            Util.mensaje("gaussiana"+i);
            N[i].mostrar();
        }
            
    }

}


    /*Agrega todas los vectores de observacion resultado de la segmentacion inicial
      a dicho estado
      */

    /*Pasarlos al GMM
    public void addObservacion(double[]y)
    {
        if(observaciones==null)
        {
            observaciones = new double[y.length][0];
        }
        this.observaciones=Util.redimensionar(observaciones, y);
    }

    public void inicializarGMM()
    {
        gmm=new GMM(observaciones, nroGaussianas);
    }
     *
     *
     */

