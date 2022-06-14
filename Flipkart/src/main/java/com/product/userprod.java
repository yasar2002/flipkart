package com.product;

import java.io.IOException;
import java.io.PrintWriter;
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


public class userprod {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);

	public void getuserproducts() throws ServletException, IOException {
		System.out.println("entry in userproduct servlet");
		PrintWriter out = response.getWriter();
    	Cookie ck[] = request.getCookies();
    	String uid = "";
    	JSONObject ob = new JSONObject();
    	JSONArray notearr = new JSONArray();
    	
    	//System.out.println("length of cookies "+ck.length);
    	
		if(ck != null) {
	    	for(int i =0;i<ck.length;i++) {
	    	if(ck[i].getName().equals("uid") && uid.equals(""))
	    		uid=ck[i].getValue();    	
	    	System.out.println("the uid is"+uid);
	    		}
	    	
    	try {
    		System.out.println("uid in userprod"+uid);
    		db dataob = new db();
    		
    		for(pojo.Products products:dataob.getProduct(uid)){
                System.out.println(products.getName()+"==="+products.getType());
                JSONObject jo = new JSONObject();
            	jo.put("pname", products.getName());
    	        jo.put("pdesc", products.getDesc());
    	        jo.put("price",products.getPrice());
    	        jo.put("id",products.getPid());
    	        jo.put("pid",products.getPid());
    	        jo.put("uid", products.getUid());
    	        jo.put("pqua",products.getQuantity());
    	        jo.put("type",products.getType());
    	        if(dataob.getRevenue(uid, products.getPid().toString()) != null) {
    	        	for (pojo.Revenue revenue:dataob.getRevenue(uid, products.getPid().toString())) {
    	        		jo.put("revenue", revenue.getRevenue());
				}	
    	        }else {
    	        	jo.put("revenue", 0);	
				}
    	        System.out.println("uid in userprod servlet "+uid);
    	        notearr.put(jo);	
            }
//    		System.out.println(notearr.length());
    	     ob.put("productdetail", notearr);
    		 out.write(ob.toString());
    		 
    		}catch (JSONException e) {
    			//TODO:
    			   System.out.println("Problem in JSON");
    		} catch (ClassNotFoundException e) {
    			
    		   System.out.println("ClassNotFoundException");
    		} catch (SQLException e) {
    			
               System.out.println("sqlException " + e);
    		}catch (Exception e) {
    			org.json.simple.JSONObject jObject=new org.json.simple.JSONObject();
    			org.json.simple.JSONObject Ob=new org.json.simple.JSONObject();
    			jObject.put("id", "null");
    			jObject.put("status", "false");
    			Ob.put("productdetail", jObject);
    			out.write(Ob.toString());
    			System.out.println("exception " + e);
				// TODO: handle exception
    		}
    	

		}
		else {
			org.json.simple.JSONObject jObject=new org.json.simple.JSONObject();
			org.json.simple.JSONObject Ob=new org.json.simple.JSONObject();
			jObject.put("id", "null");
			jObject.put("status", "false");
			Ob.put("productdetail", jObject);
			out.write(Ob.toString());	
		}
	    out.flush();
	    out.close();		
	    }
	
}
