package com.regexlab.htm2struct.browse;

import java.io.IOException;

/**
 * This class is the abstract interface of browser impl, called by frame
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public interface BrowseInterface {

    /** begin to browse */
    void browse(BrowseContext context, BrowseListener listener) throws IOException;
    
}
