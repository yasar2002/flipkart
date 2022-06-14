package com.accounts;

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
import org.json.JSONException;
import org.json.JSONObject;
import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class user2 {
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
    
	public void updateprofile() throws ServletException, IOException {
	 
		String urlString = request.getRequestURL()+request.getRequestURI()+"?"+request.getQueryString();
        String fname = null,lname = null;
        URL url = new URL(urlString);
        Cookie ck[] = request.getCookies();
    	String uid = "";
    	
    	if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
    	}
    	JSONObject ob = new JSONObject();
    	try {
			url=decoded_url(url);
			System.out.println("The current url is "+url);
			if (url.getQuery().split("&")[0].contains("fname") && url.getQuery().split("&")[1].contains("lname")) {
	            fname = url.getQuery().split("&")[0].split("=")[1];
	            lname = url.getQuery().split("&")[1].split("=")[1];
	            db oDb = new db();
	            if(oDb.upduserdetails(uid, fname, lname)) {
	            	ob.put("id", uid);
	            	ob.put("status","success");
	    			response.getWriter().write(new JSONObject().put("user",ob).toString());
	    			
	            }
	            else {
	            	ob.put("id", uid);
	            	ob.put("status","fail");
	    			response.getWriter().write(new JSONObject().put("user",ob).toString());
				}
	            
			}
			else {
		            lname = url.getQuery().split("&")[0].split("=")[1];
		            fname = url.getQuery().split("&")[1].split("=")[1];
			}
			
		} catch (MalformedURLException | UnsupportedEncodingException | URISyntaxException e) {
			System.out.println("problem in user2 class "+e);
		} catch (ClassNotFoundException e) {
			System.out.println("class not found exception in user2 class");
		} catch (SQLException e) {
			System.out.println("sql exception in user2 class");
		} catch (JSONException e) {
			System.out.println("Json exception in user2 class");
		}	
    	
    	
        
	}
	
	public static URL decoded_url(URL url) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        String decoded_url=URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8.name());
        return new URL(decoded_url).toURI().toURL();
    }
	

}
