package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Requete
{
	private Socket socketcli;
	private DataInputStream in;
	private DataOutputStream out;
	private SecretKey secretKey;
	private Cipher cipher;
	private Cipher cipherDecrypt;
	private Cipher cipherAssym;
	public boolean waitResponse = false;
	public Requete(Socket socketcli)
	{
		this.socketcli = socketcli;
		
		try
		{
			this.out = new DataOutputStream(socketcli.getOutputStream());
			this.in= new DataInputStream(socketcli.getInputStream());
			secretKey = KeyGenerator.getInstance("DES").generateKey();
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			cipherDecrypt = Cipher.getInstance("DES");
			cipherDecrypt.init(Cipher.DECRYPT_MODE,secretKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public SecretKey getSecretKey()
	{
		return this.secretKey;
	}
	
	public byte[] chiffreKey(byte[]b)
	{
		try 
		{
			KeyFactory keyfact = KeyFactory.getInstance("RSA");
			EncodedKeySpec encodKey = new X509EncodedKeySpec(b);
			PublicKey publicKey = keyfact.generatePublic(encodKey);
			System.out.println(publicKey.getEncoded());
			this.cipherAssym = Cipher.getInstance("RSA");
			cipherAssym.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipherAssym.update(this.secretKey.getEncoded());
		
			
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
		
	}
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
	public Socket getSocket() {return this.socketcli;}
}
