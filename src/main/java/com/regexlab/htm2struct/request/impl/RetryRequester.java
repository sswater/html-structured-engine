package com.regexlab.htm2struct.request.impl;

import java.io.IOException;

import com.regexlab.htm2struct.request.RequestContext;
import com.regexlab.htm2struct.request.RequestInterface;

public class RetryRequester implements RequestInterface {

	private RequestInterface requester;
	private int n = 1;

	public RetryRequester(RequestInterface requester) {
		this.requester = requester;
	}
	
	public RetryRequester(RequestInterface requester, int n) {
		this.requester = requester;
		this.n = n;
	}
	
	@Override
	public boolean request(RequestContext context) throws IOException {
		for(int i=0; i<n; i++) {
			try {
				if(requester.request(context))
					return true;
			}
			catch(IOException e) {
			}
		}
		return requester.request(context);
	}

}
