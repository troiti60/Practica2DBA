
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antonio
 */
public class AgenteEntorno extends SingleAgent{
    ArrayList<Integer> radar;
    ArrayList<Float> scanner;
    float nivelBateria;
    int x,y,lastx,lasty;
    int iter;
    Mapa map;
    public AgenteEntorno(AgentID aid, Mapa mapa) throws Exception {
        super(aid);
        this.radar = new ArrayList<Integer>(25);
        this.scanner = new ArrayList<Float>(25);
        iter=0;
        map=mapa;
        
    }
    
    @Override
    public void execute(){
        //probablemente convenga cambiar esta condición por otra
        while(true){
        ACLMessage inbox=null,outbox=null;
        try {
            inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(AgenteEntorno.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al recibir mensaje");
        }
        
        /* Código para parsear el mensaje mediante JSON*/
        /*     */
        
        //Si es la primera ejecución, mandamos la información
        //de las 25 casillas percibidas
        if(iter==0){
                map.addNode(new Nodo(x-2,y-2,radar.get(0),scanner.get(0)));
                map.addNode(new Nodo(x-1,y-2,radar.get(1),scanner.get(1)));
                map.addNode(new Nodo(x,y-2,radar.get(2),scanner.get(2)));
                map.addNode(new Nodo(x+1,y-2,radar.get(3),scanner.get(3)));
                map.addNode(new Nodo(x+2,y-2,radar.get(4),scanner.get(4)));
                map.addNode(new Nodo(x-2,y-1,radar.get(5),scanner.get(5)));
                map.addNode(new Nodo(x-1,y-1,radar.get(6),scanner.get(6)));
                map.addNode(new Nodo(x,y-1,radar.get(7),scanner.get(7)));
                map.addNode(new Nodo(x+1,y-1,radar.get(8),scanner.get(8)));
                map.addNode(new Nodo(x+2,y-1,radar.get(9),scanner.get(9)));
                map.addNode(new Nodo(x-2,y,radar.get(10),scanner.get(10)));
                map.addNode(new Nodo(x-1,y,radar.get(11),scanner.get(11)));
                map.addNode(new Nodo(x,y,radar.get(12),scanner.get(12)));
                map.addNode(new Nodo(x+1,y,radar.get(13),scanner.get(13)));
                map.addNode(new Nodo(x+2,y,radar.get(14),scanner.get(14)));
                map.addNode(new Nodo(x-2,y+1,radar.get(15),scanner.get(15)));
                map.addNode(new Nodo(x-1,y+1,radar.get(16),scanner.get(16)));
                map.addNode(new Nodo(x,y+1,radar.get(17),scanner.get(17)));
                map.addNode(new Nodo(x+1,y+1,radar.get(18),scanner.get(18)));
                map.addNode(new Nodo(x+2,y+1,radar.get(19),scanner.get(19)));
                map.addNode(new Nodo(x-2,y+2,radar.get(20),scanner.get(20)));
                map.addNode(new Nodo(x-1,y+2,radar.get(21),scanner.get(21)));
                map.addNode(new Nodo(x,y+2,radar.get(22),scanner.get(22)));
                map.addNode(new Nodo(x+1,y+2,radar.get(23),scanner.get(23)));
                map.addNode(new Nodo(x+2,y+2,radar.get(24),scanner.get(24)));
        }else if (lastx!=x&&lasty!=y){
            //en caso de no ser la primera iteración,nos aseguramos de que la 
            //última acción fuese un movimiento, ya que sino no hay que actualizar
            //el mapa ya que no habríamos percibido nada nuevo
            //Si nos hemos movido, actualizamos sólo las catorce casillas periféricas
            /*
                0   1   2   3   4  
                5   -   -   -   9   
                10  -   -   -   14
                15  -   -   -   19  
                20  21  22  23  24
            
            */
                map.addNode(new Nodo(x-2,y-2,radar.get(0),scanner.get(0)));
                map.addNode(new Nodo(x-1,y-2,radar.get(1),scanner.get(1)));
                map.addNode(new Nodo(x,y-2,radar.get(2),scanner.get(2)));
                map.addNode(new Nodo(x+1,y-2,radar.get(3),scanner.get(3)));
                map.addNode(new Nodo(x+2,y-2,radar.get(4),scanner.get(4)));
                
                map.addNode(new Nodo(x-2,y-1,radar.get(5),scanner.get(5)));
                map.addNode(new Nodo(x+2,y-1,radar.get(9),scanner.get(9)));
                
                map.addNode(new Nodo(x-2,y,radar.get(10),scanner.get(10)));                
                map.addNode(new Nodo(x+2,y,radar.get(14),scanner.get(14)));
                
                map.addNode(new Nodo(x-2,y+1,radar.get(15),scanner.get(15)));
                map.addNode(new Nodo(x+2,y+1,radar.get(19),scanner.get(19)));
                
                map.addNode(new Nodo(x-2,y+2,radar.get(20),scanner.get(20)));
                map.addNode(new Nodo(x-1,y+2,radar.get(21),scanner.get(21)));
                map.addNode(new Nodo(x,y+2,radar.get(22),scanner.get(22)));
                map.addNode(new Nodo(x+1,y+2,radar.get(23),scanner.get(23)));
                map.addNode(new Nodo(x+2,y+2,radar.get(24),scanner.get(24)));
        }
        //Una vez el mapa está actualizado, actualizamos la posición actual y
        //pasada
        map.setCoord(x,y);
        lastx=x;
        lasty=y;
        iter++;
        
        outbox=new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID("AgenteBotPrincipal"));
        /*quizá convenga meter esto con JSON también!*/
        outbox.setContent("bateria"+nivelBateria);
        
    }
    }
}
