package com.product;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class buyprod{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);

	public void buyproduct() throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Cookie ck[] = request.getCookies();
    	String uid = "";
    	JSONObject jo = new JSONObject();
    	JSONObject object;
		if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
		}
		String line = null;
		StringBuffer str = new StringBuffer();
		BufferedReader reader =  request.getReader();

	    while((line = reader.readLine()) != null) {
	    	str.append(line);
				try {
					object = new JSONObject(str.toString());
					JSONObject getSth = object.getJSONObject("order");
			         String pid = getSth.getString("pid");
			         int price = Integer.parseInt(getSth.getString("price"));
			         db objectDb = new db();
						if(objectDb.orderProduct(uid,pid,price)) {
							objectDb.buyProduct(uid, pid);
							objectDb.updateProductQuantity(pid, 1);
							objectDb.updateRevenue(uid, pid, price, 1);
							object.getJSONObject("order").put("id", pid).put("uid", uid).put("price", price).put("pid", pid).put("pqua", 1).put("status", "success");
						}else {
							object.getJSONObject("order").put("id", "null").put("price", "null").put("pid", "null").put("pqua", "null").put("status", "fail");
						}
						out.write(object.toString());
			            out.flush();
			         
				} catch (JSONException e) {
					System.out.println("json exception in buyprod class");
				} catch (ClassNotFoundException e) {
					System.out.println("class not found exception in buyprod class");
				} catch (SQLException e) {
					System.out.println("SQL exception in order");
				}
	            
	 
	    }
	    out.close();
	}

}
