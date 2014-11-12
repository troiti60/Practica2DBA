import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

public class TestAgenteComunicacion extends SingleAgent {

    private final DatosAcceso datac;
    
    public TestAgenteComunicacion(AgentID aid, DatosAcceso datac) throws Exception {
        super(aid);
        this.datac = datac;
    }
    
    @Override
    public void execute() {
        String saludo = "{\"command\":\"login\",\n\"world\":\"plainworld\"}";
        
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID(this.datac.getVirtualHost()));
        outbox.setContent(saludo);
        this.send(outbox);
        
        System.out.println("\nSaludo mandado:\n" + saludo);
        System.out.println("Esperando recibir mensaje...");
        
        ACLMessage inbox;
        try {
            inbox = this.receiveACLMessage();
        } catch (InterruptedException e) {
            System.err.println("Error recibiendo la respuesta.\n" + e.getMessage());
            return;
        }
        String respuesta = inbox.getContent();
       
        System.out.println("\nMensaje recibido: '" + respuesta + "' de " + inbox.getSender().getLocalName());
        
        datac.setKey(respuesta.substring(11, 19));
        
        ////////////////////////////////////////////////////////////////////////
        
        String moverse = "{\"command\":\"moveSW\",\n\"key\":\"" + datac.getKey() + "\"}";
        outbox.setContent(moverse);
        this.send(outbox);
        
        System.out.println("\nAcción mandado:\n" + moverse);
        System.out.println("Esperando recibir mensaje...");
        
        try {
            inbox = this.receiveACLMessage();
        } catch (InterruptedException e) {
            System.err.println("Error recibiendo la respuesta.\n" + e.getMessage());
            return;
        }
        respuesta = inbox.getContent();
       
        System.out.println("\nMensaje recibido: '" + respuesta + "' de " + inbox.getSender().getLocalName());
    }
    
}
