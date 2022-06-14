package com.database;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import pojo.Products;
import pojo.Revenue;
import pojo.order;

public class db {
	 public db() throws ClassNotFoundException, SQLException {
	        if(connection==null) {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            this.connection=DriverManager.getConnection(url,user,"");
	        }

	    }
	    private PreparedStatement ps=null;
	    private ResultSet rs=null;
	    private Connection connection=null;
	    private String user = "root";
	    private String url= "jdbc:mysql://localhost:3306/flipkart";
	    public void closeConnection(Connection connection) {
	        try {
	            connection.close();
	        } catch (SQLException e) {
	            System.out.println(e);

	        }
	    }
	    public boolean check(String mail) throws SQLException{
			System.out.println("Entered to check mail");
	        
	        try {
				ps = connection.prepareStatement("Select * from user where email = ?");
				ps.setString(1,mail);
				  rs = ps.executeQuery();
				  boolean result = rs.next();
				  closeConnection(connection);
				  return result;				  
				  			  
			} catch (SQLException e) {
	            System.out.println("SQLException "+e);
	            return false;
	        } 
			}
	    public String login(String email,String password,HttpServletResponse response){
	    	String uid="",status="";
	    	try{
	            ps  = connection.prepareStatement("select uid,status from user where email=? and password=?");
	            ps.setString(1,email);
	            ps.setString(2,encryptPassword(password));
	            rs = ps.executeQuery();
	            System.out.println(email+" pass "+password);
	            System.out.println(ps);
	            while(rs.next()){
	                uid=rs.getString(1);
	                status=rs.getString(2);
	                System.out.println("status is "+status);
	                response.addCookie(new Cookie("name","yasar"));
	            }
	            if(!uid.equals("")&&!status.equals("")) {
	            	addSession(uid,"1",response);
	            }
	        } catch (SQLException e) {
	            System.out.println("SQLException "+e);
	        }
//	    	System.out.println("status is "+ status);
	    	closeConnection(connection);
	        return status;
	    }
	    public String signup(String firstName, String lastName, String email, int role, String password,HttpServletResponse response){
	        String uid="";
	    	try{
	        	System.out.println("user role is"+role);
	        	uid = uidGenerator(30);
	            ps = connection.prepareStatement("insert into user values(?,?,?,?,?,?)");
	            ps.setString(1,uid);
	            ps.setString(2,firstName);
	            ps.setString(3,lastName);
	            ps.setString(4,email);
	            ps.setInt(5,role);
	            ps.setString(6,encryptPassword(password));
	            System.out.println("query update"+ps);
	            ps.executeUpdate();
	            addSession(uid,"1",response);
	            
	        }
	        catch (SQLException se){
	            System.out.println("exception in data"+se);
	        }
	    	return uid;
	    }
	    protected  String uidGenerator(int length) {
	        String root = "abcdefghijklmnopqrstuvwxyz1234567890";
	        String str = "";
	        for (int i = 0; i < length; i++) {
	            int randomNum = (int) (Math.random() * 36);

	            str += root.charAt(randomNum);
	        }
	        return str;
	    }
	    private String encryptPassword(String password) {
	    	System.out.println("encrypt password is "+password);
	        String generatedPassword = null;
	        password+="sfghjkl;l456789ytrcg njhytrdxv b09yuiDY^%$DCVJN";//salting
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(new StringBuilder(password).reverse().toString().getBytes());
	            byte[] bytes = md.digest();
	            StringBuilder sb = new StringBuilder();
	            for (int i = 0; i < bytes.length; i++) {
	                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	            }
	            generatedPassword = sb.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return generatedPassword;
	    }
	    public boolean addSession(String uid,String status,HttpServletResponse response){
	    	//return uid and token
	    	String token = encryptPassword(uidGenerator(30));
	        try{
	            ps=connection.prepareStatement("insert into session(uid,token,expiry,status)values(?,?,?,?)");
	            ps.setString(1,uid);
	            ps.setString(2,token);
	            ps.setString(3,String.valueOf(new Timestamp(System.currentTimeMillis()+300000000)));//for three days
	            ps.setString(4,status.equals("1")?"1":"0");
	            setCookies("uid",uid,response);
	            setCookies("token",token,response);
	            return ps.executeUpdate()==1?true:false;
	        }catch (SQLException e) {
	            System.out.println("There was an error in sql");
	            //TODO:logger to register the exception
	        }
	        catch (Exception e){
	            System.out.println("Some other Exception cased");
	            //TODO:logger to register the exception
	        }
	        return false;
	    }
	    public boolean validate(String uid,String token){
	        try{
	            ps=connection.prepareStatement("select expiry from session where uid=? and token=?");
	            ps.setString(1,uid);
	            ps.setString(2,token);
	            rs=ps.executeQuery();
	            String expiry="";
	            while (rs.next()){
	                expiry=rs.getString(1);
	            }
	            if(!expiry.equals("")){
	                if(dateValidate(expiry)==1)return true;
	                return false;
	            }
	        }catch (SQLException e){
	            System.out.println("Sql exception");
	            //TODO:logger to register the exception
	        } catch (ParseException e) {
	            System.out.println("Parse exception");
	            //TODO:logger to register the exception
	        }
	        catch (Exception e){
	            System.out.println("some other exception "+e);
	            //TODO:logger to register the exception
	        }
	        return false;
	    }
	    public int dateValidate(String time) throws ParseException {
	        LocalDate date = LocalDate.now();
	        return  new SimpleDateFormat("yyyy-MM-dd").parse(time).compareTo(new SimpleDateFormat("yyyy-MM-dd").parse(date.toString()));
	    }
	    public boolean orderProduct(String uid,String pid,float price){
	    	int quantity=1;
	        try{
	            ps = connection.prepareStatement("insert into cart(uid,pid,pquantity,price,ord_date,status) values(?,?,?,?,?,0)");
	            ps.setString(1,uid);
	            ps.setString(2,pid);
	            ps.setInt(3,quantity);
	            ps.setFloat(4,price);
	            ps.setString(5,String.valueOf(new Timestamp(System.currentTimeMillis())));
	            
	            return ps.executeUpdate()==1?true:false;
	        }
	        catch (SQLException se){
	            System.out.println("Sql exception may be unknown user try to buy the product"+se);
	            return false;
	            //TODO:logger to register the exception
	        }catch (Exception e){
	            System.out.println("some other exception");
	            //TODO:logger to register the exception
	        }
	        
	        return false;
	    }
	    public boolean updateProductQuantity(String pid,int quantity){
	        try {
	            Products product=getProductDetailsById(pid);
	            if(product.getQuantity()-1>quantity) {
	                ps = connection.prepareStatement("update product_details set pquantity=? where pid=?");
	                ps.setInt(1, product.getQuantity() - quantity);
	                ps.setString(2, pid);
	                return ps.executeUpdate()==1?true:false;
	            }else return false;
	        } catch (SQLException e) {
	            System.out.println("Sql exception");
	            //TODO:logger
	        }
	        catch (Exception e){
	            System.out.println("some other exception");
	            //TODO:logger
	        }
	        return false;
	    }

	    public List<order> getOrderedProductsId(String uid){
	        //It doesn't closes the connection
	        List<order>list=new ArrayList<>();
	        try{
	            ps  = connection.prepareStatement("select * from cart where uid=? and status=0 order by sno desc");
	            ps.setString(1,uid);
	            rs = ps.executeQuery();
	            while(rs.next()){
	                list.add(new order(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getFloat(5),rs.getString(6)));
	            }
	           return list.size()>0?list:null;
	        } catch (SQLException e) {
	            System.out.println("Sql exception "+e);
	           //TODO:
	        }
	        catch (Exception e){
	            System.out.println("Some other exception");
	            //TODO:
	        }	
	        return null;
	    }
	    public List<Products> getOrderProducts(String uid) throws SQLException {
	        List<order>ids=getOrderedProductsId(uid);
	        List<Products>products=new ArrayList<>();
	        for (order o:ids){
	            products.add(getProductDetailsById(o.getPid()));
	        }
	        return products.size()>0?products:null;
	    }
	    public Products getProductDetailsById(String pid){
	        //It doesn't closes the connection
	        Products products=null;
	        try{
	            ps  = connection.prepareStatement("select * from product_details where pid=?");
	            ps.setString(1,pid);
	            rs = ps.executeQuery();
	            while(rs.next()){
	                products=new Products(  rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getFloat(7),rs.getInt(8));
	            }
	            return !products.getName().equals("")?products:null;
	        } catch (SQLException e) {
	            System.out.println("sql exception "+e);
	        }catch (Exception e){
	            System.out.println("some other exception");
	        }	
	        return null;
	    }
	    public boolean addProduct(String uid,String pType, String pImage, String pName, String pDesc,int price, int pQuantity){
	        try{
	            ps = connection.prepareStatement("insert into product_details values(?,?,?,?,?,?,?,?,?)");
	            ps.setString(1,uidGenerator(32));
	            ps.setString(2,uid);
	            ps.setString(3,pType);
	            ps.setString(4,pImage);
	            ps.setString(5,pName);
	            ps.setString(6,pDesc);
	            ps.setFloat(7,price);
	            ps.setInt(8,pQuantity);
	            LocalDate date = LocalDate.now();
	            ps.setDate(9, Date.valueOf(date));
	            int  result = ps.executeUpdate();	
	            return result==1?true:false;
	            
	        }
	        catch (SQLException se){
	        	closeConnection(connection);	
	            return false;
	            //logger
	        }
	    }

	    public List<Products> filterProductsByType(String type){
	        try {
	            List<Products> productsList = new ArrayList<>();
	            ps = connection.prepareStatement("select * from product_details where ptype=?");
	            ps.setString(1,type);
	            rs = ps.executeQuery();
	            while (rs.next()){
	                productsList.add(new Products(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getFloat(7),rs.getInt(8)));
	            }
	            return productsList;
	        }
	        catch (SQLException se){
	            //logger
	            return null;
	        }
	        finally {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                throw new RuntimeException(e);
	            }
	        }

	    }
	       	
	    public void setCookies(String name,String value,HttpServletResponse response) {
    		Cookie cookie=new Cookie(name, value);
    		cookie.setPath("/");
    		response.addCookie(cookie);
    		
    	}
	    
	    public List<Products> getProduct(String uid){
	    	//null=all
	    	//uid=particular
	    	System.out.println("uid in db "+uid);
	        List<pojo.Products> products=new ArrayList<>();
	        try{
	            if(uid!=null) {
	            	ps  = connection.prepareStatement("select * from product_details where uid=?");
	                ps.setString(1,uid);
	            }
	            else{
	            	ps  = connection.prepareStatement("select * from product_details");
	            }
	            rs = ps.executeQuery();
	            while(rs.next()){
	                products.add(new pojo.Products(  rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getFloat(7),rs.getInt(8)));
	            }
	            return products.size()>0?products:null;
	        } catch (SQLException e) {
	            System.out.println("sql exception "+e);
	            //TODO:
	        }catch (Exception e){
	            System.out.println("some other exception");
	            //TODO:
	        }	
	        return null;
	    }
	    public String user(String uid) {
	    	String status = "";
	    	try {
				ps  = connection.prepareStatement("select status from user where uid=?");
				ps.setString(1,uid);
	            rs = ps.executeQuery();
				while (rs.next()){
	                status=rs.getString(1);
	            }
	    	} catch (SQLException e) {
				System.out.println("sql exception");
			}	
           return status;
           
	    }
	    public boolean updcart(String uid,String pid,int qua,int price){
	    	try {
				ps = connection.prepareStatement("update cart set pquantity=? ,price=? where uid=? and pid=?");
				ps.setInt(1, qua);
				ps.setFloat(2, price);
				ps.setString(3,uid);
                ps.setString(4,pid);
                return ps.executeUpdate() > 0 ? true : false;
			} catch (SQLException e) {
				 System.out.println("Sql exception may be unknown user try to add a product in cart");
                 return false;
			}
	    	catch (Exception e) {
                System.out.println("some other exception ");
            }
	    	return false;
	    }
	    
	    public List<Revenue> getRevenue(String uid,String pid){
	    	System.out.println("the uid is "+uid);
	    	System.out.println("the pid is "+pid);
	        try {
	            List<Revenue>revenue=new ArrayList<>();
	            ps  = connection.prepareStatement("select * from revenue_details where uid=? and pid=?");
	            ps.setString(1,uid);
	            ps.setString(2,pid);
	            rs=ps.executeQuery();
	            while(rs.next()){
	                revenue.add(new Revenue(rs.getString(1),rs.getString(2),rs.getFloat(3),rs.getInt(4)));
	            }
	            return revenue.size()>0?revenue:null;
	        } catch (SQLException e) {
	            System.out.println("Sql exception");
	        }
	        catch (NullPointerException e){
	            System.out.println("Null poinetr excepetion");
	        }
	        catch (Exception e){
	            System.out.println("some other exception");
	        }
	        return null;
	    }
	    /*pid,uid,revenue,sold*/
	    public boolean updateRevenue(String uid,String pid,float revenue,int sold){
	        List<Revenue>revenues=getRevenue(uid,pid);
	        if(revenues==null) {
	        	System.out.println("revenue null condition true");
	            try {	     
	                ps = connection.prepareStatement("insert into revenue_details values(?,?,?,?)");
	                ps.setString(1, pid);
	                ps.setString(2, uid);
	                ps.setFloat(3, revenue);
	                ps.setFloat(4, sold);
	                return ps.executeUpdate() == 1 ? true : false;
	            } catch (SQLException se) {
	                System.out.println("Sql exception may be unknown user try to buy the product "+se);
	                return false;
	                //TODO:logger to register the exception
	            } catch (Exception e) {
	                System.out.println("some other exception "+e);
	                //TODO:logger to register the exception
	            }
	        }else{
	            Revenue revenue1=null;
	            for (Revenue revenue2:revenues){
	                revenue1=new Revenue(revenue2.getPid(),revenue2.getUid(),revenue2.getRevenue(),revenue2.getSold());
	            }
	                try {
	                    ps = connection.prepareStatement("update revenue_details set revenue=? ,sold=? where uid=? and pid=?");
	                    ps.setFloat(1, revenue1.getRevenue() + revenue);
	                    ps.setInt(2, revenue1.getSold() + sold);
	                    ps.setString(3,uid);
	                    ps.setString(4,pid);
	                    return ps.executeUpdate() > 0 ? true : false;
	                } catch (SQLException se) {
	                    System.out.println("Sql exception may be unknown user try to buy the product");
	                    return false;
	                    //TODO:logger to register the exception
	                } catch (Exception e) {
	                    System.out.println("some other exception ");
	                    //TODO:logger to register the exception
	                }
	        }
	        return false;
	    }
	    public boolean buyProduct(String uid,String pid){
	        try {
	            ps = connection.prepareStatement("update cart set status=1 where uid=? and pid=?");
	            ps.setString(1, uid);
	            ps.setString(2, pid);
	            return ps.executeUpdate() > 0 ? true : false;
	        } catch (SQLException se) {
	            System.out.println("Sql exception may be unknown user try to buy the product");
	            return false;
	            //TODO:logger to register the exception
	        } catch (Exception e) {
	            System.out.println("some other exception ");
	            //TODO:logger to register the exception
	        }
	        return true;
	    }
	    public boolean buyoneProduct(String uid,String pid,int orderid){
	        try {
	            ps = connection.prepareStatement("update cart set status=1 where uid=? and pid=? and orderid=?");
	            ps.setString(1, uid);
	            ps.setString(2, pid);
	            ps.setInt(3,orderid);
	            return ps.executeUpdate() > 0 ? true : false;
	        } catch (SQLException se) {
	            System.out.println("Sql exception may be unknown user try to buy the product");
	            return false;
	            //TODO:logger to register the exception
	        } catch (Exception e) {
	            System.out.println("some other exception ");
	            //TODO:logger to register the exception
	        }
	        return true;
	    }
	    public String[] userdetails(String uid) {
	    	String[] arr = new String[2];
	    	try {
				ps  = connection.prepareStatement("select first_name,last_name from user where uid=?");
				ps.setString(1,uid);
	            rs = ps.executeQuery();
				while (rs.next()){
					arr[0] =rs.getString(1);
	                arr[1]=rs.getString(2);
	            } 
	    	} catch (SQLException e) {
				System.out.println("sql exception");
			}	
           return arr;
           
	    }
	    public boolean upduserdetails(String uid,String fname,String lname) {
	    	try {
				ps  = connection.prepareStatement("update user set first_name = ?,last_name = ? where uid=?");
				ps.setString(1,fname);
				ps.setString(2,lname);
				ps.setString(3,uid);
				return ps.executeUpdate() > 0 ? true : false;
	    	} catch (SQLException e) {
				System.out.println("sql exception");
			} catch (Exception e) {
	            System.out.println("some other exception ");
	        }
           return false;
           
	    }
	    
	    public boolean deleteprod(String uid,String pid) {
	    	try {
				ps  = connection.prepareStatement("delete from cart where uid = ? and pid = ?");
				ps.setString(1,uid);
				ps.setString(2,pid);
				return ps.executeUpdate() > 0 ? true : false;
	    	} catch (SQLException e) {
				System.out.println("sql exception in deleteprod "+e);
			} catch (Exception e) {
	            System.out.println("some other exception in deleteprod");
	        }
           return false;
           
	    }



}
