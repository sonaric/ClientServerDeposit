/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depositdata;

import java.io.Serializable;

/**
 *
 * @author Stanislav
 */

public class Deposit implements Serializable{
    public String bankName;
    public String country;
    public String depositType;
    public String depositor;
    public long accountId;
    public double amountOfDeposit;
    public double profitability;
    public String timeConstrain;

    public Deposit(String bankName, String country, int depositType, String depositor, long accountId, double amountOfDeposit, double profitability, String timeConstrain) {
        this.bankName = bankName;
        this.country = country;
        
        switch (depositType) {
            case 1:
                this.depositType = "on-demand";
                break;
            case 2:
                this.depositType = "urgent";
                break;
            case 3:
                this.depositType = "settlement";
                break;
            case 4:
                this.depositType = "comulative";
                break;
            case 5:
                this.depositType = "saving";
                break;
            case 6:
                this.depositType = "metal";
                break;
        }
      
        this.depositor = depositor;
        this.accountId = accountId;
        this.amountOfDeposit = amountOfDeposit;
        this.profitability = profitability;
        this.timeConstrain = timeConstrain;
    }
    
    
}
