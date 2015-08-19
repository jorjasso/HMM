/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

import java.util.Vector;

/**
 *
 * @author usuario
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
   //     double[][]A={{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
   //     double[][]B={{11,12,13,3},{14,15,16,4},{17,18,19,7}};
   //     double[][]C={{},{},{}};
        
   //     double[]D={1,2,3};
   //        double[]E={11,12,13};
        
    //    Matrices.mostrar(Matrices.multiplicar(A, B));
    //    Matrices.mostrar(Matrices.sumar(A, A));
    //     Matrices.mostrar(B);
    //      Matrices.mostrar(Matrices.coovarianza(B));
    //      Matrices.mostrar(Matrices.coovarianzaDiagonal(C, Matrices.promedio(C)));
    //      Matrices.mostrar(Matrices.inversa(C));
    // test para el metodo redimensionar de matriz
        /*Matrices.mostrar(C);
        C=Matrices.redimensionar(C, E);
        System.out.println("redimensionada");
        .mostrar(C);*/

        //test para k means
      /*  double [][] datos ={{1,2,3,7,8},{1,2,0,3,4}};
        double [] y={1,3};        
        Algoritmos.kMeans(datos,7);
       */

       /*test para quicksort de gaussianas
        double [][] datos ={{1,2,3,7,8},{1,2,0,3,4}};
        double [] y={1,3};
        GMM gmm=new GMM(datos,2);
        Gaussiana [] g=gmm.getGaussianas();
        gmm.mostrar();
        System.out.println("probabilidad del vector y");
        System.out.println(" "+g[0].logP(y)+" "+g[1].logP(y));
        Util.ordenar(gmm.getGaussianas(), y);
        System.out.println("gaussianas ordenadas");
        gmm.mostrar();
        System.out.println("probabilidad del vector y");
        System.out.println(" "+g[0].logP(y)+" "+g[1].logP(y));
        double [] x={1,3,2,6,8,5,3.3,7.4,-2.1};
        Matrices.mostrar(x);
        Util.ordenar(x);
        Matrices.mostrar(x);
       */
        /*test para logaritmo de probabilidades
        double [][] datos ={{1,2,3,7,8},{1,2,0,3,4}};
        double [] y={2,1};
        Gaussiana []g;
        GMM gmm=new GMM(datos,2);        
        gmm.mostrar();

        g=gmm.getGaussianas();
        System.out.println(" log prob "+g[0].logP(y)+" "+g[1].logP(y));

        */
        /*test para GMM y sumProbLog
        double [][] datos ={{1,2,3,7,8,3,6,8,4,6,3,6,4,3,8,7,4,9,8,2,6,4,9,7},{1,2,0,3,4,3,7,8,3,6,8,4,6,3,6,4,3,7,8,3,6,8,4,6}};
        double [] y={10,-3};
        GMM gmm=new GMM(datos,5);
        gmm.mostrar();                
        System.out.println("gmm log prob "+gmm.getLogProb(y) ) ;

        //test para Util.sumLog(log(A+B+....))
           System.out.println(Util.sumLog(Util.log(234), Util.log(123))) ;
           double []logs={Util.log(7890),Util.log(567),Util.log(345),Util.log(123), Util.log(234)}; //=log(7890+567+345+123+234)
        System.out.println(Util.sumLogProb(logs));
         */
        /***********************************************************
        CASO I
         *
        /*test para HMM y su uso con kmeans
         */
/*
        double [][] y ={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}; //observaciones
        double [][] w ={{10,20,30,40,50,60,70,80,90,100},{10,20,30,40,50,60,70,80,90,100}}; //observaciones


        Patron [] patronesE=new Patron[2];
        patronesE[0]=new Patron(y);
        patronesE[1]=new Patron(w);


        int nroEstados=5;

        /*ESTADOS S*/
  /*      HMM hmm=new HMM(nroEstados);             //numero de estados
        hmm.inicializarGaussianas(2);    //inicializa cada estado con un GMM de n gaussianas
        /*alternativamente para cada estado un numero de gaussianas en particular
        * hmm.S[0].setNroGaussianas(4);hmm.S[1].setNroGaussianas(3);...*/

        /*TRANSICIONES S*/
 /*       hmm.agregarTransiciones(0, 1);  //transicion estado 0 a estado 1
        hmm.agregarTransiciones(1, 1);  //transicion estado 1 a estado 1
        hmm.agregarTransiciones(1, 2);  //transicion estado 1 a estado 2
        hmm.agregarTransiciones(2, 2);  //transicion estado 2 a estado 2
        hmm.agregarTransiciones(2, 3);  //transicion estado 2 a estado 3
        hmm.agregarTransiciones(3, 3);  //transicion estado 3 a estado 3
        hmm.agregarTransiciones(3, 4);  //transicion estado 3 a estado 4

        /*PROBABILIDADES INICIALES PI S*/
        /*    for(int i=1;i<nroEstados-1;i++)  //inicializar las probabilidades pi_j = P(q_1=s_j)
                hmm.S[i].setPi(1.0/(double)nroEstados);
         * /
        /*PROBABILIDADES INICIALES PI S*/
 /*       hmm.S[0].setPi(1.0);   //solamente el estado inicial tiene probabilidad 1

        /*INICIALIZAR LOS GMM DE CADA ESTADO COM KMEANS Y LA MATRIZ DE TRANSICISIONES A CON LAS PROBABILIDADES INICIALES*/
 /*       hmm.addObservaciones(y);        //agregando el vector de observaciones Y de dimension d
        hmm.addObservaciones(w);        //agregando el vector de observaciones Y de dimension d

        hmm.inicializarKmeansGMM(); //inicializa la matriz de transiciones y los GMM de cada estado
        System.out.println("A");
        Matrices.mostrar(hmm.A);
        hmm.entrenarHMM(patronesE);
        hmm.mostrar();
        //hmm.verificar();

  /**CASO II
   *
   */

        /*obsevations*/
/*        double [][] y ={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}};
/*        double [][] w ={{10,20,30,40,50,60,70,80,90,100},{10,20,30,40,50,60,70,80,90,100}}; //observaciones

        /*array of observations*/
/*        Patron [] patronesE=new Patron[2];
        patronesE[0]=new Patron(y);
        patronesE[1]=new Patron(w);

        /*new HMM*/
/*        int nroEstados=5;
        HMM hmm=new HMM(nroEstados);
        /*Each state have a GMM with number of gaussians=nroGaussianas*/
/*        int nroGaussianas=2;
        hmm.inicializarGaussianas(nroGaussianas);
        /*alternativamente para cada estado un numero de gaussianas en particular
        * hmm.S[0].setNroGaussianas(4);hmm.S[1].setNroGaussianas(3);...*/

        /*Transitions of S*/
  /*      hmm.agregarTransiciones(0, 1);  //transicion estado 0 a estado 1
        hmm.agregarTransiciones(1, 1);  //transicion estado 1 a estado 1
        hmm.agregarTransiciones(1, 2);  //transicion estado 1 a estado 2
        hmm.agregarTransiciones(2, 2);  //transicion estado 2 a estado 2
        hmm.agregarTransiciones(2, 3);  //transicion estado 2 a estado 3
        hmm.agregarTransiciones(3, 3);  //transicion estado 3 a estado 3
        hmm.agregarTransiciones(3, 4);  //transicion estado 3 a estado 4
        
        /*PROBABILIDADES INICIALES PI S*/
  /*      hmm.S[0].setPi(1.0);   //solamente el estado inicial tiene probabilidad 1

        /*INICIALIZAR LOS GMM DE CADA ESTADO COM KMEANS Y LA MATRIZ DE TRANSICISIONES A CON LAS PROBABILIDADES INICIALES*/
  /*      hmm.addObservaciones(y);
        hmm.addObservaciones(w);
        /*Inicializar la matriz de Transiciones*/
 /*       hmm.inicializarAij();
        Matrices.mostrar(hmm.A);

        /*inicialiar los GMM de cada estado manualmente*/
/*        double media [][]={{30,30},
                            {4,4}};
        double coov [][]={{100,100},
                         {16.66666667,16.66666667}};

        double c[]={0.428571429,0.571428571};
       
        hmm.S[1].gmm=new GMM(hmm.S[1].observaciones, nroGaussianas,0);
        for(int i=0;i<nroGaussianas;i++){
            hmm.S[1].gmm.N[i]=new Gaussiana(media[i],coov[i]);
            hmm.S[1].gmm.c[i]=c[i];
        }
        double media1 [][]={{4.5,4.5},
                            {60,60}};
        double coov1 [][]={{0.5,0.5},
                         {100,100}};

        double c1[]={0.4,0.6};
        hmm.S[2].gmm=new GMM(hmm.S[2].observaciones, nroGaussianas,0);
        for(int i=0;i<nroGaussianas;i++){
            hmm.S[2].gmm.N[i]=new Gaussiana(media1[i],coov1[i]);
            hmm.S[2].gmm.c[i]=c1[i];
        }
        double media2 [][]={{90,90},
                            {6.5,6.5}};
        double coov2 [][]={{100,100},
                         {0.5,0.5}};

        double c2[]={0.6,0.4};
        hmm.S[3].gmm=new GMM(hmm.S[3].observaciones, nroGaussianas,0);
        for(int i=0;i<nroGaussianas;i++){
            hmm.S[3].gmm.N[i]=new Gaussiana(media2[i],coov2[i]);
            hmm.S[3].gmm.c[i]=c2[i];
        }

       // hmm.inicializarKmeansGMM(); //inicializa la matriz de transiciones y los GMM de cada estado
        System.out.println("A");

        hmm.mostrar();
        hmm.entrenarHMM(patronesE);
        hmm.mostrar();
   /*TEST PARA HMM CON PROBABILIDADES FIJAS Y PATRONES DE ENTRAMIENTO FIJOS*/
 /*       double [][] y ={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}; //observaciones
        double [][] w ={{10,20,30,40,50,60,70,80,90,100},{10,20,30,40,50,60,70,80,90,100}}; //observaciones

        int nroEstados=5;

        /*ESTADOS S*/
 /*       HMM hmm=new HMM(nroEstados);             //numero de estados
        hmm.inicializarGaussianas(2);    //inicializa cada estado con un GMM de n gaussianas

        /*TRANSICIONES S*/
 /*       hmm.agregarTransiciones(0, 1);
        hmm.agregarTransiciones(0, 2);
        hmm.agregarTransiciones(0, 3);
        hmm.agregarTransiciones(1, 1);
        hmm.agregarTransiciones(1, 2);
        hmm.agregarTransiciones(1, 3);
        hmm.agregarTransiciones(2, 1);
        hmm.agregarTransiciones(2, 2);
        hmm.agregarTransiciones(2, 3);
        hmm.agregarTransiciones(3, 1);
        hmm.agregarTransiciones(3, 2);
        hmm.agregarTransiciones(3, 3);
        hmm.agregarTransiciones(1, 4);
        hmm.agregarTransiciones(2, 4);
        hmm.agregarTransiciones(3, 4);

        hmm.inicializarAij();


        //ingresar manualmente (para test) los parametros de las GMM por estado
        //estado 1
        double [] media1={4,4};
        double [] cov1={16.6,16.6};
        Gaussiana g1=new Gaussiana(media1, cov1);
        double [] media2={30,30};
        double [] cov2={100,100};
        Gaussiana g2=new Gaussiana(media2, cov2);
        double [] coef={0.57,0.42};

        Gaussiana [] N={g1,g2};
        GMM gmm1=new GMM(coef,N);
        hmm.S[1].setGmm(gmm1);


        //estado 2
        double [] media3={4.5,4.5};
        double [] cov3={0.5,0.5};
        Gaussiana g3=new Gaussiana(media3, cov3);
        double [] media4={60,60};
        double [] cov4={100,100};
        Gaussiana g4=new Gaussiana(media4, cov4);
        double [] coef1={0.4,0.6};

        Gaussiana [] N1={g3,g4};
        GMM gmm2=new GMM(coef1,N1);
        hmm.S[2].setGmm(gmm2);

        //estado 3
        double [] media5={6.5,6.5};
        double [] cov5={0.5,0.5};
        Gaussiana g5=new Gaussiana(media5, cov5);
        double [] media6={90,90};
        double [] cov6={100,100};
        Gaussiana g6=new Gaussiana(media6, cov6);
        double [] coef7={0.4,0.6};

        Gaussiana [] N2={g3,g4};
        GMM gmm3=new GMM(coef7,N2);
        hmm.S[3].setGmm(gmm3);

        hmm.algoritmoForward(y);
        hmm.algoritmoBackward(y);
        hmm.algoritmoViterbi(y);
        hmm.getGamma_jm(y);
        hmm.getGammaLog(y);
        hmm.verificar();


        hmm.mostrar();
  *
  * TEST PARA VERSION 0.1
  */
        /*obsevations*/
        double [][] y ={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}};
        double [][] w ={{10,20,30,40,50,60,70,80,90,100},{10,20,30,40,50,60,70,80,90,100}};

        /*array of observations*/
        Patron [] patronesEntrenamiento=new Patron[2];
        patronesEntrenamiento[0]=new Patron(y);
        patronesEntrenamiento[1]=new Patron(w);


        /*HMM con 5 estados*/
        HMM hmm=new HMM(5);

        /*agregar transiciones*/
        hmm.agregarTransiciones(0, 1);  //transicion estado 0 a estado 1
        hmm.agregarTransiciones(1, 1);  //transicion estado 1 a estado 1
        hmm.agregarTransiciones(1, 2);  //transicion estado 1 a estado 2
        hmm.agregarTransiciones(2, 2);  //transicion estado 2 a estado 2
        hmm.agregarTransiciones(2, 3);  //transicion estado 2 a estado 3
        hmm.agregarTransiciones(3, 3);  //transicion estado 3 a estado 3
        hmm.agregarTransiciones(3, 4);  //transicion estado 3 a estado 4

        /*agregar GMM a cada estado*/
        hmm.setGMM(1, new GMM(2));
        hmm.setGMM(2, new GMM(2));
        hmm.setGMM(3, new GMM(2));

        hmm.inicializar(patronesEntrenamiento);
        hmm.mostrar();


        TrellisLog trellisLog=new TrellisLog(hmm, patronesEntrenamiento[0]);
        Trellis  trellis=new Trellis(hmm, patronesEntrenamiento[0]);

        trellisLog.forward();
        trellisLog.backward();
        trellisLog.viterbi(); 
        trellisLog.calcularGammaLog();
        trellisLog.calcularGamma_jm();
        trellisLog.verificar();

        

//       hmm.entrenar();
//        Util.mostrar(hmm.getA(),"Matriz A");
        

        //falta implementar las funciones 9.25 y 9.26
        // solo funciona para un hmm totalmente interconectado, verificar cuando no es totalmente inteconectado
        // verificar para las funciones que tengan aij diferente de 0

        /*Idea
         * 1. sacar la sumatoria e=1:E de actualizarCij, actualizarUjm y de actualizarEjm
         * 2. hacer funciones individuales para actualizarCij, actualizarUjm actualizarAij y de actualizarEjm pero solo para un patron
         * 3. crear un nuevo HMM pasandole los nuevos parametros obtenidos
         */


        /*FALTA VERIFICAR
         * viterbi
         * caluculo de gamaLog
         * actualizarCij  (verificado numeradorLogCjm, y denominadorLogCjm, falta el resto)
         * actualizarUjm
         * actualizarEjm
        */

        /*ENTRENAMIENTO CON BAUM-WELCH*/
        /*Para todos los patrondes de entrenamiento*/

        /*
         * modificar MFCC
         *  agregar cesptral mean substraccion
         *  agregar autoregresive coeficients
         *  agregar log energia del frame
         *  intentar implementar HFCC
         *  implementar baum welch         *  
         *  implementar un modelo de lenguaje
         */
        /*lunes 07 diciembre 2009
         * observacion:
         * cuando se accede a gammaLog_jm se accede desde el tiempo t=1 pues la formula se modificó (se encuentra al final de la tesis de maestria para t=1)
         * en caso no funcionará el modelo correctamente hacer desde t=2, verificado
         * se verificó numeradorLogCjm, y denominadorLogCjm, numeradorLogEjm,denominadorLogEjm,numeradorLogUjm,denominadorLogUjm
         *
         * -Implementadas las funciones actualizarAijNumerador y actualizarAijDenominador que implementan las ecuaciones 9.24 9.25 9.26, verificar
         *
         * martes 08 de diciembre
         * punto de verificacion algoritmo baum welch
         * se modifico la funcion alctualizarnumeradorAij y denominadorAij y se las llamo desde el algoritmo baum welch
         * falta terminar la parte de la sumatoria externa que depente de e en el algoritmo baum welch
         */

        /*sabado 26 de diciembre*/
        /*
         * falta testear baum welch, salen las probabilidades infinitas, parece que por que se calucla en cierto punto log(0) y eso lo vuelve nan
         */

        /* miercoles 13 de enero
         * modificar el algoritmo viterbi, fordward y backward pues tiene en cuenta todos los estados anteriore
         * y deberia tener solo en cuenta los estados anteriores de un estado en particular
         */

        /*sabado 16 de enero
         * modificado todo lo anterior
         * verificar que gamalog y gamalogjm den la misma suma
         * verificar para la actualización de la matriz A
         *
         */
          

    }

}



