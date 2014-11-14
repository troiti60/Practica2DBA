import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 * Clase principal que gestiona el lanzamiento y control de los agentes
 *  Modificaciones: 20-10-14 13:30 Javier Ortega Estructura y Bot
 *  Modificaciones: 06-11-14 13:00 Javier Ortega Mapa y agente Entorno
 * 
 * @author Fco Javier Ortega Rodríguez
 * 
*/
public class Lanzador {

    /**
     * Método main
     * 
     * @param args 
     * @author Fco Javier Ortega Rodríguez
     */
    public static void main(String[] args) {
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
            agenteBot = new AgenteBot(IDBot,IDEntorno);
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
