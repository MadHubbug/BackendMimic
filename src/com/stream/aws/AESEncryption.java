package com.stream.aws;

import java.security.AlgorithmParameters;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AESEncryption {

    public static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String unwrap( String cipherText, String key ) throws Exception {
        byte[] dataToDecrypt = Base64.decodeBase64( cipherText.getBytes() );
        byte[] iv = new byte[16];
        byte[] data = new byte[ dataToDecrypt.length - 16 ];

        System.arraycopy( dataToDecrypt, 0, iv, 0, 16 );
        System.arraycopy( dataToDecrypt, 16, data, 0, dataToDecrypt.length - 16 );
        
        byte[] plainText = decrypt( data, key, iv );
        return new String( plainText );
    }

    public static byte[] decrypt( byte[] cipherBytes, String key, byte[] iv ) throws Exception {
Cipher cipher = Cipher.getInstance( ENCRYPTION_ALGORITHM );
        AlgorithmParameters params = AlgorithmParameters.getInstance( "AES" );
        params.init( new IvParameterSpec( iv ) );
cipher.init( Cipher.DECRYPT_MODE, getKey( key ), params );
return cipher.doFinal( cipherBytes );
    }

    private static SecretKeySpec getKey( String key ) throws Exception {
        return new SecretKeySpec( Hex.decodeHex( key.toCharArray() ), "AES" );
    }
    
    /*
private static byte[] getIv() throws Exception {
byte[] iv = new byte[16];
new SecureRandom().nextBytes( iv );
return iv;
}
*/
}