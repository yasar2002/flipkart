package com.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import com.database.db;
import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;
import com.opensymphony.xwork2.ActionContext;


public class cart {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	
	public void cartprods() throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String uid = "";
		Cookie ck[] = request.getCookies();		
		if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
		}
		
		String line = null;
		StringBuffer str = new StringBuffer();
		BufferedReader reader =  request.getReader();
		JSONObject object;
		JSONObject object2 = new JSONObject();
	    while((line = reader.readLine()) != null) {
	    	str.append(line);
	    	try {
				object = new JSONObject(str.toString());
				String pid = object.getString("pid");
				int quantity = object.getInt("quantity");
				int price = object.getInt("price");
				db ob = new db();
				if(price == 0 && quantity== 0) {
					if(ob.deleteprod(uid, pid))
						out.write(object.put("order",(object2.put("id", "****").put("status", "empty"))).toString());
				}
				else if(ob.updcart(uid, pid, quantity, price)) {
                 object2.put("order",object.put("id", pid).put("uid", uid).put("price", price).put("pid", pid).put("pqua", quantity).put("status", "success"));
                 out.write(object2.toString());
		         out.flush();
                }
				
			} catch (JSONException e) {
				System.out.println("json exception in cart class "+e);
			} catch (ClassNotFoundException e) {
				System.out.println("class notfound exception in cart class");
			} catch (SQLException e) {
				System.out.println("sql exception in cart class");
			}
	    }
	    out.close();
	}

}
