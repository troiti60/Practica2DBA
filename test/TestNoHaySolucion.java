
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rod
 */
public class TestNoHaySolucion {
    private Mapa miMapa = Mapa.crearInstancia();
    private int numMap;
    
    public TestNoHaySolucion(int mapa){
        
        switch(mapa){
            case 0:
                // Alcanzable 2,2
                /*
                    x   x   x   x   x
                    x   0   0   0   x
                    x   0   ?   0   x
                    x   x   x   x   x
                */
                miMapa.addNodo(new Nodo(0, 0, 1, 10));
                miMapa.addNodo(new Nodo(1, 0, 1, 10));
                miMapa.addNodo(new Nodo(2, 0, 1, 10));
                miMapa.addNodo(new Nodo(3, 0, 1, 10));

                miMapa.addNodo(new Nodo(0, 1, 1, 10));
                miMapa.addNodo(new Nodo(1, 1, 0, 10));
                miMapa.addNodo(new Nodo(2, 1, 0, 10));
                miMapa.addNodo(new Nodo(3, 1, 1, 10));

                miMapa.addNodo(new Nodo(0, 2, 1, 10));
                miMapa.addNodo(new Nodo(1, 2, 0, 10));
                miMapa.addNodo(new Nodo(2, 2, 0, 10));
                miMapa.addNodo(new Nodo(3, 2, 1, 10));

                miMapa.addNodo(new Nodo(0, 3, 1, 10));
                miMapa.addNodo(new Nodo(1, 3, 0, 10));
                miMapa.addNodo(new Nodo(2, 3, 0, 10));
                miMapa.addNodo(new Nodo(3, 3, 1, 10));

                miMapa.addNodo(new Nodo(0, 4, 1, 10));
                miMapa.addNodo(new Nodo(1, 4, 1, 10));
                miMapa.addNodo(new Nodo(2, 4, 1, 10));
                miMapa.addNodo(new Nodo(3, 4, 1, 10));
                break;
            case 1:
                // No alcazable 5,5
                /*
                    x   x   x   x   x   x   x
                    x   0   0   0   x   0   x
                    x   0   0   0   x   0   x
                    x   0   0   0   x   0   x
                    x   x   x   x   x   0   x
                    x   0   0   0   0   D   x
                    x   x   x   x   x   x   x
                */
                miMapa.addNodo(new Nodo(0, 0, 1, 10));
                miMapa.addNodo(new Nodo(1, 0, 1, 10));
                miMapa.addNodo(new Nodo(2, 0, 1, 10));
                miMapa.addNodo(new Nodo(3, 0, 1, 10));
                miMapa.addNodo(new Nodo(4, 0, 1, 10));
                miMapa.addNodo(new Nodo(5, 0, 1, 10));
                miMapa.addNodo(new Nodo(6, 0, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 1, 1, 10));
                miMapa.addNodo(new Nodo(1, 1, 0, 10));
                miMapa.addNodo(new Nodo(2, 1, 0, 10));
                miMapa.addNodo(new Nodo(3, 1, 0, 10));
                miMapa.addNodo(new Nodo(4, 1, 1, 10));
                miMapa.addNodo(new Nodo(5, 1, 0, 10));
                miMapa.addNodo(new Nodo(6, 1, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 2, 1, 10));
                miMapa.addNodo(new Nodo(1, 2, 0, 10));
                miMapa.addNodo(new Nodo(2, 2, 0, 10));
                miMapa.addNodo(new Nodo(3, 2, 0, 10));
                miMapa.addNodo(new Nodo(4, 2, 1, 10));
                miMapa.addNodo(new Nodo(5, 2, 0, 10));
                miMapa.addNodo(new Nodo(6, 2, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 3, 1, 10));
                miMapa.addNodo(new Nodo(1, 3, 0, 10));
                miMapa.addNodo(new Nodo(2, 3, 0, 10));
                miMapa.addNodo(new Nodo(3, 3, 0, 10));
                miMapa.addNodo(new Nodo(4, 3, 1, 10));
                miMapa.addNodo(new Nodo(5, 3, 0, 10));
                miMapa.addNodo(new Nodo(6, 3, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 4, 1, 10));
                miMapa.addNodo(new Nodo(1, 4, 1, 10));
                miMapa.addNodo(new Nodo(2, 4, 1, 10));
                miMapa.addNodo(new Nodo(3, 4, 1, 10));
                miMapa.addNodo(new Nodo(4, 4, 1, 10));
                miMapa.addNodo(new Nodo(5, 4, 0, 10));
                miMapa.addNodo(new Nodo(6, 4, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 5, 1, 10));
                miMapa.addNodo(new Nodo(1, 5, 0, 10));
                miMapa.addNodo(new Nodo(2, 5, 0, 10));
                miMapa.addNodo(new Nodo(3, 5, 0, 10));
                miMapa.addNodo(new Nodo(4, 5, 0, 10));
                miMapa.addNodo(new Nodo(5, 5, 2, 10));
                miMapa.addNodo(new Nodo(6, 5, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 6, 1, 10));
                miMapa.addNodo(new Nodo(1, 6, 1, 10));
                miMapa.addNodo(new Nodo(2, 6, 1, 10));
                miMapa.addNodo(new Nodo(3, 6, 1, 10));
                miMapa.addNodo(new Nodo(4, 6, 1, 10));
                miMapa.addNodo(new Nodo(5, 6, 1, 10));
                miMapa.addNodo(new Nodo(6, 6, 1, 10));
                break;
            case 2:
                /*
                    x   x   x   x   x   x   x   x
                    x   ?   ?   x   ?   ?   ?   x
                    x   ?   ?   x   ?   D   ?   x
                    x   ?   ?   x   ?   ?   ?   x
                    x   x   x   x   x   x   x   x
                */
                
      
                
                
                break;
            case 3:
                // No alcanzable
                /*
                    x   x   x   x   x
                    x   0   x   D   x
                    x   0   x   x   x
                    x   0   0   0   x
                    x   x   x   x   x
                */

                // Primera fila
                miMapa.addNodo(new Nodo(0, 0, 1, 10));
                miMapa.addNodo(new Nodo(1, 0, 1, 10));
                miMapa.addNodo(new Nodo(2, 0, 1, 10));
                miMapa.addNodo(new Nodo(3, 0, 1, 10));
                miMapa.addNodo(new Nodo(4, 0, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 1, 1, 10));
                miMapa.addNodo(new Nodo(1, 1, 0, 10));
                miMapa.addNodo(new Nodo(2, 1, 1, 10));
                miMapa.addNodo(new Nodo(3, 1, 2, 10));
                miMapa.addNodo(new Nodo(4, 1, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 2, 1, 10));
                miMapa.addNodo(new Nodo(1, 2, 0, 10));
                miMapa.addNodo(new Nodo(2, 2, 1, 10));
                miMapa.addNodo(new Nodo(3, 2, 1, 10));
                miMapa.addNodo(new Nodo(4, 2, 1, 10));
                
                miMapa.addNodo(new Nodo(0, 3, 1, 10));
                miMapa.addNodo(new Nodo(1, 3, 0, 10));
                miMapa.addNodo(new Nodo(2, 3, 0, 10));
                miMapa.addNodo(new Nodo(3, 3, 0, 10));
                miMapa.addNodo(new Nodo(4, 3, 1, 10));

                // ultima fila
                miMapa.addNodo(new Nodo(0, 4, 1, 10));
                miMapa.addNodo(new Nodo(1, 4, 1, 10));
                miMapa.addNodo(new Nodo(2, 4, 1, 10));
                miMapa.addNodo(new Nodo(3, 4, 1, 10));
                miMapa.addNodo(new Nodo(4, 4, 1, 10));
                
                
                break;
        }
    }
    
    /*
        Mi solucion, determina si el destino está rodeado por muros
        LLama a está inscrito para ver si está fuera o dentro
    */
    public boolean esAlcanzable(Coord origen, Coord destino){
        
        HashMap<Coord,Nodo> cerco = new HashMap<>();
        HashMap<Coord,Nodo> muros=miMapa.getMuros();
        HashMap<Coord,Nodo> conectados=miMapa.getConectado();
        boolean solucion = true, solucionR1 = false, solucionR2 = false, noEsMuro;
        Coord miOrigen = origen;
        Coord miDestino = destino;
        Nodo norte, norteAux;
        Nodo sur, surAux;
        boolean expandida1,expandida2;
        int turno = 0;
        
        if(!muros.isEmpty() && !conectados.isEmpty()){
            sur = norte = conectados.get(miDestino);
            System.out.println("Destino: (" + norte.getX() + "," + norte.getY() + ")");
            
            // Encontrar el Nodo muro más al norte del destino   
            noEsMuro = true;
            while(noEsMuro){
                //System.out.println("N comprobando(" + norte.getX() + "," + norte.getY() + ")");
                if(muros.containsKey(norte.getCoord())){
                    noEsMuro = false;
                    //System.out.println("Norte encontrado");
                }
                else
                    norte = new Nodo(norte.getX(), norte.getY()-1, norte.getRadar(), norte.getScanner());
            }
            
            // Encontrar el Nodo muro más al sur del destino
            noEsMuro = true;
            while(noEsMuro){
                //System.out.println("S comprobando(" + sur.getX() + "," + sur.getY() + ")");
                if(muros.containsKey(sur.getCoord())){
                    noEsMuro = false;
                    //System.out.println("Sur encontrado");
                }
                else                
                    sur = new Nodo(sur.getX(), sur.getY()+1, sur.getRadar(), sur.getScanner());
            }

            System.out.println("El muro más al norte encontrado es: (" + norte.getX() + "," + norte.getY() + ")");
            System.out.println("El muro más al sur encontrado es: (" + sur.getX() + "," + sur.getY() + ")");
            
            cerco.put(norte.getCoord(), norte);
            cerco.put(sur.getCoord(), sur);
            
            // ------------
            // -- RAMA E --
            // ------------
            norteAux = norte;
            surAux = sur;
            turno = 0;
            // Hasta chocar o no poder expandir en ninguno
            do{
                // si expandida anterior es false y la de este turno también lo es, ninguno se ha expandido
                // ------------------
                expandida1 = expandida2 = false;
                
                if(turno%2==0){
                    System.out.println("Turno rama 1");
                    // Me desplazo con norteAux al sur (o Este)
                    if(muros.containsKey(new Coord(norteAux.getX(), norteAux.getY()+1))){
                        norteAux = new Nodo(norteAux.getX(), norteAux.getY()+1, norteAux.getRadar(), norteAux.getScanner());
                        expandida1 = true;
                        System.out.println("R1 -> Sur");
                        cerco.put(norteAux.getCoord(), norteAux);
                    }
                    else{
                        if(muros.containsKey(new Coord(norteAux.getX()+1, norteAux.getY()))){
                            norteAux = new Nodo(norteAux.getX()+1, norteAux.getY(), norteAux.getRadar(), norteAux.getScanner());
                            expandida1 = true;
                            System.out.println("R1 -> Este");
                            cerco.put(norteAux.getCoord(), norteAux);
                        }
                    }
                    
                    turno+=1;
                }
                else{
                    System.out.println("Turno rama 2");
                    // Me desplazo con surAux al Norte (o Este)
                    if(muros.containsKey(new Coord(surAux.getX(), surAux.getY()-1))){
                        surAux = new Nodo(surAux.getX(), surAux.getY()-1, surAux.getRadar(), surAux.getScanner());
                        expandida2 = true;
                        System.out.println("R2 -> Norte");
                        cerco.put(surAux.getCoord(), surAux);
                    }
                    else{
                        if(muros.containsKey(new Coord(surAux.getX()+1, surAux.getY()))){
                            surAux = new Nodo(surAux.getX()+1, surAux.getY(), surAux.getRadar(), surAux.getScanner());
                            expandida2 = true;
                            System.out.println("R2 -> Este");
                            cerco.put(surAux.getCoord(), surAux);
                        }
                    }
                    turno = 0;
                }
                
                System.out.println("norteAux: (" + norteAux.getX() + "," + norteAux.getY() + ")");
                System.out.println("surAux: (" + surAux.getX() + "," + surAux.getY() + ")");
                System.out.println("------------");
            
                // Si son iguales, está parcialmente cerrado
                if( norteAux.getX() == surAux.getX() && norteAux.getY() == surAux.getY() ){
                    solucionR1 = true;
                    System.out.println("Solucion r1");
                }
                
            }while((expandida1 != false || expandida2 != false) && !solucionR1);
            
            if(solucionR1)
                System.out.println("\n------\nLa parte Este tiene solucion\n-----");
            else
                System.out.println("\n------\nLa parte Este NO tiene solucion\n-----");
            
            
            // ------------
            // -- RAMA W --
            // ------------
            if(solucionR1){
                norteAux = norte;
                surAux = sur;
                turno = 0;
                // Hasta chocar o no poder expandir en ninguno
                do{
                    expandida1 = expandida2 = false;

                    if(turno%2==0){
                        System.out.println("Turno rama 1");
                        // Me desplazo con norteAux al Sur (o Oeste)
                        if(muros.containsKey(new Coord(norteAux.getX(), norteAux.getY()+1))){
                            norteAux = new Nodo(norteAux.getX(), norteAux.getY()+1, norteAux.getRadar(), norteAux.getScanner());
                            expandida1 = true;
                            System.out.println("R1 -> Sur");
                            cerco.put(norteAux.getCoord(), norteAux);
                        }
                        else{
                            if(muros.containsKey(new Coord(norteAux.getX()-1, norteAux.getY()))){
                                norteAux = new Nodo(norteAux.getX()-1, norteAux.getY(), norteAux.getRadar(), norteAux.getScanner());
                                expandida1 = true;
                                System.out.println("R1 -> Oeste");
                                cerco.put(norteAux.getCoord(), norteAux);
                            }
                        }

                        turno+=1;
                    }
                    else{
                        System.out.println("Turno rama 2");
                        // surAux al Norte (o Oeste)
                        if(muros.containsKey(new Coord(surAux.getX(), surAux.getY()-1))){
                            surAux = new Nodo(surAux.getX(), surAux.getY()-1, surAux.getRadar(), surAux.getScanner());
                            expandida2 = true;
                            System.out.println("R2 -> Norte");
                            cerco.put(surAux.getCoord(), surAux);
                        }
                        else{
                            if(muros.containsKey(new Coord(surAux.getX()-1, surAux.getY()))){
                                surAux = new Nodo(surAux.getX()-1, surAux.getY(), surAux.getRadar(), surAux.getScanner());
                                expandida2 = true;
                                System.out.println("R2 -> Oeste");
                                cerco.put(surAux.getCoord(), surAux);
                            }
                        }
                        turno = 0;
                    }

                    System.out.println("norteAux: (" + norteAux.getX() + "," + norteAux.getY() + ")");
                    System.out.println("surAux: (" + surAux.getX() + "," + surAux.getY() + ")");
                    System.out.println("------------");

                    // Si son iguales, está parcialmente cerrado
                    if( norteAux.getX() == surAux.getX() && norteAux.getY() == surAux.getY() ){
                        solucionR2 = true;
                        System.out.println("Solucion r2");
                    }

                }while((expandida1 != false || expandida2 != false) && !solucionR2);  
                
                if(solucionR2)
                  System.out.println("\n------\nLa parte Oeste tiene solucion\n-----");
                else
                    System.out.println("\n------\nLa parte Oeste NO tiene solucion\n-----");
                
            } // Fin rama W
            
            // Si está cerrado por los dos lados
            if(solucionR1 && solucionR2){
                Iterator it = cerco.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry)it.next();
                    System.out.println("(" + cerco.get(e.getKey()).getX() + "," + cerco.get(e.getKey()).getY() + ")" );
                }
                // Si el origen está fuera del cerco
                    // solucion = false;
            }    
        }
        else
            System.out.println("El mapa está vacio :(");
            
        return solucion;
    }
    
    public boolean estaInscrito(HashMap<Coord, Nodo> muros, Nodo origen, Nodo destino){
        return true;
    }
    
    /**
     * Dada una coordenada devuelve las coordenadas de la dirección deseada
     * 
     * @author Fco Javier Ortega Rodríguez
     * @param direccion  0=NW    1=N     2=NE
     *                  3=W      X      4=E
     *                  5=SW    6=S     7=SE
     * @param coordenada Coordenada donde situarse
     * @return Coordenada de la direccion
     */
    public Coord getAdyacente(Coord coordenada, int direccion){
        Coord miCoordenada = new Coord(-1, -1);
        
        switch(direccion){
            case 0:
                System.out.print("NW ");
                miCoordenada = coordenada.NO();
                break;
            case 1:
                System.out.print("N ");
                miCoordenada = coordenada.N();
                break;
            case 2:
                System.out.print("NE ");
                miCoordenada = coordenada.NE();
                break;
            case 3:
                System.out.print("W ");
                miCoordenada = coordenada.O();
                break;
            case 4:
                System.out.print("E ");
                miCoordenada = coordenada.E();
                break;
            case 5:
                System.out.print("SW ");
                miCoordenada = coordenada.SE();
                break;
            case 6:
                System.out.print("S ");
                miCoordenada = coordenada.S();
                break;
            case 7:
                System.out.print("SE ");
                miCoordenada = coordenada.SE();
                break;
        }
        
        return miCoordenada;
    }
    
    /**
     * Metodo que explora el mapa para determinar si puede existir solución o ésta es inalcanzable
     * @author Jco Javier Ortega Rodríguez
     * @param destino Coordenadas de destino
     * @return false si es imposible llegar al destino, true cualquier otro caso
     */
    public boolean puedeExistirSolucion(Coord destino){
        ArrayList<Coord> abiertos = new ArrayList<>();
        ArrayList<Coord> cerrados = new ArrayList<>();
        Coord aux, actual;
        boolean solucion = false;
        
        cerrados.add(destino);
        
        System.out.println("Destino: (" + destino.getX() + "," + destino.getY() + ")");
        System.out.print("Hijos generados: ");
        
        for(int i=0; i<8; i++){
            aux = getAdyacente(destino, i);
            abiertos.add( aux );
            System.out.print("(" + aux.getX() + "," + aux.getY() + ") ");
        }
        
        System.out.print("\n");
        
        while(abiertos.size()>0){
            aux = abiertos.get(0);
            cerrados.add(aux);
            abiertos.remove(0);
            
            System.out.println("\nExpandiendo: (" + aux.getX() + "," + aux.getY() + ")");
            
            if( !miMapa.getMuros().containsKey(aux)){
                for(int i=0; i<8; i++){
                    actual = getAdyacente(aux, i);
                    System.out.println("Actual (" + actual.getX() + "," + actual.getY() + ")");

                    if(miMapa.getConectado().containsKey( actual )){
                        System.out.println("Solución alcanzable");
                        return true;
                    }
                    else{
                        System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") no estaba conectada");

                        if(!cerrados.contains( actual )){
                                System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") estaba sin explorar");

                                if(!miMapa.getMuros().containsKey( actual )){
                                    if(actual.getX()>=0 && actual.getY()>=0){
                                        System.out.println("añadia a abiertos");
                                        abiertos.add( actual );
                                    }else{
                                        System.out.println("fuera del mapa");
                                    }
                                }
                                else
                                    System.out.println("era muro, no añado");
                        }
                    }
                }
            }
            else
                System.out.println("Es un muro, así que no expando");
        }   
        return solucion;
    }
    
    public static void main(String[] args) {
        TestNoHaySolucion test = new TestNoHaySolucion(0);
        
        System.out.println("\n------------\nMapa 0 con solución\n---------");
        if( test.puedeExistirSolucion(new Coord(2,2)))
            System.out.println("----->Podria ser alzable");
        else
            System.out.println("----->Imposible");
        
        
        System.out.println("\n------------\nMapa 1 con solucion en 2,2\n---------");
        TestNoHaySolucion test1 = new TestNoHaySolucion(1);
        
        if( test1.puedeExistirSolucion(new Coord(2,2)))
            System.out.println("----->Podria ser alzable");
        else
            System.out.println("----->Imposible");
        
        System.out.println("\n------------\nMapa 1 sin solucion en 5,5\n---------");
        if( test1.puedeExistirSolucion(new Coord(5,5)))
            System.out.println("----->Podria ser alzable");
        else
            System.out.println("----->Imposible");
        
        
        
        System.out.println("\n------------\nMapa 3 sin solución\n---------");
        TestNoHaySolucion test3 = new TestNoHaySolucion(3);
        
        if( test3.puedeExistirSolucion(new Coord(3,1)))
            System.out.println("----->Podria ser alzable");
        else
            System.out.println("----->Imposible");
    }
}
