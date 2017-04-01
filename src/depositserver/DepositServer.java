/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositserver;

import depositdata.ControllerRegistrDeposit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Stanislav
 */
public class DepositServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ControllerRegistrDeposit crd = ControllerRegistrDeposit.getInstance();
        crd.load();
        ServerSocket socketConnection = new ServerSocket(DepositServerConfig.PORT);
        
        System.out.println("Server waiting");
        while(true){
            Socket client = null;
            while(client == null){
                client = socketConnection.accept();
            }
            new ClientThread(client);
        }
    }
    
}
