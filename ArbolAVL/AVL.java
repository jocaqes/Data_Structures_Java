/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arboles;

/**
 *Clase para manejar arboles avl
 * @author JOSE QUIÑONEZ <jose at jocaqes.org>
 * @param <T> el tipo de objeto que va a contener el arbol
 */
public class AVL<T> {
    public Nodo<T> raiz;
    private boolean global_crecio;//basado en ejemplo del ingeniero    
    private boolean global_encogio;
    /**
     *Construye un nuevo arbol avl vacio
     */
    public AVL() {
        raiz=null;
    }
    
    
    /**
     *Inserta un nuevo item en el arbol avl, al mismo tiempo mantiene el equilibrio del arbol
     * @param item el item que será insertado en el arbol
     * @param llave la llave que sirve para ubicar el item en el arbol
     * @return <tt>true</tt> si el nuevo item fue insertado con exito;
     * <tt>false</tt> si no se pudo insertar el item, esto ocurre cuando
     * el nuevo item tiene una llave que ya existe en el arbol
     */
    public boolean insertar(T item, int llave)
    {
        global_crecio=false;
        if(raiz==null)
        {
            raiz=new Nodo<>(llave, item);
            return true;
        }
        else
        {
            raiz=insertar(item,llave,raiz);
        }
        return true;
    }

    private Nodo<T> insertar(T item, int llave, Nodo<T> raiz) {
        if(llave<raiz.llave)
        {
            if(raiz.izq==null)
            {
                raiz.izq=new Nodo<>(llave, item);
                raiz.fe--;
                global_crecio = raiz.fe!=0;
            }
            else
            {
                raiz.izq=insertar(item, llave, raiz.izq);
                if(global_crecio)
                {
                    raiz.fe--;
                    if(raiz.fe<-1)
                    {
                        if(raiz.izq.fe>0)
                            raiz.izq=raiz.izq.rotacionIzq();
                        global_crecio=false;
                        raiz=raiz.rotacionDer();
                        return raiz;
                    }
                    global_crecio=raiz.fe!=0;//nuevo
                }
            }
        }
        else if(llave>raiz.llave)
        {
            if(raiz.der==null)
            {
                raiz.der=new Nodo<>(llave, item);
                raiz.fe++;
                global_crecio = raiz.fe!=0;
            }
            else
            {
                raiz.der=insertar(item, llave, raiz.der);
                if(global_crecio)
                {
                    raiz.fe++;
                    if(raiz.fe>1)
                    {
                        if(raiz.der.fe<0)
                            raiz.der=raiz.der.rotacionDer();//antes era izq
                        global_crecio=false;
                        raiz=raiz.rotacionIzq();
                        return raiz;
                    }
                    global_crecio=raiz.fe!=0;//nuevo
                }
            }
        }
        return raiz;
    }
    
    public boolean modificar(int llave, T item)
    {
        Nodo<T> coincidencia=search(raiz,llave);
        if(coincidencia==null)
            return false;
        coincidencia.item=item;
        return true;
    }
    

    
    
    private Nodo<T> search(Nodo<T> raiz, int llave)
    {
        if(raiz==null)
            return null;
        if(llave==raiz.llave)
                return raiz;
        else if(llave<raiz.llave)
            return search(raiz.izq,llave);
        else
            return search(raiz.der,llave);
    }
    //////////////////////////////////////
    
    public void eliminar(int llave)
    {
        global_encogio=false;
        eliminar(raiz,null,llave);
    }
    
    private void eliminar(Nodo<T> raiz, Nodo<T> padre, int llave)//algoritmo cortesia de c++ con clase(solo algoritmo no tiene codigo hecho)
    {
        if(raiz==null)//si el arbol esta vacio o la llave no existe
            return;
        else if(llave<raiz.llave)//esta a la izquierda
            eliminar(raiz.izq, raiz, llave);
        else if(llave>raiz.llave)//esta a la derecha
            eliminar(raiz.der, raiz, llave);
        else
        {
            if(esHoja(raiz))//solo aqui elimina, de lo contrario hace varios intercambios hasta dejar al nodo a eliminar como hoja
            {
                if(padre==null)//es el unico nodo del arbol
                    this.raiz=null;
                else if(raiz==padre.der)//raiz es rama derecha de padre
                    padre.der=null;
                else if(raiz==padre.izq)//raiz es rama izquierda de padre
                    padre.izq=null;
            }
            else
            {
                Nodo<T> padre_aux=null;//conservamos padre del nodo auxiliar
                Nodo<T> aux=null;//recuperamos el menor de la derecha/mayor izquierda el que exista
                if(raiz.der!=null)
                {
                    padre_aux=rightMenor(raiz.der,raiz);
                    aux=padre_aux.izq;
                    if(aux==null)
                        aux=padre_aux.der;
                }
                else
                {
                    padre_aux=leftMayor(raiz.izq,raiz);
                    aux=padre_aux.der;
                    if(aux==null)
                        aux=padre_aux.izq;
                }
                int llave_aux=aux.llave;//guardo la llave de aux
                aux.llave=raiz.llave;//"cambio" de lugar aux con raiz
                raiz.llave=llave_aux;//"cambio" de lugar aux con raiz
                raiz.item=aux.item;//"cambio" los items(solo el de aux es necesario, el de raiz puede perderse
                eliminar(aux, padre_aux, llave);//eliminamos a "raiz" en su nueva posicion
            }
        }
    }
    
    private boolean esHoja(Nodo<T> raiz) {
        return raiz.izq==null&&raiz.der==null;
    }
    
    private Nodo<T> rightMenor(Nodo<T> raiz, Nodo<T> padre)
    {
        if(raiz==null)
            return null;
        if(raiz.izq!=null)
            return rightMenor(raiz.izq,raiz);
        else
            return padre;
    }
    private Nodo<T> leftMayor(Nodo<T> raiz,Nodo<T> padre)
    {
        if(raiz==null)
            return null;
        if(raiz.der!=null)
            return leftMayor(raiz.der,raiz);
        else
            return padre;
    }
    //////////////////////////////////////
    
    public void enOrden(Nodo<T> raiz)
    {
        if(raiz!=null)
        {
            enOrden(raiz.izq);
            System.out.println(raiz.llave+"fe:"+raiz.fe);
            enOrden(raiz.der);
        }
    }
    
    public void debug(Nodo<T> raiz,String prefijo)
    {
        if(raiz!=null)
        {
            System.out.println(prefijo+raiz.llave+"fe:"+raiz.fe);
            debug(raiz.izq,"izq:");
            debug(raiz.der,"der:");
        }
        
    }
    
    public void graficar(String url)
    {
        String codigo="digraph G{\n";
        codigo+=codigoNodos(raiz);
        codigo+="}";
        Archivo.graficar(codigo, url);
    }
    
    private String codigoNodos(Nodo<T> raiz)
    {
        String codigo="";
        if(raiz!=null)
        {
            codigo+=raiz.toString();
            codigo+=codigoNodos(raiz.izq);
            codigo+=codigoNodos(raiz.der);
        }
        return codigo;
    }
        
}
