
import edu.emory.mathcs.backport.java.util.Collections;
import es.upv.dsic.gti_ia.core.AgentID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alexander
 */
public class TestBusqueda {
    
    private static enum direccion { NO, N, NE, E, SE, S, SO, O }
    
    public static void main(String[] args) throws Exception {
        // Crear mapa y bot
        Mapa mapa = Mapa.crearInstancia();

        // Leer y crear grafo
        try {            
            // Abrir fichero del grafo
            BufferedReader inGrafo = new BufferedReader(new FileReader("testgrafos/grafo.txt"));
            String fila;
            String letra;
            
            int x, y = 0, i = 0;
            float distancia = 0.0f;
            
            // Leer coordenadas del destino
            String destCoords = inGrafo.readLine();
            int destX, destY;
            destX = Integer.parseInt(destCoords.substring(0, destCoords.indexOf(",")));
            destY = Integer.parseInt(destCoords.substring(destCoords.indexOf(",") + 1));
            Coord destino = new Coord(destX, destY);
            
            // Leer grafo
            while ((fila = inGrafo.readLine()) != null) {
                for (x = 0; x < fila.length(); x++) {
                    // Leer próxima letra
                    letra = fila.substring(x, x + 1);
                    
                    Coord coord = new Coord(x, y);
                    distancia = (float)coord.distanciaA(destino);
                    
                    // Crear nodo
                    if ("0".equals(letra) || "3".equals(letra)) mapa.addNodo(new Nodo(coord, 0, distancia));
                    if ("1".equals(letra)) mapa.addNodo(new Nodo(coord, 1, distancia));
                    if ("2".equals(letra)) mapa.addNodo(new Nodo(coord, 2, distancia));
                    
                    // Guardar posición del bot
                    if ("3".equals(letra)) mapa.setCoord(coord);
                }
                
                y++;
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
        
        boolean llegado = false;
        int pasos = 0;
        while (!llegado) {
            // Búsqueda
            direccion dir = busqueda();
            
            // Movimiento
            if (dir == direccion.NO) mapa.setCoord(mapa.getCoord().NO());
            if (dir == direccion.N) mapa.setCoord(mapa.getCoord().N());
            if (dir == direccion.NE) mapa.setCoord(mapa.getCoord().NE());
            if (dir == direccion.E) mapa.setCoord(mapa.getCoord().E());
            if (dir == direccion.SE) mapa.setCoord(mapa.getCoord().SE());
            if (dir == direccion.S) mapa.setCoord(mapa.getCoord().S());
            if (dir == direccion.SO) mapa.setCoord(mapa.getCoord().SO());
            if (dir == direccion.O) mapa.setCoord(mapa.getCoord().O());
            
            pasos++;
            if (mapa.getConectado().get(mapa.getCoord()).getRadar() == 2) llegado = true;
        }
        
        System.out.println("Llegado al destino (" + mapa.getCoord().getX() + 
                "," + mapa.getCoord().getY() + ") en " + pasos + " pasos.");
    }
    
    /**
     * La búsqueda para encontrar el mejor (con la información
     * que ya tenemos) camino
     * 
     * @return La dirección en que el bot debe moverse
     * @author Alexander Straub
     */
    private static direccion busqueda() {
        // Recoger el mapa
        Mapa mapa = Mapa.crearInstancia();
        HashMap<Coord, Nodo> map = mapa.getConectado();
        
        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext(); )
            i.next().resetBusqueda();
        Nodo nodoInicial = map.get(mapa.getCoord());
        nodoInicial.setDistancia(0.0);
        
        // Trata como el nodo destino
        double distancia = Double.MAX_VALUE;
        Nodo camino = null;
        Nodo destino = null;
        
        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo)Collections.min(nodos);
            nodos.remove(minNodo);
            
            if (minNodo.getRadar() == 2) destino = minNodo;
            
            // Recoger los vecinos del nodo actual
            for (Iterator<Nodo> i = minNodo.getAdy().iterator(); i.hasNext(); ) {
                Nodo vecino = i.next();
                
                // Si aún está en la lista
                if (nodos.contains(vecino)) {
                    // Calcular distancia alternativa
                    double alternativa = minNodo.getDistancia() 
                            + vecino.distanciaA(minNodo);
                    
                    // Si es mejor, usar la distancia alternativa con el
                    // camino correspondiente
                    if (alternativa < vecino.getDistancia()) {
                        vecino.setDistancia(alternativa);
                        vecino.setCamino(minNodo);
                    }
                }
            }
            
            // Si el entorno aún no está descubierto completamente
            if (!minNodo.explored()) {
                // También calcular distancia alternativa al destino,
                // como si no hubiera obstáculos desde ahí
                double alternativa = minNodo.getDistancia() 
                        + minNodo.getScanner();
                
                // Si es mejor: actualizar
                if (alternativa < distancia) {
                    distancia = alternativa;
                    camino = minNodo;
                }
            }
        }
        
        // Empezando con el nodo del destino ir atrás hasta haber 
        // encontrado el vecino de la posición del robot
        if (destino != null) camino = destino;
        Nodo paso = camino;
        while (paso.getCamino() != nodoInicial) {
            paso = paso.getCamino();
        }
        
        // Devolver dirección
        if (paso.getCoord().equals(nodoInicial.NO())) return direccion.NO;
        if (paso.getCoord().equals(nodoInicial.N())) return direccion.N;
        if (paso.getCoord().equals(nodoInicial.NE())) return direccion.NE;
        if (paso.getCoord().equals(nodoInicial.E())) return direccion.E;
        if (paso.getCoord().equals(nodoInicial.SE())) return direccion.SE;
        if (paso.getCoord().equals(nodoInicial.S())) return direccion.S;
        if (paso.getCoord().equals(nodoInicial.SO())) return direccion.SO;
        if (paso.getCoord().equals(nodoInicial.O())) return direccion.O;
        
        return direccion.SO; // TODO: Exception
    }
    
}
