// 3 changes port no class name userfile1
package networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */

public class Server2 {

    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The  server is running.");
        int clientNumber = 0;
		int portno=9898;
		
			System.out.println("Using Default Port Number 9898 \n");
        ServerSocket listener = new ServerSocket(portno);
        try {
            while (true) {
                new ServerThread(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class ServerThread extends Thread {
        private Socket socket;
        private int clientNumber;

		 public static int synchronizationbit=0;
		  public static ArrayList<String> list = new ArrayList<String>();

		 
        public ServerThread(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
				
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Enter a line with only a period to quit");
				out.println("Enter UserName\n");

                // Get messages from the client, line by line; return them
                // capitalized
				int userdatashouldexchange=0;
				int filedatashouldexchange=0;
				String input = in.readLine();
				String newUserName="";
				String password="";
				if(!input.equals("server"))
				{
			    newUserName=input;
				 password=newUser(input);
				int authenticated=0;
				if(password.equals("NULL"))
					{
						
						out.println("Enter the new password to open up the account");
						input = in.readLine();
						// Write new user to file
						password=input;
						writeToUserFile(newUserName,input);
						createFileForUSer(newUserName);
						userdatashouldexchange=1;
						authenticated=1;
					}
				else
					{
						out.println("Enter the password");
						input = in.readLine();
						int count=0;
						if(!password.equals(input))
						{
								while(count<2)
								{
									out.println("Enter the correct password");
									input = in.readLine();
									if(password.equals(input))
									{
										authenticated=1;
										break;
									}
									count++;
								}
						}
						else
						{
							authenticated=1;
						}
						
					}
					if(authenticated!=1)
					{
						out.println("You have entered 3 incorrect passwords !!! Please connect again");
					}
						if(authenticated==1){
						      out.println(newUserName + "  SERVER2: Please enter your choice 1.Read the file 2. Write to file 3. Delete from file  Type exit to quit. ");
						while (authenticated==1) {
								input = in.readLine();
									log("User choice is " + input +"\n");
									
									if (input == null || input.equals("exit")) {
									    out.println(".");
										break;
									}
									else if(input.equals("")||input.equals("\n"))
									{
										out.println("!");
									}
									else if(input.equals("1")) // Read from file 
									{
										 out.println("Enter file name");
										   String filename=in.readLine();
    									     FileReader fr;
                            			 BufferedReader br;
                            			 
                            			 String s;
                            		    String fulldata="";
                                        try
                                        {
										if(synchronizationbit==1)
										{
                                             out.println("Server under Synchronization try to read later"); 
										}
										else if(list.contains(filename))
										{
											 out.println("File is being edited by the owner Sorry try again later"); 
										}
										else{
											  fr=new FileReader("./Server2/"+filename+ ".txt"); // userfilename
                                                br=new BufferedReader(fr);
                                
                                                while((s=br.readLine())!=null)
                                                {
                                                    fulldata+=s;
                                                    fulldata+="*";
                                                }
                                         	br.close();       
        									    out.println(fulldata);
											
										}
                                        }
                                         catch(FileNotFoundException e)
                                        {
                                            System.out.println("File was not found!");
											out.println("No such file exsists");
                                        }
                                        catch(IOException e)    
                                        {
                                            System.out.println("No file found!");
											out.println("No such file exsists");
                                        }
                                                                    
                                     //    out.println("Please enter ur next choice ");	
                                        
									}
									else if(input.equals("2")||input.equals("3")) // Write to file
									{
									    filedatashouldexchange=1;
										 FileReader fr;
                            			 BufferedReader br;
                            			 list.add(newUserName);
										 
                            			 String s;
                            		    String fulldata="";
                                        try
                                        {
                                                fr=new FileReader("./Server2/"+newUserName + ".txt"); // userfilename
                                                br=new BufferedReader(fr);
                                
                                                while((s=br.readLine())!=null)
                                                {
                                                    fulldata+=s;
                                                    fulldata+="*";
                                                }
                                         	br.close();       
        									    out.println(fulldata);
												
											String modifieddata=in.readLine();
											
											log("Data received in server is ++"+modifieddata+"++\n");
											
											//String as = modifieddata.replaceAll("*", "\n");	
											PrintWriter writer = new PrintWriter("./Server2/"+newUserName + ".txt");
											
										
											StringTokenizer strtok;
										
											// break the response into line when * encountered
											strtok=new StringTokenizer(modifieddata,"*");
											while(strtok.hasMoreTokens())
											{
												String res=strtok.nextToken();
												writer.print(res);
												writer.print("\n");
												
											}
												list.remove(newUserName);
											out.println("done");
											
											writer.close();
											
										//	out.println("Please enter ur next choice ");
												
                                        }
                                         catch(FileNotFoundException e)
                                        {
                                            System.out.println("File was not found!");
                                        }
                                        catch(IOException e)    
                                        {
                                            System.out.println("No file found!");
                                        }
                                                
												
												
										
									}
									else
									out.println("Not a valid choice,please enter your choice again");
						    }
						}
						synchronizationbit=1;	
						if(userdatashouldexchange==1)
						{
						    	connectToServer(newUserName,password);
						}
						if(filedatashouldexchange==1)
						{
						    	transferfile(newUserName);
						}
						synchronizationbit=0;
					}
				else{	//Communication between both the servers
					out.println("Enter your choice \n 1. user credential exchange\n 2. User files updation");
					input = in.readLine();
					if(input.equals("1"))
					{
						String userName,pass;
						out.println("U");
						userName = in.readLine();
						out.println("P");
						pass = in.readLine(); 
						
						if(newUser(userName).equals("NULL")){
						writeToUserFile(userName,pass);
						createFileForUSer(userName);
						}
						out.println("done");
					}
					else{
						out.println("done");
								String 	filename=in.readLine();// get user name
								out.println("data");
								String fileData=in.readLine();// get the file data
										PrintWriter writer = new PrintWriter("./Server2/"+filename+ ".txt");
											
										
											StringTokenizer strtok;
										
											// break the response into line when * encountered
											strtok=new StringTokenizer(fileData,"*");
											while(strtok.hasMoreTokens())
											{
												String res=strtok.nextToken();
												writer.print(res);
												writer.print("\n");
												
											}
											
											out.println("done");
											
											writer.close();		
					}
				}
				
				
              
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
		private void writeToUserFile(String userName,String Password)
		{
			try
			{
 
				FileWriter fw;
				fw=new FileWriter("account2.txt", true);
				fw.write(userName+" "+Password);//appends the string to the file
				fw.write("\n");
				fw.close();
				
			}
			catch(IOException ioe)
			{
				System.err.println("IOException: " + ioe.getMessage());
			}
			 
		}
		private String newUser(String userName) {
		
			 FileReader fr;
			 BufferedReader br;
			 
			 String s;
			 StringTokenizer strtok;
			//System.out.println("Came here %s \n"+ userName);
			userName=userName.trim();
            try
            {
                fr=new FileReader("account2.txt");
                br=new BufferedReader(fr);

                while((s=br.readLine())!=null)
                {
					strtok=new StringTokenizer(s," ");
                    while(strtok.hasMoreTokens())
                    {
						String uname=strtok.nextToken();
						uname=uname.trim();
						String password;	
						//System.out.println("username is %s \n"+ uname);
						if(uname.equals(userName))
						{
							
							password=strtok.nextToken();
						//	System.out.println("The password is %s "+ password);
							br.close();
							return password;
						}
						else{
							uname=strtok.nextToken();
						}
                    }
                }
						br.close();
						return "NULL"; 
            }
            catch(FileNotFoundException e)
            {
                System.out.println("File was not found!");
            }
            catch(IOException e)    
            {
                System.out.println("No file found!");
            }
                
           return "NULL";
        }
        
        private void connectToServer(String uname,String password) throws IOException {
			// Make connection and initialize streams
		     Socket socket = new Socket("ranger0.cs.mtsu.edu", 9899); // server 1 port no server 2 is alwaya running on ranger 1 and server 1 on ranger 0 
			 BufferedReader in;
			 PrintWriter out;
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Consume the initial welcoming messages from the server
			for (int i = 0; i < 4; i++) {
				in.readLine() ;
			}
			out.println("server");
				String temp;
				temp=in.readLine(); // server 2sends options
				System.out.println(temp);
				out.println("1"); // option selected
				temp=in.readLine(); // sends U
				System.out.println(temp);
				out.println(uname); //replied uname
				temp=in.readLine(); // sends P
				System.out.println(temp);
				out.println(password); // replied password
				in.readLine();
				socket.close();
    }
	
	private void transferfile(String uname)
	{
		  try
          {
		 Socket socket = new Socket("ranger0.cs.mtsu.edu", 9899); // server 2 port no server 2 is alwaya running on ranger 1 and server 1 on ranger 0 
			 BufferedReader in;
			 PrintWriter out;
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Consume the initial welcoming messages from the server
			for (int i = 0; i < 4; i++) {
				in.readLine() ;
			}
			out.println("server");
			String fileData=in.readLine(); // get the file data with * appended 
			out.println("2"); // sends choice as file transfer
			fileData=in.readLine(); // get the signal as done
			out.println(uname);
			in.readLine(); 
			String s;
                            		    String fulldata="";
                                   
											 FileReader fr;
											BufferedReader br;
                                                fr=new FileReader("./Server2/"+uname + ".txt"); // userfilename
                                                br=new BufferedReader(fr);
                                
                                                while((s=br.readLine())!=null)
                                                {
                                                    fulldata+=s;
                                                    fulldata+="*";
                                                }
                                         	br.close();       
        									    out.println(fulldata); // sends file data 
												// waitd for signal done
												in.readLine();
										}
										catch(FileNotFoundException e)
                                        {
                                            System.out.println("File was not found!");
                                        }
                                        catch(IOException e)    
                                        {
                                            System.out.println("No file found!");
                                        }
										catch(Exception e)    
                                        {
                                            System.out.println("Error !");
                                        }
										
										
																			
			
	}
	
	
	
    
         private void createFileForUSer(String userName){
	try{
           File permfile = new File("Server2",userName + ".txt");
            permfile.createNewFile(); 
			permfile.setReadable(true, false);
			permfile.setExecutable(true, false);
			permfile.setWritable(true, false);
	}
	catch(Exception k){}
	
	}
	}
}