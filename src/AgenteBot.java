
import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Agente que se encarga de la toma de decisiones
 * Modificaciones: 20-10-14 13:30 Javier Ortega
 * 
 * @author Fco Javier Ortega Rodríguez
 *
 */
public class AgenteBot extends SingleAgent{

	private ACLMessage inbox,outbox;
	private String saludo;
	private DatosAcceso datac = new DatosAcceso();
        private JsonDBA parse = new JsonDBA();
	
        /**
         * Enum de las acciones posibles
         * 
         * @author Alexander Straub
         */
	public enum direccion {
            NO("moveNW"),
            N("moveN"),
            NE("moveNE"),
            E("moveE"),
            SE("moveSE"),
            S("moveS"),
            SO("moveSW"),
            O("moveW"),
            R("refuel");
            
            // El string que define la acción
            private final String command;

            /**
             * Constructor
             * 
             * @param command El string que define la acción
             * @author Alexander Straub
             */
            private direccion(final String command) {
		this.command = command;
            }

            /**
             * Devolver el string que define la acción
             * 
             * @return El string que define la acción
             * @author Alexander Straub
             */
            @Override
            public String toString() {
		return this.command;
            }
	}
        
        private ArrayList<direccion> camino = new ArrayList<direccion>();
        private boolean llegarAlDestino = false;
	
	public AgenteBot(AgentID aid) throws Exception {
		super(aid);
                JsonDBA login = new JsonDBA();
                saludo = login.login("plainworld",null,null,null,null);
	}
	    
    /**
     * Método inicial de logueo en el servidor
     * @author Fco Javier Ortega Rodríguez y José Carlos Alfaro
     * @return Respuesta del servidor
     * @throws InterruptedException 
     */
    public String Saludo() throws InterruptedException{
        System.out.println("\nSaludo mandado:\n" + saludo);
        
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID( datac.getVirtualHost() ));
        outbox.setContent(this.saludo);
        this.send(outbox);
        
        System.out.println("\nEsperando recibir mensaje...");
        
        inbox=this.receiveACLMessage();   
        JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = parse.getElement(MensajeRecibido, "result");
       
        System.out.println("Mensaje recibido: " +valorResult.getAsString()+" de "+inbox.getSender().getLocalName());
        
        return valorResult.getAsString();
    }

    /**
     * Método que manda la acción de repostar o el tipo de movimiento 
     * al servidor
     * @author Fco Javier Ortega Rodríguez y José Carlos Alfaro
     * @param d Acción o dirección a tomar
     * @return Respuesta del servidor
     * @throws InterruptedException 
     */
    private String RealizarAccion(direccion d) throws InterruptedException{
        String accion = null;
        LinkedHashMap lhmap = new LinkedHashMap();      // HashMap pero respetando el orden de insercion
        
        lhmap.put("command", d.toString());
        lhmap.put("key", datac.getKey());
        accion = parse.crearJson(lhmap);
        
        outbox.setContent(accion);
        this.send(outbox);
        
        System.out.println("\nAcción mandada:\n" + accion);
        System.out.println("\nEsperando recibir mensaje...");
        
        inbox=this.receiveACLMessage();   
        JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = parse.getElement(MensajeRecibido, "result");
        
        System.out.println("Mensaje recibido: " +valorResult.getAsString()+" de "+inbox.getSender().getLocalName());
        
        return valorResult.getAsString();
    }
    
    /**
     * @author Fco Javier Ortega Rodríguez
     */
    @Override
    public void execute(){
        boolean vivo = true;
        boolean keyCorrecto = false;
        float nivelBateria = 0;
        float MIN_BATERY = 3;
        direccion decision;
        String respuesta;

        System.out.println("\n\nSoy agenteBot funcionando");
        
        try {
            System.out.print("\nSaludando...");
            String key = this.Saludo();
            System.out.print("OK\n");
        
            keyCorrecto = !key.contains("BAD_");
            
            if (keyCorrecto){
                System.out.println("Key correcta");
                datac.setKey(key);
                vivo = true;
            }
            else{
                System.out.println("Key incorrecta");
                //matar agenteEntorno mesaje
                vivo = !vivo;
            }
            
            while(vivo) {                                  
                //Esperar NvBateria de agente entorno
                System.out.println("\nEsperando recibir mensaje...");

                inbox=this.receiveACLMessage();   
                JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
                JsonElement valorResult = parse.getElement(MensajeRecibido, "batery");
                nivelBateria = valorResult.getAsFloat();
                        
                //heuristica/toma decisión                
                if(nivelBateria <= MIN_BATERY)
                    decision = direccion.R;
                else
                    decision = this.busqueda();
                
                // Alternativamente se puede utilizar la función de búsqueda que
                // devuelve un camino completo hasta un nodo prometedor, o si
                // al tiro, al destino.
                //
                // Una heurística posible sería:
                //  Si es posible llegar al destino, ir el camino completo
                //  Si no:
                //   Si hay que ir atrás, es decir seguir un camino con nodos
                //    ya visitado, para estando en el primer nodo aún no visitado
                //   Si el nodo aún no era visitado, ir paso a paso
                /*
                if (nivelBateria <= MIN_BATERY) {
                    decision = direccion.R;
                } else {
                    if (this.camino == null || this.camino.isEmpty()) {
                        this.llegarAlDestino = busqueda(this.camino);
                    }
                    
                    decision = this.camino.remove(0);
                    
                    if (this.llegarAlDestino == false && !Mapa.crearInstancia()
                            .getConectado().get(Mapa.crearInstancia()
                            .getCoord().vecino(decision)).isVisitado()) {
                        this.camino.clear();
                    }
                }
                */
                
                respuesta = this.RealizarAccion(decision);
                
                if(!respuesta.contains("OK")){
                     // Matar agente entorno
                    vivo = !vivo;
                    System.out.println("Resultado inesperado\n" + respuesta);
                }
            } 		
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("\n----ERROR EN BOT----\n" + e.getMessage());
        }
    }
	
    /**
     * La búsqueda para encontrar el mejor (con la información
     * que ya tenemos) camino
     * 
     * @return La dirección en que el bot debe moverse
     * @author Alexander Straub
     */
    private direccion busqueda() {
        // Recoger el mapa
        Mapa mapa = Mapa.crearInstancia();
        HashMap<Coord, Nodo> map = mapa.getConectado();
        
        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext(); )
            i.next().resetBusqueda();
        Nodo nodoInicial = map.get(mapa.getCoord());
        nodoInicial.setDistancia(0.0);
        
        // Trata como el nodo destino
        double distancia = Double.MAX_VALUE;
        Nodo camino = null;
		Nodo destino = null;
        
        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo)Collections.min(nodos);
            nodos.remove(minNodo);
			
			if (minNodo.getRadar() == 2) destino = minNodo;
            
            // Recoger los vecinos del nodo actual
            for (Iterator<Nodo> i = minNodo.getAdy().iterator(); i.hasNext(); ) {
                Nodo vecino = i.next();
                
                // Si aún está en la lista
                if (nodos.contains(vecino)) {
                    // Calcular distancia alternativa
                    double alternativa = minNodo.getDistancia() 
                            + vecino.distanciaA(minNodo);
                    
                    // Si es mejor, usar la distancia alternativa con el
                    // camino correspondiente
                    if (alternativa < vecino.getDistancia()) {
                        vecino.setDistancia(alternativa);
                        vecino.setCamino(minNodo);
                    }
                }
            }
            
            // Si el entorno aún no está descubierto completamente
            if (!minNodo.explored()) {
                // También calcular distancia alternativa al destino,
                // como si no hubiera obstáculos desde ahí
                double alternativa = minNodo.getDistancia() 
                        + minNodo.getScanner();
                
                // Si es mejor: actualizar
                if (alternativa < distancia) {
                    distancia = alternativa;
                    camino = minNodo;
                }
            }
        }
        
        // Empezando con el nodo del destino ir atrás hasta haber 
        // encontrado el vecino de la posición del robot
		if (destino != null) camino = destino;
        Nodo paso = camino;
        while (paso.getCamino() != nodoInicial) {
            paso = paso.getCamino();
        }
        
        // Devolver dirección
        if (paso.getCoord().equals(nodoInicial.NO())) return direccion.NO;
        if (paso.getCoord().equals(nodoInicial.N())) return direccion.N;
        if (paso.getCoord().equals(nodoInicial.NE())) return direccion.NE;
        if (paso.getCoord().equals(nodoInicial.E())) return direccion.E;
        if (paso.getCoord().equals(nodoInicial.SE())) return direccion.SE;
        if (paso.getCoord().equals(nodoInicial.S())) return direccion.S;
        if (paso.getCoord().equals(nodoInicial.SO())) return direccion.SO;
        if (paso.getCoord().equals(nodoInicial.O())) return direccion.O;
        
        return direccion.SO; // TODO: Exception
    }
	
	/**
     * La búsqueda para encontrar el mejor (con la información
     * que ya tenemos) camino
     * 
     * @return La dirección en que el bot debe moverse
     * @author Alexander Straub
     */
    private static boolean busqueda(ArrayList<direccion> direcciones) {
        // Recoger el mapa
        Mapa mapa = Mapa.crearInstancia();
        HashMap<Coord, Nodo> map = mapa.getConectado();
        
        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext(); )
            i.next().resetBusqueda();
        Nodo nodoInicial = map.get(mapa.getCoord());
        nodoInicial.setDistancia(0.0);
        
        // Trata como el nodo destino
        double distancia = Double.MAX_VALUE;
        Nodo camino = null;
        Nodo destino = null;
        
        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo)Collections.min(nodos);
            nodos.remove(minNodo);
            
            if (minNodo.getRadar() == 2) destino = minNodo;
            
            // Recoger los vecinos del nodo actual
            for (Iterator<Nodo> i = minNodo.getAdy().iterator(); i.hasNext(); ) {
                Nodo vecino = i.next();
                
                // Si aún está en la lista
                if (nodos.contains(vecino)) {
                    // Calcular distancia alternativa
                    double alternativa = minNodo.getDistancia() 
                            + vecino.distanciaA(minNodo);
                    
                    // Si es mejor, usar la distancia alternativa con el
                    // camino correspondiente
                    if (alternativa < vecino.getDistancia()) {
                        vecino.setDistancia(alternativa);
                        vecino.setCamino(minNodo);
                    }
                }
            }
            
            // Si el entorno aún no está descubierto completamente
            if (!minNodo.explored()) {
                // También calcular distancia alternativa al destino,
                // como si no hubiera obstáculos desde ahí
                double alternativa = minNodo.getDistancia() 
                        + minNodo.getScanner();
                
                // Si es mejor: actualizar
                if (alternativa < distancia) {
                    distancia = alternativa;
                    camino = minNodo;
                }
            }
        }
        
        // Empezando con el nodo del destino ir atrás hasta haber 
        // encontrado el vecino de la posición del robot
        if (destino != null) camino = destino;
        Nodo paso = camino;
        while (paso != nodoInicial && paso != null) {
            if (paso.getCoord().equals(paso.getCamino().NO())) direcciones.add(direccion.NO);
            else if (paso.getCoord().equals(paso.getCamino().N())) direcciones.add(direccion.N);
            else if (paso.getCoord().equals(paso.getCamino().NE())) direcciones.add(direccion.NE);
            else if (paso.getCoord().equals(paso.getCamino().E())) direcciones.add(direccion.E);
            else if (paso.getCoord().equals(paso.getCamino().SE())) direcciones.add(direccion.SE);
            else if (paso.getCoord().equals(paso.getCamino().S())) direcciones.add(direccion.S);
            else if (paso.getCoord().equals(paso.getCamino().SO())) direcciones.add(direccion.SO);
            else if (paso.getCoord().equals(paso.getCamino().O())) direcciones.add(direccion.O);
            
            paso = paso.getCamino();
        }
        
        // Devolver dirección
        Collections.reverse(direcciones);
        return destino != null;
    }

}
