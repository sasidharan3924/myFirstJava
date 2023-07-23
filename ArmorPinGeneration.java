package com.spring.basics.springBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;



public class ArmorPinGeneration 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		String userLoginId       ="RUSSELL2016";
		String userPasswordPlain ="Mash1bank#";
		String encyptedPINValue  ="";
		try 
		{
			encyptedPINValue = ArmorPinGeneration.getTripleDESEncrypt(userLoginId, userPasswordPlain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	/*
	 * This method is called to encrypt the Host password. @param pUserId @param
	 * pPassword @return @throws Exception
	 */
	private static String encrypt(String pUserId, String pPassword) throws Exception {
		String lPassword = null;
		String cprvkey = "123456789012345678901234";
		System.out.println("=================================");
		try {
			StringBuffer lStringkey = new StringBuffer();
			lStringkey.append(pUserId).append(pPassword).append(cprvkey);
			String lValue = lStringkey.toString();

			byte[] lkey = lValue.getBytes();
			DESedeKeySpec lDesedekeyspec = new DESedeKeySpec(lkey);
			SecretKeyFactory lSecretkeyfactory = SecretKeyFactory.getInstance("DESede");
			SecretKey lSecretkey = lSecretkeyfactory.generateSecret(lDesedekeyspec);
			Cipher lencrypter = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			lencrypter.init(1, lSecretkey);

			byte[] clearText = pPassword.getBytes();

			byte[] encryptedText = lencrypter.doFinal(clearText);
			//System.out.println("encryptedText="+byteToBase16(encryptedText));
			MessageDigest lMDigest = MessageDigest.getInstance("MD5");
			lMDigest.update(encryptedText);
			lPassword = byteToBase16(lMDigest.digest());

		} catch (Exception pException) {
			System.out.println("SetUserPasswords.encrypt() Failed*");
			System.err.println(pException);			

			throw pException;
		}		
		System.out.println("=================================");
		return lPassword;
	}	
	
	/**
	 * This method is called to encrypt the User's password with the 
	 * Latest TripleDES Algorithm which will be used henceforth 
	 * for New users as well
	 * @param pUserId
	 * @param pPassword
	 * @return String 
	 * @throws Exception
	 */
	private static String  getTripleDESEncrypt(String pUserId, String pPassword)
			throws Exception {
		//cLog.logEntry("NSECValidateUserPwd :  getTripleDESEncrypt() : NEW LOGIC : Enter");
		//System.out.println("=================================");
		
		String lPasswd = null;
		String cprvkey = "123456789012345678901234";
		try {
			StringBuffer lStringkey = new StringBuffer();
			lStringkey.append(pUserId).append(pPassword).append(cprvkey);
			String value = lStringkey.toString();

			byte[] lkey = value.getBytes();
			DESedeKeySpec lDesedekeyspec = new DESedeKeySpec(lkey);
			SecretKeyFactory secretkeyfactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey secretkey = secretkeyfactory
					.generateSecret(lDesedekeyspec);
			Cipher m_encrypter = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			m_encrypter.init(1, secretkey);

			byte[] clearText = pPassword.getBytes();

			byte[] encryptedText = m_encrypter.doFinal(clearText);	

			for (byte b : encryptedText) {
				//System.out.print(b+",");
			}
			lPasswd = getMD5Hash(encryptedText);

		} catch (Exception pException) {
			//cLog.logError("NSECValidateUserPwd :getTripleDESEncrypt : NEW LOGIC : encrypt() ", "Failed*");
			throw pException;
		}
		//cLog.logExit("NSECValidateUserPwd :  getTripleDESEncrypt : NEW LOGIC :encrypt() : Exit");
		//System.out.println("=================================");
		//System.out.println("--"+pUserId+" = "+pPassword);
		//System.out.println("DELETE FROM TB_ARM_USER_PASSWD WHERE USER_ID IN (SELECT ID FROM TB_ARM_USER_MASTER WHERE USER_LOGIN = '"+pUserId+"');");
		//System.out.println("SELECT USER_LOGIN,USER_PWD FROM TB_ARM_USER_MASTER WHERE USER_LOGIN='"+pUserId+"';");
		//System.out.println("UPDATE TB_ARM_USER_MASTER SET USER_PWD='"+lPasswd+"' , LAST_PSWD_CHNG=SYSDATE , USER_STATUS=2 , UNSCSSFL_ATMPT=0 WHERE  USER_LOGIN ='"+pUserId+"';");
		//System.out.println();
		//System.out.println();
		System.out.println(pUserId+" = "+pPassword+" = "+lPasswd);
		return lPasswd;

	}
	
	/**
	 * Convert bytes to a base16 string.
	 */
	public static String byteToBase16(byte[] byteArray) {

		StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);

		for (int i = 0; i < byteArray.length; i++) 
			hexBuffer.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));

		return hexBuffer.toString();
	}
	
	/**
	 * Return the MD5 Hash String Which is an representation of byte
	 * into Alpha/numeric string
	 * @param pbCipherText
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5Hash(byte[] pbCipherText) throws NoSuchAlgorithmException
	{
		/* String lLogLocMsg = new StringBuffer(cLogLocMsg)
		.append(" ").append("getMD5Hash(byte[] pbCipherText)")
		.toString(); */
		
		MessageDigest lMDigest;
		String lMD5String = null ;
		try {
			lMDigest = MessageDigest.getInstance("MD5");

		lMDigest.update(removeTrailingByteZero(pbCipherText),0,getTrimByteArrayLengh(removeTrailingByteZero(pbCipherText)));	

		lMD5String = byteToBase16(lMDigest.digest());
		} catch (NoSuchAlgorithmException e) {			
			//cLog.logError(lLogLocMsg+"NoSuch Algorithm is Exception",e);
			throw e;
		}
		return lMD5String;
	}
	

	/**
	 * Remove the Trailing unused byte from the byte Array
	 * @param byteArray
	 * @return
	 */
	public static byte[] removeTrailingByteZero(byte[] byteArray){
		int count= 0;		
		for (int i = byteArray.length-1; i >=0  ; i--) {			
			if(byteArray[i] !=0  ){			
				break;				
			}			
			count++;			
		}	
		byte[] withOutZero = new byte[byteArray.length-count];
		for (int i = 0;i<(byteArray.length-count) ; i++) {			
			withOutZero[i] = byteArray[i];			
		}
		return withOutZero;
	}
	
	/**
	 * return the Byte Array Length after remove unuse bytes ie.AB,00,00,
	 * length = (totalLength - '00,00'.length)
	 * @param byteArray
	 * @return
	 */
	public static int getTrimByteArrayLengh(byte byteArray[])
	{
		int count =0;
		for (int i = 0; i < byteArray.length; i++)
		{			
			if(byteArray[i]==0)
			{
				break;
			}
			count++;
		}				
		return count;
	}
}
