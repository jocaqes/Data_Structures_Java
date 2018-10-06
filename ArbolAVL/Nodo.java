/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arboles;

/**
 *
 * @author JOSE QUIÑONEZ <jose at jocaqes.org>
 */
public class Nodo <T>{

    /**
     *Rama izquierda del nodo
     */
    public Nodo<T> izq;

    /**
     *Rama derecha del nodo
     */
    public Nodo<T> der;
    public int llave;
    public T item;

    /**
     *Factor de equilibrio, un factor |fe|>1 requiere rotacion
     */
    public int fe;

    /**
     *Construye un nuevo nodo con sus nodos hijo nulos
     */
    public Nodo() {
        izq=der=null;
        fe=0;
    }

    /**
     *Construye un nuevo nodo con sus nodos hijo nulos
     * @param llave el valor con el que se va a ordenar el nodo
     * @param item el item que sera guardado en el nodo
     */
    public Nodo(int llave, T item) {
        this.llave = llave;
        this.item = item;
        izq=der=null;
        fe=0;
    }
    
    /**
     *Genera una rotacion derecha del nodo y sus hijos 
     * @return un nuevo nodo raiz rotado
     */
    public Nodo<T> rotacionDer()
    {
        Nodo<T> q = izq;
        Nodo<T> p = this;
        p.izq=q.der;
        p.fe=0;
        q.der=p;
        q.fe=0;
        if(p.izq!=null&&der==null)//nuevo
        {
            p.fe--;
            q.fe++;
        }
        return q;
    }
    
    /**
     *Genera una rotaicon izquierda del nodo y sus hijos
     * @return un nuevo nodo raiz rotado
     */
    public Nodo<T> rotacionIzq()
    {
        Nodo<T> q = der;
        Nodo<T> p = this;
        p.der=q.izq;
        p.fe=0;
        q.izq=p;
        q.fe=0;
        if(p.izq!=null&&der==null)//nuevo
        {
            p.fe--;
            q.fe++;
        }
        return q;
    }

    @Override
    public String toString() {
        String codigo="";
        String id="n"+this.hashCode();
        codigo+=id+"[label=\""+llave+"fe:"+fe+"item:"+item.toString()+"\"];\n";
        if(izq!=null)
        {
            codigo+=id+"->n"+izq.hashCode()+";\n";
        }
        if(der!=null)
        {
            codigo+=id+"->n"+der.hashCode()+";\n";
        }
        return codigo;
    }
    
    
}
