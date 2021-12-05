import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
       Socket socket = new Socket("localhost", 5000);
       dataInputStream = new DataInputStream(socket.getInputStream());
       dataOutputStream = new DataOutputStream(socket.getOutputStream());

        sendFile("D:/gimp.zip");

        dataInputStream.close();
        dataOutputStream.close();
    }

    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        System.out.println(file.length());
        // send file size
        dataOutputStream.writeLong(file.length());

        // break files into chuncks
        byte[] buffer = new byte[4 * 1024];

        while((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
}
