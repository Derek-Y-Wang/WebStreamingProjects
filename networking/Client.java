import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.swing.*;


public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    // constructor 
    public Client(String host){
        super("Client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                // when you hit enter perform this
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
                
            }

        );
        // window setup
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
        
    }
    //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eofException){
            showMessage("\n Client terminated connection");

        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally{
            closeAll();
        }
    }

    // connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    //setup streams to send and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream();
        showMessage("\n Streams are setup! \n");
    }

    // while chatting with server
    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n Unknown Object Type");
            }
        }while(!message.equals("SERVER - END"));
    }

    // close the streams and socket
    private void closeAll(){
        showMessage("\n Closing streams...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }

    }

    // send messages to server
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\n CLIENT  - " + message);
        }catch(IOException ioException){
            chatWindow.append("\n Something went wrong");
        }
    }

    //change/update chatWindow
    private void showMessages(final String m){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(m);
                }
            }
        );
    }

    //gives user permission to type into the text box
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(tof);
                }
            }
        );
    }

}