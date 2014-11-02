/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author antonio
 */
public class TestMapa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        HashMap<Integer,Nodo> conectados=map.getConectado();
        HashMap<Integer,Nodo> noconectados=map.getNoConectado();
       /* for(int i=0;i<4;i++){
            cadena=new String();
            for(int j=0;j<3;j++){
                cadena+=conectados.get(((i*1000)+j)).getConectado()+" ";
            }
            cadena+="X "+noconectados.get(((i*1000)+4)).getConectado();
            System.out.println(cadena);
        
        }*/
        System.out.println("Comprobamos que los nodos están añadidos donde deben\n\n");
         for(int i=0;i<5;i++){
            cadena=new String();
            for(int j=0;j<3;j++){
                cadena+=conectados.containsKey(((j*1000)+i))+" ";
            }
            cadena+=(conectados.containsKey(((3*1000)+i))||noconectados.containsKey(((3*1000)+i)))+" "+noconectados.containsKey(((4*1000)+i));
            System.out.println(cadena);
        
        }
         System.out.println("\n\n"
                 +"Ahora, comprobaremos si los nodos tienen correctamente actualizadas sus variables\n\n");
        for(int i=0;i<5;i++){
            cadena=new String();
            for(int j=0;j<3;j++){
                cadena+=conectados.get(((j*1000)+i)).getConectado()+" ";
            }
            cadena+="X "+noconectados.get(((4*1000)+i)).getConectado();
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
         for(int i=0;i<6;i++){
            cadena=new String();
            for(int j=0;j<3;j++){
                cadena+=conectados.containsKey(((j*1000)+i))+" ";
            }
            cadena+=(conectados.containsKey(((3*1000)+i))||noconectados.containsKey(((3*1000)+i)))+" "+conectados.containsKey(((4*1000)+i));
            System.out.println(cadena);
        
        }
         System.out.println("\n\n"
                 +"Ahora, comprobaremos si los nodos tienen correctamente actualizadas sus variables2\n\n");
        for(int i=0;i<6;i++){
            cadena=new String();
            for(int j=0;j<3;j++){
                cadena+=conectados.get(((j*1000)+i)).getConectado()+" ";
            }
            if((!conectados.containsKey(((3*1000)+i))&&!noconectados.containsKey(((3*1000)+i))))
            cadena+="X "+conectados.get(((4*1000)+i)).getConectado();
            else cadena+=conectados.get((((3*1000)+i))).getConectado()+" "+conectados.get(((4*1000)+i)).getConectado();
            
            System.out.println(cadena);
        
        }
        System.out.println("\n\n");
    }
    
}
