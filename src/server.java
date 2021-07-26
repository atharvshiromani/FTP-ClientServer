import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


import java.util.Scanner;

public class server {

	public final static int FILE_SIZE = 6022386;

	public static void main(String args[]) throws Exception {
		System.out.println("The server is running.");
		ServerSocket servsoc = new ServerSocket(4321);
		int clientNum = 1;
		try {
			while (true) {
				new Server(servsoc.accept()).start();
				System.out.println("Client " + clientNum + " is connected!");
				clientNum++;
			}
		} finally {
			servsoc.close();
		}

	}

	public static class Server extends Thread {

		public FileInputStream fis = null;
		public OutputStream os = null;
		public BufferedInputStream bis = null;
		public FileOutputStream fos = null;
		public BufferedOutputStream bos = null;
		public ObjectOutputStream objectOutput = null;
		PrintStream print1 = null;
		public int bytesRead;
		public int current = 0;

		String uname = "Atharv";
		String pass = "atharv";
		String url = "127.0.0.1";
		int port = 4321;
		Socket soc1;

		public Server(Socket soc1) {
			this.soc1 = soc1;
		}

		public void run() {
			try {
				Scanner sc = new Scanner(soc1.getInputStream());
				print1 = new PrintStream(soc1.getOutputStream());
				
				
				
			while(true) {
				
				String clientres = sc.nextLine();
				if (clientres.contentEquals("ftpclient" + " " + url + " " + port)) {

					print1.println("Client connected!");
					
					while(true) {
						System.out.println("Waiting for input");
						String username = sc.nextLine();
						String password = sc.nextLine();
						if (uname.contentEquals(username) && pass.contentEquals(password)) {
							print1.println("Logged In");
								// Receive command from Client
							do
							{
								String command = sc.nextLine();
								
								if (command.contentEquals("get")) {

									try {
										// Receive file name from Client
										
										String filename = sc.nextLine();
										File propFile = new File("C:\\Users\\Dexter\\Desktop\\server", filename);

										if (propFile.exists()) {
											print1.println("Initiating download");
										
										System.out.println("Received filename " + filename + " from user");
										File file = new File(
												System.getProperty("user.home") + "/Desktop/server/" + filename);
										byte[] mybytearray = new byte[(int) file.length()];
										fis = new FileInputStream(file);
										bis = new BufferedInputStream(fis);
										bis.read(mybytearray, 0, mybytearray.length);
										os = soc1.getOutputStream();
										System.out
												.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
										os.write(mybytearray, 0, mybytearray.length);
										os.flush();
										System.out.println("Done.");

										} else{
											print1.println("File Does Not Exists");
										}} finally {
										if (bis != null)
											bis.close();
										if (os != null)
											os.close();
										if (soc1 != null)
											soc1.close();

									}
								}

								else if (command.contentEquals("upload")) {
									try {
										String filename1 = sc.nextLine();

										File propFile = new File("C:\\Users\\Dexter\\Desktop\\", filename1);

										if (!propFile.exists()) {
											print1.println("File Does Not Exists");
										}

										else {
											print1.println("Intiating upload.");
											byte[] mybytearray = new byte[FILE_SIZE];
											InputStream is = soc1.getInputStream();
											fos = new FileOutputStream(System.getProperty("user.home") + "/Desktop/server/" + filename1);
											bos = new BufferedOutputStream(fos);
											bytesRead = is.read(mybytearray, 0, mybytearray.length);
											current = bytesRead;

											do {
												bytesRead = is.read(mybytearray, current,
														(mybytearray.length - current));
												if (bytesRead >= 0)
													current += bytesRead;
											} while (bytesRead > -1);

											bos.write(mybytearray, 0, current);
											bos.flush();
											System.out.println(
													"File " + filename1 + " downloaded (" + current + " bytes read)");
										}
									} finally {
										if (bis != null)
											bis.close();
										if (os != null)
											os.close();
										if (soc1 != null)
											soc1.close();

									}
								} else if (command.contentEquals("dir")) {

						            
					                       print1.println("List of files on server are:");

					                   
					                   

								}
								else {

						            
				                       print1.println("invalid command!");

				                   
				                   

							}

							}while(true);
						}

						else {
							print1.println("Invalid Username/Password");

						}
				}

					}
				 else {
					print1.println("Invalid IP/Port");

				}
			} 
		}catch (Exception e) {

		}
	}
	}
}