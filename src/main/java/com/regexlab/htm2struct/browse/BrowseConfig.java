package com.regexlab.htm2struct.browse;

import java.util.*;

/**
 * This class stores the config of rules and patterns
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class BrowseConfig {

    // ----------------- LINK ---------------

    /** url explicitly, 'url' or 'aslink' to do a request */
    private String url = null;

    /** open interested field as a link */
    private boolean aslink = false;
    
    /** whether trigger check url event */
    private boolean checklink = true;
    
    /** method explicity, null for default */
    private String method = null;
    
    /** charset explicitly for URL encode, null for default */
    private String charset = null;
    
    /** proxy special, format host:port */
    private String proxy = null;
    
    /** language */
    private String language = null;
    
    /** explicitly request scope cookies, headers, parameters, 'k=v' separated by '|' */
    private String cookies = null, headers = null, params = null;
    
    /** explicitly session scope cookies, headers, parameters, 'k=v' separated by '|' */
    private String s_cookies = null, s_headers = null, s_params = null;
    
    // ---------------- LINK END -------------

    /** interested field, if null, first field. a fieldname or a pattern like '$(name1)$(name2)' */
    private String interest = null;
    
    /** regex pattern to match */
    private String pattern = null;

    /** group to field name, format 1=name|2=name */
    private String gfmap = null;
    
    /** pattern to make the hash, format md5:$(name1)$(name2) */
    private String hash = null;

    /** fields inherits from parent, separated by '|' */
    private String inherits = null;
    
    /** save point for multi-pages */
    private boolean save = false;
    
    /** attribute tokens to save to BrowseResult, contains placeholders */
    private Map<String, String> tokens = new HashMap<String, String>();
    
    /** further browse */
    private List<BrowseConfig> children = new ArrayList<BrowseConfig>();

    public Map<String, String> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
    }

    public List<BrowseConfig> getChildren() {
        return children;
    }

    public void setChildren(List<BrowseConfig> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAslink() {
        return aslink;
    }

    public void setAslink(boolean aslink) {
        this.aslink = aslink;
    }

    public boolean isChecklink() {
        return checklink;
    }

    public void setChecklink(boolean checklink) {
        this.checklink = checklink;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSessionCookies() {
        return s_cookies;
    }

    public void setSessionCookies(String cookies) {
        s_cookies = cookies;
    }

    public String getSessionHeaders() {
        return s_headers;
    }

    public void setSessionHeaders(String headers) {
        s_headers = headers;
    }

    public String getSessionParams() {
        return s_params;
    }

    public void setSessionParams(String params) {
        s_params = params;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getInherits() {
        return inherits;
    }

    public void setInherits(String inherits) {
        this.inherits = inherits;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setGfmap(String gfmap) {
        this.gfmap = gfmap;
    }

    public String getGfmap() {
        return gfmap;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getProxy() {
        return proxy;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
