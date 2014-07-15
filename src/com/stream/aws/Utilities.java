package com.stream.aws;


import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.Request;
import com.amazonaws.auth.AbstractAWSSigner;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.SigningAlgorithm;
import com.amazonaws.util.DateUtils;

public class Utilities {
    
    public static String getTimestamp() {
        return new DateUtils().formatIso8601Date( new Date() );
    }

    public static String extractElement( String json, String element ) {
        boolean hasElement = ( json.indexOf( element ) != -1 );
        if ( hasElement ) {
            int elementIndex = json.indexOf( element );
            int startIndex = json.indexOf( "\"", elementIndex );
            int endIndex = json.indexOf( "\"", startIndex + 1 );

            return json.substring( startIndex + 1, endIndex );
        }
        
        return null;
    }
   
    public static String getSignature( String dataToSign, String key ) {
        return new Signer().getSignature( dataToSign, key );
    }
     
    static class Signer extends AbstractAWSSigner {

        public String getSignature( String dataToSign, String key ) {
            try {
return super.signAndBase64Encode( dataToSign.getBytes( "UTF-8" ), key, SigningAlgorithm.HmacSHA256 );
            }
            catch ( Exception exception ) {
                return null;
            }
        }

@Override
public void sign(Request<?> arg0, AWSCredentials arg1)
throws AmazonClientException {
}

@Override
protected void addSessionCredentials(Request<?> arg0,
AWSSessionCredentials arg1) {
}
    }
    
}

