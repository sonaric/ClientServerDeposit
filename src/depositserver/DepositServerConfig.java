/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stanislav
 */
public class DepositServerConfig {
    private static  final String PROPERTUES_FILE = "./config.properties";
    
    public static int PORT;
    
    static {
        Properties properties =  new Properties();
        FileInputStream inputStream = null;
        
        try {
            inputStream = new FileInputStream(PROPERTUES_FILE);
            properties.load(inputStream);
            
            PORT = Integer.parseInt(properties.getProperty("PORT"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DepositServerConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DepositServerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
            }
        }
    }
}
