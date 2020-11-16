package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientScreen extends JFrame {
	
	private JPanel contentPane;
	private JTextField messageField;
	private String historyMessages = "";
	static JTextArea messageArea;
	
	static Socket socket;
	static DataInputStream dataInput;
	static DataOutputStream dataOutput;
	
	static String messageInput = "";
	String messageOutput = "";
	static List<String> messageHistory;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientScreen frame = new ClientScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			messageHistory = new ArrayList<String>();
			
			socket = new Socket("127.0.0.1", 3030);
			
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());
		
			while(!messageInput.equals("exit")) {
				messageInput = dataInput.readUTF();
				messageHistory.add("\nServer: " + messageInput);

				messageInput = "";
				
				for (String message : messageHistory) {
					messageInput = messageInput + message;
				}

				messageArea.setText(messageInput);					
			}
			
			dataInput.close();
			dataOutput.close();
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public ClientScreen() {
		setResizable(false);
		setTitle("CLIENT");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 585, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		messageField = new JTextField();
		messageField.setBounds(10, 367, 429, 34);
		contentPane.add(messageField);
		messageField.setColumns(10);
		
		JButton sendMessage = new JButton("ENVIAR");
		sendMessage.setBounds(449, 367, 120, 34);
		sendMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentMessage = "";
				
				messageOutput = messageField.getText().trim();
				
				if (messageOutput.length() > 0) {					
					messageHistory.add("\nEu: " + messageOutput);
					
					for (String message : messageHistory) {
						currentMessage = currentMessage + message;
					}
					
					messageArea.setText(currentMessage);	
					
					try {
						dataOutput.writeUTF(messageOutput);
						messageField.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					messageField.setText("");
				}
			}
		});
		contentPane.add(sendMessage);
		
		messageArea = new JTextArea();
		messageArea.setTabSize(0);
		messageArea.setLineWrap(true);
		messageArea.setForeground(SystemColor.textHighlight);
		messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
		messageArea.setEditable(false);
		messageArea.setBounds(10, 11, 559, 345);
		contentPane.add(messageArea);
	}
}
