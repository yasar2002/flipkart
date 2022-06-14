package com.product;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class singlebuy {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);

	public void buyprod() throws ServletException, IOException {
	
		String urlString = request.getRequestURL()+request.getRequestURI()+"?"+request.getQueryString();
        String pid = null,status = null;
        int ordid;
        float price;
        int quantity;
        URL url = new URL(urlString);
        Cookie ck[] = request.getCookies();
    	String uid = "";
    	
    	if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
    	}
        
        try {
			url=decoded_url(url);
			System.out.println("the url is "+url);
            	if (url.getQuery().split("&")[0].contains("id") && url.getQuery().split("&")[1].contains("ordid")) {
    	            pid = url.getQuery().split("&")[0].split("=")[1];
    	            ordid = Integer.parseInt(url.getQuery().split("&")[1].split("=")[1]);
    	            price = Float.parseFloat(url.getQuery().split("&")[2].split("=")[1]);
    	            quantity = Integer.parseInt(url.getQuery().split("&")[3].split("=")[1]);
    	            db  objectDb = new db();
    	            objectDb.buyoneProduct(uid, pid,ordid);
					objectDb.updateProductQuantity(pid, quantity);
					objectDb.updateRevenue(uid, pid, price, quantity);  
                }
		} catch (MalformedURLException | UnsupportedEncodingException | URISyntaxException e) {
			System.out.println("exception in singlebuy class");
		} catch (ClassNotFoundException e) {
			System.out.println("class not found exception in single buy class");
		} catch (SQLException e) {
			System.out.println("sql exception in single buy class");
		}
        
	}
	
	public static URL decoded_url(URL url) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        String decoded_url=URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8.name());
        return new URL(decoded_url).toURI().toURL();
    }


}
