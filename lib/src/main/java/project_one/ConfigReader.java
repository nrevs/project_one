package project_one;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance = new ConfigReader();
    private Properties props = null;

    private ConfigReader(){}

    public static ConfigReader getInstance() {
        return instance;
    }

    public Properties getProps() {
        if (this.props != null) {
            return props;
        } else {
            InputStream is;
            Properties props = new Properties();
            
            is = ConfigReader.class.getClassLoader().getResourceAsStream(".myconfig");
            try {
                props.load(is);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return props;
        }
    }
}
