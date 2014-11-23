import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import edu.emory.mathcs.backport.java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

/**
 * Agente que se encarga de la toma de decisiones
 *
 * @author Javier Ortega Rodríguez, Alexander Straub, José Carlos Alfaro, Antonio Troitiño
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

    /**
     * Constructor
     *
     * @param aid ID del agente
     * @param agenteEntorno ID del agente Entorno para comunicarse
     * @throws Exception
     * @author Javier Ortega Rodríguez
     */
    public AgenteBot(AgentID aid, AgentID agenteEntorno) throws Exception {
        super(aid);
        this.agenteEntorno = agenteEntorno;

        this.world = Lanzador.world;

        this.parse = new JsonDBA();
        this.datac = DatosAcceso.crearInstancia();
        this.mapa = Mapa.crearInstancia();

        this.camino = new ArrayList<>();
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
     * @return Respuesta del servidor
     * @throws InterruptedException
     * @author Javier Ortega Rodríguez, José Carlos Alfaro
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
     * @param d Acción o dirección a tomar
     * @return Respuesta del servidor
     * @throws InterruptedException
     * @author Javier Ortega Rodríguez, José Carlos Alfaro
     */
    private String RealizarAccion(Accion d) throws InterruptedException {
        // Crear json string para pedir que el servidor haga una acción
        // LinkedHashMap = HashMap pero respetando el orden de insercion
        LinkedHashMap lhmap = new LinkedHashMap();
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
     * Método main del bot
     *
     * @author Javier Ortega Rodríguez, Alexander Straub
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

            int iteracion = 0;

            while (vivo) {
                try { // DEBUG
                    if (iteracion % 50 == 0) { // DEBUG
                        this.mapa.dibujar(iteracion); // DEBUG
                    } // DEBUG
                } catch (Exception ex) { // DEBUG
                } // DEBUG

                //Esperar nivel de batería de agente entorno
                System.out.println("\nAgente Bot: Esperando recibir mensaje...");

                ACLMessage inbox = this.receiveACLMessage();
                JsonElement MensajeRecibido = this.parse.recibirRespuesta(inbox.getContent());
                JsonElement valorResult = this.parse.getElement(MensajeRecibido, "bateria");
                nivelBateria = valorResult.getAsFloat();

                System.out.println("Agente Bot: nivel bateria recibido: " + nivelBateria);

                // Añadir área mala que no queremos visitar
                if (vivo) {
                    // Recoger todos los nodos descubiertos y el nodo actual
                    Nodo posNodo = this.mapa.getConectado().get(this.mapa.getCoord());
                    Collection<Nodo> nodos = this.mapa.getConectado().values();

                    for (Iterator<Nodo> it = nodos.iterator(); it.hasNext();) {
                        Nodo otroNodo = it.next();

                        HashMap<Coord, Nodo> areaMala = new HashMap<>(500);

                        // Si no es el mismo nodo
                        if (otroNodo != posNodo && otroNodo.isVisitado()) {
                            // Calcular vector entre los nodos
                            Vector vec = posNodo.getCoord().sub(otroNodo.getCoord());

                            // Calcular nuevos coordenadas en dirección del vector
                            Vector curPos = vecAdd(otroNodo.getCoord(), vec);
                            Coord curCoord = new Coord((int) Math.round((double) curPos.get(0)),
                                    (int) Math.round((double) curPos.get(1)));

                            // Hasta llegar al nodo actual (parar si llegamos a un nodo descubierto)
                            while (!curCoord.equals(posNodo.getCoord())
                                    && !this.mapa.getNoConectado().containsKey(curCoord)
                                    && (!this.mapa.getMuros().containsKey(curCoord)
                                    || this.mapa.getMuros().get(curCoord).getRadar() == 3)) {

                                // Añadir área mala
                                areaMala.put(curCoord, new Nodo(curCoord, 3, 0.0f));

                                // Calcular próximo nodo
                                curPos = vecAdd(curPos, vec);
                                curCoord = new Coord((int) Math.round((double) curPos.get(0)),
                                        (int) Math.round((double) curPos.get(1)));
                            }

                            // Añadir área mala a la lista de muros
                            if (!this.mapa.getMuros().containsKey(curCoord)
                                    || this.mapa.getMuros().get(curCoord).getRadar() == 3) {

                                for (Iterator<Nodo> it2 = areaMala.values().iterator(); it2.hasNext();) {
                                    this.mapa.addNodo(it2.next());
                                }
                            }
                        }
                    }
                }

                // Si es la primera iteración, triangular coordenadas del destino
                if (iteracion == 0) {
                    Coord auxz = triangularObjetivo();
                    System.out.println("Coordenadas del objetivo obtenidas por triangulación: x="
                            + auxz.getX() + ", y=" + auxz.getY());
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

                // Realizar acción
                if (vivo) {
                    String respuesta = this.RealizarAccion(decision);

                    if (!respuesta.contains("OK")) {
                        vivo = false;
                        System.err.println("Agente Bot: Resultado inesperado\n" + respuesta);
                    }
                }

                iteracion++;
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
        } catch (IOException ex) {
        }
    }

    /**
     * Adición de vectores
     *
     * @param a Vector
     * @param b Vector
     * @return Suma
     * @author Alexander Straub
     */
    private Vector vecAdd(Vector a, Vector b) {
        Vector res = new Vector(2);
        res.add((double) a.get(0) + (double) b.get(0));
        res.add((double) a.get(1) + (double) b.get(1));

        return res;
    }

    /**
     * Adición de coordenadas con un vector
     *
     * @param a Coordenadas
     * @param b Vector
     * @return Suma
     * @author Alexander Straub
     */
    private Vector vecAdd(Coord a, Vector b) {
        Vector res = new Vector(2);
        res.add((double) a.getX() + (double) b.get(0));
        res.add((double) a.getY() + (double) b.get(1));

        return res;
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
        Nodo nodoInicial = map.get(this.mapa.getCoord());

        // Si el objetivo está al lado
        if (map.get(nodoInicial.NO()) != null && map.get(nodoInicial.NO()).getRadar() == 2) {
            return Accion.NO;
        }
        if (map.get(nodoInicial.N()) != null && map.get(nodoInicial.N()).getRadar() == 2) {
            return Accion.N;
        }
        if (map.get(nodoInicial.NE()) != null && map.get(nodoInicial.NE()).getRadar() == 2) {
            return Accion.NE;
        }
        if (map.get(nodoInicial.E()) != null && map.get(nodoInicial.E()).getRadar() == 2) {
            return Accion.E;
        }
        if (map.get(nodoInicial.SE()) != null && map.get(nodoInicial.SE()).getRadar() == 2) {
            return Accion.SE;
        }
        if (map.get(nodoInicial.S()) != null && map.get(nodoInicial.S()).getRadar() == 2) {
            return Accion.S;
        }
        if (map.get(nodoInicial.SO()) != null && map.get(nodoInicial.SO()).getRadar() == 2) {
            return Accion.SO;
        }
        if (map.get(nodoInicial.O()) != null && map.get(nodoInicial.O()).getRadar() == 2) {
            return Accion.O;
        }

        // Inicializar
        List<Nodo> nodos = new ArrayList(map.values());
        for (Iterator<Nodo> i = nodos.iterator(); i.hasNext();) {
            i.next().resetBusqueda();
        }
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
                            + minNodo.distanciaA(vecino);

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
     * Función para triangular la posición del objetivo a partir de los datos
     * del scanner en la última percepción
     *
     * @return Objetivo Posición del objetivo
     * @author Antonio Troitiño
     */
    private Coord triangularObjetivo() {
        Coord objetivo = new Coord(0, 0);
        ArrayList<Nodo> perception = this.mapa.getLastPerception();
        Coord A, C;
        A = perception.get(20).getCoord();
        C = perception.get(0).getCoord();
        double b = distanciaEuclidea(A, C);
        double a = perception.get(0).getScanner();
        double c = perception.get(20).getScanner();
        double alpha = Math.acos(((b * b) + (c * c) - (a * a)) / (2.0 * b * c));

        double x, x1, y, y1;
        x = (C.getX() - A.getX()) / b;
        x = x * c;
        y = (C.getY() - A.getY()) / b;
        y = y * c;
        if (perception.get(0).getScanner() < perception.get(1).getScanner()) {
            x1 = x * Math.cos(alpha) + y * Math.sin(alpha) + A.getX();
            y1 = y * Math.cos(alpha) - x * Math.sin(alpha) + A.getY();
        } else {
            x1 = x * Math.cos(alpha) - y * Math.sin(alpha) + A.getX();
            y1 = y * Math.cos(alpha) + x * Math.sin(alpha) + A.getY();
        }
        objetivo.setX((int)Math.round(x1));
        objetivo.setY((int)Math.round(y1));
        this.mapa.setObjetivoTriangulado(objetivo);
        return objetivo;
    }

    /**
     * Método para calcular la distancia euclídea entre dos puntos especificados
     * mediante objetos de tipo Coord
     * 
     * @param A Coordenada del primer punto
     * @param B Coordenada del segundo punto
     * @return Distancia euclídea entre ambos puntos
     * @author Antonio Troitiño
     */
    private double distanciaEuclidea(Coord A, Coord B) {
        return Math.sqrt(((B.getX() - A.getX()) * (B.getX() - A.getX())) + ((B.getY() - A.getY()) * (B.getY() - A.getY())));
    }

    /**
     * Dada una coordenada devuelve las coordenadas de la dirección deseada
     *
     * @param direccion 0=NW 1=N 2=NE 3=W X 4=E 5=SW 6=S 7=SE
     * @param coordenada Coordenada donde situarse
     * @return Coordenada de la direccion
     * @author Javier Ortega Rodríguez
     */
    private Coord getAdyacente(Coord coordenada, int direccion) {
        Coord miCoordenada = new Coord(coordenada.getX(), coordenada.getY());

        switch (direccion) {
            case 0:
                System.out.print("NW ");
                miCoordenada.setX(coordenada.getX() - 1);
                miCoordenada.setY(coordenada.getY() - 1);
                break;
            case 1:
                System.out.print("N ");
                miCoordenada.setY(coordenada.getY() - 1);
                break;
            case 2:
                System.out.print("NE ");
                miCoordenada.setX(coordenada.getX() - 1);
                miCoordenada.setY(coordenada.getY() + 1);
                break;
            case 3:
                System.out.print("W ");
                miCoordenada.setX(miCoordenada.getX() - 1);
                break;
            case 4:
                System.out.print("E ");
                miCoordenada.setX(miCoordenada.getX() + 1);
                break;
            case 5:
                System.out.print("SW ");
                miCoordenada.setX(miCoordenada.getX() - 1);
                miCoordenada.setY(miCoordenada.getY() + 1);
                break;
            case 6:
                System.out.print("S ");
                miCoordenada.setY(miCoordenada.getY() + 1);
                break;
            case 7:
                System.out.print("SE ");
                miCoordenada.setX(miCoordenada.getX() + 1);
                miCoordenada.setX(miCoordenada.getX() + 1);
                break;
        }

        return miCoordenada;
    }

    /**
     * Metodo que explora el mapa para determinar si puede existir solución o
     * ésta es inalcanzable
     *
     * @param destino Coordenadas de destino
     * @return false si es imposible llegar al destino, true cualquier otro caso
     * @author Javier Ortega Rodríguez
     */
    private boolean puedeExistirSolucion(Coord destino) {
        ArrayList<Coord> abiertos = new ArrayList<>();
        ArrayList<Coord> cerrados = new ArrayList<>();
        Coord aux, actual;
        boolean solucion = false;

        cerrados.add(destino);

        //System.out.println("Destino: (" + destino.getX() + "," + destino.getY() + ")");
        //System.out.print("Hijos generados: ");
        for (int i = 0; i < 8; i++) {
            aux = getAdyacente(destino, i);
            abiertos.add(aux);
            //System.out.print("(" + aux.getX() + "," + aux.getY() + ") ");
        }

        //System.out.print("\n");
        while (abiertos.size() > 0) {
            aux = abiertos.get(0);
            cerrados.add(aux);
            abiertos.remove(0);

            //System.out.println("\nExpandiendo: (" + aux.getX() + "," + aux.getY() + ")");
            for (int i = 0; i < 8; i++) {
                actual = getAdyacente(aux, i);
                //System.out.println("Actual (" + actual.getX() + "," + actual.getY() + ")");

                if (mapa.getConectado().containsKey(actual)) {
                    //System.out.println("Solución alcanzable");
                    return true;
                } else {
                    //System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") no estaba conectada");

                    if (!mapa.getMuros().containsKey(actual) && !cerrados.contains(actual)) {
                            //System.out.println("La coordenada (" + actual.getX() + "," + actual.getY() + ") estaba sin explorar");

                        if (actual.getX() >= 0 && actual.getY() >= 0) {
                            //System.out.println("añadia a abiertos");
                            abiertos.add(actual);
                        } else {
                            //System.out.println("fuera del mapa");
                        }
                    }
                }
            }
        }
        
        return solucion;
    }

}
