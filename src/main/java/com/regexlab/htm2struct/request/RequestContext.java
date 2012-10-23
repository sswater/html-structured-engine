package com.regexlab.htm2struct.request;

import java.util.*;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class RequestContext {

    /** IN/OUT: session data accross requests */
    private BrowseSession session = null;
    
    /** IN: url of this request */
    private String url = null;
    
    /** IN: page refer */
    private RequestContext referer = null;
    
    /** IN: connect proxy, format "host:port" */
    private String proxy = null;
    
    /** IN: request method */
    private String method = "GET";
    
    /** IN: post data */
    private byte [] post = null;
    
    /** IN: request scope cookies */
    private Map<String, String> cookies = new HashMap<String, String>();
    
    /** IN: request scope headers */
    private Map<String, String> headers = new HashMap<String, String>();
    
    /** IN: request scope params, the value should be urlencoded already */
    private Map<String, String> params  = new HashMap<String, String>();
    
    /** IN: timeout */
    private int timeout = 30000;
    
    /** OUT: content type */
    private String contentType = null;
    
    /** OUT: content encoding */
    private String charset = null;
    
    /** OUT: binary body if attachment */
    private byte [] binaryBody = null;
    
    /** OUT: string body if text or html */
    private String stringBody = null;
    
    /** OUT: base tag */
    private String base = null;
    
    /** construct a context */
    public RequestContext(String url)
    {
        this(url, null);
    }
    
    /**
     * construct a context with a reference
     * @param referer reference to previous context
     */
    public RequestContext(String url, RequestContext referer)
    {
        this.url   = url;
        this.referer = referer;
        
        session = referer != null ? referer.session : new BrowseSession();
    }
    
    /** the session data */
    static class BrowseSession
    {
        /** session scope cookies */
        Map<String, String> cookies = new HashMap<String, String>();
        
        /** session scope headers */
        Map<String, String> headers = new HashMap<String, String>();
        
        /** session scope parameters, the value should be urlencoded already */
        Map<String, String> params  = new HashMap<String, String>();
    }

    public String getProxy() {
        return proxy != null ? proxy : referer != null ? referer.getProxy() : null;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getUrl() {
        return url;
    }

    public String getReferer() {
        return referer != null ? referer.url : null;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public byte[] getPost() {
        return post;
    }

    public void setPost(byte[] post) {
        this.post = post;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset != null ? charset : referer != null ? referer.getCharset() : null;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public byte[] getBinaryBody() {
        return binaryBody;
    }

    public void setBinaryBody(byte[] binaryBody) {
        this.binaryBody = binaryBody;
    }

    public String getStringBody() {
        return stringBody;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
    }
    
    public void setRequestCookie(String key, String value) {
        if(value != null) {
            this.cookies.put(key, value);
        }
        else {
            this.cookies.remove(key);
        }
    }
    
    public void setSessionCookie(String key, String value) {
        if(value != null) {
            this.session.cookies.put(key, value);
        }
        else {
            this.session.cookies.remove(key);
        }
    }

    public void setRequestHeader(String key, String value) {
        if(value != null) {
            this.headers.put(key, value);
        }
        else {
            this.headers.remove(key);
        }
    }
    
    public void setSessionHeader(String key, String value) {
        if(value != null) {
            this.session.headers.put(key, value);
        }
        else {
            this.session.headers.remove(key);
        }
    }
    
    public void setRequestParam(String key, String value) {
        if(value != null) {
            this.params.put(key, value);
        }
        else {
            this.params.remove(key);
        }
    }
    
    public void setSessionParam(String key, String value) {
        if(value != null) {
            this.session.params.put(key, value);
        }
        else {
            this.session.params.remove(key);
        }
    }
    
    public Map<String, String> getRequestCookies() {
        return this.cookies;
    }
    
    public Map<String, String> getSessionCookies() {
        return this.session.cookies;
    }

    public Map<String, String> getRequestHeaders() {
        return this.headers;
    }
    
    public Map<String, String> getSessionHeaders() {
        return this.session.headers;
    }
    
    public Map<String, String> getRequestParams() {
        return this.params;
    }
    
    public Map<String, String> getSessionParams() {
        return this.session.params;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getBase() {
        if(base == null && url != null) {
            base = url.substring(0, url.lastIndexOf('/') + 1);
        }
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
