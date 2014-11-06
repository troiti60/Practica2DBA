/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public class Nodo {
    //coordenadas que ocupa el nodo
    private Coord coord;
    //indica si está conectado al nodo que ocupa el bot principal
    private boolean conectado = false;
    //Valor de radar del nodo: 0=libre 1=muro 2=objetivo
    private int radar;
    //Distancia euclídea del nodo al objetivo
    private float scanner;
    //Array de nodos(no muros) adyacentes
    private ArrayList<Nodo> adyacentes = new ArrayList<Nodo>();
    //array de muros adyacentes al nodo
    private ArrayList<Nodo> murosAdyacentes = new ArrayList<Nodo>();
    //Explorado es la suma de los tamaños de adyacentes y murosAdyacentes
    //Si vale 8, se toma el nodo como explorado
    private int explorado;
	
/**
    * Constructor de Nodos a partir de:
    * @param x coordenada x del nodo
    * @param y coordenada y del nodo
    * @param radar valor de radar del nodo
    * @param scanner valor de scanner del nodo
    * @author Antonio Troitiño
    */
    public Nodo(int x, int y, int radar, float scanner) {
        this.coord = new Coord(x, y);
        this.radar = radar;
        this.scanner = scanner;
        explorado=0;
    }
/**
    * Constructor de Nodos a partir de:
    * @param coord coordenadas que ocupa el nodo
    * @param radar valor de radar del nodo
    * @param scanner valor de scanner del nodo
    * @author Alexander Straub
    */	
	public Nodo(Coord coord, int radar, float scanner) {
        this.coord = coord;
        this.radar = radar;
        this.scanner = scanner;
        explorado=0;
    }
	/**
    * Función booleana que devuelve si el nodo está completamente explorado o no
    * @author Antonio Troitiño
    */
    public boolean explored(){
        if(radar!=1&&explorado==8)
        return true;
        else return false;
    }
/**
    * Añade un nodo adyacente al arrayList correspondiente
    * @param unNodo Nodo que hay que añadir a las listas
    * @author Antonio Troitiño
    */        
    public void add(Nodo unNodo) {
                if(unNodo.getRadar()!=1){
		adyacentes.add(unNodo);
                explorado++;
                }else{
                    explorado++;
                    murosAdyacentes.add(unNodo);
                }
	}
	/**
    * Devuelve la coordenada x del nodo
    * @author Antonio Troitiño
    */
    public int getX() {
		return this.coord.getX();
	}
    
/**
    * Devuelve la coordenada y del nodo
    * @author Antonio Troitiño
    */
	
    public int getY() {
		return this.coord.getY();
	}
	
/**
    * Devuelve el valor de radar del nodo
    * @author Antonio Troitiño
    */
    public int getRadar() {
		return this.radar;
	}
/**
    * Devuelve la lista de nodos adyacentes
    * @author Antonio Troitiño
    */
    public ArrayList<Nodo> getAdy() {
		return this.adyacentes;
	}
    
/**
    * Devuelve la lista de muros adyacentes
    * @author Antonio Troitiño
    */
    public ArrayList<Nodo> getMuros() {
		return this.murosAdyacentes;
	}

	/**
    * Sobreescribe el valor de atributo conectado.
    * @param con booleano con el que sobreescribir el atributo "conectado"
    * @author Antonio Troitiño
    */
    public void setConectado(boolean con) {
		this.conectado = con;
	}

/**
    * Devuelve si el nodo está en conectados o no
    * @author Antonio Troitiño
    */
    public boolean getConectado() {
		return this.conectado;
	}
/**
    * Devuelve las coordenadas del nodo
    * @author Antonio Troitiño
    */
    public Coord getCoord() {
		return this.coord;
	}
    	/**
	* Devolver la coordenada en el norte
	*
	* @return Coordenada en el norte
	* @author Alexander Straub,Antonio Troitiño
	*/
	public Coord N() {
		return new Coord(coord.getX(), coord.getY()-1);
	}
	
	/**
	* Devolver la coordenada en el este
	*
	* @return Coordenada en el este
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord E() {
		return new Coord(coord.getX()+1,coord.getY() );
	}
	
	/**
	* Devolver la coordenada en el sur
	*
	* @return Coordenada en el sur
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord S() {
		return new Coord(coord.getX(), coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el oeste
	*
	* @return Coordenada en el oeste
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord O() {
		return new Coord(coord.getX()-1, coord.getY());
	}
	
	/**
	* Devolver la coordenada en el noreste
	*
	* @return Coordenada en el noreste
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord NE() {
		return new Coord(coord.getX()+1, coord.getY()-1);
	}
	
	/**
	* Devolver la coordenada en el sureste
	*
	* @return Coordenada en el sureste
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord SE() {
		return new Coord(coord.getX()+1, coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el suroeste
	*
	* @return Coordenada en el suroeste
	* @author Alexander Straub, Antonio Troitiño
	*/
        public Coord SO() {
		return new Coord(coord.getX()-1, coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el noroeste
	*
	* @return Coordenada en el noroeste
	* @author Alexander Straub, Antonio Troitiño
	*/
	public Coord NO() {
		return new Coord(coord.getX()-1, coord.getY()-1);
	}
	
}
