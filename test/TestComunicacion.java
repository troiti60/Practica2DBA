import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

public class TestComunicacion {

    public static void main(String[] args) {
        DatosAcceso datac = new DatosAcceso();
        AgentsConnection.connect(datac.getHost(), datac.getPort(), datac.getVirtualHost(), datac.getUsername(), datac.getPassword(), datac.getSSL());
        
        TestAgenteComunicacion bot;
        
        try {
            bot = new TestAgenteComunicacion(new AgentID("Agente para testear la coneción y comunicación"), datac);
        } catch (Exception e) {
            System.err.println("Error creando el agente.\n" + e.getMessage());
            return;
        }
        
        bot.start();
    }
    
}
