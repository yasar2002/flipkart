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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class Product{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	
	public void getProducts() throws ServletException, IOException, NullPointerException {
		PrintWriter out = response.getWriter();
    	Cookie ck[] = request.getCookies();
    	String uid = "";
 
    	if(ck != null) {
    	for(int i =0;i<ck.length;i++) {
    	if(ck[i].getName().equals("uid") && uid.equals(""))
    		uid=ck[i].getValue();    	
    		}
    	}
    	
    	JSONObject ob = new JSONObject();
    	JSONArray notearr = new JSONArray();
    	
    	try {
     		if(uid.equals("")) {
    			//sample
    			db dataob = new db();
        		for(pojo.Products products:dataob.getProduct("sample")){
                    JSONObject jo = new JSONObject();
                	jo.put("pname", products.getName());
        	        jo.put("pdesc", products.getDesc());
        	        jo.put("price",products.getPrice());
        	        jo.put("id",products.getPid());
        	        jo.put("pid",products.getPid());
        	        jo.put("uid", "");
        	        jo.put("pqua",products.getQuantity());
        	        jo.put("type",products.getType());
        	        notearr.put(jo);	
            }
	        }
    		else {
    			// all products
    			db dataob = new db();
    			String status = dataob.user(uid);
    			
        		for(pojo.Products products:dataob.getProduct(null)){
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
        	        jo.put("status",status);
        	        notearr.put(jo);	
                }
    		}
	        ob.put("productdetail", notearr);
	        
		} catch (JSONException e) {
		   System.out.println("Problem in JSON");
		} catch (ClassNotFoundException e) {
		   System.out.println("ClassNotFoundException");
		} catch (SQLException e) {
           System.out.println("sqlException " + e);
		}
    	out.write(ob.toString());
		out.flush();
		out.close();
    
	}
	


	public void doPost() throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
		String line = null;
		StringBuffer str = new StringBuffer();
		BufferedReader reader =  request.getReader();

	    while((line = reader.readLine()) != null) {
	    	str.append(line);
	    	try {
	    		JSONObject object = new JSONObject(str.toString());
	            JSONObject getSth = object.getJSONObject("productdetail");
	            System.out.println("line 51 "+object);
	            Cookie ck[] = request.getCookies();
	        	String uid = "";
	            if(ck != null) {
	            	for(int i =0;i<ck.length;i++) {
	            	if(ck[i].getName().equals("uid") && uid.equals(""))
	            		uid=ck[i].getValue();    	
	            		}
	            	}
	            String pType=null, pImage = null, pName = null, pDesc = null; 
	            int pQuantity=0,price=0;
	            
	            pType = getSth.getString("type");
	            pName = getSth.getString("pname");
	            pQuantity = Integer.parseInt(getSth.getString("pqua"));
	            price = Integer.parseInt(getSth.getString("price"));
	            pDesc = getSth.getString("pdesc");
	            db obDb = new db();
	            if(obDb.addProduct(uid, pType, pImage, pName, pDesc, price, pQuantity)) {
	            	// fetch all about the product using uis
	            	System.out.println("success");
	            }
	            else {
					System.out.println("failure");
				}
	            object.getJSONObject("productdetail").put("id", uid).put("type",pType).put("pname", pName).put("pqua",pQuantity).put("price", price);
	            out.write(object.toString());
	            out.flush();
			} catch (JSONException e) {
				System.out.println("Problem in JSON");
			} catch (ClassNotFoundException e) {
				System.out.println("class not found exception in product class");
			} catch (SQLException e) {
			   System.out.println("sqlexception in product class");
			}
	    	
	     }
	    out.close();
	}

}
