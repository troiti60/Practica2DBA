/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;

/**Clase de testeo para la clase mapa (y nodo)
 *
 * @author antonio
 */
public class TestMapa {
    /**
    * Testeo básico del HashMap y las funciones equals y hashCode de Coord
    * así como de Integer
    * @author Antonio Troitiño
    */
    public static void test0(){
            HashMap<Coord,Nodo> conectados=new HashMap<Coord,Nodo>();
        HashMap<Integer,Nodo> conecta2=new HashMap<Integer,Nodo>();
        conectados.put(new Coord(0,0), new Nodo(0,0,0,0));
        conecta2.put(0,new Nodo(0,0,0,0));
        System.out.println("Conectados está vacío? "+conectados.isEmpty()
                            +"\nConectados contiene a (0,0)? "+
                            conectados.containsKey(new Coord(0,0)));
        System.out.println("Conecta2 está vacío? "+conecta2.isEmpty()
                            +"\nConectados contiene a (0,0)? "+
                            conecta2.containsKey(0));
    
    }
    /**
    * Testeo básico del HashMap y las funciones equals y hashCode de Coord
    * @author Antonio Troitiño
    */
    public static void test1(){
           Mapa map= Mapa.crearInstancia();
        
        map.addNodo(new Nodo(0, 0, 0, 0));
    
            String cadena=null;
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        System.out.println("Conectados está vacío? "+conectados.isEmpty()
                            +"\nConectados contiene a (0,0)? "+
                            conectados.containsKey(new Coord(0,0)));
    
    }
    
    /**
    * Testeo básico del Mapa y las funciones comprobar vecinos, mudar lista nodos
    * y el estado de la variable explorado de los nodos
    * @author Antonio Troitiño
    */
    public static void test2(){
        Mapa map= Mapa.crearInstancia();
        map.addNodo(new Nodo(2, 2, 0, 10));
        
        map.addNodo(new Nodo(0, 0, 0, 0));
        map.addNodo(new Nodo(1, 0, 0, 1));
        map.addNodo(new Nodo(2, 0, 0, 0));
        map.addNodo(new Nodo(3, 0, 1, 1));
        map.addNodo(new Nodo(4, 0, 0, 0));
        map.addNodo(new Nodo(0, 1, 0, 1));
        map.addNodo(new Nodo(1, 1, 0, 0));
        map.addNodo(new Nodo(2, 1, 0, 1));
        map.addNodo(new Nodo(3, 1, 1, 0));
        map.addNodo(new Nodo(4, 1, 0, 1));
        map.addNodo(new Nodo(0, 2, 0, 1));
        map.addNodo(new Nodo(1, 2, 0, 0));
        map.addNodo(new Nodo(3, 2, 1, 0));
        map.addNodo(new Nodo(4, 2, 0, 1));
        map.addNodo(new Nodo(0, 3, 0, 1));
        map.addNodo(new Nodo(1, 3, 0, 0));
        map.addNodo(new Nodo(2, 3, 0, 1));
        map.addNodo(new Nodo(3, 3, 1, 0));
        map.addNodo(new Nodo(4, 3, 0, 1));
        map.addNodo(new Nodo(0, 4, 0, 1));
        map.addNodo(new Nodo(1, 4, 0, 0));
        map.addNodo(new Nodo(2, 4, 0, 1));
        map.addNodo(new Nodo(3, 4, 1, 0));
        map.addNodo(new Nodo(4, 4, 0, 1));
        
        //inicializamos el mapa de la siguiente manera:
        
        /*  0   0   0   1   0
            0   0   0   1   0   
            0   0   0   1   0
            0   0   0   1   0   
            0   0   0   1   0
        De esta forma, los nodos de la izquierda deberían formar parte del
        grafo conexo, y los nodos de la derecha del no conexo, separados
        de los anteriores por un muro en x=3*/
        String cadena=null;
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        HashMap<Coord,Nodo> muros=map.getMuros();

        System.out.println("Comprobamos que los nodos están añadidos donde deben\n"+conectados.containsKey(new Coord(2,2))+"\n");
         for(int j=0;j<5;j++){
            cadena=new String();
            for(int i=0;i<5;i++){
                if(conectados.containsKey(new Coord(i,j)))
                        cadena+="C ";
                else if(noconectados.containsKey(new Coord(i,j)))
                        cadena+="N ";
                else if(muros.containsKey(new Coord(i,j)))
                        cadena+="X ";
                else cadena+="? ";
            }
            System.out.println(cadena);
        
        }
         System.out.println("\n\n"
                 +"Ahora, comprobaremos si los nodos tienen correctamente actualizadas sus variables\n\n");
        for(int j=0;j<5;j++){
            cadena=new String();
            for(int i=0;i<5;i++){
                if(conectados.containsKey(new Coord(i,j)))
                    cadena+=conectados.get(new Coord(i,j)).explored()+" ";
                else if(noconectados.containsKey(new Coord(i,j)))
                    cadena+=noconectados.get(new Coord(i,j)).explored()+" ";
                else if(muros.containsKey(new Coord(i,j)))
                    cadena+=muros.get(new Coord(i,j)).explored()+" ";
                else cadena+="? ";
            }
            System.out.println(cadena);
        
        }
        System.out.println("\n\n");
        /*Ahora, suponemos que el bot avanza dando un paso en dirección SUR,
        y añadimos la siguiente fila a la percepción del mapa:
        
            0   0   0   0   0
        
        De esta forma, todos los nodos del mapa deverían estar en conectado.
        */
        map.addNodo(new Nodo(0, 5, 0, 10));
        map.addNodo(new Nodo(1, 5, 0, 10));
        map.addNodo(new Nodo(2, 5, 0, 10));
        map.addNodo(new Nodo(3, 5, 0, 10));
        map.addNodo(new Nodo(4, 5, 0, 10));
        conectados=map.getConectado();
        noconectados=map.getNoConectado();
        System.out.println("Comprobamos que los nodos están añadidos donde deben2\n\n");
         for(int j=0;j<6;j++){
            cadena=new String();
            for(int i=0;i<5;i++){
               if(conectados.containsKey(new Coord(i,j)))
                        cadena+="C ";
                else if(noconectados.containsKey(new Coord(i,j)))
                        cadena+="N ";
                else if(muros.containsKey(new Coord(i,j)))
                        cadena+="X ";
                else cadena+="? ";
            }
            System.out.println(cadena);
        
        }
         System.out.println("\n\n"
                 +"Ahora, comprobaremos si los nodos tienen correctamente actualizadas sus variables2\n\n");
        for(int j=0;j<6;j++){
            cadena=new String();
            for(int i=0;i<5;i++){
                 if(conectados.containsKey(new Coord(i,j)))
                    cadena+=conectados.get(new Coord(i,j)).explored()+" ";
                else if(noconectados.containsKey(new Coord(i,j)))
                    cadena+=noconectados.get(new Coord(i,j)).explored()+" ";
                else if(muros.containsKey(new Coord(i,j)))
                    cadena+=muros.get(new Coord(i,j)).explored()+" ";
                else cadena+="? ";
            }
            System.out.println(cadena);
        
        }
        System.out.println("\n\n");

    }
    
    public static void main(String[] args) {        
        test2();

    }
    
}
