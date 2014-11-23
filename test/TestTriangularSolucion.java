
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Clase ejecutable para comprobar que el funcionamiento de la función de
 * triangulación de la posición del objetivo es correcto
 * @author Antonio Troitiño del Río
 */
public class TestTriangularSolucion {
    public static int testea=1;
    public static Coord triangularObjetivo2(){
        Mapa map=Mapa.crearInstancia();
        Coord objetivo=new Coord(0,0);
        ArrayList<Nodo> perception = map.getLastPerception();
        Coord A,C;
        A=perception.get(20).getCoord();
        C=perception.get(0).getCoord();
        double b=distanciaEuclidea(A,C);
        double a=perception.get(0).getScanner();
        double c=perception.get(20).getScanner();
        double alpha = Math.acos(((b*b)+(c*c)-(a*a))/(2.0*b*c));
        System.out.println("a,b,c,alpha:= "+a+" "+b+" "+c+" "+alpha);

        double x,x1,y,y1;
        x=(C.getX()-A.getX())/b;
        x=x*c;
        y=(C.getY()-A.getY())/b;
        y=y*c;
        System.out.println("x,y := "+x+" "+y);
        if(perception.get(0).getScanner()<perception.get(1).getScanner()){
        x1=x*Math.cos(alpha)+y*Math.sin(alpha)+A.getX();
        y1=y*Math.cos(alpha)-x*Math.sin(alpha)+A.getY();
        }else{
        x1=x*Math.cos(alpha)-y*Math.sin(alpha)+A.getX();
        y1=y*Math.cos(alpha)+x*Math.sin(alpha)+A.getY();  
        }
        objetivo.setX(redondear(x1));
        objetivo.setY(redondear(y1));
        System.out.println(x1+" "+y1);
        
        return objetivo;
    };
    public static double distanciaEuclidea(Coord A, Coord B){
        double res = Math.sqrt(((B.getX()-A.getX())*(B.getX()-A.getX()))+((B.getY()-A.getY())*(B.getY()-A.getY())));
        return res;
    }
    public static int redondear(double num){
        double aux=num;
        int res=0;
        res=(int) aux;
        aux=aux-res;
        if(aux>0.5) res++;
        return res;
    }
    public static void test0(){
         Mapa map = Mapa.crearInstancia();
         
        map.addNodo(new Nodo(99,0,0,new Float(68.593)));
        
        map.addNodo(new Nodo(99-2, -2, 1,new Float(68.622154)));
        map.addNodo(new Nodo(99-1, -2, 1, new Float(69.31089)));
        map.addNodo(new Nodo(99, -2, 1, new Float(70.00714)));
        map.addNodo(new Nodo(99+1,-2, 1, new Float(70.71068)));
        map.addNodo(new Nodo(99+2, -2, 1, new Float(71.42129)));
        
        map.addNodo(new Nodo(99-2, -1, 1, new Float(67.89698)));
        map.addNodo(new Nodo(99-1, -1, 1, new Float(68.593)));
        map.addNodo(new Nodo(99+0, -1, 1, new Float(69.29646)));
        map.addNodo(new Nodo(99+1, -1, 1, new Float(70.00714)));
        map.addNodo(new Nodo(99+2, -1, 1, new Float(70.724815)));
        
        map.addNodo(new Nodo(99-2, 0, 0, new Float(67.17886)));
        map.addNodo(new Nodo(99-1, 0, 0, new Float(67.88225)));
        map.addNodo(new Nodo(99+1, 0, 1, new Float(69.31089)));
        map.addNodo(new Nodo(99+2, 0, 1, new Float(70.035706)));
        
        map.addNodo(new Nodo(99-2, 1, 0, new Float(66.46804)));
        map.addNodo(new Nodo(99-1, 1, 0, new Float(67.17886)));
        map.addNodo(new Nodo(99+0, 1, 0, new Float(67.89698)));
        map.addNodo(new Nodo(99+1, 1, 1, new Float(68.622154)));
        map.addNodo(new Nodo(99+2, 1, 1, new Float(69.354164)));
        
        map.addNodo(new Nodo(99-2, 2, 0, new Float(65.76473)));
        map.addNodo(new Nodo(99-1, 2, 0, new Float(66.48308)));
        map.addNodo(new Nodo(99+0, 2, 0, new Float(67.20863)));
        map.addNodo(new Nodo(99+1, 2, 1, new Float(67.941154)));
        map.addNodo(new Nodo(99+2, 2, 1, new Float(68.68042)));
        
        map.setCoord(new Coord(99+0,0));
        ArrayList<Nodo> perception = map.getLastPerception();
        String cad= new String();
        for(int i=0;i<25;i++){
            cad+= perception.get(i).getScanner()+" ";
            if(i%5==4){
                System.out.println(cad);
                cad=new String();
            }
        }
        for(int i=0;i<25;i++){
            cad+= perception.get(i).getRadar()+" ";
            if(i%5==4){
                System.out.println(cad);
                cad=new String();
            }
        }
        Coord obj=triangularObjetivo2();
        System.out.println("La posición del objetivo debería ser x:52 , y:47. La posición calculada"
        +"triangulando es x:"+obj.getX()+" , y:"+obj.getY());
    
    }
    public static void test1(){
         Mapa map = Mapa.crearInstancia();

         
        map.addNodo(new Nodo(0,0,0,new Float(138.59293)));
        
        map.addNodo(new Nodo(0-2, -2, 1,new Float(141.42136)));
        map.addNodo(new Nodo(0-1, -2, 1, new Float(140.71602)));
        map.addNodo(new Nodo(0, -2, 1, new Float(140.01428)));
        map.addNodo(new Nodo(0+1,-2, 1, new Float(139.3162)));
        map.addNodo(new Nodo(0+2, -2, 1, new Float(138.62178)));
        
        map.addNodo(new Nodo(0-2, -1, 1, new Float(140.71602)));
        map.addNodo(new Nodo(0-1, -1, 1, new Float(140.00714)));
        map.addNodo(new Nodo(0+0, -1, 1, new Float(139.30183)));
        map.addNodo(new Nodo(0+1, -1, 1, new Float(138.60014)));
        map.addNodo(new Nodo(0+2, -1, 1, new Float(137.90215)));
        
        map.addNodo(new Nodo(0-2, 0, 1, new Float(140.01428)));
        map.addNodo(new Nodo(0-1, 0, 1, new Float(139.30183)));
        map.addNodo(new Nodo(0+1, 0, 0, new Float(137.88763)));
        map.addNodo(new Nodo(0+2, 0, 0, new Float(137.186)));
        
        map.addNodo(new Nodo(0-2, 1, 1, new Float(139.3162)));
        map.addNodo(new Nodo(0-1, 1, 1, new Float(138.60014)));
        map.addNodo(new Nodo(0+0, 1, 0, new Float(137.88763)));
        map.addNodo(new Nodo(0+1, 1, 0, new Float(137.17871)));
        map.addNodo(new Nodo(0+2, 1, 0, new Float(136.47343)));
        
        map.addNodo(new Nodo(0-2, 2, 1, new Float(138.62178)));
        map.addNodo(new Nodo(0-1, 2, 1, new Float(137.90215)));
        map.addNodo(new Nodo(0+0, 2, 0, new Float(137.186)));
        map.addNodo(new Nodo(0+1, 2, 0, new Float(136.47343)));
        map.addNodo(new Nodo(0+2, 2, 0, new Float(135.7645)));
        
        map.setCoord(new Coord(0+0,0));
        ArrayList<Nodo> perception = map.getLastPerception();
        String cad= new String();
        for(int i=0;i<25;i++){
            cad+= perception.get(i).getScanner()+" ";
            if(i%5==4){
                System.out.println(cad);
                cad=new String();
            }
        }
        for(int i=0;i<25;i++){
            cad+= perception.get(i).getRadar()+" ";
            if(i%5==4){
                System.out.println(cad);
                cad=new String();
            }
        }
        Coord obj=triangularObjetivo2();
        System.out.println("La posición del objetivo debería ser x:97 , y:98. La posición calculada"
        +"triangulando es x:"+obj.getX()+" , y:"+obj.getY());
    
    }
 public static void main(String[] args) { 
        if (testea==0)
            test0();
        else if(testea==1)
            test1();

    }
    
}
