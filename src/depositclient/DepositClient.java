/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositclient;

import depositdata.Deposit;
import depositdata.MessageData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Stanislav
 */
public class DepositClient {
    
    static void printDeposits(ArrayList<Deposit> dep) {
        if(dep.isEmpty()){
            System.out.println("Deposit is emplty!"); 
        }else{
        Iterator<Deposit> iter = dep.iterator();
        while (iter.hasNext()) {
            Deposit dp = iter.next();
            System.out.println(dp.accountId);
        } }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Client side");
        /*MessageData messages = new MessageData();
        Deposit d1 = new Deposit("Private bank", "Ukrain", 4, "Stas", 1231231412, 12500f, 1.5f, "2 years");
        messages.setMessage("ssad");
        messages.deposit = d1;*/
        MessageData messages = new MessageData("something",null);
        Socket fromserver =  new Socket("127.0.0.1", 1234);
        
        ObjectOutputStream clientOutputStream = new ObjectOutputStream(fromserver.getOutputStream());
        ObjectInputStream clientInputStream = new ObjectInputStream(fromserver.getInputStream());
        
         Scanner in = new Scanner(System.in);
        
         while (true) {
            messages.setMessage(in.nextLine());   
            clientOutputStream.writeObject(messages);
            if(messages.message.equalsIgnoreCase("exit"))
                break;
            try {
                messages = (MessageData)clientInputStream.readObject();
                if (messages.message.equalsIgnoreCase("list"))
                {
                    printDeposits(messages.deposit);
                }



                } catch (ClassNotFoundException ex) {
             if (false) break;              
            }
        }
        clientInputStream.close();
        clientOutputStream.close(); 
    }
}
