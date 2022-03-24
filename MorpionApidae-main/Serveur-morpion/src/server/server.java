package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

 
public class server implements Runnable
{
	private int id_joueur=1;
	protected static ServerSocket sockserv;
	
	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		sockserv = new ServerSocket(1234);
		server serv = new server();
		serv.run();
		
	}
	@Override
	public void run() 
	{
		while(true)
		{	
			Socket sockcli;
			try 
			{
				sockcli = sockserv.accept();
				Requete requete = new Requete(sockcli);
				giveSecretKeyToClient(requete);
				Route route = new Route(requete,id_joueur);
				route.start();
				id_joueur++;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void giveSecretKeyToClient(Requete requete)
	{
		Socket sock = requete.getSocket();
		byte[]b = new byte[2048];
		try 
		{
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			in.read(b,0,2048);
	
			byte[] secret_key_to_return = requete.chiffreKey(b);
			System.out.println(secret_key_to_return);
			Thread.sleep(5000);
			out.write(secret_key_to_return);
			System.out.println("azeaze");
				
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
	
}
