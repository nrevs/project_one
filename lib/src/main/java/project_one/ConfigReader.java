package project_one;

import java.io.FileInputStream;
import java.io.IOException;
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
            FileInputStream fis;
            Properties props = new Properties();
            
            fis = (FileInputStream)ConfigReader.class.getClassLoader().getResourceAsStream(".myconfig");
            try {
                props.load(fis);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return props;
        }
    }
}
