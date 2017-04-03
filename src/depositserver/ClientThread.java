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
    
    private final Socket socket;
    private MessageData messages;
    private final ControllerRegistrDeposit crd;

    public ClientThread(Socket socket) {
        crd = ControllerRegistrDeposit.getInstance();
        this.socket = socket;
        this.start();
    }
    
    @Override
    public void run() {
        try{
            try (ObjectInputStream serverInputStream = new ObjectInputStream(this.socket.getInputStream());ObjectOutputStream serverOutputStream = new ObjectOutputStream(this.socket.getOutputStream())) {
                
                while (true){
                    messages = (MessageData) serverInputStream.readObject();
                    if (messages.message.equals("list")) {
                        messages.deposit = crd.getAllDeposit();
                        serverOutputStream.writeObject(messages);
                    }else{
                    if (messages.message.equals("sum")) {
                        messages.sum = crd.totalSum();
                        serverOutputStream.writeObject(messages);
                    }else{
                    if (messages.message.equals("count")) {
                        messages.count = crd.countDeposit();
                        serverOutputStream.writeObject(messages);
                    }else{
                    if (messages.message.equalsIgnoreCase("help")) {
                        messages.errorMessage = "list - list of all deposits;\n"
                                + "sum - summa of all deposits;\n"
                                + "count - total number of deposits;\n"
                                + "info account <account id> - detail info about deposit\n"
                                + "info depositor <depositor> - info by depositor\n"
                                + "show type <type> - show deposit by tyte (type = 1...6)\n"
                                + "show bank (<bank name>) - show all deposits for bank\n"
                                + "add (<deposit info>) - add new deposit\n"
                                + "delete <account id> - delete deposit\n"
                                + "exit - close program\n";
                        serverOutputStream.writeObject(messages);
                    }else{
                    if(messages.message.startsWith("info account")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*[0-9]{13}", msgStr)){
                            Deposit temp_deposit;
                            msgStr = msgStr.substring(msgStr.length()-13, msgStr.length());
                            temp_deposit = crd.getAccountId(Long.parseLong(msgStr));
                            if(temp_deposit != null) {
                                messages.dep = temp_deposit;
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
                    }else{
                      //------------------------------INFO DEPOSITOR----------------------------------------------  
                     if(messages.message.startsWith("info depositor")) {
                        String msgStr = messages.message;
                        String[] temp_list = msgStr.split(" ");
                        if( temp_list.length == 3) {
                        msgStr = temp_list[2]; 
                        String search_name = "";
                        for(int i =0; i < msgStr.length(); i++) {
                           if (msgStr.charAt(i) != ' ') {
                               search_name += msgStr.charAt(i);
                           }
                        }
                        messages.message = "info deposit";
                        messages.deposit = crd.getDepositByDepositor(search_name);
                        if(messages.deposit != null)
                        serverOutputStream.writeObject(messages);
                        else {
                            messages.message = "error";
                            messages.errorMessage = "Depositor not find!";
                            serverOutputStream.writeObject(messages);
                        }} else{
                            messages.message = "error";
                            messages.errorMessage = "Invalid input data!";
                            serverOutputStream.writeObject(messages);
                        }
                     //-----------------------------------------------------------------------
                     }else{
                     //---------------- SHOW TYPE --------------------------------------------
                     if(messages.message.startsWith("show type")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*[1-6]", msgStr)){
                            String[] temp_list = msgStr.split(" ");
                            msgStr = temp_list[2]; 
                            String search_type = "";
                            for(int i =0; i < msgStr.length(); i++) {
                               if (msgStr.charAt(i) != ' ') {
                                   search_type += msgStr.charAt(i);
                               }
                            }
                            int type_deposit = Integer.parseInt(search_type);
                            switch (type_deposit) {
                                case 1:
                                    search_type = "on-demand";
                                    break;
                                case 2:
                                    search_type = "urgent";
                                    break;
                                case 3:
                                    search_type = "settlement";
                                    break;
                                case 4:
                                    search_type = "comulative";
                                    break;
                                case 5:
                                    search_type = "saving";
                                    break;
                                case 6:
                                    search_type = "metal";
                                    break;
                            }
                            messages.deposit = crd.getDepositsByType(search_type);
                            if(messages.deposit != null) {
                                serverOutputStream.writeObject(messages);
                            }else
                            {
                                messages.message = "error";
                                messages.errorMessage = "No search!";
                                serverOutputStream.writeObject(messages);
                            }

                        }else {
                            messages.message = "error";
                            messages.errorMessage = "Invalid deposit type!";
                            serverOutputStream.writeObject(messages);
                        }
                        //--------------------------------------------------------------
                        
                     }else{
                       //------------------------------SHOW BANK--------------------------
                         if(messages.message.startsWith("show bank")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*\\([a-zA-Z0-9,\\s]*\\)", msgStr)) {
                        String search_bank = "";
                        int start_pos = 0;
                        for(int i = 0; i < msgStr.length(); i++)
                        {
                            if(msgStr.charAt(i) == '(') {
                                start_pos = i+1;
                                break;
                            }
                        }
                        for(int i = start_pos; i<msgStr.length(); i++){
                            if(msgStr.charAt(i) != ')'){
                                search_bank += msgStr.charAt(i);
                            }else{
                            break;
                        }
                        }
                        
                        messages.deposit = crd.getDepositByBank(search_bank);
                        if(messages.deposit != null)
                        serverOutputStream.writeObject(messages);
                        else {
                            messages.message = "error";
                            messages.errorMessage = "Bank not find!";
                            serverOutputStream.writeObject(messages);
                        }} else{
                            messages.message = "error";
                            messages.errorMessage = "Invalid input data!";
                            serverOutputStream.writeObject(messages);
                        }
                       //-----------------------------------------------------------------
                       
                     }else{
                             
                        //--------------------------ADD DEPOSIT-----------------------------------
                        if(messages.message.startsWith("add")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*\\([a-zA-Z0-9,\\s]*\\)", msgStr)) {
                        String add_deposit = "";
                        int start_pos = 0;
                        for(int i = 0; i < msgStr.length(); i++)
                        {
                            if(msgStr.charAt(i) == '(') {
                                start_pos = i+1;
                                break;
                            }
                        }
                        for(int i = start_pos; i<msgStr.length(); i++){
                            if(msgStr.charAt(i) != ')'){
                                add_deposit += msgStr.charAt(i);
                            }else{
                            break;
                        }
                        }
                        String[] deposit_data;
                        deposit_data = add_deposit.split(",");
                        
                        String bankName = deposit_data[0];
                        String country = deposit_data[1];
                        int depositType = Integer.parseInt(deposit_data[2]);
                        String depositor = deposit_data[3];
                        long accountId = Long.valueOf(deposit_data[4]);
                        double amountOfDeposit = Double.valueOf(deposit_data[5]);
                        double profitability = Double.valueOf(deposit_data[6]);
                        String timeConstrain = deposit_data[7];
                        
                        if(crd.getAccountId(accountId) == null){
                            if(depositType > 0 && depositType < 7)
                            {
                                if(amountOfDeposit > 0){
                                    if(profitability > 0){
                                        if(Pattern.matches("[0-9]{13}", String.valueOf(accountId))){
                                            Deposit deposit = new Deposit(bankName, country, depositType, depositor, accountId, amountOfDeposit, profitability, timeConstrain);
                                            crd.add(deposit);
                                            crd.save();
                                            crd.load();
                                            messages.errorMessage = "OK";
                                            serverOutputStream.writeObject(messages);
                                        }else{
                                            messages.message = "error";
                                            messages.errorMessage = "Invalid account id!";
                                            serverOutputStream.writeObject(messages);
                                        }
                                    }else{
                                        messages.message = "error";
                                        messages.errorMessage = "Invalid profitability!";
                                        serverOutputStream.writeObject(messages);
                                    }
                                }else{
                                    messages.message = "error";
                                    messages.errorMessage = "Invalid amount of Deposit!";
                                    serverOutputStream.writeObject(messages); 
                                }
                            }else{
                                messages.message = "error";
                                messages.errorMessage = "Invalid deposit type!";
                                serverOutputStream.writeObject(messages);
                            }
                        }else{
                            messages.message = "error";
                            messages.errorMessage = "Account id is already exist!";
                            serverOutputStream.writeObject(messages); 
                        }
                        } else{
                            messages.message = "error";
                            messages.errorMessage = "Invalid input data!";
                            serverOutputStream.writeObject(messages);
                        }
                        //----------------------------------------------------------------
                         }else{
                            
                        //--------------------------DELETE DEPOSIT------------------------
                         if(messages.message.startsWith("delete")) {
                        String msgStr = messages.message;
                        if(Pattern.matches("[a-zA-Z,\\s]*[0-9]{13}", msgStr)){
                            Deposit temp_deposit;
                            msgStr = msgStr.substring(msgStr.length()-13, msgStr.length());
                            temp_deposit = crd.getAccountId(Long.parseLong(msgStr));
                            if(temp_deposit != null) {
                                crd.delete(Long.parseLong(msgStr));
                                crd.save();
                                crd.load();
                                messages.errorMessage = "OK";
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
                        //----------------------------------------------------------------
                        }else{
                    if(messages.message.equals("exit")){
                         messages.message = "exit";
                         serverOutputStream.writeObject(messages);
                        break;
                    }
                    else{
                        
                         messages.message = "error";
                         messages.errorMessage = "Unknown command!";
                         serverOutputStream.writeObject(messages);
                    }
                }}}}}}}}}}}
            }
                crd.save();
        
        }catch (SocketException e){
            
        }catch(IOException | ClassNotFoundException e){
    
        }finally{
           
        }
    }   
}
