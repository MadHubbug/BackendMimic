package com.stream.aws;

public class ResponseHandler {

    public Response handleResponse( int responseCode, String responseBody ) {
        return new Response( responseCode, responseBody );
    }
    
}

