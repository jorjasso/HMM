/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeloocultodemarkov;

/**ModeloOcultoDeMarkov
 *
 * @author usuario
 */
public class Estado {
    
    private String id;
    private GMM gmm;
    private double pi;
    /*observaciones asignadas a cada estado en el proceso de segmentacion*/

    //log prob de la observacion es decir se obtiene llamando a getLopProb de su GMM respectivo

    public Estado(String id)
    {
        this.id=id;
        gmm=new GMM(0);
    }

    public Estado(String id, GMM gmm, double pi) {
        this.id = id;
        this.gmm = gmm;
        this.pi = pi;
    }

    //b es la funcion de propabilidad para el la observacion y_t vector y en tiempo t
    //retorna el logaritmo de la probabilidad de y_t asociado al GMM de un estado
    public double b(double []y)
    {
        return gmm.getLogProb(y);
    }
    

    /*Getter and Setter*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPi() {
        return pi;
    }

    public GMM getGmm() {
        return gmm;
    }

    public void setGmm(GMM gmm) {
        this.gmm = gmm;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public void mostrar()
    {
        Util.mostrar(id, "Estado ");
        if(this.gmm!=null)
            this.gmm.mostrar();
    }

}



