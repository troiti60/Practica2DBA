import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Representa el mapa descubierto por los sensores del bot en forma singleton
 * para que sea posible acceserlo del agente recibiendo informaciones nuevas de
 * los sensores, tanto como del agente que ejecutará la método de búsqueda
 *
 * @author Alexander Straub
 */
public class Mapa {

    /**
     * Instancia singleton
     */
    private static Mapa instancia;

    /**
     * Crear la instancia singleton. Si ya existe, devolver la referencia
     *
     * @return Referencia al único objeto
     * @author Alexander Straub
     */
    public static Mapa crearInstancia() {
        if (Mapa.instancia == null) {
            Mapa.instancia = new Mapa();
        }
        return Mapa.instancia;
    }

    /**
     * Mapa representando el grafo de nodos conectado con la posición del agente
     */
    private final HashMap<Coord, Nodo> conectado;
    
    /**
     * Nodos en un grafo del mapa ya descubierto pero (aún) no conectado con el
     * nodo donde se encuentra el agente
     */
    private final HashMap<Coord, Nodo> noConectado;
    
    /**
     * Mapa de Muros descubiertos
     */
    private final HashMap<Coord, Nodo> muros;

    /**
     * Coordenadas que ocupa el robot en un instante de tiempo
     */
    private Coord coord;
    
    /**
     * Imagen del mapa
     */
    private final BufferedImage imagen;

    /**
     * Constructor
     *
     * @author Alexander Straub, Antonio Troitiño
     */
    private Mapa() {
		// Iniciando con tamaño de 10 000 para que no haga falta cambiarlo
        //  muchas vezes y así llevando a ser más eficiente en cuanto a velocidad
        final int tamanoInicial = 10000;

        this.conectado = new HashMap<Coord, Nodo>(tamanoInicial);
        this.noConectado = new HashMap<Coord, Nodo>(tamanoInicial);
        this.muros = new HashMap<Coord, Nodo>(tamanoInicial);
        
        // Crear imagen
        this.imagen = new BufferedImage(Lanzador.tamano, Lanzador.tamano, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < Lanzador.tamano; x++)
            for (int y = 0; y < Lanzador.tamano; y++)
                this.imagen.setRGB(x, y, Color.GRAY.getRGB());
    }
    
    /**
     * Guardar el imagen creado en el disco duro
     * 
     * @throws IOException 
     * @author Alexander Straub
     */
    public void dibujar() throws IOException {
        File outputfile = new File(Lanzador.world + ".bmp");
        ImageIO.write(this.imagen, "bmp", outputfile);
    }

    /**
     * Getter para devolver el mapa de nodos conectados con el agente
     *
     * @return Mapa de nodos conectados
     * @author Alexander Straub
     */
    public HashMap<Coord, Nodo> getConectado() {
        return this.conectado;
    }

    /**
     * Getter para devolver el mapa de nodos (aún) no conectados
     *
     * @return Mapa de nodos no conectados
     * @author Alexander Straub
     */
    public HashMap<Coord, Nodo> getNoConectado() {
        return this.noConectado;
    }

    /**
     * Getter para devolver el mapa de muros
     *
     * @return Mapa de nodos no conectados
     * @author Antonio Troitiño
     */
    public HashMap<Coord, Nodo> getMuros() {
        return this.muros;
    }

    /**
     * Getter para devolver la coordenada X de la posición del agente
     *
     * @return Coordenada X
     * @author Antonio Troitiño
     */
    public int getX() {
        return this.coord.getX();
    }

    /**
     * Getter para devolver la coordenada Y de la posición del agente
     *
     * @return Coordenada Y
     * @author Antonio Troitiño
     */
    public int getY() {
        return this.coord.getY();
    }

    /**
     * Getter para devolver las coordenadas del robot
     *
     * @return Coordenadas del bot
     * @author Alexander Straub
     */
    public Coord getCoord() {
        return this.coord;
    }
    
    /**
     * Getter para devolver los 25 nodos captados por AgenteEntorno
     * en la última iteración
     * 
     * @return ArrayList con los 25 nodos de la última percepción
     * @author Antonio Troitiño del Río
     */
    public ArrayList<Nodo> getLastPerception(){
        ArrayList<Nodo> resultado = new ArrayList<Nodo>(25);
        Coord aux;
        for(int y=-2;y<3;y++){
            for(int x=-2;x<3;x++){
                aux=new Coord(coord.getX()+x,coord.getY()+y);
                if(conectado.containsKey(aux))
                    resultado.add(conectado.get(aux));
                else if(noConectado.containsKey(aux))
                    resultado.add(noConectado.get(aux));
                else if(muros.containsKey(aux))
                    resultado.add(muros.get(aux));
            
            }
        }
        
        
        return resultado;
    }
    /**
     * Setter para establecer la nueva posición del robot
     *
     * @param newCoord Coordenadas nuevas del bot
     * @author Antonio Troitiño y Alexander Straub
     */
    public void setCoord(Coord newCoord) {
        this.coord = newCoord;
        
        // Dibujar
        if (this.imagen.getRGB(newCoord.getX(), newCoord.getY()) != Color.RED.getRGB() && 
                this.imagen.getRGB(newCoord.getX(), newCoord.getY()) != Color.BLUE.getRGB())
            this.imagen.setRGB(newCoord.getX(), newCoord.getY(), Color.GREEN.getRGB()); // camino: verde
    }

    /**
     * Añadir un nodo nuevo (o decidir si ya lo hay en el grafo)
     *
     * @param nodoNuevo Nodo que será añadido al mapa
     * @author Antonio Troitiño y Alexander Straub
     */
    public void addNodo(Nodo nodoNuevo) {
        // Dibujar
        if (nodoNuevo.getX() >= 0 && nodoNuevo.getY() >= 0 && 
                nodoNuevo.getX() < Lanzador.tamano && nodoNuevo.getY() < Lanzador.tamano && 
                (this.imagen.getRGB(nodoNuevo.getX(), nodoNuevo.getY()) == Color.GRAY.getRGB() || 
                 this.imagen.getRGB(nodoNuevo.getX(), nodoNuevo.getY()) == Color.WHITE.getRGB())) {
            if (this.conectado.isEmpty()) {
                this.imagen.setRGB(nodoNuevo.getX(), nodoNuevo.getY(), Color.BLUE.getRGB()); // origin: azul
            } else if (nodoNuevo.getRadar() == 0) {
                this.imagen.setRGB(nodoNuevo.getX(), nodoNuevo.getY(), Color.WHITE.getRGB()); // normal: blanco
            } else if (nodoNuevo.getRadar() == 1) {
                this.imagen.setRGB(nodoNuevo.getX(), nodoNuevo.getY(), Color.BLACK.getRGB()); // muro: negro
            } else if (nodoNuevo.getRadar() == 2) {
                this.imagen.setRGB(nodoNuevo.getX(), nodoNuevo.getY(), Color.RED.getRGB()); // destino: rojo
            }
        }
        
        comprobarVecinos(nodoNuevo);
    }

    /**
     * Actualizar la lista de nodos adyacentes de nodoNuevo
     *
     * @param nodoNuevo Nodo cuyos vecinos serán insertados
     * @author Antonio Troitiño, Alexander Straub
     */
    private void adyacentes(Nodo nodoNuevo) {
        Nodo aux = null;

        if (this.conectado.containsKey(nodoNuevo.getCoord().NO())) {
            aux = this.conectado.get(nodoNuevo.getCoord().NO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().N())) {
            aux = this.conectado.get(nodoNuevo.getCoord().N());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().NE())) {
            aux = this.conectado.get(nodoNuevo.getCoord().NE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().E())) {
            aux = this.conectado.get(nodoNuevo.getCoord().E());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().SE())) {
            aux = this.conectado.get(nodoNuevo.getCoord().SE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().S())) {
            aux = this.conectado.get(nodoNuevo.getCoord().S());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().SO())) {
            aux = this.conectado.get(nodoNuevo.getCoord().SO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.conectado.containsKey(nodoNuevo.getCoord().O())) {
            aux = this.conectado.get(nodoNuevo.getCoord().O());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }

        if (this.noConectado.containsKey(nodoNuevo.getCoord().NO())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().NO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().N())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().N());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().NE())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().NE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().E())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().E());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().SE())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().SE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().S())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().S());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().SO())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().SO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.noConectado.containsKey(nodoNuevo.getCoord().O())) {
            aux = this.noConectado.get(nodoNuevo.getCoord().O());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().NO())) {
            aux = this.muros.get(nodoNuevo.getCoord().NO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().N())) {
            aux = this.muros.get(nodoNuevo.getCoord().N());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().NE())) {
            aux = this.muros.get(nodoNuevo.getCoord().NE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().E())) {
            aux = this.muros.get(nodoNuevo.getCoord().E());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().SE())) {
            aux = this.muros.get(nodoNuevo.getCoord().SE());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().S())) {
            aux = this.muros.get(nodoNuevo.getCoord().S());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().SO())) {
            aux = this.muros.get(nodoNuevo.getCoord().SO());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
        if (this.muros.containsKey(nodoNuevo.getCoord().O())) {
            aux = this.muros.get(nodoNuevo.getCoord().O());
            nodoNuevo.add(aux);
            aux.add(nodoNuevo);
        }
    }

    /**
     * Comprobar si está posible conectar el nodo nuevo al "mapa conectado". Si
     * está posible: añadirlo y (si es necesario) llamar a mudarListaNodos Si
     * no: añadirlo al "mapa no conectado"
     *
     * @param nodoNuevo Nodo cuyos vecinos serán comprobado
     * @author Antonio Troitiño, Alexander Straub
     */
    private void comprobarVecinos(Nodo nodoNuevo) {
        Coord clave = nodoNuevo.getCoord();

        //Si es el primer nodo de todos, lo añadimos a conectados directamente
        //porque desde AgenteEntorno nos hemos asegurado de que sea el nodo
        //que ocupa el bot, por lo que será un nodo libre y conectado
        if (this.conectado.isEmpty() && this.noConectado.isEmpty()) {
            this.conectado.put(clave, nodoNuevo);
            nodoNuevo.setConectado(true);

            //y actualizamos su vector de nodos adyacentes
            //(puede ser que ya ha sido añadido un muro)
            adyacentes(nodoNuevo);
        }

        //Si es un nodo que no ha sido añadido aún y no es un muro, lo procesamos
        if (!this.conectado.containsKey(clave) && !this.noConectado.containsKey(clave)
                && nodoNuevo.getRadar() != 1) {
            //Si tiene algún adyacente en conectado, lo añadimos a conectado
            if (this.conectado.containsKey(nodoNuevo.NO())
                    || this.conectado.containsKey(nodoNuevo.N())
                    || this.conectado.containsKey(nodoNuevo.NE())
                    || this.conectado.containsKey(nodoNuevo.E())
                    || this.conectado.containsKey(nodoNuevo.SE())
                    || this.conectado.containsKey(nodoNuevo.S())
                    || this.conectado.containsKey(nodoNuevo.SO())
                    || this.conectado.containsKey(nodoNuevo.O())) {

                this.conectado.put(clave, nodoNuevo);
                nodoNuevo.setConectado(true);

                //y actualizamos su vector de nodos adyacentes
                adyacentes(nodoNuevo);

                    //Si, además de ser un nodo conectado, tiene algún adyacente en
                //noconectado, tendremos que llamar a mudarListaNodos para que
                //se encargue de añadirlos a todos a conectado
                if (this.noConectado.containsKey(nodoNuevo.NO())
                        || this.noConectado.containsKey(nodoNuevo.N())
                        || this.noConectado.containsKey(nodoNuevo.NE())
                        || this.noConectado.containsKey(nodoNuevo.E())
                        || this.noConectado.containsKey(nodoNuevo.SE())
                        || this.noConectado.containsKey(nodoNuevo.S())
                        || this.noConectado.containsKey(nodoNuevo.SO())
                        || this.noConectado.containsKey(nodoNuevo.O())) {

                    mudarListaNodos(nodoNuevo);
                }
            } else {
                    //Si, por el contrario, no tiene ningún adyacente en conectado
                //lo añadimos a noConectado
                this.noConectado.put(clave, nodoNuevo);
                nodoNuevo.setConectado(false);
                adyacentes(nodoNuevo);
            }
        } else if (nodoNuevo.getRadar() == 1) {
            if (!muros.containsKey(nodoNuevo.getCoord())) {
                muros.put(nodoNuevo.getCoord(), nodoNuevo);
                adyacentes(nodoNuevo);
            }
        }
    }

    /**
     * Desplazar nodos del "mapa no conectado" al "mapa conectado" si es vecino
     * del nodo añadido en el último paso. También desplazar todos los nodos que
     * estaban conectado al nodo desplazado
     *
     * @param nodoAnadido Nodo que fue desplazado el último paso
     * @author Antonio Troitiño
     */
    private void mudarListaNodos(Nodo nodoAnadido) {
        ArrayList<Nodo> ady = nodoAnadido.getAdy();
        Nodo aux;
        
        for (int i = 0; i < ady.size(); i++) {
            aux = ady.get(i);
            if (aux.getConectado() == false) {
                aux.setConectado(true);
                this.conectado.put(aux.getCoord(), aux);
                this.noConectado.remove(aux.getCoord());
                mudarListaNodos(aux);
            }
        }
    }

}
