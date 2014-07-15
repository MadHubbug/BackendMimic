package com.stream.aws;


public class Response {
    public static final Response SUCCESSFUL = new Response( 200, "OK" );

    private final int responseCode;
    private final String responseMessage;
        
    public Response( final int responseCode, final String responseMessage ) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
    
    public boolean requestWasSuccessful() {
        return this.getResponseCode() == 200;
    }
    
    public int getResponseCode() {
        return this.responseCode;
    }
    
    public String getResponseMessage() {
        return this.responseMessage;
    }
        
}