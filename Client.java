import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyListener;

    public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Client Area");
    private JTextArea MessageArea =new JTextArea();
    private JTextField MessageInput = new  JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);



    public Client() {

        try {
            System.out.println("Sending request to server");

            socket = new Socket("localhost", 1236);

         System.out.println("Connection done");

           br = new BufferedReader(
                   new InputStreamReader(socket.getInputStream())
            );

            out = new PrintWriter(socket.getOutputStream());

              CreateGUI();
              handleEvents();
           startreading();
           
            //startwriting();
        } catch (Exception e) {
          //  e.printStackTrace();
      // System.out.println("connection is closed");
        }
    }
    private void handleEvents(){
        MessageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // Handle key typed event
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // Handle key pressed event
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // Handle key released event
               // System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10){
                 String contentTOSend = MessageInput.getText();
                 if(contentTOSend != null && !contentTOSend.trim().isEmpty()){
                  MessageArea.append("me: " + contentTOSend + "\n");
                  out.println(contentTOSend);
                  out.flush();
                  MessageInput.setText("");
                  MessageInput.requestFocus();
                }
            }
        }
    });
    }
    private void CreateGUI(){
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font);
        MessageArea.setFont(font);
        MessageInput.setFont(font);

 heading.setHorizontalAlignment(SwingConstants.CENTER);
 heading.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20,20, 20));
 MessageArea.setEditable(false);
MessageInput.setHorizontalAlignment(SwingConstants.CENTER);
this.setLayout( new BorderLayout());

this.add(heading,BorderLayout.NORTH);
JScrollPane jScrollpane = new JScrollPane (MessageArea);
this.add(jScrollpane,BorderLayout.CENTER);
this.add(MessageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }

    public void startreading() {

        Runnable r1 = () -> {

            System.out.println("reader started..");
              try{
            while (true) {
        
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        SwingUtilities.invokeAndWait((Runnable) () -> {
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        MessageInput.setEnabled(false);
                    });
                        socket.close();
                        break;
                    }
final String finalmsg = msg;
SwingUtilities.invokeLater(() -> MessageArea.append("server: "+finalmsg +"\n"));
                }
                } catch (Exception e) {
                   // e.printStackTrace();
                // System.out.println("connection is closed");

                }
            };

        new Thread(r1).start();
    }

    public void startwriting() {

        Runnable r2 = () -> {

            System.out.println("writer started..");
            try {
                while(true && !socket.isClosed()){
                    BufferedReader br1 =
                            new BufferedReader(
                                    new InputStreamReader(System.in));
                                    String content = br1.readLine();
                                    out.println(content);
                        out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }      
                    }catch(Exception e){
                       // e.printStackTrace();
                       // System.out.println("connection is closed");
                    }
                };
        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("this is client..");

        new Client();
    }
    }