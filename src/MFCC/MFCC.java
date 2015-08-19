package MFCC;

public class MFCC
{
    int frecuenciaDeSampleo ; //frecuencia de sampleo
    double[] arreglo;
    int nBins;

    double [] arregloPreenfasis; //arreglo despues del preenfasis
    double[][] matrizVentaneamiento;//matriz conteniendo los valores del ventaneamiento
    double [][]matrizFourier;//matriz con el módulo de la transformada de fourier
    double [][]matrizMFCC;//matriz con los valores MFCC
    double [][]MFCCDelta;//MFCC + delta
    double [][]MFCCDeltaDelta;//MFCC+delta+delta
    // lleva las frecuencias a la escala bin
    //nBins es el numero de bins
    //el arreglo que debe entrar es el modulo del FFT de tama�o N/2

    public MFCC(int frecuenciaDeSampleo, double [] arreglo, int nBins)
    {
        this.frecuenciaDeSampleo=frecuenciaDeSampleo;
	this.arreglo=arreglo;
	this.nBins=nBins;
    }

    public void procesar()
    {
        // filtro pre enfasis  alfa>0 filtro de enfasis en bajas frecuencias , alfa <0 se tiene un filtro de enfasis en altas frecuencias
        double alfa=-0.9;
        for(int i=1;i<arreglo.length;i++)
            arreglo[i]=arreglo[i]+alfa*arreglo[i-1];

        // ventaneamiento
        int tam=arreglo.length;
        int tamVentana= 512;   // 16ms
        int tamSlope = 160; //    10ms
        int nroFrames=(int)Math.ceil(((tam-tamVentana+1)/(double)tamSlope)+1);

        double [][] matriz = new double[nroFrames][];
        System.out.println("numero de Frames"+nroFrames);
        matriz=obtenerMatrizVentanamiento(arreglo,tamVentana, tamSlope,nroFrames);

        this.matrizVentaneamiento=matriz;//asignacion a atributo de la clase
        // fourier
         //computa iterativamente la matriz de fourier
        Fourier fft;
        double mayorEspectro=0;
        for(int i=0;i<matriz.length;i++)
        {
            fft=new Fourier(matriz[i]); //construye el objeto Fourier
            fft.FFTradix2DIF();					//computa el fft
            double [] arreglo1 ;
            matriz[i]=fft.moduloI(); //devuelve el modulo de la mitad de valores computados incluyendo el primer elemento

            if(mayorEspectro<fft.obtenerMayor())
                mayorEspectro=fft.obtenerMayor();
        }
        this.matrizFourier=matriz;//asignacion a atributo de la clase
        // bins  cepstrum  DTCII
        int filas=matriz.length;
        nBins=13;

        matrizMFCC=new double[filas][nBins];//crea matriz MFCC

        for(int i=0;i<filas;i++)
            matrizMFCC[i]=MFCC.coeficientesMFCC(frecuenciaDeSampleo,matriz[i],nBins);
        
        // delta
        // delta-delta

    }



    public double [][] obtenerMatrizVentanamiento( double [] arreglo, int tamVentana, int tamSlope, int nroFrames)
    {
        int a,b,j,k;
        int tam=arreglo.length;
        double [][] matriz = new double[nroFrames][];
	j=0;k=0;
	a=0;b=tamVentana;

        while(b<=tam)
        {
            matriz[j] = new double[tamVentana];
            for (int i=a;i<b;i++)
            {
                //ventana hamming
                matriz[j][k]=(0.54-0.46*Math.cos(2*Math.PI*k/(tamVentana-1)))*arreglo[i];
                k++;
            }
            a=a+tamSlope;
            b=b+tamSlope;
            j++;
            k=0;
        }
        if(b==tam)
            return matriz;
        if (b>tam)
        {
            matriz[j] = new double[tamVentana];
            for (int i=a;i<b;i++)
            {
                if(i<tam)
                    matriz[j][k]=(0.54-0.46*Math.cos(2*Math.PI*k/(tamVentana-1)))*arreglo[i];//ventana hamming

                else
                    matriz[j][k]=2*matriz[j][k-1]-matriz[j][k-2];    //extrapolacion

                k++;
            }
        }
        return matriz;
    }

    public static double []  coeficientesMFCC(int Fs, double [] arreglo, int nBins)
    {
        int N=arreglo.length;
	double [] Bins= new double [nBins];
	double fMenor=0;      //menor frecuencia   0Hz
	double fMayor=(double)(N-1)*(double)Fs/((double)N*(double)2);    //mayor frecuencia  = 1/(2*T) o 1*fs/2 criterio nyquist

        double fMelMenor = 0;
	double fMelMayor= 1125*(Math.log(1+(double)((fMayor))/700));

        double paso=fMelMayor/(nBins+1);
	double k=0,H=0;

        int i=0;int j=0; int ban=0; int pos=0; int b=0;
	double suma=0;
	double fMenos,fMedio,fMas;
		
	fMenos=0;fMedio=paso;fMas=2*paso;
		
	for(i=0;i<N;i++)
        {
            k= Mel((double)i*(double)Fs/((double)N*(double)2));  //por que es i*Fs/N, pero este vector solo tiene los N/2 samples de fourier, no tienen los conjugados

            if(k>fMas)
            {
                Bins[b]=Math.log(suma);   //cepstrum logaritmo natural
		i=pos;
		ban=0;
		suma=0;
		fMenos=fMenos+paso;
		fMedio=fMedio+paso;
		fMas=fMas+paso;
		k= Mel((double)i*(double)Fs/((double)N*(double)2));
                b++;
            }
            if(k>=fMenos&&k<fMedio)
            {
                H=(k-fMenos)/paso;
            }
            if(k>=fMedio&&k<=fMas)
            {
                if(ban==0)
                {
                    pos=i;
                    ban=1;
                }
                H=(fMas-k)/paso;
            }
            suma =suma+ arreglo[i]*H;  //pues arreglo[i] contiene los modulos
        }
        Bins[b]=Math.log(suma);  //cepstrum
        Bins=MFCC.DTCII(Bins);
	return Bins;
    }

    /****************/
    //trasnformada discreta del coseno para obtener el cepstrum
    //entrada ln[valor del bin]

    public static double [] DTCII(double [] arreglo)
    {
        double [] DCT = new double [arreglo.length];
	double suma =0;
	double N =(double)arreglo.length;
	for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
            //    suma = suma +arreglo[j]*Math.cos((Math.PI*((double)i+1)*((double)j+0.5))/N);
                suma = suma +arreglo[j]*Math.cos((Math.PI*((double)i)*((double)j+0.5))/N); //ec 6.145 spoken languaje procesing
            }
            DCT[i] = Math.sqrt(2/N)*suma;
            suma=0;
        }
        return DCT;
    }
    private static double Mel(double frecuencia)
    {
        frecuencia=1125*Math.log(1+frecuencia/(double)700);
	return frecuencia;
    }
}