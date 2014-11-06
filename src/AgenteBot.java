
import com.google.gson.JsonElement;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	private DatosAcceso datac;
	
	private enum direccion { NO, N, NE, E, SE, S, SO, O }
	
	public AgenteBot(AgentID aid) throws Exception {
		super(aid);
		
		/*saludo = "{\"command\":\"login\"," +
				 "\"world\":\"plainworld\"," + 
				 "\"radar\":\"bot4\"" +
				 //"\"radar\":\"" + aid +"\"" + 
				"}";*/
		// TODO Auto-generated constructor stub
                JsonDBA login = new JsonDBA();
                saludo = login.login("plainworld",null,null,null,null);
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
        JsonDBA parse = new JsonDBA();
        System.out.println("\n\nSoy agenteBot funcionando");
        
        try {
            System.out.print("\nSaludando...");
            System.out.println("\nSaludo mandado:\n" + saludo);
            this.Saludo();
            System.out.print("OK\n");
        	
            System.out.println("\nEsperando recibir mensaje...");
            inbox=this.receiveACLMessage();
            JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
            JsonElement valorResult = parse.getElement(MensajeRecibido, "result");
            datac.setKey(valorResult.getAsString());
            System.out.println("Mensaje recibido: " +valorResult.getAsString()+" de "+inbox.getSender().getLocalName());
			
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
        
        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo)Collections.min(nodos);
            nodos.remove(minNodo);
            
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

}
