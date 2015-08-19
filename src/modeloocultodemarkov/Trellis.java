/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;
import java.util.Vector;

/**
 * Construye un Trellis para un determinado patron,
 * implementa los algoritmos forward, backward y viterbi
 * @author jorjasso
 */
public class Trellis {
    /*Malla de estados de nroEstados - 2 X tiempo */
    Estado [][] estados;

    private double [][] alfa;  //arreglo de alfa, cada posicion indica un instante de tiempo
    private double [][] beta;  //arreglo de beta, cada posicion indica un instante de tiempo
    private double [][] alfaViterbi;//arreglo de alfaViterbi, cada posicion indica un instante de tiempo
    private double [][] gamma; //arreglo de gamma, cada posicion indica un instante de tiempo

    private Gamma_jm [][] gamma_jm;
    int T;
    double probabilidadForward;
    double probabilidadViterbi;
    //almacena las observaciones para el entrenamiento, luego son borradas
    private double[][] y;
    int N;//numero de estados
    int I;//estado inicial
    int F;//estado final
    HMM hmm;

    public Trellis(HMM hmm,Patron patronEntrenamiento)
    {
        T=patronEntrenamiento.getnroFrames();
        N=hmm.getS().length;
        this.hmm=hmm;
        this.estados  =   new Estado[N][T];
        this.alfa    =   new double[N][T];
        this.beta    =   new double[N][T];
        this.gamma   =   new double[N][T];
        this.alfaViterbi =   new double[N][T];
        this.gamma_jm  =new Gamma_jm[N][T];
        this.y=patronEntrenamiento.getDatos();
        this.I=0;
        this.F=N-1;

    }

    /****************************/
    public void forward()
    {
        //trabajar con las traspuestas
        double[][]yTranspuesta=Util.traspuesta(y);

        /*INICIALIZACION*/
        //se inicializa para t=0...T-1
        int t=0;
        //ec 9.8
        for(int j=1;j<(N-1);j++)
            if(this.hmm.grafoHMM[I][j]==1)            
                    this.alfa[j][t]=hmm.logA[I][j]*Math.exp(hmm.S[j].b(yTranspuesta[t]));

        /*INDUCCION*/
        //ec 9.7
        double temp=0;

        for(t=1;t<T;t++){
            for(int j=1;j<(N-1);j++){
                for(int i=1;i<(N-1);i++)
                    if(hmm.grafoHMM[i][j]==1)//si existe una conexion del estado i al estado j
                        if(this.alfa[i][t-1]!=0) //para evitar que un estado en un tiempo t que previamente no tiene valor en el tiempo t-1 se llene con valores erroneos
                            temp+=this.alfa[i][t-1]*hmm.logA[i][j];

                this.alfa[j][t]=temp*Math.exp(hmm.S[j].b(yTranspuesta[t]));
                if(alfa[j][t]>1)
                        Util.mensaje("Error ->alfas, clase Trellis, mètodo forward");
                temp=0;
            }
        }
        //TERMINACION
        //alfa_F(T)  ecu 9.9
        temp=0;
        for(int i=1;i<(N-1);i++)
            if(this.hmm.grafoHMM[i][F]==1)//si existe una conexion del estado i al estado F
                temp+=alfa[i][T-1]*(hmm.logA[i][F]);

        this.probabilidadForward=temp;
        double [][]tem=Util.log(alfa);
        Util.mostrar(tem, "ALgoritmo Forward, logaritmos de alfa");
    }



    public void backward()
    {
         //trabajar con las traspuestas
        double[][]yTranspuesta=Util.traspuesta(y);

        for(int i=1;i<(N-1);i++)
            if(this.hmm.grafoHMM[i][F]==1)
                beta[i][T-1]=hmm.logA[i][F];  //ec 9.16
        
        double temp=0;

        for (int t=T-2; t>=0;t--)
        {
            for(int i=1;i<(N-1);i++)
            {                
                for(int j=1;j<(N-1);j++)
                    if(this.hmm.grafoHMM[i][j]==1)
                        if(beta[j][t+1]!=0) //para evitar que un estado en un tiempo t que previamente no tiene valor en el tiempo t+1 se llene con valores erroneos
                            temp+=hmm.logA[i][j]*Math.exp( hmm.S[j].b(yTranspuesta[t+1]))*beta[j][t+1]; //ec 9.15 tener en cuenta S[j].b(yTranspuesta[t] es b_j(y_t+1), pues t=1 es y[0]

                beta[i][t]=temp;
                if(beta[i][t]>1)
                    Util.mensaje("Error ->betas, clase Trellis, mètodo backward");

                temp=0;
            }
        }
        double [][]tem=Util.log(beta);
        Util.mostrar(tem, "ALgoritmo Backward, logaritmos de beta");
    }

    public void viterbi()
    {
        //trabajar con las traspuestas
        double[][]yTranspuesta=Util.traspuesta(y);

        /*INICIALIZACION*/
        //log (alfa_i(1)) =log(A[I=0][j])             +        log (b_i(Y_1))  ec 9.8
        int t=0;
        for(int i=1;i<(N-1);i++)

            if(this.hmm.grafoHMM[I][i]==1)//si existe una conexion del estado I al estado i
                alfaViterbi[i][t]   =hmm.logA[I][i]*Math.exp(hmm.S[i].b(yTranspuesta[0]));



        /*INDUCCION*/
        //para t=2 hasta t=T
        //ec 9.7
        //alfa_j(t)=sum_{i=1...N}(alfa _i(t-1)Aij)b_j(y_t)
        //log( alfa_j(t) ) = log( sum_{i=1...N}(alfa _i(t-1)Aij) ) + log( b_j(y_t) )

        double []temp=new double[N];
        int cont;

        for(t=1;t<T;t++)
        {
            for(int j=1;j<(N-1);j++)
            {
                cont=0;
                //     =log( alfa _i(t-1) ) + log (Aij) donde  S[i].alfaLog[t-1]=log( alfa _i(t-1) )
                for(int i=1;i<(N-1);i++)
                    if(this.hmm.grafoHMM[i][j]==1) //si existe una conexion del estado i al estado j
//                        if(alfaViterbiLog[i][t-1]!=0) //para evitar que un estado en un tiempo t que previamente no tiene valor en el tiempo t-1 se llene con valores erroneos
                            temp[cont++]=alfaViterbi[i][t-1]*hmm.logA[i][j];

                alfaViterbi[j][t]=Util.max(temp)*Math.exp(hmm.S[j].b(yTranspuesta[t]));
                if(alfaViterbi[j][t]>1)
                    Util.mensaje("Error ->viterbi, clase Trellis, mètodo viterbi");

                temp=Util.resetear(temp);

            }
        }
        //TERMINACION
        //alfa_F(T)  ecu 9.9
        cont=0;
        for(int i=1;i<(N-1);i++)
            if(hmm.grafoHMM[i][F]==1)// si existe una conexion del estado i al estado F
                temp[cont++]=alfaViterbi[i][T-1]*hmm.logA[i][F];
//        temp=Util.eliminarCeros(temp);
        this.probabilidadViterbi=Util.max(temp);
        Util.mostrar(this.alfaViterbi, "ALgoritmo Viterbi, logaritmos de viterbi");
    }



    public void calcularGamma()
    {
        //implementacion ec 9.19
        for(int t=0;t<T;t++){
            for(int j=1;j<(N-1);j++){
/*ES NECESARIA ESTA VALIDACION?*/
                if(alfa[j][t]!=0 && beta[j][t]!=0)   //validacion
                {
                this.gamma[j][t]=this.alfa[j][t]*this.beta[j][t]/this.probabilidadForward;
                if(this.gamma[j][t]>1)
                {
                    Util.mensaje("Error -> gammaLog, clase Trellis, mètodo calcularGamma");
                    Util.mostrar(this.gamma[j][t], "error gamma ["+j+"]["+t+"]");
                    Util.mostrar(Util.log(this.gamma[j][t]), "logaritmo de la probabilidad anterior");

                }
                }
                }
            }
        
        Util.mostrar(this.gamma, "probabilidades Gamma");
     }

    public void verificar()
     {
         double []suma=new double [T];
         int cont;

         Util.mensaje("verificacion ecuacion 9.18");

         double temp=0;
         for(int t=0;t<T;t++){
             for(int i=1;i<(N-1);i++)
                 temp+=temp+this.alfa[i][t]*this.beta[i][t];

             suma[t]=temp;
             temp=0;
         }
         Util.mostrar(suma, "sumatoria de gama en el tiempo t",'H');
         Util.mostrar(this.probabilidadForward, "probabilidad forward");

     }



    /****************************/


    /*Getter and Setter*/

    public int getF() {
        return F;
    }

    public void setF(int F) {
        this.F = F;
    }

    public int getI() {
        return I;
    }

    public void setI(int I) {
        this.I = I;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getT() {
        return T;
    }

    public void setT(int T) {
        this.T = T;
    }

    public double[][] getAlfa() {
        return alfa;
    }

    public void setAlfa(double[][] alfa) {
        this.alfa = alfa;
    }

    public double[][] getAlfaViterbi() {
        return alfaViterbi;
    }

    public void setAlfaViterbi(double[][] alfaViterbi) {
        this.alfaViterbi = alfaViterbi;
    }

    public double[][] getBeta() {
        return beta;
    }

    public void setBeta(double[][] beta) {
        this.beta = beta;
    }

    public Estado[][] getEstados() {
        return estados;
    }

    public void setEstados(Estado[][] estados) {
        this.estados = estados;
    }

    public double[][] getGamma() {
        return gamma;
    }

    public void setGamma(double[][] gamma) {
        this.gamma = gamma;
    }

    public Gamma_jm[][] getGamma_jm() {
        return gamma_jm;
    }

    public void setGamma_jm(Gamma_jm[][] gamma_jm) {
        this.gamma_jm = gamma_jm;
    }

    public HMM getHmm() {
        return hmm;
    }

    public void setHmm(HMM hmm) {
        this.hmm = hmm;
    }

    public double getProbabilidadForward() {
        return probabilidadForward;
    }

    public void setProbabilidadForward(double probabilidadForward) {
        this.probabilidadForward = probabilidadForward;
    }

    public double getProbabilidadViterbi() {
        return probabilidadViterbi;
    }

    public void setProbabilidadViterbi(double probabilidadViterbi) {
        this.probabilidadViterbi = probabilidadViterbi;
    }

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        this.y = y;
    }
}

