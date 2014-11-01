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
    private int clave;
    private boolean conectado=false;
    private int x,y,radar;//radar: 0=libre 1=muro 2=objetivo
    private float scanner;
    private ArrayList<Nodo> adyacentes=new ArrayList<Nodo>();
    public Nodo(int x1, int y1, int radar1,float scanner1){
        x=x1;
        y=y1;
        clave=(x*1000)+y;
        radar=radar1;
        scanner=scanner1;        
    }
    public void add(Nodo unNodo){adyacentes.add(unNodo);}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getRadar(){return radar;}
    public ArrayList<Nodo> getAdy(){return adyacentes;}
    public void setConectado(boolean con){conectado=con;}
    public boolean getConectado(){return conectado;}
    public int getKey(){return clave;}
    
}
