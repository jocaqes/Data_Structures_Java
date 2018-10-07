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
    private boolean global_encogio;//contraparte de creio para la eliminacion
    private boolean global_elimino;//por ahora necesario para la eliminacion
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
        if(llave<raiz.llave)//si el nuevo va a la izquierda
        {
            if(raiz.izq==null)//si el espacio esta disponible
            {
                raiz.izq=new Nodo<>(llave, item);//agregamos nuevo nodo
                raiz.fe--;//actualizamos el factor de equilibrio
                global_crecio = raiz.fe!=0;//revisamos si crecio raiz
            }
            else//el espacio no esta disponible
            {
                raiz.izq=insertar(item, llave, raiz.izq);//nos adentramos para insertar, si ocurre alguna rotacion actualizamos raiz.izq por el resultado
                if(global_crecio)//si crecio
                {
                    raiz.fe--;//actualizamos el factor de equilibrio
                    if(raiz.fe<-1)//si esta desbalanceado por mas de |1|
                    {
                        boolean doble=false;//bandera par verificar rotacion doble
                        if(raiz.izq.fe>0)//RDD
                        {
                            raiz.izq=raiz.izq.rotacionIzq();//rotamos a la izquierda su nodo izquierdo
                            doble=true;//actualizamos bandera
                        }
                        global_crecio=false;//actualizamos bandera crecio, @deprecated
                        raiz=raiz.rotacionDer();//siempre hay que hacer rotacion derecha
                        actualizarFE(raiz, doble);//actualizamos factores de equilibiro
                        return raiz;//retornamos la raiz luego de sus rotaciones
                    }
                    global_crecio=raiz.fe!=0;//nueva actualizacion de crecimiento, necesario para llevar el crecimiento hasta raiz si es necesario
                }
            }
        }
        else if(llave>raiz.llave)//el nuevo va a la derecha
        {
            if(raiz.der==null)//si el espacio esta disponible
            {
                raiz.der=new Nodo<>(llave, item);//agregamos nuevo nodo
                raiz.fe++;//actualizamos el factor de equilibrio
                global_crecio = raiz.fe!=0;//revisamos si grecio la raiz
            }
            else//el espacio no esta disponible
            {
                raiz.der=insertar(item, llave, raiz.der);//nos adentramos para insertar, si ocurre alguna rotacion actualizamos raiz.der por el resultado
                if(global_crecio)//si crecio
                {
                    raiz.fe++;//actualizamos el factor de equilibrio
                    if(raiz.fe>1)//si esta desbalanceado por mas de |1|
                    {
                        boolean doble=false;//bandera para verificar rotacion doble
                        if(raiz.der.fe<0)//RDI
                        {
                            raiz.der=raiz.der.rotacionDer();//rotamos a la derecha su derecho
                            doble=true;//actualizamos bandera
                        }
                        global_crecio=false;//actualizamos bandera crecio, @deprecated
                        raiz=raiz.rotacionIzq();//siempre hay que hacer rotacion izquierda
                        actualizarFE(raiz, doble);//actualizamos factores de equilibiro
                        return raiz;//retornamos la raiz luego de sus rotacciones
                    }
                    global_crecio=raiz.fe!=0;//nueva actualizacion de crecimiento, necesario para llevar el crecimiento hasta la raiz si es necesario
                }
            }
        }
        return raiz;//si el valor esta repetido lo ignoro y reconstruyo el arbol tal y como estaba
    }
    
    /**
     *Permite moficar valores en el arbol avl con la restriccion de que la llave es inmutable
     * @param llave el parametro de busqueda
     * @param item el nuevo valor para el nodo
     * @return <tt>true</tt> si se logro hacer la modificacion, <tt>false</tt> si el nodo con 
     * la llave otorgada no existe
     */
    public boolean modificar(int llave, T item)
    {
        Nodo<T> coincidencia=search(raiz,llave);//nodo encontrado
        if(coincidencia==null)//si no se encontro nada
            return false;
        coincidencia.item=item;//cambio el item
        return true;
    }
       
    private Nodo<T> search(Nodo<T> raiz, int llave)
    {
        if(raiz==null)
            return null;
        if(llave==raiz.llave)//coincidencia
                return raiz;
        else if(llave<raiz.llave)//nodo puede estar a la izquierda
            return search(raiz.izq,llave);
        else//nodo puede estar a la derecha
            return search(raiz.der,llave);
    }
    //////////////////////////////////////
    
    /**
     *Permite eliminar un nodo del arbol avl y lo reconstruye respetando los factores de
     * equilibrio y las rotaciones necesarias para que el arbol siempre quede balanceado
     * @param llave el parametro de busqueda
     * @return <tt>true</tt> si un nodo fue eliminado <tt>false</tt> si no se encontro
     * ningun nodo para eliminar
     */
    public boolean eliminar(int llave)
    {
        global_encogio=false;
        global_elimino=true;
        raiz=eliminar(raiz,null,llave);
        return global_elimino;
    }
    
    private Nodo<T> eliminar(Nodo<T> raiz, Nodo<T> padre, int llave)//algoritmo cortesia de c++ con clase(solo algoritmo no se copio codigo)
    {
        if(raiz==null)//si el arbol esta vacio o la llave no existe
        {
            global_elimino=false;//no se elimino nada
            return raiz;//retorno la raiz
        }
        else if(llave<raiz.llave)//nodo puede estar a la izquierda
        {
            raiz.izq=eliminar(raiz.izq, raiz, llave);//se adentra para eliminar y actualiza rama izq de raiz
            if(global_elimino){//si hubo eliminacion
                if(raiz.izq==null||global_encogio)//si elimino directo de raiz, o mas abajo(global_encogio)
                    raiz.fe++;//actualizamos factor de equilibrio
                global_encogio=raiz.fe==0&&global_encogio||raiz.der==null&&raiz.izq==null;//actualizamos bandera encogio
            }
        }
        else if(llave>raiz.llave)//nodo puede estar a la derecha
        {
            raiz.der=eliminar(raiz.der, raiz, llave);//se adentra para eliminar y actualiza rama der de raiz
            if(global_elimino)//si hubo eliminacion
            {
                if(raiz.der==null||global_encogio)//si elimino directo de raiz, o mas abajo(global_encogio)
                    raiz.fe--;//actualizamos factor de equilibrio
                global_encogio=raiz.fe==0&&global_encogio||raiz.der==null&&raiz.izq==null;//actualizamos bandera encogio
            }
        }
        else//coincidencia
        {
            if(esHoja(raiz))//ES HOJA y por tanto elimina el nodo
            {
                if(padre==null)//es el unico nodo del arbol
                    this.raiz=null;//actualizo la raiz @deprecated
                return null;//porque elimine a raiz
            }
            else//no es hoja, hacemos intercambios con menor derecho/mayor izquierdo hasta llevar al nodo coincidencia a ser hoja
            {
                Nodo<T> padre_aux=null;//conservamos padre del nodo auxiliar
                Nodo<T> aux=null;//recuperamos el menor de la derecha/mayor izquierda el que exista
                if(raiz.der!=null)//si hay arbol derecho
                {
                    padre_aux=rightMenor(raiz.der,raiz);//padre del arbol @deprecated
                    aux=padre_aux.der;//nodo menor derecho
                }
                else//no hay arbol derecho
                {
                    padre_aux=leftMayor(raiz.izq,raiz);//padre del arbol @deprecated
                    aux=padre_aux.izq;//nodo mayor izquierdo
                }
                int llave_aux=aux.llave;//guardo la llave de aux
                aux.llave=raiz.llave;//"cambio" de lugar aux con raiz
                raiz.llave=llave_aux;//"cambio" de lugar aux con raiz
                raiz.item=aux.item;//"cambio" los items(solo el de aux es necesario, el de raiz puede perderse
                if(raiz.der!=null)//si der no es null entonces use rightMenor, asi que en sub-arbol derecho se encuentra raiz
                {
                    raiz.der=eliminar(raiz.der,raiz,llave);//nos adentramos a eliminar y actualizamos raiz der
                    if((raiz.der==null||global_encogio)&&global_elimino)//si hubo eliminacion y fue directamente a raiz o se encogio
                        raiz.fe--;//actualizamos factor de equilibrio
                }
                else//si der es null entonces use leftMayor, asi que en sub-arbol izquierdo se encuentra raiz
                {
                    raiz.izq=eliminar(raiz.izq,raiz,llave);//nos adentramos a eliminar y actualizamos raiz izq
                    if((raiz.izq==null||global_encogio)&&global_elimino)//si hubo eliminacion y fue directamente a raiz o se encogio
                        raiz.fe++;//actualizamos factor de equilibrio
                }
                if(global_elimino)//si hubo eliminacion
                    global_encogio=raiz.fe==0&&global_encogio||raiz.der==null&&raiz.izq==null;//actualizamos encogio
            }
        }
        
        if(global_elimino)//si hubo eliminacion
        {
            if(raiz.fe<-1)//si esta desbalanceado por mas de |1|
            {
                boolean doble=false;//mismos comentarios que en insertar
                if(raiz.izq.fe>0)
                {
                    raiz.izq=raiz.izq.rotacionIzq();
                    doble=true;
                }
                raiz=raiz.rotacionDer();
                actualizarFEDelete(raiz, doble, true);//actualizacion de factor de equilibrio distinta
                global_encogio=raiz.fe==0;
            }
            else if(raiz.fe>1)
            {
                boolean doble=false;
                if(raiz.der.fe<0)
                {
                    raiz.der=raiz.der.rotacionDer();//antes era izq
                    doble=true;
                }
                raiz=raiz.rotacionIzq();
                actualizarFEDelete(raiz, doble, false);
                global_encogio=raiz.fe==0;
            }
        }
        return raiz;
    }
    
    private boolean esHoja(Nodo<T> raiz) {//revisa si el nodo es hoja(no tiene rama izquierda ni derecha)
        return raiz.izq==null&&raiz.der==null;
    }
    
    private Nodo<T> rightMenor(Nodo<T> raiz, Nodo<T> padre)//busca el nodo menor de la rama derecha de raiz
    {
        if(raiz==null)
            return null;
        if(raiz.izq!=null)
            return rightMenor(raiz.izq,raiz);
        else
        {
            Nodo<T> padre_auxiliar=new Nodo<>(padre.llave,padre.item);
            padre_auxiliar.der=raiz;
            return padre_auxiliar;
        }
            //return padre;
    }
    private Nodo<T> leftMayor(Nodo<T> raiz,Nodo<T> padre)//busca nodo mayor de la rama izquierda de raiz
    {
        if(raiz==null)
            return null;
        if(raiz.der!=null)
            return leftMayor(raiz.der,raiz);
        else
        {
            Nodo<T> padre_auxiliar = new Nodo<>(padre.llave, padre.item);
            padre_auxiliar.izq=raiz;
            return padre_auxiliar;
        }
            //return padre;
    }
    //////////////////////////////////////
    
    /**
     *Imprime los valores del arbol avl en orden, de la forma llavefe:fe
     * @param raiz la raiz del arbol 
     */
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
    
    /**
     *Permite graficar el arbol avl con la herramienta graphviz
     * @param url la ubicacion final del archivo
     */
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
    
    private void actualizarFE(Nodo<T> raiz, boolean doble)//para insercion
    {
        if(doble)
        {
            switch(raiz.fe)//buscar explicacion en "c++ con clase"
            {
                case -1:raiz.izq.fe=0;raiz.der.fe=1;break;
                case 0:raiz.izq.fe=0;raiz.der.fe=0;break;//check
                case 1:raiz.izq.fe=-1;raiz.der.fe=0;break;//check
            }
            raiz.fe=0;
        }
        else
        {
            switch(raiz.fe)
            {
                case -1:raiz.der.fe=0;break;
                case 0:raiz.der.fe=0;raiz.izq.fe=0;break;
                case 1:raiz.izq.fe=0;break;
            }
            raiz.fe=0;
        }
    }
    
    private void actualizarFEDelete(Nodo<T> raiz, boolean doble, boolean der)
    {
        if(doble)
        {
            switch(raiz.fe)//mismo al doble de incersion
            {
                case -1:raiz.izq.fe=0;raiz.der.fe=1;break;
                case 0:raiz.izq.fe=0;raiz.der.fe=0;break;//check
                case 1:raiz.izq.fe=-1;raiz.der.fe=0;break;//check
            }
            raiz.fe=0;
        }
        else
        {
            if(der)
            {
                switch(raiz.fe)//valores basados en pruebas realizadas con distintas posibilidades
                {
                    case -1:raiz.der.fe=0;raiz.fe=0;break;//raiz.fe es nuevo
                    case 0:raiz.der.fe=-1;raiz.fe=1;break;
                    case 1:raiz.der.fe=-1;raiz.fe=1;break;
                }
            }
            else
            {
                switch(raiz.fe)
                {
                    case -1:raiz.izq.fe=1;raiz.fe=-1;break;
                    case 0:raiz.izq.fe=1;raiz.fe=-1;break;
                    case 1:raiz.izq.fe=0;raiz.fe=0;break;//raiz.fe es nuevo
                }
            }
        }
    }    
        
}
