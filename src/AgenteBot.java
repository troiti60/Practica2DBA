import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import edu.emory.mathcs.backport.java.util.Collections;
import java.io.IOException;
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
    private final String world;

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

        this.world = Lanzador.world;
        
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
        System.out.println("\nAgente Bot: Despedida mandado:\n" + despedida);

        // Enviar despedida
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
            int iter=0;
            while (vivo) {
                //Esperar nivel de batería de agente entorno
                System.out.println("\nAgente Bot: Esperando recibir mensaje...");

                ACLMessage inbox = this.receiveACLMessage();
                JsonElement MensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
                JsonElement valorResult = this.parse.getElement(MensajeRecibido, "bateria");
                nivelBateria = valorResult.getAsFloat();
                
                System.out.println("Agente Bot: nivel bateria recibido: "+nivelBateria);
                if(iter==0){ 
                    Coord auxz=triangularObjetivo2();
                    System.out.println("Coordenadas del objetivo obtenidas por triangulación: x="
                    +auxz.getX()+", y="+auxz.getY());
                }
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
                iter++;
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
        
        // Dibujar mapa
        try {
            this.mapa.dibujar();
        } catch (IOException ex) {}
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

    /**
    * Triangular objetivo:
    * @param mapa primera percepcion del mapa
    * @return una lista de coordenadas con los posibles objetivos
    * @author Jesús
    */
    public Coord triangularObjetivo(List<Nodo> mapa){
        /* 
        1. Si es esquina:
            1- Calculamos el angulo hacia el objetivo
            2- Sacamos un vector director entre dos puntos conocidos
            3- Rotamos ese vector por su angulo en direccion antihoraria
            4- Escalamos el vector hasta el objetivo
            5- Sacamos su coordenada
        2. Si no es esquina:
            1- Sacamos su coordenada directamente
        */
        Coord objetivo=new Coord(0,0);
        boolean esEsquina=false;
        boolean esEsquinaSuperior=false;
        float numeroDeCasillas;
        
        //calculamos la coordenada de la casilla mas cercana
        Coord casillaMasCercana=calcularCoordenadaMasCercana(mapa);
        //calculamos el nodo mas cercano al objetivo
        Nodo nodoMasCercano=calcularNodoMasCercano(mapa);
        //calculamos la distancia entre casillas adyacentes no diagonales
        float distanciaCasilla=distanciaCasilla(mapa);
        //calculamos la distancia entre casillas adyacentes diagonales
        float distanciaCasillaDiagonal=distanciaCasillaDiagonal(mapa);
        
        //calculamos si la casilla mas cercana es una esquina
        esEsquina=esEsquina(mapa,casillaMasCercana);
        esEsquinaSuperior=esEsquinaSuperior(mapa,casillaMasCercana);
                
        if(esEsquina){
            if(esEsquinaSuperior){
                int posicion=calcularPosicionDentroDelMapaMasCercano(mapa);
                Nodo nodoAuxiliar;
                
                if(posicion==0){
                    nodoAuxiliar=mapa.get(1);
                }else{
                    nodoAuxiliar=mapa.get(9);
                }
                
                float angulo=hallarAnguloAlObjetivo(nodoMasCercano.getScanner(),nodoAuxiliar.getScanner(),distanciaCasilla);
                float pos_i_nodo_cercano=casillaMasCercana.getX()*distanciaCasilla;
                float pos_j_nodo_cercano=casillaMasCercana.getY()*distanciaCasilla;
                
                float pos_i_nodo_auxiliar=nodoAuxiliar.getX()*distanciaCasilla;
                float pos_j_nodo_auxiliar=nodoAuxiliar.getY()*distanciaCasilla;
                
                List<Float> vector;
                List<Float> vectorRotado;
                List<Float> vectorRotadoEscalado;
                vector=hallarVectorEntreDosPuntos(pos_i_nodo_cercano,pos_j_nodo_cercano, pos_i_nodo_auxiliar, pos_j_nodo_auxiliar);
                vectorRotado=rotarVectorUnAngulo(vector.get(0),vector.get(1),angulo);
                
                vectorRotadoEscalado=productoEscalarVector(vectorRotado.get(0), vectorRotado.get(1), nodoMasCercano.getScanner()/distanciaCasilla);
                objetivo=sacarCoordenadaVector(vectorRotadoEscalado.get(0),vectorRotadoEscalado.get(1), distanciaCasilla);
            }else{
                int posicion=calcularPosicionDentroDelMapaMasCercano(mapa);
                Nodo nodoAuxiliar;
                
                if(posicion==20){
                    nodoAuxiliar=mapa.get(15);
                }else{
                    nodoAuxiliar=mapa.get(23);
                }
                
                float angulo=hallarAnguloAlObjetivo(nodoMasCercano.getScanner(),nodoAuxiliar.getScanner(),distanciaCasilla);
                float pos_i_nodo_cercano=casillaMasCercana.getX()*distanciaCasilla;
                float pos_j_nodo_cercano=casillaMasCercana.getY()*distanciaCasilla;
                
                float pos_i_nodo_auxiliar=nodoAuxiliar.getX()*distanciaCasilla;
                float pos_j_nodo_auxiliar=nodoAuxiliar.getY()*distanciaCasilla;
                
                List<Float> vector;
                List<Float> vectorRotado;
                List<Float> vectorRotadoEscalado;
                vector=hallarVectorEntreDosPuntos(pos_i_nodo_cercano,pos_j_nodo_cercano, pos_i_nodo_auxiliar, pos_j_nodo_auxiliar);
                vectorRotado=rotarVectorUnAngulo(vector.get(0),vector.get(1),angulo);
                
                vectorRotadoEscalado=productoEscalarVector(vectorRotado.get(0), vectorRotado.get(1), nodoMasCercano.getScanner()/distanciaCasilla);
                objetivo=sacarCoordenadaVector(vectorRotadoEscalado.get(0),vectorRotadoEscalado.get(1), distanciaCasilla);
            }
        }else{
            numeroDeCasillas=nodoMasCercano.getScanner()/distanciaCasilla;
            if(nodoMasCercano.getCoord()==mapa.get(9).getCoord() || nodoMasCercano.getCoord()==mapa.get(14).getCoord() || nodoMasCercano.getCoord()==mapa.get(19).getCoord()){
                objetivo.setX((casillaMasCercana.getX()+(int)numeroDeCasillas));
            }else if(nodoMasCercano.getCoord()==mapa.get(21).getCoord() || nodoMasCercano.getCoord()==mapa.get(22).getCoord() || nodoMasCercano.getCoord()==mapa.get(23).getCoord()){
                objetivo.setY((casillaMasCercana.getX()+(int)numeroDeCasillas));
            }else if(nodoMasCercano.getCoord()==mapa.get(5).getCoord() || nodoMasCercano.getCoord()==mapa.get(10).getCoord() || nodoMasCercano.getCoord()==mapa.get(15).getCoord()){
                objetivo.setX((casillaMasCercana.getX()-(int)numeroDeCasillas));
            }else if(nodoMasCercano.getCoord()==mapa.get(1).getCoord() || nodoMasCercano.getCoord()==mapa.get(2).getCoord() || nodoMasCercano.getCoord()==mapa.get(3).getCoord()){
                objetivo.setY((casillaMasCercana.getX()-(int)numeroDeCasillas));
            }
        }
        
        return objetivo;
    }
    /**
    * calcular coordenada mas cercana:
    * @param percepcion matriz del scanner
    * @return la coordenada mas cercana
    * @author Jesús
    */
    public Coord calcularCoordenadaMasCercana(List<Nodo> percepcion){
        Nodo nodoMasCercano=percepcion.get(0);
        Coord coordenadaMasCercana;
        for (Nodo nodo : percepcion) {
             if(nodoMasCercano.getScanner()>nodo.getScanner()){
                 nodoMasCercano=nodo;
             }
        }
        coordenadaMasCercana=nodoMasCercano.getCoord();
        
        return coordenadaMasCercana;
    }
    
    /**
    * calcular posicion dentro de la percepcion mas cercana:
    * @param percepcion matriz del scanner
    * @return posicion de la casilla mas cercana dentro de la percepcion
    * @author Jesús
    */
    public int calcularPosicionDentroDelMapaMasCercano(List<Nodo> percepcion){
        Nodo nodoMasCercano=percepcion.get(0);
        int posicion;

        for (posicion=0; posicion<percepcion.size(); posicion++) {
             if(nodoMasCercano.getScanner()>percepcion.get(posicion).getScanner()){
                 nodoMasCercano=percepcion.get(posicion);
             }
        }
        
        return posicion;
    }
    
    /**
    * calcular nodo mas cercano:
    * @param percepcion matriz del scanner
    * @return el nodo mas cercano al objetivo
    * @author Jesús
    */
    public Nodo calcularNodoMasCercano(List<Nodo> percepcion){
        Nodo nodoMasCercano=percepcion.get(0);

        for (Nodo nodo : percepcion) {
             if(nodoMasCercano.getScanner()>nodo.getScanner()){
                 nodoMasCercano=nodo;
             }
        }
        
        return nodoMasCercano;
    }
    
    /**
    * calcular coordenada mas cercana:
    * @param percepcion matriz del scanner
    * @return la distancia entre dos casillas adyacentes y no diagonales
    * @author Jesús
    */
    public float distanciaCasilla(List<Nodo> percepcion){
        float distancia;
        Nodo nodo_1=percepcion.get(0);
        Nodo nodo_2=percepcion.get(1);
        float nodo1=nodo_1.getScanner();
        float nodo2=nodo_2.getScanner();
        
        distancia=nodo1-nodo2;
        if(distancia<0){
            distancia=distancia*(-1);
        }
        
        return distancia;
    }
    
    /**
    * calcular coordenada mas cercana:
    * @param percepcion matriz del scanner
    * @return la distancia entre dos casillas adyacentes que sean diagonales
    * @author Jesús
    */
    public float distanciaCasillaDiagonal(List<Nodo> percepcion){
        float distanciaDiagonal;
        Nodo nodo_1=percepcion.get(0);
        Nodo nodo_2=percepcion.get(6);
        float nodo1=nodo_1.getScanner();
        float nodo2=nodo_2.getScanner();
        
        distanciaDiagonal=nodo1-nodo2;
        if(distanciaDiagonal<0){
            distanciaDiagonal=distanciaDiagonal*(-1);
        }
        
        return distanciaDiagonal;
    }
    /**
    * calcular si una coordenada es una esquina:
    * @param percepcion matriz del scanner
     * @param c
    * @return si es esquina o no de la matriz percepcion
    * @author Jesús
    */
    public boolean esEsquina(List<Nodo> percepcion, Coord c){
        boolean esEsquina=false;

        if(c==percepcion.get(0).getCoord()){
            esEsquina=true;
            
        }else if(c==percepcion.get(4).getCoord()){
            esEsquina=true;
            
        }else if(c==percepcion.get(20).getCoord()){
            esEsquina=true;
            
        }else if(c==percepcion.get(24).getCoord()){
            esEsquina=true;
            
        }
        
        return esEsquina;
    }
    
    /**
    * calcular si una coordenada es una esquina superior:
    * @param percepcion matriz del scanner
     * @param c
    * @return si es esquina o no de la matriz percepcion
    * @author Jesús
    */
    public boolean esEsquinaSuperior(List<Nodo> percepcion, Coord c){
        boolean esEsquinaSuperior=false;
        
        if(c==percepcion.get(0).getCoord()){
            esEsquinaSuperior=true;
            
        }else if(c==percepcion.get(4).getCoord()){
            esEsquinaSuperior=true; 
        }
        return esEsquinaSuperior;
    }
    /**
    * hallar el angulo de dos puntos hacia el objetivo para hallar la direccion:
    * @param distancia_A distancia al obejtivo
    * @param distancia_B dsitancia al objetivo
    * @param distancia_entre_los_puntos A y B
    * @return el angulo hacia el objetivo
    * @author Jesús
    */
    public float hallarAnguloAlObjetivo(float distancia_A, float distancia_B, float distancia_entre_los_puntos){
        double angulo=0;
        angulo=Math.acos(-((Math.pow(distancia_A, 2)-Math.pow(distancia_B, 2)-Math.pow(distancia_entre_los_puntos, 2))/(2*distancia_entre_los_puntos*distancia_A)));
        
        float ret=(float)angulo;
        return ret;
    }
    /**
    * hallar el angulo de dos puntos hacia el objetivo para hallar la direccion:
    * @param pos_i_pA posicion i del punto A
    * @param pos_j_pA posicion j del punto A
    * @param pos_i_pB posicion i del punto B
    * @param pos_j_pB posicion j del punto B
    * @return vector entre dos puntos, List(0) ->pos i, List(1) -> pos j
    * @author Jesús
    */
    public List<Float> hallarVectorEntreDosPuntos(float pos_i_pA, float pos_j_pA, float pos_i_pB, float pos_j_pB){
        List<Float> vector=new ArrayList();
        vector.add(pos_i_pB-pos_i_pA);
        vector.add(pos_j_pB-pos_j_pA);
        
        return vector;
    }
    
    /**
    * rotar un vector un angulo dado antihorario:
    * @param pos_i posicion i del vector
    * @param pos_j posicion j del vector
    * @param angulo angulo a rotar antihorario
    * @return vector rotado
    * @author Jesús
    */
    public List<Float> rotarVectorUnAngulo(float pos_i, float pos_j, float angulo){
        List<Float> vector=new ArrayList();
        float i=(float)((double)pos_i*Math.cos(angulo)-(double)pos_j*Math.sin(angulo));
        float j=(float)((double)pos_i*Math.sin(angulo)+(double)pos_j*Math.cos(angulo));
        
        vector.add(i);
        vector.add(j);
        
        return vector;
    }
    
    /**
    * rotar un vector un angulo dado antihorario:
    * @param pos_i posicion i del vector
    * @param pos_j posicion j del vector
    * @param k constante a multiplicar
    * @return vector escalado
    * @author Jesús
    */
    public List<Float> productoEscalarVector(float pos_i, float pos_j, float k){
        List<Float> vector=new ArrayList();
        float i=pos_i*k;
        float j=pos_j*k;
        
        vector.add(i);
        vector.add(j);
        
        return vector;
    }
    /**
    * rotar un vector un angulo dado antihorario:
    * @param pos_i posicion i del vector
    * @param pos_j posicion j del vector
     * @param distancia_entre_dos_puntos --
    * @return coordenadas del mapa
    * @author Jesús
    */
    public Coord sacarCoordenadaVector(float pos_i, float pos_j, float distancia_entre_dos_puntos){
        int coordenada_x=(int)(pos_i/distancia_entre_dos_puntos);
        int coordenada_y=(int)(pos_j/distancia_entre_dos_puntos);
        
        Coord c=new Coord(coordenada_x,coordenada_y);
        
        return c;
    }
    
    /**
     * Función para triangular la posición del objetivo a partir de los datos
     * del scanner en la última percepción
     * @author Antonio Troitiño del Río
     * @return objetivo: posición del objetivo 
     */
        public Coord triangularObjetivo2(){
        Coord objetivo=new Coord(0,0);
        ArrayList<Nodo> perception = mapa.getLastPerception();
        Coord A,C;
        A=perception.get(20).getCoord();
        C=perception.get(0).getCoord();
        double b=distanciaEuclidea(A,C);
        double a=perception.get(0).getScanner();
        double c=perception.get(20).getScanner();
        double alpha = Math.acos(((b*b)+(c*c)-(a*a))/(2.0*b*c));

        double x,x1,y,y1;
        x=(C.getX()-A.getX())/b;
        x=x*c;
        y=(C.getY()-A.getY())/b;
        y=y*c;
        if(perception.get(0).getScanner()<perception.get(1).getScanner()){
        x1=x*Math.cos(alpha)+y*Math.sin(alpha)+A.getX();
        y1=y*Math.cos(alpha)-x*Math.sin(alpha)+A.getY();
        }else{
        x1=x*Math.cos(alpha)-y*Math.sin(alpha)+A.getX();
        y1=y*Math.cos(alpha)+x*Math.sin(alpha)+A.getY();  
        }
        objetivo.setX(redondear(x1));
        objetivo.setY(redondear(y1));
        mapa.setObjetivoTriangulado(objetivo);
        return objetivo;
    };
    /**
     * Método para calcular la distancia euclídea entre dos puntos especificados
     * mediante objetos de tipo Coord
     * @param A Coordenada del primer punto
     * @param B Coordenada del segundo punto
     * @author Antonio Troitiño del Río
     * @return distancia euclídea entre ambos puntos
     */
    public double distanciaEuclidea(Coord A, Coord B){
        double res = Math.sqrt(((B.getX()-A.getX())*(B.getX()-A.getX()))+((B.getY()-A.getY())*(B.getY()-A.getY())));
        return res;
    }
    /**
     * Método para redondear un double a int, de forma que si la parte decimal
     * vale más de 0.5 devuelva el siguiente entero, y el anterior en caso contrario
     * @author Antonio Troitiño
     * @param num numero a redondear
     * @return 
     */
    public int redondear(double num){
        double aux=num;
        int res=0;
        res=(int) aux;
        if(aux>0){
        aux=aux-res;
        if(aux>0.5) res++;
        }
        else{
        aux=aux-res;
        if(aux<-0.5) res--;
        }
        return res;
    }
    
       /**
     * Dada una coordenada devuelve las coordenadas de la dirección deseada
     *
     * @author Fco Javier Ortega Rodríguez
     * @param direccion  0=NW    1=N     2=NE
     *                   3=W      X      4=E
     *                   5=SW    6=S     7=SE
     * @param coordenada Coordenada donde situarse
     * @return Coordenada de la direccion
     */
    public Coord getAdyacente(Coord coordenada, int direccion){
        Coord miCoordenada = new Coord(coordenada.getX(), coordenada.getY());
       
        switch(direccion){
            case 0:
                System.out.print("NW ");
                miCoordenada.setX( coordenada.getX()-1 );
                miCoordenada.setY( coordenada.getY()-1 );
                break;
            case 1:
                System.out.print("N ");
                miCoordenada.setY( coordenada.getY()-1 );
                break;
            case 2:
                System.out.print("NE ");
                miCoordenada.setX( coordenada.getX()-1 );
                miCoordenada.setY( coordenada.getY()+1 );
                break;
            case 3:
                System.out.print("W ");
                miCoordenada.setX( miCoordenada.getX()-1 );
                break;
            case 4:
                System.out.print("E ");
                miCoordenada.setX( miCoordenada.getX()+1 );
                break;
            case 5:
                System.out.print("SW ");
                miCoordenada.setX( miCoordenada.getX()-1 );
                miCoordenada.setY( miCoordenada.getY()+1 );
                break;
            case 6:
                System.out.print("S ");
                miCoordenada.setY( miCoordenada.getY()+1 );
                break;
            case 7:
                System.out.print("SE ");
                miCoordenada.setX( miCoordenada.getX()+1 );
                miCoordenada.setX( miCoordenada.getX()+1 );
                break;
        }
       
        return miCoordenada;
    }
   
    /**
     * Metodo que explora el mapa para determinar si puede existir solución o ésta es inalcanzable
     * @author Jco Javier Ortega Rodríguez
     * @param destino Coordenadas de destino
     * @return false si es imposible llegar al destino, true cualquier otro caso
     */
    public boolean puedeExistirSolucion(Coord destino){
        ArrayList<Coord> abiertos = new ArrayList<>();
        ArrayList<Coord> cerrados = new ArrayList<>();
        Coord aux, actual;
        boolean solucion = false;
       
        cerrados.add(destino);
       
        //System.out.println("Destino: (" + destino.getX() + "," + destino.getY() + ")");
        //System.out.print("Hijos generados: ");
       
        for(int i=0; i<8; i++){
            aux = getAdyacente(destino, i);
            abiertos.add( aux );
            //System.out.print("(" + aux.getX() + "," + aux.getY() + ") ");
        }
       
        //System.out.print("\n");
       
        while(abiertos.size()>0){
            aux = abiertos.get(0);
            cerrados.add(aux);
            abiertos.remove(0);
           
            //System.out.println("\nExpandiendo: (" + aux.getX() + "," + aux.getY() + ")");
           
            for(int i=0; i<8; i++){
                actual = getAdyacente(aux, i);
                //System.out.println("Actual (" + actual.getX() + "," + actual.getY() + ")");
               
                if(mapa.getConectado().containsKey( actual )){
                    //System.out.println("Solución alcanzable");
                    return true;
                }
                else{
                    //System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") no estaba conectada");
                   
                    if(!mapa.getMuros().containsKey( actual ) && !cerrados.contains( actual )){
                            //System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") estaba sin explorar");
                           
                            if(actual.getX()>=0 && actual.getY()>=0){
                                //System.out.println("añadia a abiertos");
                                abiertos.add( actual );
                            }else{
                                //System.out.println("fuera del mapa");
                            }
                    }
                }
            }
        }
        return solucion;
    }
}
