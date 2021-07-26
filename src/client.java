import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client {

	// public final static String
	// FILE_TO_RECEIVED = "C:\\Users\\Dexter\\Desktop\\abc.pdf";

	public final static int FILE_SIZE = 6022386;
	static BufferedReader UserInput = null;
	static PrintStream print = null;
	static Scanner sc1 = null;
	static int bytesRead;
	static int current = 0;
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static FileInputStream fis = null;
	static OutputStream os = null;
	static BufferedInputStream bis = null;
	static ObjectInputStream objectInput = null;

	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException {

		String username, password;
		UserInput = new BufferedReader(new InputStreamReader(System.in));
		Scanner in = new Scanner(System.in);
		String url = "127.0.0.1";
		int port = 4321;
		Socket soc = new Socket(url, port);
		print = new PrintStream(soc.getOutputStream());
		sc1 = new Scanner(soc.getInputStream());

		ftpIpPort();
		// Check connection with server

		System.out.println("--------------------");
		login();

		dummy(soc);

	}

	public static void dummy(Socket soc) throws IOException {
		System.out.println("Enter Command");
		String msg3 = UserInput.readLine();
		print.println(msg3);
		// String msg4 = sc1.nextLine();
		//System.out.println("Enter Filename");
		//String filename = UserInput.readLine();
		//print.println(filename);

		if (msg3.contentEquals("get")) {

			downloadFile(soc);

		} else if (msg3.contentEquals("dir")) {

			listOfFiles();

		}

		else if (msg3.contentEquals("upload")) {

			uploadFile(soc);
		}
		else {

			System.out.println(sc1.nextLine());
			dummy(soc);
		}

	}

	public static void login() {
		try {
			System.out.println("Enter Username");
			String username = UserInput.readLine();
			print.println(username);
			System.out.println("Enter Password");
			String password = UserInput.readLine();
			print.println(password);
			String msg = sc1.nextLine();

			if (!msg.equals("Logged In")) {
				System.out.println("Try Again");
				login();
			}
		} catch (Exception e) {

		}

	}

	public static void ftpIpPort() {
		try {
			String clientres = UserInput.readLine();
			print.println(clientres);
			String servres1 = sc1.nextLine();
			System.out.println(servres1);

			if (!servres1.equals("Client connected!")) {
				System.out.println("Try Again");
				ftpIpPort();
				return;
			}

		} catch (Exception e) {
		}
	}

	public static void uploadFile(Socket soc) {
		try {
			System.out.println("Enter Filename");
			String filename = UserInput.readLine();
			print.println(filename);
			String fileres = sc1.nextLine();
			if (fileres.contentEquals("File Does Not Exists")) {
				System.out.println(fileres);
				dummy(soc);

			} else {
				System.out.println(fileres);
				File file = new File(System.getProperty("user.home") + "/Desktop/" + filename);
				byte[] mybytearray = new byte[(int) file.length()];
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				os = soc.getOutputStream();
				System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				System.out.println("Done.");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (soc != null)
				try {
					soc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}

	public static void downloadFile(Socket soc) {
		try {
			
			System.out.println("Enter Filename");
			String filename = UserInput.readLine();
			print.println(filename);

			String get = sc1.nextLine();
			if (get.contentEquals("Initiating download")) {
				
				
				byte[] mybytearray = new byte[FILE_SIZE];
				InputStream is = soc.getInputStream();
				fos = new FileOutputStream(System.getProperty("user.home") + "/Desktop/client/" + filename);
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				System.out.println("File " + filename + " downloaded (" + current + " bytes read)");

			} else {
				System.out.println(get);
				dummy(soc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (soc != null)
				try {
					soc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}


public static void listOfFiles()

{
	String lis=sc1.nextLine();
	File f = new File(System.getProperty("user.home")+"/Desktop/server");

	File[] lof = f.listFiles();
	System.out.println(lis);
	for(int i=0; i<lof.length; i++)

	{
		String name = lof[i].getName();
		System.out.println(name);
	}
	
}

}



