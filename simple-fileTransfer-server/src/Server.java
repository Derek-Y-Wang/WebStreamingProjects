import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Listening to port:5000");
        Socket clientSocket = serverSocket.accept();
        System.out.println(clientSocket + " connected\n");

        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        receiveFile("D:/projects/JavaProjects/gimp.zip");

        dataInputStream.close();
        dataOutputStream.close();
        clientSocket.close();
    }

    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }
}
