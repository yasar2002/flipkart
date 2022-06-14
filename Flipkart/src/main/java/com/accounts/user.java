package com.accounts;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class user{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
   
	public void editprofile() throws ServletException, IOException {
		Cookie ck[] = request.getCookies();
    	String uid = "";
    	
    	JSONArray notearr = new JSONArray();
    	JSONObject ob = new JSONObject();
		if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
		}
		
		
		try {
			db oDb = new db();
			
			String arr[] = oDb.userdetails(uid);
			ob.put("id", uid);
			ob.put("fname",arr[0]);	
			ob.put("lname",arr[1]);
			notearr.put(ob);
			response.getWriter().write(new JSONObject().put("user",notearr).toString());
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("sql exception in user class");
		} catch (JSONException e) {
			System.out.println("json exception in user class");
		}
		
	}

	
	
}
