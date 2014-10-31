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
    private Map conectado;
	/**
	* Nodos en un grafo del mapa ya descubierto pero (aún) no conectado con
	* el nodo donde se encuentra el agente
	*/
    private Map noConectado;
    
    	/**
	* Coordenadas que ocupa el robot en un instante de tiempo
	*/
    private int x,y;
	/**
	* Constructor
	*
	* @author Alexander Straub
	*/
    private Mapa() {
		// Iniciando con tamaño de 10 000 para que no haga falta cambiarlo
		//  muchas vezes y así llevando a ser más eficiente en cuanto a velocidad
		final int tamanoInicial = 10000;
		
        this.conectado = new HashMap(tamanoInicial);
        this.noConectado = new HashMap(tamanoInicial);
    }
    
	/**
	* Getter para devolver el mapa de nodos conectados con el agente
	*
	* @author Alexander Straub
	*/
    public Map getConectado() {
        return this.conectado;
    }

    
	/**
	* Getter para devolver el mapa de nodos (aún) no conectados
	*
	* @author Alexander Straub
	*/
    public Map getNoConectado() {
        return this.noConectado;
    }
        
    	/**
	* Getter para devolver la coordenada X de la posición del agente
	*
	* @author Antonio Troitiño
	*/
    public int getX() {
        return this.x;
    }
    
    	/**
	* Getter para devolver la coordenada Y de la posición del agente
	*
	* @author Antonio Troitiño
	*/
    public int getY() {
        return this.y;
    }
    
       	/**
	* Setter para establecer la nueva posición del robot
	*
	* @author Antonio Troitiño
	*/
    public void setCoord(int x1,int y1) {
        this.x=x1;
        this.y=y1;
    }
    
	/**
	* Añadir un nodo nuevo (o decidir si ya lo hay en el grafo)
	*
	* @author 
	*/
    public void addNodo(Nodo nodoNuevo) {
        // TODO
    }
    
	/**
	* Comprobar si está posible conectar el nodo nuevo al "mapa conectado".
	* Si está posible: añadirlo y (si es necesario) llamar a mudarListaNodos
	* Si no: añadirlo al "mapa no conectado"
	*
	* @author 
	*/
    private void comprobarVecinos(Nodo nodoNuevo) {
        // TODO
    }
    
	/**
	* Desplazar nodos del "mapa no conectado" al "mapa conectado" si es vecino
	* del nodo añadido en el último paso. También desplazar todos los nodos que
	* estaban conectado al nodo desplazado
	*
	* @author 
	*/
    private void mudarListaNodos(Nodo nodoNuevo) {
        // TODO
    }
    
}
