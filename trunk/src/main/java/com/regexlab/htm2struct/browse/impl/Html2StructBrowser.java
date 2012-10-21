package com.regexlab.htm2struct.browse.impl;

import java.util.*;
import java.util.regex.*;

import org.apache.commons.logging.*;

import com.regexlab.htm2struct.browse.*;
import com.regexlab.htm2struct.request.RequestContext;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Html2StructBrowser implements BrowseInterface {
    private static Log log = LogFactory.getLog(Html2StructBrowser.class);

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

    public void browse(BrowseContext brs_context, BrowseListener listener) {
        
        boolean islink = config.isAslink();
        String source = null;
        
        // check url explicitly
        if(config.getUrl() != null) {
            source = config.getUrl();
            islink = true;
        }
        else {
            String interest = config.getInterest();
            
            // null for first field
            if(interest == null) {
                source = String.valueOf(brs_context.getFields().values().iterator().next());
            }
            
            // placeholder
            else if(interest.indexOf("$(") > 0) {
                source = placeholder(interest, brs_context.getFields(), "$(", ")");
            }
            
            // interest
            else {
                Object v = brs_context.getFields().get(interest);
                if(v != null) {
                    source = String.valueOf(v);
                }
                else {
                    source = "";
                    if(log.isWarnEnabled()) log.warn("The interested field is null!");
                }
            }
        }
        
        RequestContext req_context = brs_context.getRequest();
        
        // do link
        if(islink) {
            // to absolute
            if( ! startsWithHttp.matcher(source).find() ) {
                source = makeAbsoluteUrl(req_context, source);
            }
            
            // whether skip
            if(config.isChecklink() && ! listener.beforeOpenURL(source)) {
                if(log.isInfoEnabled()) log.info(source + " skipped.");
                return;
            }
            
            // open url
            RequestContext req_context_2 = new RequestContext(source, req_context);
            if( ! brs_context.getRequester().request(req_context_2) ) {
                if(log.isWarnEnabled()) log.warn(source + " request failed.");
                return;
            }
            
            // replace context
            req_context = req_context_2;
            brs_context = new BrowseContext(brs_context);
            source = req_context.getStringBody();
            brs_context.getFields().put("", source);
        }
        
        // parse fields
        Matcher m = regex.matcher(source);
        
        
        
    }

    
    /**
     * make an absolute path
     */
    private String makeAbsoluteUrl(RequestContext req_context, String rel) {
        // check valid
        if(req_context == null || req_context.getBase() == null)
            return rel;

        // get base
        String base = req_context.getBase();
        if(!base.endsWith("/")) base += "/";
        
        Matcher matcher = regexHttp.matcher(base);
        
        if(matcher.find()) {
            if(rel.startsWith("/") || rel.startsWith("\\")) {
                // path from root
                return matcher.group(1) + rel;
            }
            else {
                // path relative
                return matcher.group(1) + regexUpDir.matcher(matcher.group(2) + rel).replaceAll("");
            }
        }
        return rel;
    }
    
    private String placeholder(String pattern, Map<String, Object> env, String prefix, String suffix) {
        
        StringBuffer sb = new StringBuffer();
        int lastpos = 0, pos1, pos2 = 0;
        
        while((pos1 = pattern.indexOf(prefix, pos2)) >= 0) {
            if(pos1 > lastpos) {
                sb.append(pattern.substring(lastpos, pos1));
                lastpos = pos1;
            }
            pos2 = pattern.indexOf(suffix, pos1 + prefix.length());
            
            // empty such as $()
            if(pos2 == pos1 + prefix.length()) {
                pos2 += suffix.length();
                continue;
            }
            
            // key
            String key = pattern.substring(pos1 + prefix.length(), pos2 > 0 ? pos2 : pattern.length());
            
            if(pos2 < 0)
                pos2 = pattern.length();
            else
                pos2 += suffix.length();
            
            // get the value
            Object value = env.get(key);
            if(value != null) {
                sb.append(value);
                lastpos = pos2;
            }
        }
        
        return sb.toString();
    }
    
    protected static Pattern regexHttp      = Pattern.compile("(http://[^/\\\\?&#\\s]+)(/?(?:[^/\\\\?&#\\s]*/)*)[^/\\\\?&#\\s]*");
    protected static Pattern regexUpDir     = Pattern.compile("(?<=[/\\\\])(?!\\.\\.[/\\\\])[^/\\\\?&#\\s]*[/\\\\]((?!\\.\\.[/\\\\])[^/\\\\?&#\\s]*[/\\\\]((?!\\.\\.[/\\\\])[^/\\\\?&#\\s]*[/\\\\]((?!\\.\\.[/\\\\])[^/\\\\?&#\\s]*[/\\\\]((?!\\.\\.[/\\\\])[^/\\\\?&#\\s]*[/\\\\]\\.\\.[/\\\\])*\\.\\.[/\\\\])*\\.\\.[/\\\\])*\\.\\.[/\\\\])*\\.\\.[/\\\\]|[/\\\\]\\.(?=[/\\\\])");
    protected static Pattern startsWithHttp = Pattern.compile("^https?://", Pattern.CASE_INSENSITIVE);
    protected static Pattern regexNum       = Pattern.compile("\\d+");
}
