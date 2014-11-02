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
	
    public Nodo(int x, int y, int radar, float scanner) {
        this.coord = new Coord(x, y);
        this.radar = radar;
        this.scanner = scanner;
    }
	
	public Nodo(Coord coord, int radar, float scanner) {
        this.coord = coord;
        this.radar = radar;
        this.scanner = scanner;
    }
	
    public void add(Nodo unNodo) {
		adyacentes.add(unNodo);
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
	
    public void setConectado(boolean con) {
		this.conectado = con;
	}
	
    public boolean getConectado() {
		return this.conectado;
	}
	
    public Coord getCoord() {
		return this.coord;
	}
    
}
