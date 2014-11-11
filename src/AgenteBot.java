
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
        private JsonDBA parse;
	
	private enum direccion { NO, N, NE, E, SE, S, SO, O, R }
	
	public AgenteBot(AgentID aid) throws Exception {
		super(aid);
                JsonDBA login = new JsonDBA();
                saludo = login.login("plainworld",null,null,null,null);
	}
	    
    /**
     * Método inicial de logueo en el servidor
     * @author Fco Javier Ortega Rodríguez
     * @return Respuesta del servidor
     * @throws InterruptedException 
     */
    public String Saludo() throws InterruptedException{
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID( datac.getVirtualHost() ));
        outbox.setContent(this.saludo);
        this.send(outbox);
        
        System.out.println("\nSaludo mandado:\n" + saludo);
        System.out.println("\nEsperando recibir mensaje...");
        
        inbox=this.receiveACLMessage();   
        JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = parse.getElement(MensajeRecibido, "result");
        
        System.out.println("Mensaje recibido: " +valorResult.getAsString()+" de "+inbox.getSender().getLocalName());
        
        return valorResult.getAsString();
    }

    /**
     * Método que manda la acción de repostar o el tipo de movimiento 
     * al servidor
     * @author Fco Javier Ortega Rodríguez
     * @param d Acción o dirección a tomar
     * @return Respuesta del servidor
     * @throws InterruptedException 
     */
    private String RealizarAccion(direccion d) throws InterruptedException{
        String accion = null;
        
        /*
        accion = cadena JSOn con la accion
        */
        
        outbox.setContent(accion);
        this.send(outbox);
        
        System.out.println("\nAcción mandada:\n" + accion);
        System.out.println("\nEsperando recibir mensaje...");
        
        inbox=this.receiveACLMessage();   
        JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
        JsonElement valorResult = parse.getElement(MensajeRecibido, "result");
        
        System.out.println("Mensaje recibido: " +valorResult.getAsString()+" de "+inbox.getSender().getLocalName());
        
        return valorResult.getAsString();
    }
    
    /**
     * @author Fco Javier Ortega Rodríguez
     */
    @Override
    public void execute(){
	datac = new DatosAcceso();
        JsonDBA parse = new JsonDBA();
        System.out.println("\n\nSoy agenteBot funcionando");
        
        try {
            
            System.out.print("\nSaludando...");
            String key = this.Saludo();
            System.out.print("OK\n");
            
            boolean vivo = true;
            boolean keyCorrecto = false;
            float nivelBateria = 0;
            float MIN_BATERY = 3;
            direccion decision;
            String respuesta;
            
            if (keyCorrecto){
                datac.setKey(key);
            }
            else{
                //matar agenteEntorno mesaje
                vivo = !vivo;
            }
            
            while(vivo)
                //Esperar NvBateria de agente entorno
                System.out.println("\nEsperando recibir mensaje...");

                inbox=this.receiveACLMessage();   
                JsonElement MensajeRecibido = parse.recibirRespuesta(inbox.getContent());
                JsonElement valorResult = parse.getElement(MensajeRecibido, "batery");
                nivelBateria = valorResult.getAsFloat();
                        
                //heuristica/toma decisión                
                if(nivelBateria <= MIN_BATERY)
                    decision = direccion.R;
                else
                    decision = this.busqueda();
                
                respuesta = this.RealizarAccion(decision);
                
                if(respuesta!= "o" || respuesta!="BAD_"){
                    // Matar agente entorno
                    vivo = !vivo;
                }
               	
            	
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
	
    /**
     * La búsqueda para encontrar el mejor (con la información
     * que ya tenemos) camino
     * 
     * @return La dirección en que el bot debe moverse
     * @author Alexander Straub
     */
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
		Nodo destino = null;
        
        // Si aún hay nodos en la lista, sigue
        while (!nodos.isEmpty()) {
            // Recoger el nodo en la lista con menos distancia al origin
            Nodo minNodo = (Nodo)Collections.min(nodos);
            nodos.remove(minNodo);
			
			if (minNodo.getRadar() == 2) destino = minNodo;
            
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
		if (destino != null) camino = destino;
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
