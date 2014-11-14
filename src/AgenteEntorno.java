import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clase Agente Entorno encargado de recoger la información de los sensores
 *
 * @author Antonio Troitiño y Jose Carlos Alfaro
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
    private final Coord coord;
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
     * @author
     */
    public AgenteEntorno(AgentID aid, AgentID agenteBot) throws Exception {
        super(aid);
        this.agenteBot = agenteBot;

        this.radar = new ArrayList<Integer>(25);
        this.scanner = new ArrayList<Float>(25);
        this.nivelBateria = 0.0f;
        this.coord = new Coord(-1, -1);
        this.lastCoord = new Coord(-1, -1);

        this.iter = 0;
        this.mapa = Mapa.crearInstancia();
    }

    /**
     * Funcion de ejecución del bot AgenteEntorno
     *
     * @author Antonio Troitiño y Jose Carlos Alfaro
     */
    @Override
    public void execute() {
        // Variable para la comunicación usando JSon
        JsonDBA parser = new JsonDBA();

        // Empezando con un bot vivo
        boolean vivo = true;

        while (vivo) {
            ACLMessage inbox = null, outbox = null;
            System.out.println("Agente Entorno: Esperando recibir mensajes del servidor.");
            // Recibir todos los mensajes de los sensores
            for (int i = 0; i < 4 && vivo; i++) {
                String strJson = null;
                JsonElement json = null, result = null, resultDentro = null;

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
                else if (parser.contains(strJson, "scanner")) {
                    result = parser.getElement(json, "scanner");
                    this.scanner = parser.jsonElementToArrayFloat(result);
                    
                    System.out.println("Agente Entorno: Recibido scanner: "+this.scanner);
                } // Recibir los datos del radar
                else if (parser.contains(strJson, "radar")) {
                    result = parser.getElement(json, "radar");
                    System.out.println("Agente Entorno peta aquí");

                    this.radar = parser.jsonElementToArrayInt(result);
                    
                    System.out.println("Agente Entorno: Recibido radar: "+this.radar);
                } // Recibir el nivel de la batería
                else if (parser.contains(strJson, "bateria")) {
                    result = parser.getElement(json, "bateria");
                    this.nivelBateria = result.getAsFloat();
                    
                    System.out.println("Agente Entorno: Recibido bateria: "+this.nivelBateria);
                } // Recibir la posición del bot
                else if (parser.contains(strJson, "gps")) {
                    result = parser.getElement(json, "gps");
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
                if (iter == 0) {
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
                    
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 2, this.radar.get(0),  this.scanner.get(0)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() - 2, this.radar.get(1),  this.scanner.get(1)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() - 2, this.radar.get(2),  this.scanner.get(2)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() - 2, this.radar.get(3),  this.scanner.get(3)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 2, this.radar.get(4),  this.scanner.get(4)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() - 1, this.radar.get(5),  this.scanner.get(5)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() - 1, this.radar.get(9),  this.scanner.get(9)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY(),     this.radar.get(10), this.scanner.get(10)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY(),     this.radar.get(14), this.scanner.get(14)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 1, this.radar.get(15), this.scanner.get(15)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 1, this.radar.get(19), this.scanner.get(19)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 2, this.coord.getY() + 2, this.radar.get(20), this.scanner.get(20)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() - 1, this.coord.getY() + 2, this.radar.get(21), this.scanner.get(21)));
                    this.mapa.addNodo(new Nodo(this.coord.getX(),     this.coord.getY() + 2, this.radar.get(22), this.scanner.get(22)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 1, this.coord.getY() + 2, this.radar.get(23), this.scanner.get(23)));
                    this.mapa.addNodo(new Nodo(this.coord.getX() + 2, this.coord.getY() + 2, this.radar.get(24), this.scanner.get(24)));
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
                System.out.println("Agente Entorno: Enviado nivel de batería : "+this.nivelBateria);
            }
        }
    }
}
