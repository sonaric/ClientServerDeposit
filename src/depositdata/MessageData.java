/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Stanislav
 */
public class MessageData implements Serializable{
    public String message;
    public ArrayList<Deposit> deposit;
    public double sum;
    public int count;
    public Deposit dep;
    public String errorMessage;

    public MessageData(String message, ArrayList<Deposit> deposit) {
        this.message = message;
        this.deposit = deposit;
    }
    public MessageData(String message, double sum) {
        this.message = message;
        this.sum = sum;
    }
    public MessageData(String message, int count) {
        this.message = message;
        this.count = count;
    }
    public MessageData(String message, Deposit dep) {
        this.message = message;
        this.dep = dep;
    }
    public MessageData() {
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeposit(ArrayList<Deposit> deposit) {
        this.deposit = deposit;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setDep(Deposit dep) {
        this.dep = dep;
    }
    
}
