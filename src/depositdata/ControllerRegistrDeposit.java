/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Stanislav
 */
public class ControllerRegistrDeposit {
    private ArrayList<Deposit> deposits;
    public ControllerRegistrDeposit() {
       
    }
    public void load(){
        
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("deposits.dat"));
            deposits = (ArrayList<Deposit>)in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
           deposits = new ArrayList<>();
        }
        
    }
    public ArrayList<Deposit> getAllDeposit() {
        return deposits;
    }
    
    public void add(Deposit data) {
        deposits.add(data);
    }
    
    public double totalSum() {
        double sum = 0;
        Iterator<Deposit> iter = deposits.iterator();
        while (iter.hasNext()) {
            Deposit dep = iter.next();
            sum+=dep.amountOfDeposit;
        }       
        return sum;
    }
    
    public int countDeposit() {
        return deposits.size();
    }
    
    public Deposit getAccountId(long accountId) {
        Iterator<Deposit> iter = deposits.iterator();
        Deposit resDep = null;
        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.accountId == accountId){
                resDep = dep;
                break;
            }
        } 
        return resDep;
    }
    
    public ArrayList<Deposit> getDepositByDepositor(String depositor){
        Iterator<Deposit> iter = deposits.iterator();
        ArrayList<Deposit> temp_list = new ArrayList<>();

        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.depositor.equals(depositor)){
                temp_list.add(dep);
            }
        } 
        return temp_list;
    }
    
    public ArrayList<Deposit> getDepositByBank(String bankName){
        Iterator<Deposit> iter = deposits.iterator();
        ArrayList<Deposit> temp_list = new ArrayList<>();

        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.bankName.equals(bankName)){
                temp_list.add(dep);
            }
        } 
        return temp_list;
    }
    
    public ArrayList<Deposit> getDepositsByType(String type){
        Iterator<Deposit> iter = deposits.iterator();
        ArrayList<Deposit> temp_list = new ArrayList<>();

        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.depositType.equals(type)){
                temp_list.add(dep);
            }
        } 
        return temp_list;
    }
    
    public boolean delete(long accout_id) {
        int flag = 200;
        Iterator<Deposit> iter = deposits.iterator();
        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.accountId == accout_id) {
                iter.remove();
                flag = 0;
            }
        }
        if (flag == 0)
            return true;
        else
            return false;
    }
    
    public void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("deposits.dat"));
            out.writeObject(deposits);
            out.close();
        } catch (IOException ex) {
            System.out.println("Save is fail...");
        }
    }
    
}
