package soget.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Util {
	public final static String KEY ="markin";
	
	public static BigInteger randomBigInteger(BigInteger n) {
		SecureRandom rnd = new SecureRandom();
        int maxNumBitLength = n.bitLength();
        BigInteger aRandomBigInt;
        do {
            aRandomBigInt = new BigInteger(maxNumBitLength, rnd);
            // compare random number lessthan ginven number
        } while (aRandomBigInt.compareTo(n) > 0); 
        return aRandomBigInt;
    }
	
    public static BigInteger nextRandomInteger() {
			SecureRandom random = new SecureRandom();
		    return new BigInteger(10, random);
    }
		
    public static boolean isAlphaNumeric(String s){
	    String pattern= "^[a-zA-Z0-9]*$";
	        if(s.matches(pattern)){
	            return true;
	        }
	        return false;   
	}
	
	public static String Decrypt(String text, String key) throws Exception
    {
              Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
              byte[] keyBytes= new byte[16];
              byte[] b= key.getBytes("UTF-8");
              int len= b.length;
              if (len > keyBytes.length) len = keyBytes.length;
              System.arraycopy(b, 0, keyBytes, 0, len);
              SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
              IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
              cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

              BASE64Decoder decoder = new BASE64Decoder();
              byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
              return new String(results,"UTF-8");
    }

    public static String Encrypt(String text, String key) throws Exception
    {
              Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
              byte[] keyBytes= new byte[16];
              byte[] b= key.getBytes("UTF-8");
              int len= b.length;
              if (len > keyBytes.length) len = keyBytes.length;
              System.arraycopy(b, 0, keyBytes, 0, len);
              SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
              IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
              cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

              byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
              BASE64Encoder encoder = new BASE64Encoder();
              return encoder.encode(results);
    }
}
