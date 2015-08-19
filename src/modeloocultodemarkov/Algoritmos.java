/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**
 *
 * @author jorge
 */
public class Algoritmos {
    //algoritmo k means
    //referencia machine learnig tom mitchell pag 191
    //Y son las observaciones dxn n datos d dimesionales
    public static Cluster[] kMeans(double [][] Y, int nroCluster)
    {
        Cluster [] cluster=new Cluster[nroCluster];
        int d=Y.length; // d= dimension del vector
        int n=Y[0].length;//n=cantidad de vectores de observacion
        int i,j,k;
        double distancia;
        double min;
        int pos=0;

        //criterios para evaluar el error cuadratico medio
        double umbralError=0.000000001;
        double error=0;//error actual
        double errorAnterior=0;//error anterior

  /*      System.out.println(" ");
        System.out.println("................");
        System.out.println("Algoritmo Kmeans");
        System.out.println("................");
  */    //inicializar clusters
        for(i=0;i<nroCluster;i++)
        {
            cluster[i]=new Cluster(d);
        }
               
        //KMedias es una matriz con las medias, una media por cluster
        double [][] kMedias =new double[d][nroCluster];

        //escoger aleatoriamente k medias
        
        for(k=0;k<nroCluster;k++)
        {
            j=(int)Math.round(Util.T(Math.random(), 0, 1, 0, n-1));
             //System.out.println(""+Math.round(Util.T(Math.random(), 0, 1, 0, Y[0].length-1)));
            for(i=0;i<d;i++)//para cada dimension
            {     
                kMedias[i][k]=Y[i][j];
            }                        
        }
        

        //error cuadratico medio inicial

        //proceso de clustering

        do{
            errorAnterior=error;
            //para todos los vectores de observacion Y
            for(j=0;j<n;j++)
            {
                min=100000000;
                pos=0;
                for(k=0;k<nroCluster;k++)
                {
                    //calculo de la distancia
                    distancia=0;
                    //System.out.println("vectores");
                    for(i=0;i<d;i++)
                    {
                        distancia=distancia+Math.pow(Y[i][j]-kMedias[i][k],2);
                      //  System.out.println(""+Y[i][j]+" "+kMedias[i][k]);
                    }
                    distancia=Math.sqrt(distancia);
                   // System.out.println(""+distancia);
                    if(distancia<min)
                    {
                        min=distancia;
                        pos=k;
                    }
                }
          
            //guardar datos en el cluster
                double [][]transpuestaY=Util.traspuesta(Y);

                cluster[pos].addDatos(transpuestaY[j]);

            }

            //actualizar medias
            kMedias=Util.traspuesta(kMedias);

            for(i=0;i<nroCluster;i++)
            {
                cluster[i].actualizarMedia();
                cluster[i].actualizarCoovarianzaDiagona();
                kMedias[i]=cluster[i].getMedia();
            }
            kMedias=Util.traspuesta(kMedias);

            for(i=0;i<nroCluster;i++)
            {
       //         Util.mensaje(new String("cluster "+i));
       //         cluster[i].imprimir();
            }

   //         System.out.println("kMedias");
   //         Util.mostrar(kMedias);

            //calcular el error cuadratico medio
            error=0;
            for(i=0;i<nroCluster;i++)
            {
             error=error+cluster[i].getErrorCuadraticoMedio();
            }

        //    Util.mensaje(new String("\nError "+error));
        //    System.out.println("---------------");

            //limpiar los clusters
            if(Math.abs(error-errorAnterior)>umbralError)
            {
                for(i=0;i<nroCluster;i++)
                {
                 cluster[i].clear();
                }
            }

        }while(Math.abs(error-errorAnterior)>umbralError);
        
        return cluster;
        
    }

    /*inicializa la matriz Aij para un HMM*/
    
}

