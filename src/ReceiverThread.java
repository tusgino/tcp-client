import java.io.IOException;

public class ReceiverThread extends Thread {

  private ChatClient client;
  private ChatApplication gui;

  public ReceiverThread(ChatClient client, ChatApplication gui) {
    this.client = client;
    this.gui = gui;
  }

  public void run() {
    try {
      String message;
      while (client.isConnected()) {
        System.out.println(client.isConnected());
        message = client.receiveMessage();
        gui.displayMessage(message);
      }
    } catch (IOException e) {
      if (client.isConnected()) {
        e.printStackTrace();
      } else {
        System.out.println("Connection closed");
      }
    }
  }
}
