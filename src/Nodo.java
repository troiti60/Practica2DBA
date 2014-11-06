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
    private Coord coord;
    private boolean conectado = false;
    private int radar; //radar: 0=libre 1=muro 2=objetivo
    private float scanner;
    private ArrayList<Nodo> adyacentes = new ArrayList<Nodo>();
    private ArrayList<Nodo> murosAdyacentes = new ArrayList<Nodo>();
    private int explorado;
	
    public Nodo(int x, int y, int radar, float scanner) {
        this.coord = new Coord(x, y);
        this.radar = radar;
        this.scanner = scanner;
        explorado=0;
    }
	
	public Nodo(Coord coord, int radar, float scanner) {
        this.coord = coord;
        this.radar = radar;
        this.scanner = scanner;
        explorado=0;
    }
	
    public boolean explored(){
        return (explorado==8);
    }
        
    public void add(Nodo unNodo) {
                if(unNodo.getRadar()!=1){
		adyacentes.add(unNodo);
                explorado++;
                }else{
                    explorado++;
                    murosAdyacentes.add(unNodo);
                }
	}
	
    public int getX() {
		return this.coord.getX();
	}
	
    public int getY() {
		return this.coord.getY();
	}
	
    public int getRadar() {
		return this.radar;
	}
	
    public ArrayList<Nodo> getAdy() {
		return this.adyacentes;
	}
    public ArrayList<Nodo> getMuros() {
		return this.murosAdyacentes;
	}
	
    public void setConectado(boolean con) {
		this.conectado = con;
	}
	
    public boolean getConectado() {
		return this.conectado;
	}
	
    public Coord getCoord() {
		return this.coord;
	}
    	/**
	* Devolver la coordenada en el norte
	*
	* @return Coordenada en el norte
	* @author Alexander Straub
	*/
	public Coord N() {
		return new Coord(coord.getX(), coord.getY()-1);
	}
	
	/**
	* Devolver la coordenada en el este
	*
	* @return Coordenada en el este
	* @author Alexander Straub
	*/
	public Coord E() {
		return new Coord(coord.getX()+1,coord.getY() );
	}
	
	/**
	* Devolver la coordenada en el sur
	*
	* @return Coordenada en el sur
	* @author Alexander Straub
	*/
	public Coord S() {
		return new Coord(coord.getX(), coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el oeste
	*
	* @return Coordenada en el oeste
	* @author Alexander Straub
	*/
	public Coord O() {
		return new Coord(coord.getX()-1, coord.getY());
	}
	
	/**
	* Devolver la coordenada en el noreste
	*
	* @return Coordenada en el noreste
	* @author Alexander Straub
	*/
	public Coord NE() {
		return new Coord(coord.getX()+1, coord.getY()-1);
	}
	
	/**
	* Devolver la coordenada en el sureste
	*
	* @return Coordenada en el sureste
	* @author Alexander Straub
	*/
	public Coord SE() {
		return new Coord(coord.getX()+1, coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el suroeste
	*
	* @return Coordenada en el suroeste
	* @author Alexander Straub
	*/
        public Coord SO() {
		return new Coord(coord.getX()-1, coord.getY()+1);
	}
	
	/**
	* Devolver la coordenada en el noroeste
	*
	* @return Coordenada en el noroeste
	* @author Alexander Straub
	*/
	public Coord NO() {
		return new Coord(coord.getX()-1, coord.getY()-1);
	}
	
}
