/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositdata;

import depositdata.Deposit;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Stanislav
 */
public class MessageData implements Serializable{
    public String message;
    public ArrayList<Deposit> deposit;

    public MessageData(String message, ArrayList<Deposit> deposit) {
        this.message = message;
        this.deposit = deposit;
    }

    public MessageData() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeposit(ArrayList<Deposit> deposit) {
        this.deposit = deposit;
    }
    
}
