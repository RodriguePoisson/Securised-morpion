package utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Requete
{
	private Socket socketcli;
	private DataInputStream in;
	private DataOutputStream out;
	private Cipher cipher;
	private Cipher cipherDecrypt;
	public boolean waitResponse = false;
	public SecretKey secretKey;
	public Requete(Socket socketcli)
	{
		this.socketcli = socketcli;
		
		try
		{
			this.out = new DataOutputStream(socketcli.getOutputStream());
			this.in= new DataInputStream(socketcli.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initCiphers()
	{
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			cipherDecrypt = Cipher.getInstance("DES");
			cipherDecrypt.init(Cipher.DECRYPT_MODE,secretKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getSocket() {return this.socketcli;}
	public void Write(String query)
	{
		try 
		{
			waitResponse = true;
			this.out.write(this.cipher.update(query.getBytes()));
			
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String read()
	{
		byte[]b = new byte[80];
		try 
		{
			this.in.read(b,0,80);
			String regExp="\u0000*$";
			b = this.cipherDecrypt.update(b);
			String reponse = new String(b, StandardCharsets.UTF_8);
			String[] reponse_a_trier= reponse.split(regExp);
			reponse = reponse_a_trier[0];
			return reponse;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
