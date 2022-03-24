package connexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import utils.Requete;

public final class Connexion 
{
	private static Socket socket;
	public static Requete requete;
	public static void initialiseSocket() throws UnknownHostException, IOException
	{
		try 
		{
			socket = new Socket("127.0.0.1",1234);
			requete = new Requete(socket);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			KeyPair keypair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
			PrivateKey privateKey = keypair.getPrivate();
			PublicKey publicKey = keypair.getPublic();
			System.out.println(publicKey.getEncoded());
			out.write(publicKey.getEncoded());
			
			byte[]b = new byte[2048];
			Thread.sleep(2000);
			in.read(b,0,2048);
		
			
			Cipher cipherDec = Cipher.getInstance("RSA");
			cipherDec.init(Cipher.DECRYPT_MODE, privateKey);
			b = cipherDec.doFinal(b);
			requete.secretKey = new SecretKeySpec(b,"RSA");
			requete.initCiphers();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
