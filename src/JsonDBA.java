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
import java.util.LinkedHashMap;

/**
 *
 * @author JotaC
 */
public class JsonDBA {

    private Gson gson;
    private JsonParser parser;
    
    public JsonDBA()
    {
        gson = new Gson();
        parser = new JsonParser();
        
    }
    
    //**************************************************************************************************//
    //*******************   FUNCIONES DE CREACION DE JSON (SERIALIZACION) ******************************//
    //**************************************************************************************************//
    
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
        //JsonDBA parser = new JsonDBA("login", world, radar, scanner, battery, gps);
        LinkedHashMap hash = new LinkedHashMap();
        
        hash.put("command", "login");
        hash.put("world", world);
        hash.put("radar", radar);
        hash.put("scanner", scanner);
        hash.put("battery", battery);
        hash.put("gps", gps);
        
        return gson.toJson(hash); 
    }
    
    /**
     * Funcion que te serializa una coleccion de datos a formato JSON
     * @param coleccion
     * @return Devuelve un String con el texto en formato JSOn
     */
    public String crearJson(LinkedHashMap coleccion)
    {
        return gson.toJson(coleccion);
    }
    
    //**************************************************************************************************//
    //**********************   FUNCIONES DE RECEPCION DE JSON (DESERIALIZACION) ************************//
    //**************************************************************************************************//

    /**
     * Se coge el elemento de la cadena json que pasamos por parametro
     * @param cadena: Contiene un elemento json Clave: Valor
     * @param clave: Key por la que se accedera
     * @return
     */
    public JsonElement getElement(JsonElement cadena,String clave)
    {
        LinkedHashMap lhashmap = new LinkedHashMap();
        String mensaje = cadena.toString();
        JsonElement element = null;
        
        lhashmap = gson.fromJson(cadena, LinkedHashMap.class);
       
        if(lhashmap.containsKey(clave))
        {       
            element = parser.parse(mensaje);
            return element.getAsJsonObject().get(clave);
        }
        else
            System.out.print("La clave "+clave+ " no está en el mensaje");
        
        return element;
    }
    
    /**
     * 
     * @param result
     * @return 
     */
    public JsonElement recibirRespuesta(String result)
    {   
        return parser.parse(result);
    }
    
    //**************************************************************************************************//
    //*******************************   FUNCIONES AUXILIARES *******************************************//
    //**************************************************************************************************//
    
    public void JsonElementToArrayFloat(JsonElement cadena)
    {
       //JsonParser parser = new JsonParser();
       ArrayList <Float> arr_float = new ArrayList<>();
       JsonElement element = parser.parse(cadena.getAsString());
       JsonArray jsArray = element.getAsJsonArray(); 
       
       for(JsonElement jse: jsArray)
       {
            arr_float.add(jse.getAsFloat());
       }
       System.out.print(arr_float);     
    }
}
