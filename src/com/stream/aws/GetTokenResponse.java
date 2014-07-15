package com.stream.aws;

public class GetTokenResponse extends Response {
    private final String accessKey;
    private final String secretKey;
    private final String securityToken;
    private final String expirationDate;
    
    public GetTokenResponse( final int responseCode, final String responseMessage ) {
        super( responseCode, responseMessage );
        this.accessKey = null;
        this.secretKey = null;
        this.securityToken = null;
        this.expirationDate = null;
    }
    
    public GetTokenResponse( final String accessKey, final String secretKey, final String securityToken, final String expirationDate ) {
        super( 200, null );
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.securityToken = securityToken;
        this.expirationDate = expirationDate;
    }
    
    public String getAccessKey() {
        return this.accessKey;
    }
    
    public String getSecretKey() {
        return this.secretKey;
    }
    
    public String getSecurityToken() {
        return this.securityToken;
    }
    
    public String getExpirationDate() {
        return this.expirationDate;
    }
}