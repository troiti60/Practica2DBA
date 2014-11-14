/**
 * Clase que contiene los datos de conexión y el acceso a ellos
 *
 * @author Fco Javier Ortega Rodríguez
 *
 */
public class DatosAcceso {

    /**
     * Instancia singleton
     */
    private static DatosAcceso instancia = null;

    /**
     * Crear la instancia singleton. Si ya existe, devolver la referencia
     *
     * @return Referencia al único objeto
     * @author Alexander Straub
     */
    public static DatosAcceso crearInstancia() {
        if (DatosAcceso.instancia == null) {
            DatosAcceso.instancia = new DatosAcceso();
        }
        return DatosAcceso.instancia;
    }

    /**
     * Configuración del servidor
     */
    private final String host;
    private final int port;
    private final String virtualhost;
    private final String username;
    private final String passwd;
    private final Boolean ssl;
    
    /**
     * Nombres de los bots
     */
    private static final String nombreBotPrincipal = "BotPrincipal";
    private static final String nombreBotEntorno = "BotEntorno";

    /**
     * La clave de la conección
     */
    private String key;

    /**
     * Constructor
     *
     * @author Fco Javier Ortega Rodríguez
     */
    private DatosAcceso() {
        this.host = "siadex.ugr.es";
        this.port = 6000;
        this.virtualhost = "Canis";
        this.username = "Capricornio";
        this.passwd = "Gaugin";
        this.ssl = false;

        this.key = null;
        

    }

    /**
     * Getter para devolver el nombre del servidor
     *
     * @return Nombre del servidor
     * @author Fco Javier Ortega Rodríguez
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Getter para devolver el port del servidor
     *
     * @return Port del servidor
     * @author Fco Javier Ortega Rodríguez
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Getter para devolver el virtual host del servidor
     *
     * @return Virtual host del servidor
     * @author Fco Javier Ortega Rodríguez
     */
    public String getVirtualHost() {
        return this.virtualhost;
    }

    /**
     * Getter para devolver el nombre para logearse
     *
     * @return Nombre para logearse
     * @author Fco Javier Ortega Rodríguez
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Getter para devolver la contraseña
     *
     * @return Contraseña
     * @author Fco Javier Ortega Rodríguez
     */
    public String getPassword() {
        return this.passwd;
    }

    /**
     * Getter para devolver si se utiliza encriptación
     *
     * @return True si se utiliza encriptación
     * @author Fco Javier Ortega Rodríguez
     */
    public Boolean getSSL() {
        return this.ssl;
    }

    /**
     * Setter para guardar la clave de sesión
     *
     * @param key Clave nueva de sesión
     * @author Fco Javier Ortega Rodríguez
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter para devolver la clave de sesión
     *
     * @return Clave de sesión
     * @author Fco Javier Ortega Rodríguez
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Getter para devolver el nombre del bot principal
     *
     * @return Nombre del servidor
     * @author Fco Javier Ortega Rodríguez, Antonio Troitiño
     */
    public static String getNombreBotPrincipal() {
        return DatosAcceso.nombreBotPrincipal;
    }

    /**
     * Getter para devolver el nombre del bot entorno
     *
     * @return Nombre del servidor
     * @author Fco Javier Ortega Rodríguez, Antonio Troitiño
     */
    public static String getNombreBotEntorno() {
        return DatosAcceso.nombreBotEntorno;
    }
    
}