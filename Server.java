import java.net.*;
import java.io.*;

public class Server {

    ServerSocket Server;
Socket socket;

BufferedReader br;
PrintWriter out;

public Server(){
  try{
    Server= new ServerSocket(1236);
    System.out.println("server is ready to accept connections");
    System.out.println("Waiting..");
    socket = Server.accept();
    System.out.println("Client Connection");

    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream());

    startreading();
    startwriting();

  } catch (Exception e) {
    e.printStackTrace();
  }
  
}
public void startreading(){
Runnable  r1 = () -> {

System.out.println("reader started..");
    try{
        while(true){
    String msg=br.readLine();
 if(msg.equals("exit")) {
    System.out.println("Client terminated the chat");
    socket.close();
    break;
}
    System.out.println("Client: "+ msg);
}
    }
catch (Exception e){
    e.printStackTrace();
    System.out.println("connection is closed");
}

};
new Thread(r1).start();
}
public void startwriting(){

    System.out.println("writer started");

Runnable  r2 = () -> {
    try{
    while (true && !socket.isClosed()) {

     BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
     String Content = br1.readLine();

     out.println(Content);
     out.flush();
     
     if(Content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
                System.out.println("connection is closed");
            }
    catch (Exception e){
        e.printStackTrace();
        System.out.println("connection is closed");
    }
};

    new Thread(r2).start();
}
    public static void main(String[] args) {
        System.out.println("this is server.. going on to start server");
          new Server();
   }
}