package com.regexlab.htm2struct.browse.impl;

import java.util.*;
import java.util.regex.Pattern;

import com.regexlab.htm2struct.browse.*;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Html2StructBrowser implements BrowseInterface {

    /** config */
    private BrowseConfig config = null;

    /** match regex */
    private Pattern regex = null;

    /** group number to field name */
    private Map<Integer, String> gfmap = new HashMap<Integer, String>();

    /** fields inherits from parent */
    private Set<String> inherits = new HashSet<String>();
    
    /** children browser for further browse */
    private List<Html2StructBrowser> children = new ArrayList<Html2StructBrowser>();

    /**
     * Construct a browser
     * @param config
     */
    public Html2StructBrowser(BrowseConfig config) {
        this.config = config;
        
        // build
        buildConfig();
        
        // construct children
        if(config.getChildren() != null) {
            for(BrowseConfig c : config.getChildren()) {
                children.add(new Html2StructBrowser(c));
            }
        }
    }

    private void buildConfig() {
        if(config.getPattern() != null) {
            regex = Pattern.compile(config.getPattern(), Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        }
        
        if(config.getGfmap() != null) {
            String [] maps = config.getGfmap().split("\\|");
            for(String m : maps) {
                int e = m.indexOf('=');
                if(e > 0) {
                    String k = m.substring(0, e);
                    if(regexNum.matcher(k).matches()) {
                        gfmap.put(Integer.parseInt(k), m.substring(e + 1));
                    }
                }
            }
        }
        
        if(config.getInherits() != null) {
            String [] ins = config.getInherits().split("\\|");
            for(String in : ins) {
                inherits.add(in);
            }
        }
    }
    
    static Pattern regexNum = Pattern.compile("\\d+");

    public void browse(BrowseContext request, BrowseListener listener) {
        
    }
    
}
