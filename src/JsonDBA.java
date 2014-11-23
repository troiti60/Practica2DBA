import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clase para parsear y crear strings de JSON
 *
 * @author José Carlos Alfaro
 */
public class JsonDBA {

    private final Gson gson;
    private final JsonParser parser;

    /**
     * Constructor
     *
     * @author José Carlos Alfaro
     */
    public JsonDBA() {
        this.gson = new Gson();
        this.parser = new JsonParser();
    }

    //**************************************************************************************************//
    //*******************   FUNCIONES DE CREACION DE JSON (SERIALIZACION) ******************************//
    //**************************************************************************************************//
    /**
     * Funcion de login en el Servidor. Parametro World es obligatorio.
     *
     * @param world Nombre del mapa
     * @param radar Nombre del agente Radar
     * @param scanner Nombre del agente Scanner
     * @param battery Nombre del agente Battery
     * @param gps Nombre del agente GPS
     * @return Devuelve el string de logeo
     * @author José Carlos Alfaro
     */
    public String login(String world, String radar, String scanner, String battery, String gps) {
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
     *
     * @param coleccion Colección de parejas key-value
     * @return Devuelve un String con el texto en formato JSON
     * @author José Carlos Alfaro
     */
    public String crearJson(LinkedHashMap coleccion) {
        return gson.toJson(coleccion);
    }

    //**************************************************************************************************//
    //**********************   FUNCIONES DE RECEPCION DE JSON (DESERIALIZACION) ************************//
    //**************************************************************************************************//
    /**
     * Se coge el elemento de la cadena json que pasamos por parametro
     *
     * @param cadena Contiene un elemento json Clave: Valor
     * @param clave Key por la que se accedera
     * @return Elemento json, indicado por la cadena
     * @author José Carlos Alfaro
     */
    public JsonElement getElement(JsonElement cadena, String clave) {
        String mensaje = cadena.toString();
        JsonElement element = null;

        if (mensaje.contains(clave)) {
            element = parser.parse(mensaje);
            return element.getAsJsonObject().get(clave);
        } else {
            System.out.print("La clave " + clave + " no está en el mensaje");
        }

        return element;
    }

    /**
     * Parsea el string de respuesta del servidor a JsonElement
     *
     * @param result String recibido del servidor
     * @return JsonElement con el mensaje
     * @author José Carlos Alfaro
     */
    public JsonElement recibirRespuesta(String result) {
        return parser.parse(result);
    }

    //**************************************************************************************************//
    //*******************************   FUNCIONES AUXILIARES *******************************************//
    //**************************************************************************************************//
    /**
     * Transforma un elemento JsonELement con una colección de float en un Array
     * de float
     *
     * @param cadena Contiene la cadena de float
     * @return Array de float
     * @author José Carlos Alfaro
     */
    public ArrayList<Float> jsonElementToArrayFloat(JsonElement cadena) {
        ArrayList<Float> arr_float = new ArrayList<>();
        JsonElement element = parser.parse(cadena.toString());
        JsonArray jsArray = element.getAsJsonArray();

        for (JsonElement jse : jsArray) {
            arr_float.add(jse.getAsFloat());
        }
        return arr_float;
    }

    /**
     * Transforma un elemento JsonELement con una colección de enteros en un
     * Array de enteros
     *
     * @param cadena Contiene la cadena de enteros
     * @return Array de enteros
     * @author José Carlos Alfaro
     */
    public ArrayList<Integer> jsonElementToArrayInt(JsonElement cadena) {
        ArrayList<Integer> arr_int = new ArrayList<>();
        JsonElement element = parser.parse(cadena.toString());
        JsonArray jsArray = element.getAsJsonArray();

        for (JsonElement jse : jsArray) {
            arr_int.add(jse.getAsInt());
        }
        return arr_int;
    }
}
