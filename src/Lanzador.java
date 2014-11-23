import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 * Clase principal que gestiona el lanzamiento y control de los agentes
 *
 * @author Javier Ortega Rodríguez
 */
public class Lanzador {

    /**
     * El mapa a "jugar".
     *
     * Se puede eligir entre:
     *  + plainworld
     *  + earthquake1
     *  + earthquake2
     *  + earthquake3
     *  + earthquake4
     *  + largeearthquake1
     *  + largeearthquake2
     *  + largeearthquake3
     *  + indoor1
     */
    public static final String world = "largeearthquake2";
    public static int tamano = 100;

    /**
     * Método main
     *
     * @param args Parametros (no utilizados)
     * @author Javier Ortega Rodríguez
     */
    public static void main(String[] args) {
        // Guardar tamaño del mapa para dibujar las imagenes del mundo
        // (Solamente se usa este variable para crear imagenes, no tiene nada
        // que ver con la búsqueda del camino)
        if (Lanzador.world.contains("large")
                || Lanzador.world.contains("indoor")) {
            Lanzador.tamano = 500;
        }

        // Instanciación del agente
        AgenteBot agenteBot;
        AgenteEntorno agenteEntorno;

        // Instanciación de la clase que contiene los datos de acceso
        DatosAcceso datac = DatosAcceso.crearInstancia();
        AgentID IDBot = new AgentID(DatosAcceso.getNombreBotPrincipal());
        AgentID IDEntorno = new AgentID(DatosAcceso.getNombreBotEntorno());

        // Crear connección al servidor
        System.out.print("Iniciando...\n");
        System.out.print("Conectando... ");
        AgentsConnection.connect(datac.getHost(), datac.getPort(),
                datac.getVirtualHost(), datac.getUsername(),
                datac.getPassword(), datac.getSSL());
        System.out.print("OK\n");

        try {
            // Crear los agentes
            System.out.print("Inicializando bot principal...");
            agenteBot = new AgenteBot(IDBot, IDEntorno);
            System.out.print("OK\n");

            System.out.print("Inicializando bot entorno...");
            agenteEntorno = new AgenteEntorno(IDEntorno, IDBot);
            System.out.print("OK\n");

            // Lanzamiento de las "hebras" de los agentes
            agenteEntorno.start();
            agenteBot.start();
        } catch (Exception e) {
            System.err.println("\n----ERROR----\n" + e.getMessage());
            return;
        }

        System.out.println("\nLanzamiento terminado");
    }

}
