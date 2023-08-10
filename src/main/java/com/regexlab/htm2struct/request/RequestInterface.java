package com.regexlab.htm2struct.request;

import java.io.IOException;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public interface RequestInterface {

    /**
     * do a request 
     * @param context request context for input and output
     */
    boolean request(RequestContext context) throws IOException;
    
}
