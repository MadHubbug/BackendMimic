package com.stream.aws;
import com.amazonaws.util.HttpUtils;

public class RegisterDeviceRequest extends Request {

    private final String endpoint;
    private final String uid;
    private final String key;
    private final boolean useSSL;
    
    public RegisterDeviceRequest( final String endpoint, final boolean useSSL, final String uid, final String key ) {
        this.endpoint = endpoint;
        this.useSSL = useSSL;
        this.uid = uid;
        this.key = key;
    }
    
    public String buildRequestUrl() {
        StringBuilder builder = new StringBuilder( ( this.useSSL ? "https://" : "http://" ) );
        builder.append( this.endpoint );
        builder.append( "/" );
        builder.append( "registerdevice" );
        builder.append( "?uid=" + HttpUtils.urlEncode( this.uid, false ) );
        builder.append( "&key=" + HttpUtils.urlEncode( this.key, false ) );
        
        return builder.toString();
    }
    
}