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
 * Agente que se encarga de la toma de decisiones Modificaciones: 20-10-14 13:30
 * Javier Ortega
 *
 * @author Fco Javier Ortega Rodríguez
 */
public class AgenteBot extends SingleAgent {

    /**
     * Guardar la ID del agente Entorno para comunicarse
     */
    private final AgentID agenteEntorno;

    /**
     * Guardar el world
     */
    private final String world = "plainworld";

    /**
     * Parser y datos
     */
    private final JsonDBA parse;
    private final DatosAcceso datac;
    private final Mapa mapa;

    /**
     * Variables para guardar información del camino enontrado
     */
    private final ArrayList<Accion> camino;
    private boolean llegarAlDestino;

    /**
     * Constructor
     *
     * @param aid ID del agente
     * @param agenteEntorno ID del agente Entorno para comunicarse
     * @throws Exception
     * @author
     */
    public AgenteBot(AgentID aid, AgentID agenteEntorno) throws Exception {
        super(aid);
        this.agenteEntorno = agenteEntorno;

        this.parse = new JsonDBA();
        this.datac = DatosAcceso.crearInstancia();
        this.mapa = Mapa.crearInstancia();

        this.camino = new ArrayList<Accion>();
        this.llegarAlDestino = false;
    }

    /**
     * Enum de las acciones posibles
     *
     * @author Alexander Straub
     */
    public enum Accion {

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
        private Accion(final String command) {
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

    /**
     * Método inicial de logueo en el servidor
     *
     * @author Fco Javier Ortega Rodríguez y José Carlos Alfaro
     * @return Respuesta del servidor
     * @throws InterruptedException
     */
    public String Saludo() throws InterruptedException {
        // Crear string para saludar al servidor
        String saludo = this.parse.login(this.world,
                this.agenteEntorno.getLocalName(), this.agenteEntorno.getLocalName(),
                this.agenteEntorno.getLocalName(), this.agenteEntorno.getLocalName());
        System.out.println("\nAgente Bot: Saludo mandado:\n" + saludo);

        // Enviar saludo
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID(this.datac.getVirtualHost()));
        outbox.setContent(saludo);
        this.send(outbox);

        System.out.println("\nAgente Bot: Esperando recibir mensaje...");

        // Recibir respuesta
        ACLMessage inbox = this.receiveACLMessage();
        JsonElement mensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = this.parse.getElement(mensajeRecibido, "result");

        System.out.println("Agente Bot: Mensaje recibido: " + valorResult.getAsString()
                + " de " + inbox.getSender().getLocalName());

        // Devolver respuesta
        return valorResult.getAsString();
    }
    
    /**
     * Método para deslogearse del servidor
     * 
     * @return Respuesta del servidor
     * @throws InterruptedException 
     * @author Alexander Straub
     */
    public String despedida() throws InterruptedException {
        // Crear json string para despedirse
        LinkedHashMap lhmap = new LinkedHashMap();
        lhmap.put("command", "logout");
        lhmap.put("key", this.datac.getKey());
        String despedida = this.parse.crearJson(lhmap);
        System.out.println("\nAgente Bot: Saludo mandado:\n" + despedida);

        // Enviar saludo
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID(this.datac.getVirtualHost()));
        outbox.setContent(despedida);
        this.send(outbox);

        System.out.println("\nAgente Bot: Esperando recibir mensaje...");

        // Recibir respuesta
        ACLMessage inbox = this.receiveACLMessage();
        JsonElement mensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = this.parse.getElement(mensajeRecibido, "result");

        System.out.println("Agente Bot: Mensaje recibido: " + valorResult.getAsString()
                + " de " + inbox.getSender().getLocalName());

        // Devolver respuesta
        return valorResult.getAsString();
    }

    /**
     * Método que manda la acción de repostar o el tipo de movimiento al
     * servidor
     *
     * @author Fco Javier Ortega Rodríguez y José Carlos Alfaro
     * @param d Acción o dirección a tomar
     * @return Respuesta del servidor
     * @throws InterruptedException
     */
    private String RealizarAccion(Accion d) throws InterruptedException {
        // Crear json string para pedir que el servidor haga una acción
        LinkedHashMap lhmap = new LinkedHashMap(); // HashMap pero respetando el orden de insercion
        lhmap.put("command", d.toString());
        lhmap.put("key", this.datac.getKey());
        String accion = this.parse.crearJson(lhmap);

        System.out.println("\nAgente Bot: Acción mandada:\n" + accion);

        // Enviar mensaje
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID(this.datac.getVirtualHost()));
        outbox.setContent(accion);
        this.send(outbox);

        System.out.println("\nAgente Bot: Esperando recibir mensaje...");

        // Recibir respuesta
        ACLMessage inbox = this.receiveACLMessage();
        JsonElement MensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = this.parse.getElement(MensajeRecibido, "result");

        System.out.println("Agente Bot: Mensaje recibido: " + valorResult.getAsString()
                + " de " + inbox.getSender().getLocalName());

        // Devolver respuesta
        return valorResult.getAsString();
    }

    /**
     *
     *
     * @author Fco Javier Ortega Rodríguez
     */
    @Override
    public void execute() {
        boolean vivo, keyCorrecto;
        float nivelBateria;
        float MIN_BATERIA = 3.0f;

        System.out.println("\n\nAgente Bot iniciado");

        try {
            // Enviar saludo y recibir clave de sesión
            System.out.print("\nAgente Bot: Saludando...");
            String key = this.Saludo();

            keyCorrecto = !key.contains("BAD_");

            if (keyCorrecto) {
                System.out.println("Agente Bot: Key correcta");
                this.datac.setKey(key);
                vivo = true;
            } else {
                System.err.println("Agente Bot: Key incorrecta");
                vivo = false;
            }

            while (vivo) {
                //Esperar nivel de batería de agente entorno
                System.out.println("\nAgente Bot: Esperando recibir mensaje...");

                ACLMessage inbox = this.receiveACLMessage();
                JsonElement MensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
                JsonElement valorResult = this.parse.getElement(MensajeRecibido, "bateria");
                nivelBateria = valorResult.getAsFloat();
                
                System.out.println("Agente Bot: nivel bateria recibido: "+nivelBateria);
                
                // Tomar decisión
                Accion decision = null;
                if (this.mapa.getConectado().get(this.mapa.getCoord()).getRadar() == 2) {
                    // Ya hemos llegado
                    String respuesta = despedida();
                    System.out.println("Agente Bot: Llegado al destino\n" + respuesta);
                    vivo = false;
                } else if (nivelBateria <= MIN_BATERIA) {
                    decision = Accion.R;
                } else {
                    try {
                        decision = this.busqueda();
                    } catch (Exception ex) {
                        System.err.println("Agente Bot: Búsqueda falló\n" + ex.getMessage());
                        vivo = false;
                    }
                }

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
                /*if (nivelBateria <= MIN_BATERIA) {
                    decision = Accion.R;
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
                }*/

                // Realizar acción
                if (vivo) {
                    String respuesta = this.RealizarAccion(decision);

                    if (!respuesta.contains("OK")) {
                        vivo = false;
                        System.err.println("Agente Bot: Resultado inesperado\n" + respuesta);
                    }
                }
            }
        } catch (InterruptedException e) {
            System.err.println("\n----ERROR EN BOT PRINCIPAL----\n" + e.getMessage());
        }

        // Matar al agente Entorno
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(this.agenteEntorno);
        outbox.setContent("Muere hijofruta!");
        this.send(outbox);
    }

    /**
     * La búsqueda para encontrar el mejor (con la información que ya tenemos)
     * camino
     *
     * @return La dirección en que el bot debe moverse
     * @throws Exception
     * @author Alexander Straub
     */
    private Accion busqueda() throws Exception {
        // Recoger el mapa
        HashMap<Coord, Nodo> map = this.mapa.getConectado();

        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext();) {
            i.next().resetBusqueda();
        }
        Nodo nodoInicial = map.get(this.mapa.getCoord());
        nodoInicial.setDistancia(0.0);

        // Trata como el nodo destino
        double distancia = Double.MAX_VALUE;
        Nodo camino = null;
        Nodo destino = null;

        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo) Collections.min(nodos);
            nodos.remove(minNodo);

            if (minNodo.getRadar() == 2) {
                destino = minNodo;
            }

            // Recoger los vecinos del nodo actual
            for (Iterator<Nodo> i = minNodo.getAdy().iterator(); i.hasNext();) {
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
        if (destino != null) {
            camino = destino;
        }
        Nodo paso = camino;
        while (paso != null && paso.getCamino() != nodoInicial) {
            paso = paso.getCamino();
        }

        // Devolver dirección
        if (paso != null && paso.getCoord().equals(nodoInicial.NO())) {
            return Accion.NO;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.N())) {
            return Accion.N;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.NE())) {
            return Accion.NE;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.E())) {
            return Accion.E;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.SE())) {
            return Accion.SE;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.S())) {
            return Accion.S;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.SO())) {
            return Accion.SO;
        }
        if (paso != null && paso.getCoord().equals(nodoInicial.O())) {
            return Accion.O;
        }

        throw new Exception();
    }

    /**
     * La búsqueda para encontrar el mejor (con la información que ya tenemos)
     * camino
     *
     * @return La dirección en que el bot debe moverse
     * @author Alexander Straub
     */
    private boolean busqueda(ArrayList<Accion> direcciones) {
        // Recoger el mapa
        HashMap<Coord, Nodo> map = this.mapa.getConectado();

        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext();) {
            i.next().resetBusqueda();
        }
        Nodo nodoInicial = map.get(this.mapa.getCoord());
        nodoInicial.setDistancia(0.0);

        // Trata como el nodo destino
        double distancia = Double.MAX_VALUE;
        Nodo camino = null;
        Nodo destino = null;

        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo) Collections.min(nodos);
            nodos.remove(minNodo);

            if (minNodo.getRadar() == 2) {
                destino = minNodo;
            }

            // Recoger los vecinos del nodo actual
            for (Iterator<Nodo> i = minNodo.getAdy().iterator(); i.hasNext();) {
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
        if (destino != null) {
            camino = destino;
        }
        Nodo paso = camino;
        while (paso != nodoInicial && paso != null) {
            if (paso.getCoord().equals(paso.getCamino().NO())) {
                direcciones.add(Accion.NO);
            } else if (paso.getCoord().equals(paso.getCamino().N())) {
                direcciones.add(Accion.N);
            } else if (paso.getCoord().equals(paso.getCamino().NE())) {
                direcciones.add(Accion.NE);
            } else if (paso.getCoord().equals(paso.getCamino().E())) {
                direcciones.add(Accion.E);
            } else if (paso.getCoord().equals(paso.getCamino().SE())) {
                direcciones.add(Accion.SE);
            } else if (paso.getCoord().equals(paso.getCamino().S())) {
                direcciones.add(Accion.S);
            } else if (paso.getCoord().equals(paso.getCamino().SO())) {
                direcciones.add(Accion.SO);
            } else if (paso.getCoord().equals(paso.getCamino().O())) {
                direcciones.add(Accion.O);
            }

            paso = paso.getCamino();
        }

        // Devolver dirección
        Collections.reverse(direcciones);
        return destino != null;
    }

}
