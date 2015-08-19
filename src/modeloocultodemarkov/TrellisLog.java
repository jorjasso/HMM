/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

import java.util.Vector;

/**
 * Construye un Trellis para un determinado patron, donde el cáculo de los algoritmos del HMM se hace usando logaritmos,
 * implementa los algoritmos forward, backward y viterbi
 * @author jorjasso
 */
public class TrellisLog {
    /*Malla de estados de nroEstados - 2 X tiempo */
    Estado [][] estados;

    private double [][] alfaLog;  //arreglo de logaritmo de alfa, cada posicion indica un instante de tiempo
    private double [][] betaLog;  //arreglo de logaritmo de beta, cada posicion indica un instante de tiempo
    private double [][] alfaViterbiLog;//arreglo de logaritmo de alfaViterbi, cada posicion indica un instante de tiempo
    private double [][] gammaLog; //arreglo de logaritmo de "m".gamma, cada posicion indica un instante de tiempo, ver ec 9.43
    private Gamma_jm [][] gamma_jm_Log;
    private double [][] epsilonLog;//arreglo de los logaritmos de epsilon
    int T;
    double probabilidadForward;
    double probabilidadViterbi;
    //almacena las observaciones para el entrenamiento, luego son borradas
    private double[][] y;
    int N;//numero de estados
    int I;//estado inicial
    int F;//estado final
    HMM hmm;

    public TrellisLog(HMM hmm,Patron patronEntrenamiento)
    {
        T=patronEntrenamiento.getnroFrames();
        N=hmm.getS().length;
        this.hmm=hmm;
        this.estados  =   new Estado[N][T];
        this.alfaLog    =   new double[N][T];
        this.betaLog    =   new double[N][T];
        this.gammaLog   =   new double[N][T];
        this.alfaViterbiLog =   new double[N][T];
        this.gamma_jm_Log   =new Gamma_jm[N][T];
        this.y=patronEntrenamiento.getDatos();
        this.I=0;
        this.F=N-1;

    }

    public void forward()
    {
        //trabajar con las traspuestas
        double[][]yTranspuesta=Util.traspuesta(y);

        /*INICIALIZACION*/
        //se inicializa para t=0...T-1        
        int t=0;
        //log (alfa_i(1)) =log(A[I=0][j])             +        log (b_i(Y_1))  ec 9.8
        for(int j=1;j<(N-1);j++)                        
            if(this.hmm.grafoHMM[I][j]==1)
                this.alfaLog[j][t]=hmm.logA[I][j]+hmm.S[j].b(yTranspuesta[t]);
        

        /*INDUCCION*/
        //para t=1 hasta t=T-1
        //ec 9.7
        //alfa_j(t)=sum_{i=1...N}(alfa _i(t-1)Aij)b_j(y_t)
        //log( alfa_j(t) ) = log( sum_{i=1...N}(alfa _i(t-1)Aij) ) + log( b_j(y_t) )

        double []temp=new double[N];
        int cont; 

        for(t=1;t<T;t++){
            for(int j=1;j<(N-1);j++){
                cont=0;
                for(int i=1;i<(N-1);i++)
                   //     =log( alfa _i(t-1) ) + log (Aij) donde  S[i].alfaLog[t-1]=log( alfa _i(t-1) )
                    if(hmm.grafoHMM[i][j]==1)//si existe una conexion del estado i al estado j                    
                        if(this.alfaLog[i][t-1]!=0) //para evitar que un estado en un tiempo t que previamente no tiene valor en el tiempo t-1 se llene con valores erroneos
                            temp[cont++]=this.alfaLog[i][t-1]+hmm.logA[i][j];
                                    
              //log( alfa_j(t) ) = log( sum_{i=1...N}(alfa _i(t-1)Aij) ) + log( b_j(y_t) )
                double suma=Util.sumLog(temp);
                if(suma!=0)
                {
                    this.alfaLog[j][t]=suma+hmm.S[j].b(yTranspuesta[t]);
                    if(Math.exp(alfaLog[j][t])>1)
                        Util.mensaje("Error ->alfas, clase Trellis, mètodo forward");
                }
                temp=Util.resetear(temp);
            }            
        }
        //TERMINACION
        //alfa_F(T)  ecu 9.9
        cont=0;
        for(int i=1;i<(N-1);i++)        
            if(this.hmm.grafoHMM[i][F]==1)//si existe una conexion del estado i al estado F            
                temp[cont++]=alfaLog[i][T-1]+hmm.logA[i][F];
        
        this.probabilidadForward=Util.sumLog(temp);
        Util.mostrar(this.alfaLog, "ALgoritmo Forward, logaritmos de alfa");
    }



    public void backward()
    {             
        double[][]yTranspuesta=Util.traspuesta(y);

        for(int i=1;i<(N-1);i++)        
            if(this.hmm.grafoHMM[i][F]==1)
                betaLog[i][T-1]=hmm.logA[i][F];  //ec 9.16
        

        double []temp=new double[N];
        int cont; 

        for (int t=T-2; t>=0;t--)
        {
            for(int i=1;i<(N-1);i++)
            {
                cont=0;
                for(int j=1;j<(N-1);j++)
                    if(this.hmm.grafoHMM[i][j]==1)                    
                        if(betaLog[j][t+1]!=0) //para evitar que un estado en un tiempo t que previamente no tiene valor en el tiempo t+1 se llene con valores erroneos
                            temp[cont++]=hmm.logA[i][j] + hmm.S[j].b(yTranspuesta[t+1]) + betaLog[j][t+1]; //ec 9.15 tener en cuenta S[j].b(yTranspuesta[t] es b_j(y_t+1), pues t=1 es y[0]
                                
                double suma=Util.sumLog(temp);
                if(suma!=0)
                {
                    betaLog[i][t]  = suma;
                    if(Math.exp(betaLog[i][t])>1)
                        Util.mensaje("Error ->betas, clase Trellis, mètodo backward");                  
                }
                temp=Util.resetear(temp);
            }
        }
        Util.mostrar(this.betaLog, "ALgoritmo Backward, logaritmos de beta");
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
                alfaViterbiLog[i][t]   =hmm.logA[I][i]+hmm.S[i].b(yTranspuesta[0]);

        

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
                            temp[cont++]=alfaViterbiLog[i][t-1]+hmm.logA[i][j];

              //log( alfa_j(t) ) = log( sum_{i=1...N}(alfa _i(t-1)Aij) ) + log( b_j(y_t) )
                if(Util.sumLog(temp)!=0)
                {                    
                    alfaViterbiLog[j][t]  = Util.max(Util.eliminarCeros(temp))    +   hmm.S[j].b(yTranspuesta[t]);
                    if(Math.exp(alfaViterbiLog[j][t])>1)
                        Util.mensaje("Error ->viterbi, clase Trellis, mètodo viterbi");                    
                }
                temp=Util.resetear(temp);

            }
        }
        //TERMINACION
        //alfa_F(T)  ecu 9.9
        cont=0;
        for(int i=1;i<(N-1);i++)        
            if(hmm.grafoHMM[i][F]==1)// si existe una conexion del estado i al estado F            
                temp[cont++]=alfaViterbiLog[i][T-1]+hmm.logA[i][F];
//        temp=Util.eliminarCeros(temp);
        this.probabilidadViterbi=Util.max(temp);
        Util.mostrar(this.alfaViterbiLog, "ALgoritmo Viterbi, logaritmos de viterbi");
    }


    public void calcularGammaLog()
    {
        //implementacion ec 9.19
        for(int t=0;t<T;t++){
            for(int j=1;j<(N-1);j++){
/*ES NECESARIA ESTA VALIDACION?*/
                if(alfaLog[j][t]!=0 && betaLog[j][t]!=0)   //validacion
                {
                    this.gammaLog[j][t]=this.alfaLog[j][t]+this.betaLog[j][t]-this.probabilidadForward;
                    /*verfica si ocurrió algun error debido a presición numérica*/
                    if(Math.exp(this.gammaLog[j][t])>1&&Math.exp(this.gammaLog[j][t])<1.000005)
                    {
                        //Si existe error por aproximación numérica, corregimos los datos levemente
                        Util.mostrar(gammaLog[j][t], "valor con error -> gammaLog["+j+"]["+t+"]");
                        Util.mostrar(Math.exp(gammaLog[j][t]), "valor con error (probabilidades) -> gamma["+j+"]["+t+"]");
                        Util.mostrar(betaLog[j][t], "beta antes de la modificaciòn -> beta["+j+"]["+t+"]");
                        Util.mostrar(Math.exp(betaLog[j][t]), "beta antes de la modificaciòn (probabilidades)-> beta["+j+"]["+t+"]");
                        double valor=1-0.000000000000001;
                        gammaLog[j][t]=Util.log(valor);
                        betaLog[j][t]=this.gammaLog[j][t]-this.alfaLog[j][t]+this.probabilidadForward;
                        Util.mensaje("Valores corregidos por error en aproximación numérica");
                        Util.mostrar(gammaLog[j][t], "valor corregigo gammaLog["+j+"]["+t+"]");
                        Util.mostrar(Math.exp(gammaLog[j][t]), "valor con error (probabilidades) -> gamma["+j+"]["+t+"]");
                        Util.mostrar(betaLog[j][t], "valor corregigo betaLog["+j+"]["+t+"]");
                        Util.mostrar(Math.exp(betaLog[j][t]), "beta antes de la modificaciòn (probabilidades)-> beta["+j+"]["+t+"]");
                    }
                    if(Math.exp(this.gammaLog[j][t])>1)
                    {
                        Util.mensaje("Error -> gammaLog, clase TrellisLog, mètodo calcularGammaLog");
                        Util.mostrar(this.gammaLog[j][t], "error gammalog ["+j+"]["+t+"]");
                        Util.mostrar(Math.exp(this.gammaLog[j][t]), "exponencial del logaritmo de la probabilidad anterior");
                    }
                        
                }
            }
        }
        Util.mostrar(this.gammaLog, "Logaritmo de las probabilidades Gamma");
     }

    /************************************************************************/
    //implementacion de ec 9.43
     public void calcularGamma_jm(){
        double[][]yTranspuesta=Util.traspuesta(y);

        double []temp=new double[N];//acumulador
        int cont; //cont para temp
        int M; //numero de gaussianas por estado

        for (int t=0;t<T;t++)
        {
            for(int j=1;j<(N-1);j++)
            {
                //cuantas gaussianas hay por estado
                M=hmm.S[j].getGmm().getM();
                for(int m=0;m<M;m++)
                {
                    if(this.gammaLog[j][t]!=0&&Util.log(hmm.S[j].getGmm().getC(m))!=0)
                    {
                        this.gamma_jm_Log[j][t].gamma_jm[m]=this.gammaLog[j][t]+Util.log(hmm.S[j].getGmm().getC(m))+hmm.S[j].getGmm().getN(m).logP(yTranspuesta[t])-hmm.S[j].b(yTranspuesta[t]);
                        if(Math.exp(this.gamma_jm_Log[j][t].gamma_jm[m])>1)
                            Util.mostrar(Math.exp(this.gamma_jm_Log[j][t].gamma_jm[m]), "Error -> procedimento getGamma_jm, clase TrellisLog ");
                    }
                    
                }
            }
        }
     }

    
    /***************************************************************************/
    //ec 9.22
    public double epsilonLogFT(int i)
    {
        if(alfaLog[i][T-1]!=0)//validaciones
        {
            double result=alfaLog[i][T-1]+hmm.logA[i][F]-this.probabilidadForward;
                 if (result==0)
                     result=0.000000000000001;
                 return result;
        }
        else
            return 0;
    }
    //ec 9.23
    public double epsilonLogIO(int j)
    {
        double [][]yTranspuesta=Util.traspuesta(y);
        if(betaLog[j][0]!=0)//validaciones
        {
            double result=hmm.logA[I][j]+hmm.S[j].b(yTranspuesta[0])+betaLog[j][0]-this.probabilidadForward;
            if (result==0)
                result=0.000000000000001;
            return result;
        }
        else
            return 0;
    }

    //ec 9.21
     public double epsilonLog(int i, int j,int t)
     {
         double [][]yTranspuesta=Util.traspuesta(y);
         if(alfaLog[i][t]!=0.&&betaLog[j][t+1]!=0)//validaciones
         {
             double result=alfaLog[i][t]+hmm.logA[i][j]+hmm.S[j].b(yTranspuesta[t+1])+betaLog[j][t+1]-this.probabilidadForward;        
             if (result==0)
                 result=0.000000000000001;
             return result;
         }
         else
             return 0;
                                            
     }

    /***************************************************************************/

    public void verificar()
     {
         double []suma=new double [T];
         double []temp=new double [N];
         int cont=0;

         Util.mensaje("verificacion ecuacion 9.18 speech synthesis and recognition");

         for(int t=0;t<T;t++){
             for(int i=1;i<(N-1);i++)
             {
                 temp[cont++]=this.alfaLog[i][t]+this.betaLog[i][t];
             }

             suma[t]=Util.sumLog(temp);
             Util.resetear(temp);
             cont=0;
         }
         
         Util.mostrar(suma, "Verificación -> sumatoria de gama en el tiempo t",'H');
         Util.mostrar(this.probabilidadForward, "logaritmo de la probabilidad forward");         
         Util.mensaje("verificacion ecuacion 38 a tutorial on hidden markov models");
         Util.resetear(temp);

         double tem[][] = new double[T][N];
         for(int t=0;t<(T-1);t++)
         {    for(int i=1;i<(N-1);i++)
             {       
                 for(int j=1;j<(N-1);j++)
                 {
                     if(hmm.grafoHMM[i][j]==1)
                         temp[j]=this.epsilonLog(i, j, t);
                 } 
                 tem[t][i]=Util.sumLog(temp);         
                 Util.resetear(temp);
             }              
         }
         Util.mostrar(Util.traspuesta(tem), "Verificación -> sumatoria de los epsilon");
         
         Util.mensaje("verificacion de gammaLog_jm");
         
         for(int t=0;t<T;t++)
             for(int j=1;j<(N-1);j++)
                 tem[t][j]=Util.sumLog(this.gamma_jm_Log[j][t].getGama_jm());
         
         Util.mostrar(Util.traspuesta(tem), "Verificación -> gamma_jm_log");
     }

 
    /****************************/


    /*Getter and Setter*/
    public int getF() {
        return F;
    }

    public void setF(int F) {
        this.F = F;
    }

    public double[][] getGammaLog() {
        return gammaLog;
    }

    public void setGammaLog(double[][] gammaLog) {
        this.gammaLog = gammaLog;
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

    public double[][] getAlfaLog() {
        return alfaLog;
    }

    public void setAlfaLog(double[][] alfaLog) {
        this.alfaLog = alfaLog;
    }

    public double[][] getAlfaViterbiLog() {
        return alfaViterbiLog;
    }

    public void setAlfaViterbiLog(double[][] alfaViterbiLog) {
        this.alfaViterbiLog = alfaViterbiLog;
    }

    public double[][] getBetaLog() {
        return betaLog;
    }

    public void setBetaLog(double[][] betaLog) {
        this.betaLog = betaLog;
    }

    public Estado[][] getEstados() {
        return estados;
    }

    public void setEstados(Estado[][] estados) {
        this.estados = estados;
    }

    public Gamma_jm[][] getGamma_jm_Log() {
        return gamma_jm_Log;
    }

    public void setGamma_jm_Log(Gamma_jm[][] gamma_jm_Log) {
        this.gamma_jm_Log = gamma_jm_Log;
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

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        this.y = y;
    }

}

