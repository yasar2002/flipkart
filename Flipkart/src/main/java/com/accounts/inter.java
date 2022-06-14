package com.accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.database.db;
import com.opensymphony.xwork2.ActionContext;


public class inter {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse  response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	  
	public void login() throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
        String urlString = request.getRequestURL()+request.getRequestURI()+"?"+request.getQueryString();
        String mail = null,pass = null,status;
        
	    JSONObject object = new JSONObject();
	    JSONObject mod = new JSONObject();
	    try{
            URL url = new URL(urlString);
            url=decoded_url(url);
            if(url.getQuery().contains("mail") && url.getQuery().contains("pass")) {
            	if (url.getQuery().split("&")[0].contains("pass") && url.getQuery().split("&")[1].contains("mail")) {
    	            pass = url.getQuery().split("&")[0].split("=")[1];
    	            mail = url.getQuery().split("&")[1].split("=")[1];
    	             
    	            db dbobject = new db();
    	            status =dbobject.login(mail, pass,response);
    	            System.out.println("------------------->"+response.getHeaderNames());
    	            mod.put("user",object.put("status",status));
                    mod.put("user",object.put("id",mail));
                }
                else {
                    mail = url.getQuery().split("&")[0].split("=")[1];
                    pass =  url.getQuery().split("&")[1].split("=")[1];
                    
                    db login = new db();
                    status = login.login(mail, pass,response);
                    System.out.println("------------------->"+response.getHeaderNames());
                    	mod.put("user",object.put("status", status));
                        mod.put("user",object.put("id",mail));
    			}
            }
            else if(url.getQuery().contains("mail")) {
            	mail = url.getQuery().split("&")[0].split("=")[1];
            	System.out.println("mail is "+mail);
            	db dbobject = new db();
            	if(dbobject.check(mail)) {
            		 mod.put("user",object.put("status", "Success"));
                     mod.put("user",object.put("id",mail));
            	}
            	else {
            		mod.put("user",object.put("status","Failure"));
                	mod.put("user",object.put("id", "null"));
				}
            }
            
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException");
        } catch (URISyntaxException e) {
            throw new RuntimeException("URISyntaxException");
        } catch (JSONException e) {
        	System.out.println("Json exception in inter class");
		} catch (ClassNotFoundException e) {
			System.out.println("class not found exception in inter class");
		} catch (SQLException e) {
			System.out.println("sql exception in inter class");
		}
	    out.write(mod.toString());
	    out.flush();
	    out.close();


		}

        
	public static URL decoded_url(URL url) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        String decoded_url=URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8.name());
        return new URL(decoded_url).toURI().toURL();
    }

    

	public void signup() throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		System.out.println("Entering to userdetails Servlet");    
		String line = null;
		StringBuffer str = new StringBuffer();
		BufferedReader reader =  request.getReader();
	    String email,password,cnfpassword,firstName,lastName,state;
        
	    while((line = reader.readLine()) != null) {
	    	str.append(line);
	    	try {
	    		JSONObject object = new JSONObject(str.toString());
	            JSONObject getSth = object.getJSONObject("user");
	            System.out.println("line 51 "+object);
	            email = getSth.getString("mail");
	            password = getSth.getString("pass");
	            cnfpassword = getSth.getString("cnfpass");
	            boolean c1 = Pattern.matches("[a-zA-Z0-9]+[@][a-z]+[.][a-z]{2,3}", email);
	            boolean c2 = Pattern.matches("[a-zA-z0-9!@#$%_*]{8,}", password);
	            boolean c3 = (password.equals(cnfpassword));
	             System.out.println(c1+" "+c2+" "+c3);
	            if(c1  && c2 && c3) {
	            	firstName = getSth.getString("fname");
		            System.out.println(firstName);
		             lastName= getSth.getString("lname");
		            System.out.println(lastName);
		            state = getSth.getString("status");
	            
		            int role = 2;
		            if(state.equals("Seller"))
		              role = 1;
		            
		            
		            System.out.println("Role "+role);
	    		    System.out.println("Valid Input");
	    		    db obDb = new db();
	    		    String uid = obDb.signup(firstName, lastName, email, role, cnfpassword,response);
	    		    object.getJSONObject("user").put("id",uid).put("pass","*****").put("cnfpass","*****").put("status",role);
	                    System.out.println(object.getJSONObject("user"));
	        			out.write(object.toString()); 
	        		    out.flush();	
	            }else {  
	         	 System.out.println("Invalid input");
	         	object.getJSONObject("user").put("status","Invalid");
    			out.write(object.toString()); 
	            }
			} catch (JSONException e) {
				System.out.println("Problem in JSON");
			} catch (ClassNotFoundException e) {
				System.out.println("class not found exception in inter class");
			} catch (SQLException e) {
				System.out.println("sql exception in inter class");
			}
			
	}
	    out.close();
	}
   			
}
