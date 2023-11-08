import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChatApplication extends JFrame implements ActionListener {

  private final CardLayout cardLayout;
  private final JPanel cards;
  private final JComboBox<String> chatTypeBox;
  private final JTextArea textArea;
  private final JTextField nameField, textField, ipField, portField;
  private ChatClient client;

  public ChatApplication() {
    setTitle("Chat Application");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    cardLayout = new CardLayout();
    cards = new JPanel(cardLayout);

    // Login panel
    JPanel loginPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    loginPanel.add(new JLabel("IP:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    loginPanel.add(ipField = new JTextField("localhost", 10), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    loginPanel.add(new JLabel("Port:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    loginPanel.add(portField = new JTextField("14215", 10), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    loginPanel.add(new JLabel("Name:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    loginPanel.add(nameField = new JTextField("tugino", 10), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    loginPanel.add(new JLabel("Chat Type:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    loginPanel.add(chatTypeBox = new JComboBox<>(new String[]{"Chat với PC", "Chat người dùng"}),
        gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;

    JButton submitButton;
    loginPanel.add(submitButton = new JButton("Submit"), gbc);
    submitButton.addActionListener(this);

    // Chat panel
    JPanel chatPanel = new JPanel(new BorderLayout());
    textArea = new JTextArea();
    textArea.setEditable(false);
    chatPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    textField = new JTextField();
    chatPanel.add(textField, BorderLayout.SOUTH);
    textField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String message = textField.getText();
        try {
          client.sendMessage(message);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        textField.setText("");
      }
    });

    JPanel userChatPanel = new JPanel(new BorderLayout());
    JComboBox<String> onlineUsersBox = new JComboBox<>();
    userChatPanel.add(onlineUsersBox, BorderLayout.NORTH);
    JTextArea userTextArea = new JTextArea();
    userTextArea.setEditable(false);
    userChatPanel.add(new JScrollPane(userTextArea), BorderLayout.CENTER);
    JTextField userTextField = new JTextField();
    userChatPanel.add(userTextField, BorderLayout.SOUTH);

    cards.add(loginPanel, "Login Panel");
    cards.add(chatPanel, "Chat Panel");
    cards.add(userChatPanel, "User Chat Panel");

    add(cards);
  }

  @Override
  public void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      try {
        if (client != null) {
          client.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      System.exit(0);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String name = nameField.getText();
    int chatType = (int) chatTypeBox.getSelectedIndex();
    String ip = ipField.getText();
    int port = Integer.parseInt(portField.getText());
    if (!name.isEmpty()) {
      try {
        client = new ChatClient(ip, port, name, chatType);

        cardLayout.show(cards, "Chat Panel");

        new ReceiverThread(client, this).start();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please enter your name");
    }
  }

  public void displayMessage(String message) {
    textArea.append(message + "\n");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ChatApplication chatApp = new ChatApplication();
      chatApp.setVisible(true);
    });
  }
}
