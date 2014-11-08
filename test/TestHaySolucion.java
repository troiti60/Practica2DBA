
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**Clase para el testeo de la función encargada de encontrar si hay solución
 *
 * @author antonio
 */
public class TestHaySolucion {
    public static boolean haySolucion(Mapa map,Coord bot, Coord Sol){
        return true;
    }
    
 /** Resolver el mapa simple (0=vacío, 1=muro, 2=solución, C=conectado, 
  * N=noconectado, B=bot):
  * 
  * 0C 0C 0C 0C 0C 1X 1X 1X 1X 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0B 0C 0C 1X 0N 2N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 1X 1X 1X 1X 
  * 
  * @author antonio
  */   
    public static void test0(){
        Mapa map = Mapa.crearInstancia();
        map.addNodo(new Nodo(2, 2, 0, 10));
        for (int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(!(i==2&&j==2))
                    map.addNodo(new Nodo(i,j,0,10));
            }
        }
        
        for(int i=5;i<10;i++){
            for(int j=0;j<5;j++){
                if(i==5||i==9||j==0||j==4)
                    map.addNodo(new Nodo(i, j, 1, 1));
                else if (i==7&&j==2)
                    map.addNodo(new Nodo(i,j,2,0));
                else map.addNodo(new Nodo(i,j,0,1));
                
            }        
        }
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        HashMap<Coord,Nodo> muros=map.getMuros();
        String cad = new String();
        for(int i=0;i<5;i++){
             for(int j=0;j<10;j++){
                 if(conectados.containsKey(new Coord(j,i)))
                     cad+=conectados.get(new Coord(j,i)).getRadar()+"C ";
                 else if(noconectados.containsKey(new Coord(j,i)))
                     cad+=noconectados.get(new Coord(j,i)).getRadar()+"N ";
                 else if (muros.containsKey(new Coord(j,i)))
                     cad+=muros.get(new Coord(j,i)).getRadar()+"X ";
                 else cad+="?? ";
             }
             System.out.println(cad);
             cad=new String();
        }
        if (haySolucion(map,new Coord(2,2),new Coord(7,2)))
            System.out.println("Sí hay solución, es decir, haySolución no funciona correctamente");
        else 
            System.out.println("No hay solución, es decir, haySolución funciona correctamente");
    
    }
 /** Resolver el mapa simple (0=vacío, 1=muro, 2=solución, C=conectado, 
  * N=noconectado, B=bot, ?? = desconocido):
  * 
  * 0C 0C 0C 0C 0C 1X 1X 1X 1X 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0C 0C 0C ?? 0N 2N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 1X 1X 1X 1X 
  * 
  * @author antonio
  */
        public static void test1(){
        Mapa map = Mapa.crearInstancia();
        map.addNodo(new Nodo(2, 2, 0, 10));
        for (int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(!(i==2&&j==2))
                    map.addNodo(new Nodo(i,j,0,10));
            }
        }
        
        for(int i=5;i<10;i++){
            for(int j=0;j<5;j++){
                if(!(i==5&&j==2)){
                if(i==5||i==9||j==0||j==4)
                    map.addNodo(new Nodo(i, j, 1, 1));
                else if (i==7&&j==2)
                    map.addNodo(new Nodo(i,j,2,0));
                else map.addNodo(new Nodo(i,j,0,1));
                } 
            }        
        }
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        HashMap<Coord,Nodo> muros=map.getMuros();
        String cad = new String();
        for(int i=0;i<5;i++){
             for(int j=0;j<10;j++){
                 if(conectados.containsKey(new Coord(j,i)))
                     cad+=conectados.get(new Coord(j,i)).getRadar()+"C ";
                 else if(noconectados.containsKey(new Coord(j,i)))
                     cad+=noconectados.get(new Coord(j,i)).getRadar()+"N ";
                 else if (muros.containsKey(new Coord(j,i)))
                     cad+=muros.get(new Coord(j,i)).getRadar()+"X ";
                 else cad+="?? ";
             }
             System.out.println(cad);
             cad=new String();
        }
        if (haySolucion(map,new Coord(2,2),new Coord(7,2)))
            System.out.println("Sí hay solución, es decir, haySolución no funciona correctamente");
        else 
            System.out.println("No hay solución, es decir, haySolución funciona correctamente");
    
    }
    /** Resolver el mapa simple (0=vacío, 1=muro, 2=solución, C=conectado, 
  * N=noconectado, B=bot, ??= desconocido):
  * 
  * 0C 0C 0C 0C 0C 1X 1X 1X 1X 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0B 0C 0C 1X 0N 2N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 0N 0N 0N 1X 
  * 0C 0C 0C 0C 0C 1X 1X ?? 1X 1X 
  * 
  * @author antonio
  */     
        public static void test2(){
        Mapa map = Mapa.crearInstancia();
        map.addNodo(new Nodo(2, 2, 0, 10));
        for (int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(!(i==2&&j==2))
                    map.addNodo(new Nodo(i,j,0,10));
            }
        }
        
        for(int i=5;i<10;i++){
            for(int j=0;j<5;j++){
                if(!(i==7&&j==4)){
                if(i==5||i==9||j==0||j==4)
                    map.addNodo(new Nodo(i, j, 1, 1));
                else if (i==7&&j==2)
                    map.addNodo(new Nodo(i,j,2,0));
                else map.addNodo(new Nodo(i,j,0,1));
                } 
            }        
        }
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        HashMap<Coord,Nodo> muros=map.getMuros();
        String cad = new String();
        for(int i=0;i<5;i++){
             for(int j=0;j<10;j++){
                 if(conectados.containsKey(new Coord(j,i)))
                     cad+=conectados.get(new Coord(j,i)).getRadar()+"C ";
                 else if(noconectados.containsKey(new Coord(j,i)))
                     cad+=noconectados.get(new Coord(j,i)).getRadar()+"N ";
                 else if (muros.containsKey(new Coord(j,i)))
                     cad+=muros.get(new Coord(j,i)).getRadar()+"X ";
                 else cad+="?? ";
             }
             System.out.println(cad);
             cad=new String();
        }
        if (haySolucion(map,new Coord(2,2),new Coord(7,2)))
            System.out.println("Sí hay solución, es decir, haySolución no funciona correctamente");
        else 
            System.out.println("No hay solución, es decir, haySolución funciona correctamente");
    
    }
/** Resolver el mapa simple (0=vacío, 1=muro, 2=solución, C=conectado, 
  * N=noconectado, B=bot, ??=desconocido):
  * 
  * 0C 0C 0C 0C 0C 
  * 0C 0C 0C 0C 0C 
  * 0C 0C 0B 0C 0C
  * 0C 0C 0C 0C 0C
  * 0C 0C 0C 0C 0C 
  * x==100&&y==100
  * 1X 1X 1X 1X 1X 
  * 1X 0N 0N 0N 1X 
  * 1X 0N 2N 0N ?? 
  * 1X 0N 0N 0N 1X 
  * 1X 1X 1X 1X 1X
  * 
* @author antonio
  */   
        public static void test3(){
        Mapa map = Mapa.crearInstancia();
        map.addNodo(new Nodo(2, 2, 0, 10));
        for (int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(!(i==2&&j==2))
                    map.addNodo(new Nodo(i,j,0,10));
            }
        }
        
        for(int i=100;i<105;i++){
            for(int j=100;j<105;j++){
                if(!(i==104&&j==102)){
                if(i==100||i==104||j==100||j==104)
                    map.addNodo(new Nodo(i, j, 1, 1));
                else if (i==102&&j==102)
                    map.addNodo(new Nodo(i,j,2,0));
                else map.addNodo(new Nodo(i,j,0,1));
                } 
            }        
        }
        HashMap<Coord,Nodo> conectados=map.getConectado();
        HashMap<Coord,Nodo> noconectados=map.getNoConectado();
        HashMap<Coord,Nodo> muros=map.getMuros();
        String cad = new String();
        for(int i=0;i<5;i++){
             for(int j=0;j<5;j++){
                 if(conectados.containsKey(new Coord(j,i)))
                     cad+=conectados.get(new Coord(j,i)).getRadar()+"C ";
                 else if(noconectados.containsKey(new Coord(j,i)))
                     cad+=noconectados.get(new Coord(j,i)).getRadar()+"N ";
                 else if (muros.containsKey(new Coord(j,i)))
                     cad+=muros.get(new Coord(j,i)).getRadar()+"X ";
                 else cad+="?? ";
             }
             System.out.println(cad);
             cad=new String();
        }
        
        System.out.println("x==100&&y==100");
        for(int i=100;i<105;i++){
             for(int j=100;j<105;j++){
                 if(conectados.containsKey(new Coord(j,i)))
                     cad+=conectados.get(new Coord(j,i)).getRadar()+"C ";
                 else if(noconectados.containsKey(new Coord(j,i)))
                     cad+=noconectados.get(new Coord(j,i)).getRadar()+"N ";
                 else if (muros.containsKey(new Coord(j,i)))
                     cad+=muros.get(new Coord(j,i)).getRadar()+"X ";
                 else cad+="?? ";
             }
             System.out.println(cad);
             cad=new String();
        }
        if (haySolucion(map,new Coord(2,2),new Coord(7,2)))
            System.out.println("Sí hay solución, es decir, haySolución no funciona correctamente");
        else 
            System.out.println("No hay solución, es decir, haySolución funciona correctamente");
    
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        test1();
    }
    
}
