package com.regexlab.htm2struct.browse;

import java.util.*;

import com.regexlab.htm2struct.request.*;

/**
 * This class stores browse result: structured data
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class BrowseContext {

    /** impl to do request */
    private RequestInterface requester = null;
    
    /** request context */
    private RequestContext request = null;
    
    /** parent result which this is upon */
    private BrowseContext parent = null;
    
    /** context to save */
    private BrowseContext save = null;
    
    /** structured browse data */
    private Map<String, Object> fields = new HashMap<String, Object>();
    
    /** special attribute not from remote server, for internal use */
    private Map<String, String> attribute = new HashMap<String, String>();
    
    /**
     * Construct a startup browse context
     * @param requester
     */
    public BrowseContext(RequestInterface requester) {
        this.requester = requester;
    }

    /**
     * Construct a sub context
     * @param parent
     */
    public BrowseContext(BrowseContext parent) {
        this.parent = parent;
        this.requester = parent.requester;
        this.request = parent.request;
        this.save = parent.save;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }

    public BrowseContext getParent() {
        return parent;
    }

    public RequestInterface getRequester() {
        return requester;
    }

    public void setRequester(RequestInterface requester) {
        this.requester = requester;
    }

    public RequestContext getRequest() {
        return request;
    }

    public void setRequest(RequestContext request) {
        this.request = request;
    }

    public BrowseContext getSave() {
        return save;
    }

    public void setSave(BrowseContext save) {
        this.save = save;
    }
    
}
