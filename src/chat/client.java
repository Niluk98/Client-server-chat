/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;


//import java.awt.event.;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

/**
 *
 * @author nilpa
 */
public class client extends JFrame implements ActionListener{
    Socket socket;
     BufferedReader br;
    PrintWriter out;
    //Declare Components
    private JLabel heading =new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    JTextField messageInput=new JTextField();
    
    private Font font=new Font("Roboto",Font.PLAIN,20);
    
    
    
    
    
        public client(){
            
            try{
                System.out.println("Sending request to server");
                socket=new Socket("127.0.0.1",7777);
                System.out.println("Connection done");
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out=new PrintWriter(socket.getOutputStream()); 
            createGUI();
            handleEvents();
            startReading();
            startWriting();

                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void createGUI(){
            //gui code
            this.setTitle("Client Messenger[End]");
            this.setSize(600,600);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //coding for component
            heading.setFont(font);
            messageArea.setFont(font);
            messageInput.setFont(font);
            heading.setIcon(new ImageIcon("./i.png"));
            heading.setHorizontalTextPosition(SwingConstants.CENTER);
            heading.setVerticalTextPosition(SwingConstants.BOTTOM);
//            messageInput.setHorizontalAlignment(SwingConstants.CENTER);
            messageInput.setSize(100,200);
            messageInput.setEditable(true);
//            messageInput.addActionListener(this);
            messageArea.setEditable(false);
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            JScrollPane jScrollPane =new JScrollPane(messageArea);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            //frame layout
            this.setLayout(new BorderLayout());
            //adding the components
//            this.getContentPane().add(jScrollPane);
            this.add(heading,BorderLayout.NORTH);
           this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageInput,BorderLayout.SOUTH);
            
            
            this.setVisible(true);
        }
        private void handleEvents(){
            messageInput.addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e) {
                     
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    
//                    System.out.print("key released"+e.getKeyCode());
                    
                    if(e.getKeyCode()==10){
                        System.out.println("you have pressed enter button");
                        String contentTosend= messageInput.getText();
                        out.println(contentTosend);
                        out.flush();
                        if(contentTosend.equals("exit")){
                        messageInput.setEnabled(false);
                        JOptionPane.showMessageDialog(messageArea,"Chat Terminated");
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                    }
                        messageArea.append("Me: "+ contentTosend+"\n");
                        messageInput.setText("");
                        messageInput.requestFocus();
                            
                    }
                }
                
            });
        }
        
        
        
        public void startReading(){
        // thread-read thread
        Runnable r1=()->{
            System.out.println("chat.Server.startReading()");
            try{
            while(true){
                
                    String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server chat end");
                    JOptionPane.showMessageDialog(this,"Server Terminated chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
//                System.out.println("Server: "+msg);
                 messageArea.append("Server: "+ msg+"\n");
                 
                
            }
                } catch(Exception e){
                    e.printStackTrace();
                }
        };
        new Thread(r1).start();
    }
        public void startWriting(){
        //thread-take data and send to client
        Runnable r2=()->{
            System.out.print("Writer started");
             try{
            while(true && !socket.isClosed()){
               
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
//                    String content=br1.readLine();
//                    out.println(content);
//                    out.flush();
//                    if(content.equals("exit")){
//                        socket.close();
//                        break;
//                    }
                
            }
            System.out.println("Connection is closed");
                }catch(Exception e){
                    e.printStackTrace();
                }
            
        };
        new Thread(r2).start();
    }
        
    public static void main(String[] args) {
        System.out.print("this is client..");
        new client();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
