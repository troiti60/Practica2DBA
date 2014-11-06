/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;



/**
 *
 * @author JotaC
 */
public class JsonDBA {

    private String command;
    private String world;
    private String radar;
    private String scanner;
    private String battery;
    private String gps;
    private String key;
    
    public JsonDBA()
    {
        this.command = null;
        this.world   = null;
        this.radar   = null;
        this.scanner = null;
        this.battery = null;
        this.gps     = null;
        this.key     = null;
    }
    
    public JsonDBA(String command, String world,String radar,String scanner,String battery,String gps)
    {
        this.command = command;
        this.world   = world;
        this.radar   = radar;
        this.scanner = scanner;
        this.battery = battery;
        this.gps     = gps;
    }
    
    /**
     * Funcion de login en el Servidor.Parametro World es obligatorio.
     * @param world     Nombre del mapa
     * @param radar     Nombre del agente radar
     * @param scanner   Nombre del agente Scanner
     * @param battery   Nombre del agente Battery
     * @param gps       Nombre del agente gps.
     * @return          Devuelve el string de logeo. 
     */
    public String login(String world,String radar,String scanner,String battery,String gps)
    {
        Gson gson = new Gson();
        JsonDBA js = new JsonDBA("login", world, radar, scanner, battery, gps);
        
        return gson.toJson(js);
    }

    /**
     * Se coge el elemento de la cadena json que pasamos por parametro
     * @param cadena
     * @param clave 
     * @return
     */
    public JsonElement getElement(JsonElement cadena,String clave)
    {
        JsonParser parser = new JsonParser();
        String mensaje = cadena.toString();
        JsonElement element = null;
        
        if(mensaje.contains(clave))
        {       
            element = parser.parse(mensaje);
            return element.getAsJsonObject().get(clave);
        }
        else
            System.out.print("La clave "+clave+ " no está en el mensaje");
        
        return element;
    }
    public JsonElement recibirRespuesta(String result)
    {
        
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);
        
        return element;
    }
    
    public void JsonElementToArrayFloat(JsonElement cadena)
    {
       JsonParser js = new JsonParser();
       ArrayList <Float> arr_float = new ArrayList<>();
       JsonElement element = js.parse(cadena.getAsString());
       JsonArray jsArray = element.getAsJsonArray(); 
       
       for(JsonElement jse: jsArray)
       {
            arr_float.add(jse.getAsFloat());
       }
       System.out.print(arr_float);
           
    }
    
    /*public void arrayStringToArrayFloat(String cadena)
    {
       JsonParser js = new JsonParser();
       ArrayList <Float> arr_float = new ArrayList<>();
       JsonElement element = js.parse(cadena); 
       JsonArray jsArray = element.getAsJsonArray(); 
       
       for(JsonElement jse: jsArray)
       {
            arr_float.add(jse.getAsFloat());
       }
       System.out.print(arr_float);
           
    }*/
    
}
