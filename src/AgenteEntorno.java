
import com.google.gson.JsonElement;
import com.mysql.jdbc.util.LRUCache;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clase Agente Entorno encargado de recoger la información de los sensores
 * @author Antonio Troitiño y Jose Carlos Alfaro
 */
public class AgenteEntorno extends SingleAgent{
    ArrayList<Integer> radar;
    ArrayList<Float> scanner;
    float nivelBateria;
    Coord coord, lastCoord;
    int iter;
    Mapa mapa;
    JsonDBA parser;
    String strJson;
    JsonElement json,result;
    public AgenteEntorno(AgentID aid, Mapa mapa) throws Exception {
        super(aid);
        this.radar = new ArrayList<Integer>(25);
        this.scanner = new ArrayList<Float>(25);
        this.iter = 0;
        this.mapa = mapa;
    }
    /**
     * Funcion de ejecucion del bot AgenteEntorno
     * @author Antonio Troitiño y Jose Carlos Alfaro
     */
    @Override
    public void execute() {
        boolean vivo=true;
        //probablemente convenga cambiar esta condición por otra
        while(vivo) {
			ACLMessage inbox = null, outbox = null;
                        
                        for(int i = 0;i<3 && vivo;i++){
                            try {
                                    inbox = this.receiveACLMessage();
                                    strJson = inbox.getContent();
                                    json = parser.recibirRespuesta(strJson);
                                    
                            } catch (InterruptedException ex) {
                                    Logger.getLogger(AgenteEntorno.class.getName()).log(Level.SEVERE, null, ex);
                                    System.err.println("Error al recibir mensaje");
                            }
                            
                            if(inbox.getSender() == new AgentID("botPrincipal"))
                                vivo = false;
                            
                                else if(parser.contains(strJson, "scanner"))
                                {
                                    
                                    result  = parser.getElement(json, "scanner");
                                    scanner = parser.jsonElementToArrayFloat(result);

                                }else if(parser.contains(strJson, "radar"))
                                {
                                    
                                    result = parser.getElement(json, "radar");
                                    radar  = parser.jsonElementToArrayInt(result);

                                }else if(parser.contains(strJson, "bateria"))
                                {
                                   result       = parser.getElement(json, "bateria");
                                   nivelBateria = result.getAsFloat();
                                }
                            
                        }
                        
                        if(vivo){
			//Si es la primera ejecución, mandamos la información
			//de las 25 casillas percibidas
			if (iter == 0) {
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY(),   radar.get(12), scanner.get(12)));
				
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()-2, radar.get(0),  scanner.get(0)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()-2, radar.get(1),  scanner.get(1)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()-2, radar.get(2),  scanner.get(2)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()-2, radar.get(3),  scanner.get(3)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()-2, radar.get(4),  scanner.get(4)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()-1, radar.get(5),  scanner.get(5)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()-1, radar.get(6),  scanner.get(6)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()-1, radar.get(7),  scanner.get(7)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()-1, radar.get(8),  scanner.get(8)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()-1, radar.get(9),  scanner.get(9)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY(),   radar.get(10), scanner.get(10)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY(),   radar.get(11), scanner.get(11)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY(),   radar.get(13), scanner.get(13)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY(),   radar.get(14), scanner.get(14)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()+1, radar.get(15), scanner.get(15)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()+1, radar.get(16), scanner.get(16)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()+1, radar.get(17), scanner.get(17)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()+1, radar.get(18), scanner.get(18)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()+1, radar.get(19), scanner.get(19)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()+2, radar.get(20), scanner.get(20)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()+2, radar.get(21), scanner.get(21)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()+2, radar.get(22), scanner.get(22)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()+2, radar.get(23), scanner.get(23)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()+2, radar.get(24), scanner.get(24)));
			} else if (this.coord != this.lastCoord) {
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
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()-2, radar.get(0),  scanner.get(0)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()-2, radar.get(1),  scanner.get(1)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()-2, radar.get(2),  scanner.get(2)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()-2, radar.get(3),  scanner.get(3)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()-2, radar.get(4),  scanner.get(4)));
					
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()-1, radar.get(5),  scanner.get(5)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()-1, radar.get(9),  scanner.get(9)));
					
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY(),   radar.get(10), scanner.get(10)));                
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY(),   radar.get(14), scanner.get(14)));
					
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()+1, radar.get(15), scanner.get(15)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()+1, radar.get(19), scanner.get(19)));
					
					this.mapa.addNodo(new Nodo(this.coord.getX()-2, this.coord.getY()+2, radar.get(20), scanner.get(20)));
					this.mapa.addNodo(new Nodo(this.coord.getX()-1, this.coord.getY()+2, radar.get(21), scanner.get(21)));
					this.mapa.addNodo(new Nodo(this.coord.getX(),   this.coord.getY()+2, radar.get(22), scanner.get(22)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+1, this.coord.getY()+2, radar.get(23), scanner.get(23)));
					this.mapa.addNodo(new Nodo(this.coord.getX()+2, this.coord.getY()+2, radar.get(24), scanner.get(24)));
			}
			
			//Una vez el mapa está actualizado, actualizamos la posición actual y
			//pasada
			this.mapa.setCoord(this.coord);
			this.lastCoord = this.coord;
			iter++;
			
			outbox = new ACLMessage();
			outbox.setSender(this.getAid());
			outbox.setReceiver(new AgentID("botPrincipal"));
                  
                        LinkedHashMap lhm = new LinkedHashMap();
                        lhm.put("bateria", nivelBateria);
                        
			outbox.setContent(parser.crearJson(lhm));
        
		}
            }
    }
}
