package com.stream.aws;

public class GetTokenResponseHandler extends ResponseHandler {

    private final String key;

    public GetTokenResponseHandler( final String key ) {
        this.key = key;
    }

    public Response handleResponse( int responseCode, String responseBody ) {
        if ( responseCode == 200 ) {
            try {
                String json = AESEncryption.unwrap( responseBody, this.key );
                String accessKey = Utilities.extractElement( json, "accessKey" );
                String secretKey = Utilities.extractElement( json, "secretKey" );
                String securityToken = Utilities.extractElement( json, "securityToken" );
                String expirationDate = Utilities.extractElement( json, "expirationDate" );

                return new GetTokenResponse( accessKey, secretKey, securityToken, expirationDate );
            }
            catch ( Exception exception ) {
                return new GetTokenResponse( 500, exception.getMessage() );
            }
        }
        else {
            return new GetTokenResponse( responseCode, responseBody );
        }
    }
    
}