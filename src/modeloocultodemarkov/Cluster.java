/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**
 *
 * @author usuario
 */
public class Cluster {
    
    double[][] datos;   //datos dxn n datos d dimensionales
    double [] media;
    double [] coovarianzaDiagonal;
    int datosPorCluster; //
    int dim;
    
    //ingresa la dimension de los vectores que almacena este cluster
    public Cluster(int dim)
    {
        this.dim=dim;
        this.datos=new double[dim][0];
        datosPorCluster=0;
        media=generarMedia();
    }
    public Cluster(double datos[][])
    {
        this.datos=datos;
    }

    public void clear()
    {
        this.datos=new double[dim][0];
        datosPorCluster=0;
    }
    //obtiene el error cuadratico medio de los datos con respecto a la media
    public double getErrorCuadraticoMedio()
    {
        if(datos[0].length!=0)
        {
        double errorCM=0;//error cuadratico medio
        datos=Util.traspuesta(datos);
        for(int i=0;i<datos.length;i++)
        {
            errorCM=errorCM+Math.pow(Util.distancia(datos[i], media), 2);
        }
        datos=Util.traspuesta(datos);
        return errorCM;
        }
        return 0.0;
    }
    public void addDatos(double []Y)
    {        
        datos=Util.redimensionar(datos, Y);
        datosPorCluster++;        
    }

    public double[] generarMedia()
    {
        double []u=new double[dim];
        for(int i=0;i<dim;i++)
        {
            u[i]=0;
        }
        return u;
    }
    public void addMedia(double [] media)
    {
        this.media=media;
    }
    
    public void actualizarMedia()
    {
        if(datos[0].length!=0)
        {
            media=Util.promedio(datos);
        }
        
    }

    public void actualizarCoovarianzaDiagona()
    {
        coovarianzaDiagonal=Util.coovarianzaDiagonal(datos, media);
    }
    
    public double[] getMedia()
    {
        return media;
    }

    public double[] getMatrizCoovarianzaDiagonal()
    {
        return coovarianzaDiagonal;
    }
    public int getNumeroDatosPorCluster()
    {
        return datosPorCluster;
    }
    
    public void imprimir()
    {        
        Util.mostrar(datos,"Datos");
        Util.mostrar(media,"Media",'H');
        Util.mostrar(coovarianzaDiagonal,"Coovarianza Diagonal",'H');
        System.out.println(datosPorCluster+"Datos por Cluster");
    }

    public double[] getCoovarianzaDiagonal() {
        return coovarianzaDiagonal;
    }

    public void setCoovarianzaDiagonal(double[] coovarianzaDiagonal) {
        this.coovarianzaDiagonal = coovarianzaDiagonal;
    }

    public double[][] getDatos() {
        return datos;
    }

    public void setDatos(double[][] datos) {
        this.datos = datos;
    }

    public int getDatosPorCluster() {
        return datosPorCluster;
    }

    public void setDatosPorCluster(int datosPorCluster) {
        this.datosPorCluster = datosPorCluster;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }



}
