/*
* Primero hago el pull
    toqueteo
    commit con su comentario
    push
 */
import com.sun.org.apache.xpath.internal.operations.Bool;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
* Clase principal que gestiona el lanzamiento y control de los agentes
* Modificaciones: 20-10-14 13:30 Javier Ortega
*   Estructura y Bot
* Modificaciones: 06-11-14 13:00 Javier Ortega
*   Mapa y agente Entorno
* 
* @author Fco Javier Ortega Rodríguez
* @throws Exception Para controlar la instanciación de los agentes
* 
*/

public class Lanzador {
    public static void main(String[] args) {	
        // Instanciación del agente
        AgenteBot agenteBot;
        AgenteEntorno agenteEntorno;
        // Instanciación de la clase que contiene los datos de acceso
	DatosAcceso datac = new DatosAcceso();
        Mapa miMapa = Mapa.crearInstancia();
		
	System.out.println("Iniciando...");
        System.out.print("Conectando...");
        AgentsConnection.connect(datac.getHost(), datac.getPort(), datac.getVirtualHost(), datac.getUsername(), datac.getPassword(), datac.getSSL());
        System.out.print("OK\n");

        try {
            System.out.print("Inicializando bot principal...");
            agenteBot = new AgenteBot(new AgentID("botPrincipal"));
            System.out.print("OK\n");
            System.out.print("Inicializando bot entorno...");
            agenteEntorno = new AgenteEntorno(new AgentID("botEntorno"),miMapa);
            System.out.print("OK\n");
            
            // Lanzamiento de las "hebras" de los agentes
            agenteEntorno.start();
            agenteBot.start();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.err.println("\n----ERROR----\n" + e.getMessage());
            }
        
            System.out.println("\nFIN");
    }

}
