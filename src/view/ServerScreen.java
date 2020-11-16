package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
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

import javax.swing.JTextField;
import javax.swing.JButton;

public class ServerScreen extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	static JTextArea messageArea;
	
	static ServerSocket serverSocket;
	static Socket socket;
	static DataInputStream dataInput;
	static DataOutputStream dataOutput;
	
	static String messageInput = "";
	String messageOutput = "";
	static List<String> messageHistory;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerScreen frame = new ServerScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			messageHistory = new ArrayList<String>();
			
			serverSocket = new ServerSocket(3030);
			socket = serverSocket.accept();
			
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());
			
			
			while(!messageInput.equals("exit")) {
				messageInput = dataInput.readUTF();
				messageHistory.add("\nClient: " + messageInput);

				messageInput = "";
				
				for (String message : messageHistory) {
					messageInput = messageInput + message;
				}

				messageArea.setText(messageInput);					
			}
			
			dataInput.close();
			dataOutput.close();
			socket.close();
			serverSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public ServerScreen() {
		setResizable(false);
		messageArea = new JTextArea();
		
		setTitle("SERVER");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 585, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		messageArea.setTabSize(0);
		messageArea.setLineWrap(true);
		messageArea.setForeground(SystemColor.textHighlight);
		messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
		messageArea.setEditable(false);
		messageArea.setBounds(10, 11, 559, 345);
		contentPane.add(messageArea);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(10, 367, 429, 34);
		contentPane.add(textField);
		
		JButton sendMessage = new JButton("ENVIAR");
		sendMessage.setBounds(449, 367, 120, 34);
		sendMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentMessage = "";
				
				messageOutput = textField.getText().trim();
				
				if (messageOutput.length() > 0) {			
					messageHistory.add("\nEu: " + messageOutput);
					
					for (String message : messageHistory) {
						currentMessage = currentMessage + message;
					}
					
					messageArea.setText(currentMessage);					
					
					try {
						dataOutput.writeUTF(messageOutput);
						textField.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					textField.setText("");
				}
			}
		});
		contentPane.add(sendMessage);
	}
	
	/**
	 * Launch the application.
	 */
}
