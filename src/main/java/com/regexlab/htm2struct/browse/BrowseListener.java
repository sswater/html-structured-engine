package com.regexlab.htm2struct.browse;

/**
 * This class is a callback interface when browsing 
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public interface BrowseListener {

    /**
     * event before a new request
     * @param url request URL
     * @return return false to cancel
     */
    boolean beforeOpenURL(String url);

    /**
     * event when a result has finished 
     * @param context browse result
     */
    void save(BrowseContext context);
    
}
