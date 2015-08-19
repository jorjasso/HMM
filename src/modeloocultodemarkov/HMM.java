/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**
 *Clase que implementa un HMM
 *notacion a usar del libro Speech Recognition and Synthesis capitulo 9
 * Y= vectores columna de observaciones
 * S=estados siemrep T+2 estados : 1 estado inicial T estados para los simbolos de observacion y 1 estado final
 * a=transiciones
 * b=observaciones
 */
public class HMM
{
    Estado [] S;  //estados s[0] , s[1]... en ese orden
    double [][]logA; //matriz de transiciones
    int [][] grafoHMM;//matriz que guarda la configuracion del HMM
    
    public HMM(int nroEstados)//nroEstados=T+2 estados
    {
        S=new Estado[nroEstados];
        logA=new double[nroEstados][nroEstados];
        grafoHMM=new int[nroEstados][nroEstados];
        inicializarEstados();
    }

    /*METODOS PRIVADOS*/
    /*inicialia Aij*/
    private void inicializarAij()
    {
        double suma;
        int i=0;
        int j=0;

        for(i=0;i<logA.length;i++)
        {
            suma=0;

            for(j=0;j<logA[0].length;j++)
                suma=suma+logA[i][j];

            suma=1.0/suma;

            for(j=0;j<logA[0].length;j++)
                if(logA[i][j]!=0)
                    logA[i][j]=suma;
        }
     
        /*matriz de los logaritmos de cada elemento de A*/
        for( i=0;i<logA.length;i++)
            for( j=0;j<logA[0].length;j++)
                if(this.grafoHMM[i][j]==1)
                    logA[i][j]=Util.log(logA[i][j]);
        
    }

    /*inicializa los estados*/

    private void inicializarEstados()
    {
        S[0]=new Estado("Estado = Inicial");
        S[S.length-1]=new Estado("Estado = Final");
        for(int i=1;i<S.length-1;i++)
            S[i]=new Estado("Estado = "+i);

    }

    private void segmentar(Patron[] patronesEntrenamiento)
    {
        /*acumularà las observaciones segmentadas*/
        Cluster[] observaciones=new Cluster[S.length-2];
        int dim=patronesEntrenamiento[0].getDimension();
        /*inicializar cada cluster*/
        for(int j=0;j<S.length-2;j++)
            observaciones[j]=new Cluster(dim);

        for(int i=0;i<patronesEntrenamiento.length;i++)
            segmentarObservacion(observaciones, patronesEntrenamiento[i].getDatos());

    }


    private void segmentarObservacion(Cluster [] observaciones, double y[][])
    {        
        

        int nroEstados=S.length;
        //nroEstados-2 pues solo T estados emiten simbolos de observacion
        double nroObsPorEstado=(double)y[0].length/(double)(nroEstados-2);

        //asignando una cantidad de observaciones proporcional a cada estado

        //trabajar con las traspuestas
        double [][]yTranspuest = Util.traspuesta(y);     
        int j=1;                        //para cada estado
        for(int i=0;i<y[0].length;i++)  //para cada observacion
        {
            if(i<j*nroObsPorEstado)
            {
                observaciones[j-1].addDatos(yTranspuest[i]);
                //observaciones[j]=Util.redimensionar(observaciones[j], yTranspuest[i]);
                //S[j].getGmm().inicializacionKmeans(y);
                    //S[j].addObservacion(yTranspuest[i]);
                //S[j].setGmm(new GMM);
            }

            else
            {   j++;
                observaciones[j-1].addDatos(yTranspuest[i]);
                //observaciones[j]=Util.redimensionar(observaciones[j], yTranspuest[i]);
            }
        }

        /*enviar los datos segmentados a cada GMM para el càlculo del kmeans por cada GMM*/
        for(j=1;j<S.length-1;j++)
        {

            double [][] nuevo=observaciones[j-1].getDatos();
        //    Util.mostrar(nuevo, "Observaciones");
            GMM gmm=S[j].getGmm();
            gmm.inicializacionKmeans(nuevo);
            S[j].setGmm(gmm);
        }

    //        Util.mostrar(observaciones[j], null);
            //S[j].getGmm().inicializacionKmeans(observaciones[j]);
            

    }


    /*METODOS PUBLICOS*/

    public void agregarTransiciones(int i, int j)
    {
        //inicializaremos todas las transiciones que tienen participación en el modelo a 1
        //luego calcularemos las probabilidades iniciales de estas transiciones
        //el enfoque será dividirlas según el numero de conexiones de salida
        //así por ejemplo A[i][j]=1 se podría convertir en 1/3 si para el estado i
        // existen 3 conexiones de salida eso se ha´ra en inicializarAij
        logA[i][j]=1;
        grafoHMM[i][j]=1;
    }

    public void setGMM(int nroEstado, GMM gmm)
    {
        /*establece el GMM de cada estado*/
        this.S[nroEstado].setGmm(gmm);
    }


    
    public void inicializar(Patron[] patronesEntrenamiento)
    {
        /*inicializa todo el modelo*/
        inicializarAij();     
        segmentar(patronesEntrenamiento);
    }
    //Entra la matriz de observaciones para una señal de habla
    //debemos llamar tantas veces esta función como señales de habla existan en
    //la base de entrenamiento (testeado funciona ok)
    //lo que hace es distribuir uniformemente las observaciones/ estado


    public void mostrar()
    {
        Util.mensaje("Estados del HMM");
        for(int i=0;i<S.length;i++)
            S[i].mostrar();
        Util.mostrar(logA, "Matriz A (logaritmos de las probabilidades aij)");
        
        double temp[][]=new double[logA.length][logA[0].length];
        for( int i=0;i<logA.length;i++)
            for( int j=0;j<logA[0].length;j++)
                if(this.grafoHMM[i][j]==1)
                    temp[i][j]=Math.exp(logA[i][j]);

        Util.mostrar(temp, "Matriz A (probabilidades)");

    }



    public void setS(Estado[] S) {
        this.S = S;
    }

    public void setA(double[][] A) {
        this.logA = A;
    }

    public Estado[] getS() {
        return S;
    }

    public double[][] getA() {
        return logA;
    }
    
       public int[][] getGrafoHMM() {
        return grafoHMM;
    }

    public void setGrafoHMM(int[][] grafoHMM) {
        this.grafoHMM = grafoHMM;
    }
}