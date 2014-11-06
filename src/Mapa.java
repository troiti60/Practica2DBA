
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa el mapa descubierto por los sensores del bot en forma singleton
 * para que sea posible acceserlo del agente recibiendo informaciones nuevas
 * de los sensores, tanto como del agente que ejecutará la método de búsqueda
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
    * @author Alexander Straub
    */
    public static Mapa crearInstancia() {
    	if (Mapa.instancia == null) Mapa.instancia = new Mapa();
    	return Mapa.instancia;
    }
	
    /**
    * Mapa representando el grafo de nodos conectado con la posición del agente
    */
    private HashMap<Coord, Nodo> conectado;
    /**
    * Nodos en un grafo del mapa ya descubierto pero (aún) no conectado con
    * el nodo donde se encuentra el agente
    */
    private HashMap<Coord, Nodo> noConectado;
        /**
    * Mapa de Muros descubiertos
    */
    private HashMap<Coord, Nodo> muros;
    
    /**
	* Coordenadas que ocupa el robot en un instante de tiempo
	*/
    private Coord coord;
	
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
        this.muros=new HashMap<Coord,Nodo>(tamanoInicial);
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
	* @author Antonio Troitiño
	*/
    public int getX() {
        return this.coord.getX();
    }
    
    /**
	* Getter para devolver la coordenada Y de la posición del agente
	*
	* @author Antonio Troitiño
	*/
    public int getY() {
        return this.coord.getY();
    }
	
	/**
	* Getter para devolver las coordenadas del robot
	*
	* @author Alexander Straub
	*/
    public Coord getCoord() {
        return this.coord;
    }
    
    /**
	* Setter para establecer la nueva posición del robot
	*
	* @author Antonio Troitiño
	*/
    public void setCoord(Coord newCoord) {
        this.coord = newCoord;
    }
    
    /**
    * Añadir un nodo nuevo (o decidir si ya lo hay en el grafo)
    *
    * @param nodoNuevo Nodo que será añadido al mapa
    * @author Antonio Troitiño
    */
    public void addNodo(Nodo nodoNuevo) {
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
    * Comprobar si está posible conectar el nodo nuevo al "mapa conectado".
    * Si está posible: añadirlo y (si es necesario) llamar a mudarListaNodos
    * Si no: añadirlo al "mapa no conectado"
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
		}
		
        //Si es un nodo que no ha sido añadido aún y no es un muro, lo procesamos
        if (!this.conectado.containsKey(clave) && !this.noConectado.containsKey(clave) 
                && nodoNuevo.getRadar() != 1) {
                //Si tiene algún adyacente en conectado, lo añadimos a conectado
                if (this.conectado.containsKey(nodoNuevo.NO()) ||
                    this.conectado.containsKey(nodoNuevo.N())  ||
                    this.conectado.containsKey(nodoNuevo.NE()) ||
                    this.conectado.containsKey(nodoNuevo.E())  ||
                    this.conectado.containsKey(nodoNuevo.SE()) ||
                    this.conectado.containsKey(nodoNuevo.S())  ||
                    this.conectado.containsKey(nodoNuevo.SO()) ||
                    this.conectado.containsKey(nodoNuevo.O())) {
				   
                    this.conectado.put(clave, nodoNuevo);
                    nodoNuevo.setConectado(true);
					
                    //y actualizamos su vector de nodos adyacentes
                    adyacentes(nodoNuevo);
					
                    //Si, además de ser un nodo conectado, tiene algún adyacente en
                    //noconectado, tendremos que llamar a mudarListaNodos para que
                    //se encargue de añadirlos a todos a conectado
                    if (this.noConectado.containsKey(nodoNuevo.NO()) ||
			this.noConectado.containsKey(nodoNuevo.N())  ||
                        this.noConectado.containsKey(nodoNuevo.NE()) ||
                        this.noConectado.containsKey(nodoNuevo.E())  ||
                        this.noConectado.containsKey(nodoNuevo.SE()) ||
                        this.noConectado.containsKey(nodoNuevo.S())  ||
                        this.noConectado.containsKey(nodoNuevo.SO()) ||
                        this.noConectado.containsKey(nodoNuevo.O()) ){
						
						mudarListaNodos(nodoNuevo);
					}
                } else {
                    //Si, por el contrario, no tiene ningún adyacente en conectado
                    //lo añadimos a noConectado
                    this.noConectado.put(clave, nodoNuevo);
                    nodoNuevo.setConectado(false);
                    adyacentes(nodoNuevo);
				}
        }
        else if(nodoNuevo.getRadar()==1){
            if(!muros.containsKey(nodoNuevo.getCoord())){
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
