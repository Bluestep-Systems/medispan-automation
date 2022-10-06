package net.bluestep.medispan.downloader;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 *
 * @author CHale
 * @authur James Schriever
 */
public class ConfigurationManager {
    private static ConfigurationManager _instance =null;
    private static Properties _props = null;
    public  ConfigurationManager(){
        try {
            _props = new Properties();
            final java.net.URL url = ClassLoader.getSystemResource("config.properties");
            _props.load(url.openStream());
        } catch (final IOException ex) {

            System.out.println(ConfigurationManager.class.getName()+" "+ex.getMessage());
        }
    }
    public static boolean init(){
        if (_instance==null){
            _instance = new ConfigurationManager();
        }
        return true;
    }
    public static ConfigurationManager getInstance()
    {
        if (_instance==null){
            init();
        }
        return _instance;
    }
    public static String getAppSettings(final String property){
        if(_instance==null){
            init();
        }
        return Optional.ofNullable(System.getenv("m6n_" + property))
            .orElseGet(() -> _props.getProperty(property));
    }
}
