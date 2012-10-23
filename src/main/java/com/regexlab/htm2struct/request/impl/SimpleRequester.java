package com.regexlab.htm2struct.request.impl;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.apache.commons.logging.*;

import com.regexlab.htm2struct.request.*;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class SimpleRequester implements RequestInterface {
    private static Log log = LogFactory.getLog(SimpleRequester.class);

    public boolean request(RequestContext context) {
        
        // get url
        String url     = context.getUrl();
        String charset = context.getCharset() != null ? context.getCharset() : "utf-8";
        
        url = escape128(url, charset);
        
        // simple cache
        // TODO
        
        // do request
        HttpURLConnection conn = null;
        try {
            // open connect
            conn = openConnection(url, context, charset);

            // fixed headers
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1;) Gecko/20100722 Firefox/3.6.8");
            
            // code
            int code = conn.getResponseCode();
            if(!(code >= 200 && code < 300)) {
                if(log.isErrorEnabled()) log.error("connect failed, code = " + code + ", status = " + conn.getResponseMessage());
                return false;
            }

            // cookies to session
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            if(cookies != null) {
                for(String cookie : cookies) {
                    int pe = cookie.indexOf('=');
                    int pc = cookie.indexOf(';', pe); if(pc < 0) pc = cookie.length();
                    context.setSessionCookie(cookie.substring(0, pe), cookie.substring(pe+1, pc));
                }
            }
            
            // body
            InputStream in = conn.getInputStream();
            byte [] binary = null;
            
            // read binary
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte [] buf = new byte[10240];
                int red;
                try {
                    while((red = in.read(buf)) > 0) {
                        out.write(buf, 0, red);
                    }
                    out.close();
                }
                catch(IOException ioe) {
                }
                binary = out.toByteArray();
            }
            context.setBinaryBody(binary);
            
            // content type
            String contentType = conn.getContentType();
            context.setContentType(contentType);
            
            // try to get charset from content type            
            Matcher m = regexContentType.matcher(contentType);
            String charset_2 = null;
            if(m.find())  charset_2 = m.group(1);

            // find charset assuming it is html
            if(charset_2 == null) {
                String siso = new String(binary, "iso-8859-1");
                m = regexMeta.matcher(siso);
                if(m.find()) {
                    charset_2 = m.group(1);
                }
            }

            // if it is a text/*
            if(contentType.startsWith("text") || charset_2 != null) {
                
                context.setCharset(charset_2);
                charset_2 = charset_2 != null ? charset_2 : "utf-8";
                
                // get text with charset_2
                String textbody = new String(binary, charset_2);
                context.setStringBody(textbody);
                
                // get base
                Matcher mbase = regexBase.matcher(textbody);
                if (mbase.find()) {
                    context.setBase( mbase.group(2) );
                }
            }
            
            // binary to string anyway
            else {
                context.setStringBody(new String(binary, "iso-8859-1"));
            }
            
            return true;
        }
        catch (IOException e) {
            if(log.isErrorEnabled()) log.error("request failed ", e);
            return false;
        }
        finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * context settings
     * @param url 
     */
    private HttpURLConnection openConnection(String url, RequestContext context, String charset) throws IOException {

        boolean post   = "POST".equalsIgnoreCase(context.getMethod());
        StringBuffer qstrdata = new StringBuffer();
        StringBuffer postdata = new StringBuffer();
        StringBuffer paramto  = post ? postdata : qstrdata;
        StringBuffer cookies  = new StringBuffer();
        
        // raw data
        if(post && context.getPost() != null) {
            postdata.append(new String(context.getPost(), "iso-8859-1"));
        }
        
        // params
        for(Map.Entry<String, String> e : context.getRequestParams().entrySet()) {
            paramto.append(paramto.length()>0?"&":"")
                   .append(e.getKey() + "=" + escape128(e.getValue(), charset));
        }
        for(Map.Entry<String, String> e : context.getSessionParams().entrySet()) {
            paramto.append(paramto.length()>0?"&":"")
                   .append(e.getKey() + "=" + escape128(e.getValue(), charset));
        }
        
        // proxy
        Proxy proxy = Proxy.NO_PROXY;
        if(context.getProxy() != null) {
            String p = context.getProxy();
            int pp = p.indexOf(':');
            if(pp > 0) {
                String host = p.substring(0, pp);
                try {
                    int port = Integer.parseInt(p.substring(pp + 1));
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                }
                catch(Exception ne) { }
            }
        }

        // open connection
        if(qstrdata.length() > 0) {
            url += url.indexOf('?') > 0 ? "&" : "?";
            url += qstrdata.toString();
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection(proxy);
        
        // method
        conn.setRequestMethod(post?"POST":"GET");

        // headers
        for(Map.Entry<String, String> e : context.getRequestHeaders().entrySet()) {
            if("Cookie".equalsIgnoreCase(e.getKey()))
                cookies.append(cookies.length()>0?";":"")
                       .append(e.getValue());
            else
                conn.setRequestProperty(e.getKey(), e.getValue());
        }
        for(Map.Entry<String, String> e : context.getSessionHeaders().entrySet()) {
            if("Cookie".equalsIgnoreCase(e.getKey()))
                cookies.append(cookies.length()>0?";":"")
                       .append(e.getValue());
            else
                conn.setRequestProperty(e.getKey(), e.getValue());
        }
        
        // cookies
        for(Map.Entry<String, String> e : context.getRequestCookies().entrySet()) {
            cookies.append(cookies.length()>0?";":"")
                   .append(e.getKey() + "=" + e.getValue());
        }
        for(Map.Entry<String, String> e : context.getSessionCookies().entrySet()) {
            cookies.append(cookies.length()>0?";":"")
                   .append(e.getKey() + "=" + e.getValue());
        }
        
        if(cookies.length() > 0) {
            conn.setRequestProperty("Cookie", cookies.toString());
        }
        
        // time out
        conn.setReadTimeout(context.getTimeout());
        
        // language
        if(context.getLanguage() != null) {
            conn.addRequestProperty("Accept-Language", context.getLanguage());
        }
        else if(System.getProperty("user.language") != null) {
            conn.addRequestProperty("Accept-Language", System.getProperty("user.language"));            
        }
        
        // Refer
        if(context.getReferer() != null) {
            conn.setRequestProperty("Referer", context.getReferer());
        }
        
        // read
        conn.setDoOutput(true);
        
        // post data
        if(post && postdata.length() > 0) {
            conn.setDoInput(true);
            OutputStream out = conn.getOutputStream();
            out.write(postdata.toString().getBytes("iso-8859-1"));
            out.close();
        }
        
        return conn;
    }

    /**
     * only escape char greater than 128
     */
    private String escape128(String url, String charset) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<url.length(); i++) {
            char ch = url.charAt(i);
            if(ch < 128) {
                sb.append(ch);
            }
            else {
                try {
                    sb.append(URLEncoder.encode(String.valueOf(ch), charset));
                }
                catch (UnsupportedEncodingException e) {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    private static Pattern regexContentType = Pattern.compile("charset=([^;\\s]+)", Pattern.CASE_INSENSITIVE);
    private static Pattern regexMeta        = Pattern.compile("<meta(?:\\s*(?:http-equiv\\s*=\\s*\"?content-type\"?|content\\s*=\\s*\"?.*?charset\\s*=\\s*([^\">\\s]+)\"?)){2,}.*?>", Pattern.CASE_INSENSITIVE);
    private static Pattern regexBase        = Pattern.compile("<base\\b(?:(?:href\\s*=\\s*(?:(\"|\')((?:(?!\\1).)*)\\1|((?![\"\'])(?:[^/>=\\s]|/(?!>))+(?=[\"\'>\\s=]|/>)))|(?:\"[^\"]*\"|\'[^\']*\'|(?![\"\'])(?:[^/>=\\s]|/(?!>))+(?=[\"\'>\\s=]|/>)|=\\s*(?!\\s)(?:(?=[\"\'])|(?![\"\'])(?:[^/>\\s]|/(?!>))*(?=[>\\s]|/>))|\\s+(?!\\s)))*)(?:/?>)", Pattern.CASE_INSENSITIVE);
}
