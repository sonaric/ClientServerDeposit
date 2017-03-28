/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositserver;

import depositdata.ControllerRegistrDeposit;
import depositdata.MessageData;
import depositdata.Deposit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

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
        ControllerRegistrDeposit crd = new ControllerRegistrDeposit();
        crd.load();
        /*Deposit dep1 = new Deposit("Private bank", "Ukraine", 1, "Stas", new Date().getTime(), 12000, 5, "2 years");
        Deposit dep2 = new Deposit("Private bank", "Ukraine", 2, "Stas", new Date().getTime(), 12500, 5, "2 years");
        Deposit dep3 = new Deposit("Private bank", "Ukraine", 2, "Stas", new Date().getTime(), 18000, 5, "2 years");
        Deposit dep4 = new Deposit("Private bank", "Ukraine", 1, "Stas", new Date().getTime(), 50000, 5, "2 years");
        crd.add(dep1);
        crd.add(dep2);
        crd.add(dep3);
        crd.add(dep4);*/
        MessageData messages = null;
        
        ServerSocket socketConnection = new ServerSocket(DepositServerConfig.PORT);
        
        System.out.println("Server waiting");
        
        Socket pipe = socketConnection.accept();
        
        ObjectInputStream serverInputStream = new ObjectInputStream(pipe.getInputStream());
        ObjectOutputStream serverOutputStream = new ObjectOutputStream(pipe.getOutputStream());
        while (true){
        messages = (MessageData) serverInputStream.readObject();
        //System.out.println(messages.deposit.bankName);
        if (messages.message.equals("list")) {
            messages.deposit = crd.getAllDeposit();
            serverOutputStream.writeObject(messages);
        }
        if(messages.message.equals("exit")) break;
        }
        serverOutputStream.close();
        serverInputStream.close();
        crd.save();
    }
    
}
