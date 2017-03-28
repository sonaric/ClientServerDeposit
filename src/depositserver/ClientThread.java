/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositserver;

import java.net.Socket;

/**
 *
 * @author Stanislav
 */
public class ClientThread extends Thread{
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }
    
    public void run() {
        
    }
    
}
