/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;


import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author nilpa
 */
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server extends JFrame implements ActionListener{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    JLabel heading=new JLabel("Server Area");
    JTextArea messageArea=new JTextArea();
    JTextField messageInput=new JTextField();
    Font font=new Font("Roboto",Font.PLAIN,20);
    //constructor
    public Server(){
        try {
            createGUI();
            server=new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.print("waiting..");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out=new PrintWriter(socket.getOutputStream()); 
            
            handleEvents();
            startReading();
            startWriting();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void createGUI(){
        this.setTitle("Server Messenger[end]");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageInput.setSize(100,200);
        messageInput.setEditable(true);
        messageArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane jScrollPane =new JScrollPane(messageArea);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        heading.setBorder(javax.swing.BorderFactory.createEmptyBorder(20,20,20,20));   
        this.setLayout(new BorderLayout());
//        this.getContentPane().add(jScrollPane);
        this.add(heading,BorderLayout.NORTH);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        
        
        this.setVisible(true);
    }
    public void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    System.out.println("you pressed enter");
                    String contentToSend=messageInput.getText();
                    out.println(contentToSend);
                    out.flush();
                    if(contentToSend.equals("exit")){
                        messageInput.setEnabled(false);
                        JOptionPane.showMessageDialog(messageArea,"Chat Terminated");
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                
                    messageArea.append("Me: "+contentToSend+"\n");
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
                    JOptionPane.showMessageDialog(this,"Client chat end");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                 messageArea.append("Client: "+ msg+"\n");
                
            
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
                
//                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
//                    String content=br1.readLine();
////                    out.println(content);
////                    out.flush();
//                    if(content.equals("exit")){
//                        JOptionPane.showMessageDialog(this,"Connection Terminated !!!!");
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
        System.out.println("this is server..going to start server");
        new Server();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
