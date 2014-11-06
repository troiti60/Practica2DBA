/**
 * Clase que contiene los datos de conexión y el acceso a ellos
 * 
 * @author Fco Javier Ortega Rodríguez
 *
 */
public class DatosAcceso {

    private String host;
    private int port;
    private String virtualhost;
    private String username;
    private String passwd;
    private Boolean ssl;

    public DatosAcceso(){
        host = "siadex.ugr.es";
        port = 6000;
        virtualhost = "Canis";
        username = "Capricornio";
        passwd = "Gaugin";
        ssl = false;
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }

    public String getVirtualHost(){
        return this.virtualhost;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.passwd;
    }

    public Boolean getSSL(){
        return this.ssl;
    }
}
