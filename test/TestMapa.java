/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;

/**Clase de testeo para la clase mapa (y nodo)
 *
 * @author antonio
 */
public class TestMapa {
    public static int testea = 3;
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
        map.addNodo(new Nodo(2, 2, 0, 0));
        
        map.addNodo(new Nodo(0, 0, 0, 1));
        map.addNodo(new Nodo(1, 0, 0, 2));
        map.addNodo(new Nodo(2, 0, 0, 3));
        map.addNodo(new Nodo(3, 0, 1, 4));
        map.addNodo(new Nodo(4, 0, 0, 5));
        map.addNodo(new Nodo(0, 1, 0, 6));
        map.addNodo(new Nodo(1, 1, 0, 7));
        map.addNodo(new Nodo(2, 1, 0, 8));
        map.addNodo(new Nodo(3, 1, 1, 9));
        map.addNodo(new Nodo(4, 1, 0, 10));
        map.addNodo(new Nodo(0, 2, 0, 11));
        map.addNodo(new Nodo(1, 2, 0, 12));
        map.addNodo(new Nodo(3, 2, 1, 14));
        map.addNodo(new Nodo(4, 2, 0, 15));
        map.addNodo(new Nodo(0, 3, 0, 16));
        map.addNodo(new Nodo(1, 3, 0, 17));
        map.addNodo(new Nodo(2, 3, 0, 18));
        map.addNodo(new Nodo(3, 3, 1, 19));
        map.addNodo(new Nodo(4, 3, 0, 20));
        map.addNodo(new Nodo(0, 4, 0, 21));
        map.addNodo(new Nodo(1, 4, 0, 22));
        map.addNodo(new Nodo(2, 4, 0, 23));
        map.addNodo(new Nodo(3, 4, 1, 24));
        map.addNodo(new Nodo(4, 4, 0, 25));
        
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
        map.addNodo(new Nodo(0, 5, 0, 26));
        map.addNodo(new Nodo(1, 5, 0, 27));
        map.addNodo(new Nodo(2, 5, 0, 28));
        map.addNodo(new Nodo(3, 5, 0, 29));
        map.addNodo(new Nodo(4, 5, 0, 30));
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
        map.setCoord(new Coord(2,3));
    }
    /**
     * Método test para probar el método de mapa getLastPerception.
     * Se introduce elsiguiente mapa:
     * X X X X X
     * X X X X X
     * X X B 0 0
     * X X 0 0 0
     * X X 0 0 0
     */
    public static void test3(){
        Mapa map = Mapa.crearInstancia();
        map.addNodo(new Nodo(0,0,0,13));
        map.addNodo(new Nodo(-2, -2, 1, 1));
        map.addNodo(new Nodo(-1, -2, 1, 2));
        map.addNodo(new Nodo(0, -2, 1, 3));
        map.addNodo(new Nodo(1,-2, 1, 4));
        map.addNodo(new Nodo(2, -2, 1, 5));
        map.addNodo(new Nodo(-2, -1, 1, 6));
        map.addNodo(new Nodo(-1, -1, 1, 7));
        map.addNodo(new Nodo(0, -1, 1, 8));
        map.addNodo(new Nodo(1, -1, 1, 9));
        map.addNodo(new Nodo(2, -1, 1, 10));
        map.addNodo(new Nodo(-2, 0, 1, 11));
        map.addNodo(new Nodo(-1, 0, 1, 12));
        map.addNodo(new Nodo(1, 0, 0, 14));
        map.addNodo(new Nodo(2, 0, 0, 15));
        map.addNodo(new Nodo(-2, 1, 1, 16));
        map.addNodo(new Nodo(-1, 1, 1, 17));
        map.addNodo(new Nodo(0, 1, 0, 18));
        map.addNodo(new Nodo(1, 1, 0, 19));
        map.addNodo(new Nodo(2, 1, 0, 20));
        map.addNodo(new Nodo(-2, 2, 1, 21));
        map.addNodo(new Nodo(-1, 2, 1, 22));
        map.addNodo(new Nodo(0, 2, 0, 23));
        map.addNodo(new Nodo(1, 2, 0, 24));
        map.addNodo(new Nodo(2, 2, 0, 25));
        map.setCoord(new Coord(0,0));
        ArrayList<Nodo> perception = map.getLastPerception();
        String cad= new String();
        for(int i=0;i<25;i++){
            cad+= perception.get(i).getScanner()+" ";
            if(i%5==4){
                System.out.println(cad);
                cad=new String();
            }
        }
    
    }
    
    public static void main(String[] args) { 
        if (testea==0)
            test0();
        else if (testea==1)
            test1();
        else if (testea==2)
            test2();
        else if (testea==3)
            test3();
 

    }
    
}
