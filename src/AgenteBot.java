
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

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
	private DatosAcceso datac;
	
	public AgenteBot(AgentID aid) throws Exception {
		super(aid);
		
		saludo = "{\"command\":\"login\"," +
				 "\"world\":\"plainworld\"," + 
				 "\"radar\":\"bot4\"" +
				 //"\"radar\":\"" + aid +"\"" + 
				"}";
		// TODO Auto-generated constructor stub
	}
	
	public void Saludo(){
		outbox = new ACLMessage();
		outbox.setSender(this.getAid());
		outbox.setReceiver(new AgentID( datac.getVirtualHost() ));
		outbox.setContent(this.saludo);
		
		this.send(outbox);
	}

	@Override
    public void execute(){
	datac = new DatosAcceso();	
        System.out.println("\n\nSoy agenteBot funcionando");
        
        try {
            System.out.print("\nSaludando...");
            System.out.println("\nSaludo mandado:\n" + saludo);
            this.Saludo();
            System.out.print("OK\n");
        	
            System.out.println("\nEsperando recibir mensaje...");
            inbox=this.receiveACLMessage();
            System.out.println("Mensaje recibido" +inbox.getContent()+" de "+inbox.getSender().getLocalName());
			
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        /* Saludar al controlador
         	Esperar su respuesta
         	si es ok
         		esperar 4 respuesta de los sensores
         		calcular moviento (heurística)
         		mandar mensaje a controlador
     		si no es ok
     			error de conexión
        */
    }

}
