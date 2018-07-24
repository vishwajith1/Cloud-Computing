package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 * A simple Swing-based client for the capitalization server.
 * It has a main frame window with a text field for entering
 * strings and a textarea to see the results of capitalizing
 * them.
 */

 
public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Cloud Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(40, 60);
	
	private JFrame editorframe = new JFrame("Editing Window");
	private JTextArea editor = new JTextArea(60, 60);
	private  JButton OK = new JButton("OK");
	
	private  JPanel panel2 = new JPanel();
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Enter in the
     * listener sends the textfield contents to the server.
     */
    public Client() {

        // Layout GUI
		
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		panel2.setLayout (null);
		editorframe.setSize(600,600);		
		editorframe.pack();
		// writer window
		OK.setBounds(380,580,80,20);
		editor.setEditable(true);
		editorframe.setLocation(200,100);
		
		editor.setBounds(10,10,559,559);
		//panel2.setSize( 1024, 768 );
		  panel2.setPreferredSize(new Dimension(600, 600));
		panel2.add(editor);
		panel2.add(OK);
		editorframe.getContentPane().add(panel2, BorderLayout.CENTER);
		editorframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add buttons action listner also 
		
		 
		
        // Add Listeners
        dataField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield
             * by sending the contents of the text field to the
             * server and displaying the response from the server
             * in the text area.  If the response is "." we exit
             * the whole application, which closes all sockets,
             * streams and windows.
             */
            public void actionPerformed(ActionEvent e) {
				String userChoice=dataField.getText();
				//System.out.println("+++++++++ "+ userChoice +"\n");
                out.println(dataField.getText());
                String response;
                try {
                    response = in.readLine();
                    if (response == null || response.equals(".")) {
                        messageArea.append("Server connection terminated"+"\n");
                          System.exit(0);
                      }
                } catch (IOException ex) {
                       response = "Error: " + ex;
                }
				if(response.equals("!"))
				{
					
				}
				else if(userChoice.equals("1"))
				{
					 try {
				//System.out.println("+++++++++ Came inside if \n");
				
				String filename=JOptionPane.showInputDialog(
            frame,
            "Enter file read to be read:",
            "File names is the username n its case sensitive",
            JOptionPane.QUESTION_MESSAGE);
				 out.println(filename); // sends file name
					String s;
					StringTokenizer strtok;
					s=in.readLine();
					// break the response into line when * encountered
					strtok=new StringTokenizer(s,"*");
                    while(strtok.hasMoreTokens())
                    {
						String res=strtok.nextToken();
						messageArea.append(res + "\n");
						
					}
				}
				catch (IOException ex) {
                       response = "Error: " + ex;
                }
				
				}
				else if(userChoice.equals("2")||userChoice.equals("3"))
				{
					editorframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					editorframe.pack();
					frame.setVisible(false);
					editorframe.setVisible(true);
					
					
					String s;
					StringTokenizer strtok;
					s=response;
					// break the response into line when * encountered
					strtok=new StringTokenizer(s,"*");
                    while(strtok.hasMoreTokens())
                    {
						String res=(strtok.nextToken()).trim();
						editor.append(res + "\n");
						
					}
					actionOK();
					 
				}
				else{
			//	System.out.println("+++++++++ Came inside else\n");
                messageArea.append(response + "\n");
				}
				dataField.selectAll();
            }
        });
    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The Capitalizer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
	 
	 
	 public void actionOK()
	 {
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				
				System.out.println("OK function called \n");
				String ln = System.getProperty("line.separator");
				String text =editor.getText();
				editor.setText("");
				//String as = text.replaceAll("\n", "*");
			
				text=text.trim();
					String s;
					StringTokenizer strtok;
					s=text;
					// break the response into line when * encountered
					strtok=new StringTokenizer(s,"\n");
					text="";
                    while(strtok.hasMoreTokens())
                    {
						text+=(strtok.nextToken()).trim();
						text+="*";
					}
				System.out.println("Here++" + text +"++\n");
				out.println(text);
				 try {
                     in.readLine();
                   
                } catch (IOException ex) {
                       
                }
				frame.setVisible(true);
				editorframe.setVisible(false);
				editorframe.dispose();
				
			}
		 });
	 }
	 
    public void connectToServer(int portno) throws IOException {

        // Get the server address from a dialog box.
    
		// popup code for ip address
		 String serverAddress =JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Client Program",
            JOptionPane.QUESTION_MESSAGE);
			
		 // serverAddress ="localhost";	
			
        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, portno);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 4; i++) {
            messageArea.append(in.readLine() + "\n");
        }
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
		int portno=0;
		if(args.length>0)
		{
			portno=Integer.parseInt(args[0]);
		}
		else
		{
			System.out.println("Execute with portnumber as argument\n");
			System.exit(0);
		}
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer(portno);
    }
}