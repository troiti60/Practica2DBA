
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
    private Map<Integer,Nodo> conectado;
    /**
    * Nodos en un grafo del mapa ya descubierto pero (aún) no conectado con
    * el nodo donde se encuentra el agente
    */
    private Map<Integer,Nodo> noConectado;
    
    	/**
	* Coordenadas que ocupa el robot en un instante de tiempo
	*/
    private int x, y;
	
	/**
	* Constructor
	*
	* @author Alexander Straub
	*/
    private Mapa() {
		// Iniciando con tamaño de 10 000 para que no haga falta cambiarlo
		//  muchas vezes y así llevando a ser más eficiente en cuanto a velocidad
		final int tamanoInicial = 10000;
		
        this.conectado = new HashMap<Integer,Nodo>(tamanoInicial);
        this.noConectado = new HashMap<Integer,Nodo>(tamanoInicial);
    }
    
    /**
    * Getter para devolver el mapa de nodos conectados con el agente
    *
    * @return Mapa de nodos conectados
    * @author Alexander Straub
    */
    public Map getConectado() {
        return this.conectado;
    }
    
    /**
    * Getter para devolver el mapa de nodos (aún) no conectados
    *
    * @return Mapa de nodos no conectados
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
    public void setCoord(int x1, int y1) {
        this.x = x1;
        this.y = y1;
    }
    
    /**
    * Añadir un nodo nuevo (o decidir si ya lo hay en el grafo)
    *
    * @param nodoNuevo Nodo que será añadido al mapa
    * @author 
    */
    public void addNodo(Nodo nodoNuevo) {
        comprobarVecinos(nodoNuevo);
    }
    
        /**
    * Actualizar la lista de nodos adyacentes de nodoNuevo
    *
    * @param nodoNuevo Nodo cuyos vecinos serán insertados
    * @author Antonio Troitiño
    */
    private void adyacentes(Nodo nodoNuevo) {
        Nodo aux=null;
    if(conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1)){
        aux=conectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}
    if(conectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1)){
        aux=conectado.get(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}        
    if(conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1)){
        aux=conectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY())){
        aux=conectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY());
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY())){
        aux=conectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY());
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1)){
        aux=conectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(conectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1)){
        aux=conectado.get(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1)){
        aux=conectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);} 
    
    if(noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1)){
        aux=noConectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}
    if(noConectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1)){
        aux=noConectado.get(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}        
    if(noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1)){
        aux=noConectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY())){
        aux=noConectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY());
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY())){
        aux=noConectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY());
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1)){
        aux=noConectado.get(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(noConectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1)){
        aux=noConectado.get(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}         
    if(noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1)){
        aux=noConectado.get(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1);
        nodoNuevo.add(aux);
        aux.add(nodoNuevo);}
    
    }
    
    /**
    * Comprobar si está posible conectar el nodo nuevo al "mapa conectado".
    * Si está posible: añadirlo y (si es necesario) llamar a mudarListaNodos
    * Si no: añadirlo al "mapa no conectado"
    *
    * @param nodoNuevo Nodo cuyos vecinos serán comprobado
    * @author Antonio Troitiño
    */
    private void comprobarVecinos(Nodo nodoNuevo) {
        int clave =(nodoNuevo.getX()*1000)+nodoNuevo.getY();
        //Si es el primer nodo de todos, lo añadimos a conectados directamente
        //porque desde AgenteEntorno nos hemos asegurado de que sea el nodo
        //que ocupa el bot, por lo que será un nodo libre y conectado
        if(conectado.isEmpty()&&noConectado.isEmpty()) conectado.put(clave,nodoNuevo);
        //Si es un nodo que no ha sido añadido aún y no es un muro, lo procesamos
        if(!conectado.containsKey(clave)&&!noConectado.containsKey(clave)&&nodoNuevo.getRadar()!=1){
                //Si tiene algún adyacente en conectado, lo añadimos a conectado
                if(conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1)||
                   conectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1)||
                   conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1)||
                   conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY())||
                   conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY())||
                   conectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1)||
                   conectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1)||
                   conectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1)){
                    conectado.put(clave, nodoNuevo);
                    nodoNuevo.setConectado(true);
                    //y actualizamos su vector de nodos adyacentes
                    adyacentes(nodoNuevo);
                    //Si, además de ser un nodo conectado, tiene algún adyacente en
                    //noconectado, tendremos que llamar a mudarListaNodos para que
                    //se encargue de añadirlos a todos a conectado
                    if(noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()-1)||
                       noConectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()-1)||
                       noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()-1)||
                       noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY())||
                       noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY())||
                       noConectado.containsKey(((nodoNuevo.getX()-1)*1000)+nodoNuevo.getY()+1)||
                       noConectado.containsKey(((nodoNuevo.getX())*1000)+nodoNuevo.getY()+1)||
                       noConectado.containsKey(((nodoNuevo.getX()+1)*1000)+nodoNuevo.getY()+1))
                        mudarListaNodos(nodoNuevo);
                }else{//Si, por el contrario, no tiene ningún adyacente en conectado
                      //lo añadimos a noConectado
                    noConectado.put(clave, nodoNuevo); adyacentes(nodoNuevo);
                    nodoNuevo.setConectado(false);}
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
        ArrayList<Nodo> ady=nodoAnadido.getAdy();
        Nodo aux;
        for(int i=0;i<ady.size();i++){
            aux=ady.get(i);
            if(aux.getConectado()==false){
                aux.setConectado(true);
                conectado.put(aux.getKey(), aux);
                noConectado.remove(aux.getKey());
                mudarListaNodos(aux);
            }
            
        }
    }
    
}
