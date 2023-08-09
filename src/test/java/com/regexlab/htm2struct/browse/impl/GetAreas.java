package com.regexlab.htm2struct.browse.impl;

import java.util.Arrays;

import com.regexlab.htm2struct.browse.BrowseConfig;
import com.regexlab.htm2struct.browse.BrowseContext;
import com.regexlab.htm2struct.browse.BrowseListener;
import com.regexlab.htm2struct.request.impl.SimpleRequester;

public class GetAreas {

	public static void main(String[] args) {
		
		// 实现一个回调类
		BrowseListener listener = new BrowseListener() {
		    public void save(BrowseContext context) {
		        System.out.println(context.getFields());
		    }
		    
		    public boolean beforeOpenURL(String url) {
		        System.out.println("即将打开页面：" + url);
		        return true;
		    }
		};
		
		// 省
		BrowseConfig config = new BrowseConfig();
		config.setUrl("http://www.stats.gov.cn/sj/tjbz/tjyqhdmhcxhfdm/2022/index.html");
		config.setPattern("<td><a\\s+href\\=\"((?:(?!\">)(?:.|\\n))*)\">((?:(?!<br\\s+/></a></td>)(?:.|\\n))*)<br\\s+/></a></td>");
		config.setGfmap("1=link|2=prov");
		 
		// 构造一个 Browser 类并调用 browse 方法
		new Html2StructBrowser(config).browse(
		        new BrowseContext(new SimpleRequester()), listener);

		// 市
		BrowseConfig config2 = new BrowseConfig();
		config2.setInterest("link");
		config2.setAslink(true);
		config2.setPattern("<tr\\s+class\\=\"citytr\"><td><a\\s+href\\=\"((?:(?!\">)(?:.|\\n))*)\">((?:(?!</a></td><td><a\\s+href\\=\")(?:.|\\n))*)</a></td><td><a\\s+href\\=\"(?:(?!\">)(?:.|\\n))*\">((?:(?!</a></td></tr>)(?:.|\\n))*)</a></td></tr>");
		config2.setGfmap("1=link|2=code|3=city");
		
		config.setChildren(Arrays.asList(config2));
		
		// 构造一个 Browser 类并调用 browse 方法
		new Html2StructBrowser(config).browse(
		new BrowseContext(new SimpleRequester()), listener);
		
		// 县
		BrowseConfig config3 = new BrowseConfig();
		config3.setInterest("link");
		config3.setAslink(true);
		config3.setPattern("<tr\\s+class\\=\"countytr\"><td><a\\s+href\\=\"(?:(?!\">)(?:.|\\n))*\">((?:(?!</a></td><td><a\\s+href\\=\")(?:.|\\n))*)</a></td><td><a\\s+href\\=\"(?:(?!\">)(?:.|\\n))*\">((?:(?!</a></td></tr>)(?:.|\\n))*)</a></td></tr>");
		config3.setGfmap("1=code|2=county");
		
		config2.setChildren(Arrays.asList(config3));
		
		// 构造一个 Browser 类并调用 browse 方法
		new Html2StructBrowser(config).browse(
		new BrowseContext(new SimpleRequester()), listener);
		
	}

}
