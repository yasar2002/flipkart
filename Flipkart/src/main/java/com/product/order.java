package com.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
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
import pojo.Products;


public class order{
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	
	public void getorderproducts() throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Cookie ck[] = request.getCookies();
    	String uid = "";
    	
    	JSONArray notearr = new JSONArray();
    	JSONObject ob = new JSONObject();
		if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	}
	    	System.out.println("the uid is"+uid);
		try {
			db DB = new db();
			int i=0;
			List<Products> order=DB.getOrderProducts(uid);//order details
			 for(pojo.order products: DB.getOrderedProductsId(uid)){//price,qunatity
			    JSONObject jo = new JSONObject();
			    jo.put("pname",order.get(i).getName());
			    jo.put("pdesc", order.get(i).getDesc());
			    jo.put("ordid", products.getOrderid());//order id
    	        jo.put("price",products.getPrice());
    	        jo.put("pqua",products.getPquantity());
    	        jo.put("id",products.getPid());
    	        jo.put("pid", products.getPid());
    	        jo.put("uid", products.getUid());
    	        notearr.put(jo);
			    i++;
			    }
			 ob.put("order", notearr);
			 out.write(ob.toString());
		}catch (JSONException | ClassNotFoundException | SQLException e) {
			System.out.println("exception in order class");
		}catch (Exception e) {
			org.json.simple.JSONObject jObject=new org.json.simple.JSONObject();
			org.json.simple.JSONObject Object=new org.json.simple.JSONObject();
			jObject.put("status", "empty");
			jObject.put("id", "****");
			Object.put("order", jObject);
			out.write(Object.toJSONString());	
		}
		
	}else { 
		try {
		JSONObject jo = new JSONObject();
			jo.put("Status", "fail");
			jo.put("id", "null");
			jo.put("uid", "null");
			ob.put("order",jo);	
			out.write(ob.toString());
		} catch (JSONException e) {
			System.out.println("json exception in order class");
		}
	}
		out.flush();
		out.close();
	}

	
	public void addcart() throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Cookie ck[] = request.getCookies();
    	String uid = "";
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
							object.getJSONObject("order").put("id", pid).put("uid", uid).put("price", price).put("pid", pid).put("pqua", 1).put("status", "success");
						}else {
							object.getJSONObject("order").put("id", "null").put("price", "null").put("pid", "null").put("pqua", "null").put("status", "fail");
						}
						out.write(object.toString());
			            out.flush();
			         
				} catch (JSONException e) {
					System.out.println("json exception in order class");
				} catch (ClassNotFoundException e) {
					System.out.println("class not found exception in order class");
				} catch (SQLException e) {
					System.out.println("SQL exception in order");
				}
	            
	 
	    }
	    out.close();
	}

}



