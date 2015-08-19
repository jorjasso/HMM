/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author usuario
 */
public class Util {
     
   //devuelve la suma de dos logaritmos ec 9.54 log(A+B) , pero cuando se ingresa log(A) y log(B)
   public static double sumLog(double logA, double logB)
    {
        double C;
        double temp;
        //puede haber una situacion como sumLog(0,0)=log(exp(0)+exp(0))
        if(logA==0&&logB==0)
        {
            return log(2);
        }
        if (logB>logA)
        {
            temp=logA;
            logA=logB;
            logB=temp;
        }

        C=logB-logA;
        if(C<-700.0005) //pues existe mucha diferencia de magnitud ver alg pag 154 Holmes y aproximadamente el log del numero mas bajo representable en la computadora es aproximadamente --700.0005
            C=0;
        else
            C=Util.log(1+Math.exp(C));

       return logA+C;
    }
   //devuelve la suma de log(A+B+....)
   public static double sumLog(double[] logs)
   {
        /*ordenar*/
       // sirve para tener ordenadas logaritmos de las probabilidades antes de sumarlas
       // pues se requiere paraevitar errores numericos pues se requiere que cuando
       //sum (logA + logB) logA siempre sea mayot a logB, entonces se requiere realizar una
       //eliminar ceros
       logs=Util.eliminarCeros(logs);

       if(logs==null)
           return 0;

       Util.ordenar(logs);
       return sumLogProb(logs);
   }
    //metodo recursivo a usarse en sumLog para devolver la suma de log(A+B+....)
   private static double sumLogProb(double [] logs)
   {
       if(logs.length==1)                  
               return logs[0];                  

       if (logs.length==2)
            return Util.sumLog(logs[0],logs[1]);
       else
       {
            double [] temp=new double[logs.length-1];
            for(int i=0;i<temp.length;i++)
                temp[i]=logs[i];

            return sumLogProb(temp)+Util.log(1+Math.exp(logs[logs.length-1]-sumLogProb(temp)));
        }
   }


   public static double[] ordenar(double[] vector)
   {
       quicksort(vector,0,vector.length-1);
       return vector;
   }

   /*quicksort introduction to algorithms capitulo 7*/
   public static void quicksort(double []A,int p,int r)
   {
       if(p<r)
       {
           int q=partition(A,p,r);
           quicksort(A,p,q-1);
           quicksort(A,q+1,r);
       }
   }
   public static int partition(double []A,int p,int r)
   {
       double x=A[r];
       int i=p-1;
       for(int j=p;j<=r-1;j++)
       {
           if (A[j]>=x)
           {
               i=i+1;
               double temp;
               temp=A[i];
               A[i]=A[j];
               A[j]=temp;
           }
       }
       double temp;
       temp=A[i+1];
       A[i+1]=A[r];
       A[r]=temp;

       return i+1;
   }


   //funcion de trasnformacion c, d rango de entrada, m,n rango de salida, Y es el numero a encontrar
   public static double T(double y, double c, double d, double m, double n)
   { 
       return ((n-m)*y+m*d-n*c)/(d-c);       
   }
   
   //calcula la distancia euclidiana entre dos vectores
   public static double distancia(double []A,double []B)
   {
       double dist=0;
       for(int i=0;i<A.length;i++)
       {
           dist=dist+Math.pow((A[i]-B[i]),2);
       }
       return Math.sqrt(dist);
   }
   public static double max(double[] y){
       double max=-700;
       for(int i=0;i<y.length;i++){
           if(max<y[i])
               max=y[i];
       }
           return max;       
   }

   public static double[] sumar(double c, double[] A)
   {
       for(int i=0;i<A.length;i++)
           A[i]=A[i]+c;
       return A;
   }

   public static double[] restar(double[] A, double c)
   {
       for(int i=0;i<A.length;i++)
           A[i]=A[i]-c;
       return A;
   }

   public static double[] multiplicar(double c, double[] A)
   {
       for(int i=0;i<A.length;i++)
           A[i]=c*A[i];
       return A;
   }

   public static double [] exp(double []A)
   {
       for(int i=0;i<A.length;i++)
           A[i]=Math.exp(A[i]);
       return A;
   }
   public static double [] log(double [] A)
   {
       for(int i=0;i<A.length;i++)
           A[i]=Util.log(A[i]);
       return A;
   }
   public static double [][]log(double[][]A)
   {
       double[][] temp=new double[A.length][A[0].length];
       for (int i=0;i<A.length;i++)
           for(int j=0;j<A[0].length;j++)
               temp[i][j]=Util.log(A[i][j]);
       return temp;
   }

   public static double[] restarAbs(double[] A, double[] B)
   {
       for(int i=0;i<A.length;i++)
           A[i]=Math.abs(A[i]-B[i]);
       return A;
   }
   public static double [] eliminarCeros(double[] A)
   {
       int cont=0;
       for(int i=0;i<A.length;i++)
       {
               if(A[i]==0)
                   cont++;
       }

       double []B=new double[A.length-cont];
       cont=0;
       for(int i=0;i<A.length;i++)
       {
           if(A[i]!=0)
           {
               B[cont]=A[i];
               cont++;
           }
       }
       if(B.length==0)
           return null;
       else       
           return B;                  
   }
   public static double log(double n)
   {
       if(n==0)
           return 0;
       if (n==1)
           return 0.000000000000001;
       else if(n>0)
       {  
           return Math.log(n);
       }
       else
       {
               return Math.log(-n);
       }
   }

   
    //calculo de la inversa de una matriz por medio de eliminacion Gauss-Jordan
    public static double [][] inversa(double [][]M)
    {
        System.out.println("matriz inversa");
        double [][]Tem=identidad(M.length);
        double [][]I=identidad(M.length);

        for(int k=0;k<M.length;k++)
        {
            for(int i=0;i<M.length;i++)
            {
                if(k!=i){ Tem[i][k]=-M[i][k]/M[k][k];}
            }

            M=multiplicar(Tem,M);
            I=multiplicar(Tem,I);

            for(int i=0;i<M.length;i++)
            {
                if(k!=i){Tem[i][k]=0;}
            }
        }

        for(int k=0;k<M.length;k++)
        {
            if(M[k][k]>1)
            {
                for(int i=0;i<M.length;i++){ I[k][i]=I[k][i]/M[k][k];}
            }
        }
        return I;
    }

    //devuelde la matriz identidad de tamaño n x n
    public static double[][] identidad(int n)
    {
        double [][]I=new double[n][n];
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(i==j)
                {
                    I[i][j]=1;
                }
                else
                {
                    I[i][j]=0;
                }

            }
        }
        return I;
    }

    //obtiene el promedio de los elementos de cada fila de la matriz
    //M = matriz d x n , n datos d dimensionales
    public static double [] promedio(double [][]M)
    {
        double []u=new double[M.length];
        double suma=0;

        for(int i=0;i<M.length;i++)
        {
            suma=0;
            for(int j=0;j<M[0].length;j++)
            {
            suma=suma+M[i][j];
            }
            u[i]=suma/M[i].length;
        }

        return u;
    }

    //matriz M de d x n , es decir n vectores columna d dimensionales

    public static double [][] coovarianza(double [][]M)
    {
        double []u=promedio(M);
        double [][]suma=new double[M.length][M.length];
        double[][]A=new double[M.length][1];
        double[][]B=new double[1][M.length];

        for (int i=0;i<M[0].length;i++)//para todos los n vectores
        {
            for(int j=0;j<M.length;j++) //para cada dimension
            {
                A[j][0]=M[j][i]-u[j];
                B[0][j]=A[j][0];
            }

            suma=sumar(suma,multiplicar(A,B));
        }

        return escalar(suma,(M[0].length-1));
    }

    public static double [] coovarianzaDiagonal(double [][]M,double [] u)
    {
        //double []u=Matrices.promedio(M);
        double []A=new double[M.length];
        double []suma=new double[M.length];

        for(int i=0;i<M.length;i++)
        {
            suma[i]=0;
        }

        for (int i=0;i<M[0].length;i++)//para todos los n vectores
        {
            for(int j=0;j<M.length;j++) //para cada dimension
            {
                A[j]=(M[j][i]-u[j])*(M[j][i]-u[j]);
            }

            //suma=sumar(suma,multiplicar(A,B));
            for(int j=0;j<M.length;j++)
            {
            suma[j]=suma[j]+A[j];
            }

        }

        for(int j=0;j<M.length;j++)
        {
            if(M[0].length!=1)
            {
                suma[j]=suma[j]/(M[0].length-1);
            }
            if(suma[j]==0.0)
            {
                suma[j]=0.00000000000000000000000000000000000000000000000000000000000000000000009;
            }

        }

        return suma;
    }


    public static double[][] multiplicar(double [][]A,double[][]B)
    {
        double[][] C=new double[A.length][B[0].length];
        double suma=0;

        for(int i=0;i<A.length;i++)
        {
            for(int j=0;j<B[0].length;j++)
            {
                suma=0;
                for(int k=0;k<A[0].length;k++)
                {
                    suma=suma+A[i][k]*B[k][j];
                }
                C[i][j]=suma;
            }
        }

        return C;
    }

    //
    public static double[][] escalar(double [][]A,double f)
    {
        double[][] C=new double[A.length][A[0].length];

        for(int i=0;i<A.length;i++)
        {
            for(int j=0;j<A[0].length;j++)
            {
                C[i][j]=A[i][j]/f;
            }
        }

        return C;
    }

    //suma dos matrices C=A+B
    public static double [][] sumar(double [][]A,double[][]B)
    {
        double[][] C=new double[A.length][A[0].length];


        for(int i=0;i<A.length;i++)
        {
            for(int j=0;j<A[0].length;j++)
            {
                C[i][j]=A[i][j]+B[i][j];
            }
        }
        return C;
    }

    public static double[][] traspuesta(double [][]M)
    {
        double [][]T=new double[M[0].length][M.length];

        for(int i=0;i<T.length;i++)
        {
            for(int j=0;j<T[0].length;j++)
            {
                T[i][j]=M[j][i];
            }
        }

        return T;

    }

    //redimensiona la matriz de d x n a una matriz de d x (n+1) datos, agregando el vector Y[] en la ultima columna de la matriz

    public static double[][] redimensionar(double[][]M,double[]Y)
    {
        double[][]temp=M;

        M=new double [M.length][M[0].length+1];

        for(int i=0;i<M.length;i++)
        {
            for(int j=0;j<M[0].length-1;j++)
            {
                M[i][j]=temp[i][j];
            }
        }

        for(int i=0;i<M.length;i++)
        {
            M[i][M[0].length-1]=Y[i];
        }

        return M;
    }

    public static void mostrar(double [][]M, String mensaje)
    {        
        if(M==null)
            System.out.println("null");
        else
        {
            System.out.println("-----------------");
            System.out.println(mensaje);
            System.out.println("-----------------");

            for(int i=0;i<M.length;i++)
            {
                for(int j=0;j<M[0].length;j++)
                {
                    System.out.print(" "+(double)Math.round(100*M[i][j])/100+" ");
                }                
                System.out.println(" ");
            }            
        }        
    }

    /**
     *
     * @param M = arreglo a imprimirse
     * @param mensaje = mensaje personalizado
     * @param opcion = puede ser 'H' o 'V' si se quiere la visualizar la impresiòn 'H'=horizontal ó 'V'=vèrtical
     */


    public static void mostrar(double []M, String mensaje, char opcion)
    {
        System.out.println("-----------------");
        System.out.println(mensaje);
        System.out.println("-----------------");
        if(M==null)
            System.out.println("null");
        else
        {
            if(opcion=='V')
            {
                for(int i=0;i<M.length;i++)
                    System.out.println(""+M[i]+" ");
                System.out.println(" ");
            }
            else
            {
                for(int i=0;i<M.length;i++)
                    System.out.print(""+M[i]+" ");
                System.out.println(" ");
            }
        }

    }
    public static void mostrar(double M, String mensaje)
    {
        System.out.println("-----------------");
        System.out.println(mensaje);
        System.out.println("-----------------");
        System.out.println(" "+M);        
    }

    public static void mostrar(String M, String mensaje)
    {
        if(M==null)
            System.out.println("null");
        else
        {
            System.out.println("-----------------");
            System.out.println(mensaje);
            System.out.println("-----------------");
            System.out.println(" "+M);
        }
    }

    public double[] copiar(double []A,double B[])
    {
        for(int i=0;i<A.length;i++)
        {
            A[i]=B[i];
        }
        return A;
    }

    //retorna una matriz de ceros de mxn
    public static double[][] ceros(int m, int n)
    {
        double[][] M=new double[m][n];
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++)
                M[i][j]=0;
        return M;
    }
    /**
     *
     * @param A
     * @return arreglo A lleno de ceros
     */

    public static double[] resetear(double A[])
    {
    //    double []temp=new double[A.length];
    //    return temp;
        for(int i=0;i<A.length;i++)
            A[i]=0;
        return A;

    }
    /**
     *
     * @param A
     * @return matriz A llena de ceros
     */

    public static double[][] resetear(double A[][])
    {
        for(int i=0;i<A.length;i++)
            for(int j=0;j<A[0].length;j++)
                A[i][j]=0;
        return A;
    }
    public static void mensaje(String[] mensaje)
    {
        for(int i=0;i<mensaje.length;i++)
            System.out.println(mensaje);
    }
    public static void mensaje(String mensaje)
    {
            System.out.println(mensaje);
    }

    /*convierte un arreglo de doubles a String*/
    public static String doubleArrayToString(double datos[])
    {
        String str="";
        for(int i=0;i<datos.length;i++)
            str+=" "+datos[i]+" ";
        return str;
    }

    /**
     * Retorna el exp de cada elemento del arreglo A
     * @param A
     * @return Retorna el exp de cada elemento del arreglo A
     */
    public static double[] exponecial(double []A)
    {
        double []temp=new double[A.length];
        for(int i=0;i<A.length;i++)
            temp[i]=Math.exp(A[i]);
        return temp;
    }

    public static double[][] exponencial(double[][]A)
    {
        double[][]temp=new double[A.length][A[0].length];
        for(int i=0;i<A.length;i++)
            for(int j=0;j<A[0].length;j++)
                temp[i][j]=Math.exp(A[i][j]);
        return temp;
    }

    /**
     * Visualiza una matriz del tipo aij=log(nij) como aij=exp(log(aij))
     *
     * @param A es una matriz de logaritmos
     * @param info es l ainformaciòn adicional sobre la matriz
     */

    public static void visualizarLogToProb(double A[][],String info)
    {
        double temp[][]=new double[A.length][A[0].length];
        temp=Util.exponencial(A);
        Util.mostrar(A, "Matriz en probabilidades"+info);
    }

}

