package com.stream.aws;

import com.amazonaws.util.HttpUtils;

public class GetTokenRequest extends Request {

    private final String endpoint;
    private final String uid;
    private final String key;
    private final boolean useSSL;
    
    public GetTokenRequest( final String endpoint, final boolean useSSL, final String uid, final String key ) {
        this.endpoint = endpoint;
        this.useSSL = useSSL;
        this.uid = uid;
        this.key = key;
    }
    
    public String buildRequestUrl() {
        StringBuilder builder = new StringBuilder( ( this.useSSL ? "https://" : "http://" ) );
        builder.append( this.endpoint );
        builder.append( "/" );
        
        String timestamp = Utilities.getTimestamp();
        String signature = Utilities.getSignature( timestamp, key );
        
        builder.append( "gettoken" );
        builder.append( "?uid=" + HttpUtils.urlEncode( this.uid, false ) );
        builder.append( "&timestamp=" + HttpUtils.urlEncode( timestamp, false ) );
        builder.append( "&signature=" + HttpUtils.urlEncode( signature, false ) );
        
        return builder.toString();
    }
    
}
