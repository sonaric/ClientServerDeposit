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
    
    private static volatile ControllerRegistrDeposit instance;
    
    private ControllerRegistrDeposit() {
       
    }
    
    public static ControllerRegistrDeposit getInstance() {
        if(instance == null)
            synchronized (ControllerRegistrDeposit.class) {
                if(instance == null)
                    instance = new ControllerRegistrDeposit();
            }
        return instance;
    }
    public void load(){
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("deposits.dat"))) {
            deposits = (ArrayList<Deposit>)in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
           deposits = new ArrayList<>();
        }
        
    }
    public synchronized ArrayList<Deposit> getAllDeposit() {
        return deposits;
    }
    
    public synchronized void add(Deposit data) {
        deposits.add(data);
    }
    
    public synchronized double totalSum() {
        double sum = 0;
        Iterator<Deposit> iter = deposits.iterator();
        while (iter.hasNext()) {
            Deposit dep = iter.next();
            sum+=dep.amountOfDeposit;
        }       
        return sum;
    }
    
    public synchronized int countDeposit() {
        return deposits.size();
    }
    
    public synchronized Deposit getAccountId(long accountId) {
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
    
    public synchronized ArrayList<Deposit> getDepositByDepositor(String depositor){
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
    
    public synchronized ArrayList<Deposit> getDepositByBank(String bankName){
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
    
    public synchronized ArrayList<Deposit> getDepositsByType(String type){
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
    
    public synchronized void delete(long accout_id) {

        Iterator<Deposit> iter = deposits.iterator();
        while (iter.hasNext()) {
            Deposit dep = iter.next();
            if (dep.accountId == accout_id) {
                iter.remove();
            }
        }
    }
    
    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("deposits.dat"))) {
            out.writeObject(deposits);
        } catch (IOException ex) {
            System.out.println("Save is fail...");
        }
    }
    
}
