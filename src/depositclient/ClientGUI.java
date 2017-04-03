package depositclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import depositdata.Deposit;
import depositdata.MessageData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Stanislav
 */
public class ClientGUI extends JFrame{
    private final JButton button = new JButton("Execute");
    private final JTextField input = new JTextField("", 5);
    private final JTextArea textArea = new JTextArea("", 5, 5);
    private final JLabel label = new JLabel("Command:");
    ObjectOutputStream clientOutputStream = null;
    ObjectInputStream clientInputStream = null;
    MessageData messages = new MessageData();

    public void initGUI(){
        this.setBounds(100,100,450,450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                super.windowClosing(we);
                messages.setMessage("exit");
            }
        });
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(84,143,30));
        textArea.setForeground(Color.white);
        textArea.repaint();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(textArea);
        JPanel p2 = new JPanel();
        p2.setBackground(Color.white);
        p2.setLayout((new BoxLayout(p2, BoxLayout.LINE_AXIS)));
        p2.add(javax.swing.Box.createRigidArea(new Dimension(10,0)));
        p2.add(label);
        p2.add(javax.swing.Box.createRigidArea(new Dimension(10,0)));
        p2.add(input);
        label.setForeground(new Color(84,143,30));
        input.addActionListener(new ButtonEventListener());
        
        button.addActionListener(new ButtonEventListener());
        p2.add(button);      
        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);
        contentPane.add(p2, BorderLayout.PAGE_END);
        this.setVisible(true);

    }
    public ClientGUI() {
        super("Deposit client");
    }
    
    void Connection(){ 
        try {
            Socket fromServer = new Socket("127.0.0.1", 1234);
            clientOutputStream = new ObjectOutputStream(fromServer.getOutputStream());
            clientInputStream = new ObjectInputStream(fromServer.getInputStream());
            textArea.append("Connected!\n");
            textArea.append("Input \"help\" for view list of command.");
        } catch (IOException ex) {
            textArea.append("Error connection!\n");
        }
    }
    
    void printDeposit(ArrayList<Deposit> dep){
        if(dep.isEmpty()){
            textArea.append("Deposits is empty!\n");
        }else{
            textArea.append("List of deposits:\n");
        Iterator<Deposit> iter = dep.iterator();
        while (iter.hasNext()) {
            Deposit dp = iter.next();
            textArea.append(dp.accountId+" | ");
            textArea.append(dp.bankName+" | ");
            textArea.append(dp.depositType+" \n");
        } }
    };
    
    void printAccount(Deposit dp){
        textArea.append(dp.bankName+" | ");
        textArea.append(dp.country+" | ");
        textArea.append(dp.depositType+" | ");
        textArea.append(dp.depositor+" | ");
        textArea.append(dp.accountId+" | ");
        textArea.append(dp.amountOfDeposit+" | ");
        textArea.append(dp.profitability+" | ");
        textArea.append(dp.timeConstrain+"\n");
    }
    
    
    class ButtonEventListener implements ActionListener{
       
        @Override
        public void actionPerformed(ActionEvent ae) {
            textArea.setText("");
            messages = new MessageData();
           messages.setMessage(input.getText());
        try {
            clientOutputStream.writeObject(messages);
            messages = (MessageData)clientInputStream.readObject();
            if (messages.message.equalsIgnoreCase("list"))
            {
                printDeposit(messages.deposit);
            }
            if (messages.message.equalsIgnoreCase("sum"))
            {
                textArea.append(String.valueOf("Total summa: "+messages.sum+"\n"));
            }

            if (messages.message.equalsIgnoreCase("count"))
            {
                textArea.append(String.valueOf("Total count: "+messages.count+"\n"));
            }
            if(messages.message.startsWith("info account"))
            {
                printAccount(messages.dep);
            }
            if(messages.message.startsWith("info depositor"))
            {
                printAccount(messages.dep);
            }
            if(messages.message.equals("error"))
            {
                textArea.append(messages.errorMessage+"\n");
            }

            if(messages.message.equalsIgnoreCase("help"))
            {
                textArea.append(messages.errorMessage+"\n");
            }
            if(messages.message.startsWith("info deposit"))
            {
                printDeposit(messages.deposit);
            }
            
            if(messages.message.startsWith("show type"))
            {
                printDeposit(messages.deposit);
            }
            if(messages.message.startsWith("show bank"))
            {
                printDeposit(messages.deposit);
            }
            if(messages.message.startsWith("add"))
            {
                textArea.append(messages.errorMessage);
            }
            if(messages.message.startsWith("delete"))
            {
                textArea.append(messages.errorMessage);
            }
            input.setText("");
            if(messages.message.equalsIgnoreCase("exit"))
            {
                System.exit(0);
            }
        } catch (IOException | ClassNotFoundException ex) {
            textArea.append(ex.getMessage());
        }
        
        }   
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI app = new ClientGUI();
            app.initGUI();
            app.Connection();
        });
    }
    
}
