import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatClient {

  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  private String name;

  private int chatType;

  private AtomicBoolean isRunning = new AtomicBoolean(true);

  public boolean isConnected() {
    return isRunning.get();
  }

  public ChatClient(String serverAddress, int serverPort, String name, int chatType)
      throws IOException {
    socket = new Socket(serverAddress, serverPort);
    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.name = name;
    this.chatType = chatType;

    out.println(this.name);
    out.println(this.chatType);
  }

  public void sendMessage(String message) throws IOException {
    out.println(message);
  }

  public String receiveMessage() throws IOException {
    return in.readLine();
  }

  public void close() throws IOException {
    isRunning.set(false);
    socket.close();
  }
}