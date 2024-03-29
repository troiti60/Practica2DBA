import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clase Agente Entorno encargado de recoger la información de los sensores
 *
 * @author Antonio Troitiño, Jose Carlos Alfaro, Alexander Straub
 */
public class AgenteEntorno extends SingleAgent {

    /**
     * Guardar la ID del agente Bot para comunicarse
     */
    private final AgentID agenteBot;

    /**
     * Variables para guardar la información del entorno
     */
    private ArrayList<Integer> radar;
    private ArrayList<Float> scanner;
    private float nivelBateria;
    private Coord coord;
    private Coord lastCoord;

    /**
     * Guardar iteración y acceso al mapa
     */
    private int iter;
    private final Mapa mapa;

    /**
     * Constructor
     *
     * @param aid ID del agente
     * @param agenteBot ID del agente principal necesario para comunicarse
     * @throws Exception Avisar, si no pudo iniciarse
     * @author FALTA NOMBRE DEL AUTOR
     */
    public AgenteEntorno(AgentID aid, AgentID agenteBot) throws Exception {
        super(aid);
        this.agenteBot = agenteBot;

        this.radar = new ArrayList<>(25);
        this.scanner = new ArrayList<>(25);
        this.nivelBateria = 0.0f;
        this.coord = null;
        this.lastCoord = new Coord(-1, -1);

        this.iter = 0;
        this.mapa = Mapa.crearInstancia();
    }

    /**
     * Funcion de ejecución del bot AgenteEntorno
     *
     * @author Antonio Troitiño, Jose Carlos Alfaro, Alexander Straub
     */
    @Override
    public void execute() {
        // Variable para la comunicación usando JSon
        JsonDBA parser = new JsonDBA();

        // Empezando con un bot vivo
        boolean vivo = true;

        while (vivo) {
            ACLMessage inbox = null, outbox;
            System.out.println("Agente Entorno: Esperando recibir mensajes del servidor.");
            // Recibir todos los mensajes de los sensores
            for (int i = 0; i < 4 && vivo; i++) {
                String strJson = null;
                JsonElement json = null, result, resultDentro;

                // Recibir mensaje
                try {
                    inbox = this.receiveACLMessage();
                    strJson = inbox.getContent();
                    json = parser.recibirRespuesta(strJson);
                } catch (InterruptedException ex) {
                    System.err.println("Error al recibir mensaje\n" + ex.getMessage());
                    vivo = false;
                }
                System.out.println("Agente Entorno: mensaje recibido "+ json);

                // Si el mensaje recibido es del agente bot hay que terminar
                if (inbox != null && inbox.getSender() == this.agenteBot) {
                    vivo = false;
                    System.out.println("Agente Entorno: recibido mensaje de agentebot");
                } // Recibir los datos del scanner
                else if (strJson != null && strJson.contains("scanner")) {
                    result = parser.getElement(json, "scanner");
                    this.scanner = parser.jsonElementToArrayFloat(result);
                    
                    System.out.println("Agente Entorno: Recibido scanner: "+this.scanner);
                } // Recibir los datos del radar
                else if (strJson != null && strJson.contains("radar")) {
                    result = parser.getElement(json, "radar");
                    this.radar = parser.jsonElementToArrayInt(result);
                    
                    System.out.println("Agente Entorno: Recibido radar: "+this.radar);
                } // Recibir el nivel de la batería
                else if (strJson != null && strJson.contains("battery")) {
                    result = parser.getElement(json, "battery");
                    this.nivelBateria = result.getAsFloat();
                    
                    System.out.println("Agente Entorno: Recibido bateria: "+this.nivelBateria);
                } // Recibir la posición del bot
                else if (strJson != null && strJson.contains("gps")) {
                    result = parser.getElement(json, "gps");
                    this.coord = new Coord(0, 0);
                    resultDentro = parser.getElement(result, "x");
                    this.coord.setX(resultDentro.getAsInt());
                    resultDentro = parser.getElement(result, "y");
                    this.coord.setY(resultDentro.getAsInt());
                    
                    System.out.println("Agente Entorno: Recibido gps: "+this.coord.getX() + " " + this.coord.getY());
                }
                
            }

            if (vivo) {
                // Si es la primera ejecución, mandamos la información
                // de las 25 casillas percibidas
                if (this.iter == 0) {
                    // Empezando con el nodo centrico para que sea añadido al grafo conectado
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY(),     this.radar.get(12), this.scanner.get(12)));

                    // Añadir todos las casillas en el entorno
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 2, this.radar.get(0),  this.scanner.get(0)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() - 2, this.radar.get(1),  this.scanner.get(1)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() - 2, this.radar.get(2),  this.scanner.get(2)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() - 2, this.radar.get(3),  this.scanner.get(3)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 2, this.radar.get(4),  this.scanner.get(4)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 1, this.radar.get(5),  this.scanner.get(5)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() - 1, this.radar.get(6),  this.scanner.get(6)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() - 1, this.radar.get(7),  this.scanner.get(7)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() - 1, this.radar.get(8),  this.scanner.get(8)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 1, this.radar.get(9),  this.scanner.get(9)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY(),     this.radar.get(10), this.scanner.get(10)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY(),     this.radar.get(11), this.scanner.get(11)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY(),     this.radar.get(13), this.scanner.get(13)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY(),     this.radar.get(14), this.scanner.get(14)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 1, this.radar.get(15), this.scanner.get(15)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() + 1, this.radar.get(16), this.scanner.get(16)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() + 1, this.radar.get(17), this.scanner.get(17)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() + 1, this.radar.get(18), this.scanner.get(18)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 1, this.radar.get(19), this.scanner.get(19)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 2, this.radar.get(20), this.scanner.get(20)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() + 2, this.radar.get(21), this.scanner.get(21)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() + 2, this.radar.get(22), this.scanner.get(22)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() + 2, this.radar.get(23), this.scanner.get(23)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 2, this.radar.get(24), this.scanner.get(24)));
                } else if (this.coord != this.lastCoord) {
                    // En caso de no ser la primera iteración, nos aseguramos de que la 
                    // última acción fuese un movimiento, ya que sino no hay que actualizar
                    // el mapa ya que no habríamos percibido nada nuevo.
                    // Si nos hemos movido, actualizamos sólo las catorce casillas periféricas
                    
                    /*
                     0   1   2   3   4  
                     5   -   -   -   9   
                     10  -   -   -   14
                     15  -   -   -   19  
                     20  21  22  23  24
                     */
                    
                    // Si hay muros, borrar área mala en el entorno del bot
                    boolean force = false;
                    for (int i = 0; i < 24; i++)
                        force = force || this.radar.get(i) == 1;
                    
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 2, this.radar.get(0),  this.scanner.get(0)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() - 2, this.radar.get(1),  this.scanner.get(1)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() - 2, this.radar.get(2),  this.scanner.get(2)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() - 2, this.radar.get(3),  this.scanner.get(3)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 2, this.radar.get(4),  this.scanner.get(4)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 1, this.radar.get(5),  this.scanner.get(5)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 1, this.radar.get(9),  this.scanner.get(9)),  force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY(),     this.radar.get(10), this.scanner.get(10)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY(),     this.radar.get(14), this.scanner.get(14)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 1, this.radar.get(15), this.scanner.get(15)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 1, this.radar.get(19), this.scanner.get(19)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 2, this.radar.get(20), this.scanner.get(20)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() + 2, this.radar.get(21), this.scanner.get(21)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() + 2, this.radar.get(22), this.scanner.get(22)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() + 2, this.radar.get(23), this.scanner.get(23)), force);
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 2, this.radar.get(24), this.scanner.get(24)), force);
                    
                    // Si hay muros encontrados por los sensores, borrar área mala en dirección del movimiento
                    if (force) {
                        if (this.lastCoord.getX() == this.coord.getX()) {
                            if (this.lastCoord.getY() > this.coord.getY()) { // N
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() - 3));
                            } else { // S
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() + 3));
                            }
                        }
                        if (this.lastCoord.getY() == this.coord.getY()) {
                            if (this.lastCoord.getX() > this.coord.getX()) { // O
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 2));
                            } else { // E
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 2));
                            }
                        }
                        if (this.lastCoord.getX() > this.coord.getX()) {
                            if (this.lastCoord.getY() > this.coord.getY()) { // NO
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 2));
                            } else { // SO
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 3, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() + 3));
                            }
                        }
                        if (this.lastCoord.getX() < this.coord.getX()) {
                            if (this.lastCoord.getY() > this.coord.getY()) { // NE
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 2));
                            } else { // SE
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 2, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() - 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX(),     this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 1, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 2, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 3));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 2));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() + 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY()));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 1));
                                this.mapa.removeNodo(new Coord(this.coord.getX() + 3, this.coord.getY() - 2));
                            }
                        }
                    }
                }

                // Una vez el mapa está actualizado, actualizamos la posición
                // actual y pasada
                this.mapa.setCoord(this.coord);
                this.lastCoord = this.coord;
                this.iter++;

                // Enviar el nivel de la batería al agente bot
                outbox = new ACLMessage();
                outbox.setSender(this.getAid());
                outbox.setReceiver(this.agenteBot);
                System.out.println("Agente Entorno: Enviando mensaje a botprincipal");
                LinkedHashMap lhm = new LinkedHashMap();
                lhm.put("bateria", this.nivelBateria);
                
                outbox.setContent(parser.crearJson(lhm));
                this.send(outbox);
                System.out.println("Agente Entorno: Enviado nivel de batería : "+this.nivelBateria);
            }
        }
    }
}
