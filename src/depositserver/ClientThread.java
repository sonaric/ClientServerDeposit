/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositserver;

import depositdata.ControllerRegistrDeposit;
import depositdata.Deposit;
import depositdata.MessageData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Pattern;

/**
 *
 * @author Stanislav
 */
public class ClientThread extends Thread{
    private final static int DELAY = 30000;
    
    private Socket socket;
    private MessageData messages;
    private ControllerRegistrDeposit crd;

    public ClientThread(Socket socket) {
        crd = ControllerRegistrDeposit.getInstance();
        this.socket = socket;
        this.start();
    }
    
    @Override
    public void run() {
        try{
            final ObjectInputStream serverInputStream = new ObjectInputStream(this.socket.getInputStream());
            final ObjectOutputStream serverOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            
            while (true){
                messages = (MessageData) serverInputStream.readObject();
                //System.out.println(messages.deposit.bankName);
                if (messages.message.equals("list")) {
                    messages.deposit = crd.getAllDeposit();
                    serverOutputStream.writeObject(messages);
                }
                if (messages.message.equals("sum")) {
                    messages.sum = crd.totalSum();
                    serverOutputStream.writeObject(messages);
                }
                if (messages.message.equals("count")) {
                    messages.count = crd.countDeposit();
                    serverOutputStream.writeObject(messages);
                }
                if (messages.message.equalsIgnoreCase("help")) {
                    messages.errorMessage = "list - list of all deposits;\n"
                            + "sum - summa of all deposits;\n"
                            + "count - total number of deposits;\n"
                            + "info account <account id> - detail info about deposit";
                    serverOutputStream.writeObject(messages);
                }
                if(messages.message.startsWith("info account")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*[0-9]{13}", msgStr)){
                            Deposit temp_deposit = new Deposit();
                            msgStr = msgStr.substring(msgStr.length()-13, msgStr.length());
                            temp_deposit = crd.getAccountId(Long.parseLong(msgStr));
                            if(temp_deposit != null) {
                                messages.dep = temp_deposit;
                                System.out.println(messages.dep.bankName);
                                serverOutputStream.writeObject(messages);
                            }else
                            {
                                messages.message = "error";
                                messages.errorMessage = "No search!";
                                serverOutputStream.writeObject(messages);
                            }

                        }else {
                            messages.message = "error";
                            messages.errorMessage = "Invalid account id!";
                            serverOutputStream.writeObject(messages);
                        }
                }
                if(messages.message.equals("exit")) break;
                }
                serverOutputStream.close();
                serverInputStream.close();
                crd.save();
        
        }catch (SocketException e){

        }catch(IOException | ClassNotFoundException e){
    
        }
        /*catch(ClassNotFoundException e){
        }*/ /*catch(ClassNotFoundException e){
    
    }*/
    }   
}
