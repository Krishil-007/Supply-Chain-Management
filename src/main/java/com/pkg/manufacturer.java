package com.pkg;
import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.valves.rewrite.InternalRewriteMap.Escape;

import com.google.gson.Gson;
import com.pkg.consumer.conf_data;
import com.pkg.consumer.order_data;

public class manufacturer extends HttpServlet {
	
	public Object[] user_data;
	public class order_data{
		int count;
		String img_name;
		String product_name;
		int total_price;
		int unit_price;
	}
	public class item_data{
		String itemID;
		String itemName;
		int itemQty;
		int itemPrice;
		String itemCateg;
	}
	public class conf_data{
		String dep_port;
		String db_port;
		String db_username;
		String db_password;
		
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		try {
			ServletContext context = req.getServletContext();
			Reader reader = new FileReader(context.getRealPath("conf.json"));
		    Gson gson = new Gson();
		    conf_data prop = gson.fromJson(reader, conf_data.class);
		    
		    String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
		    Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
		    
		    
		    
		    
		    user_data = (Object[])req.getSession().getAttribute("user_data");
		    ResultSet rs = st.executeQuery("select * from profile_details where user_id='"+user_data[0]+"';");
			if(!rs.next())
			{
				req.getSession().setAttribute("msg_type","Unable To Login");
				res.sendRedirect("/Innovative/");
			}
			int numColumns = rs.getMetaData().getColumnCount();
			user_data = new Object[numColumns];
			for (int i = 1; i <= numColumns; i++) {
	            user_data[i - 1] = rs.getObject(i);
	        }
			req.getSession().setAttribute("user_data",user_data);
			
//			Object[] user_data = (Object[])req.getSession().getAttribute("user_data");
//			System.out.println("In Manufacturer");
			String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("manufacturer.html"))));
			
			String footer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\footer_css.css"))));
			String skeleton_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\skeleton.css"))));
			String Tables_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\Tables.css"))));
			
			String skeleton_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\skeleton.js"))));
			
			
			html = html.replace("<!--  footer css data -->","<style type=\"text/css\">"+footer_css+"</style>");
			html = html.replace("<!--  skeleton css data -->","<style type=\"text/css\">"+skeleton_css+"</style>");
			html = html.replace("<!--  Tables css data -->","<style type=\"text/css\">"+Tables_css+"</style>");
			html = html.replace("<!--  skeleton js data -->","<script type=\"text/javascript\">"+skeleton_js+"</script>");
			
			
			
			
			
			
			String str = "SELECT Order_ID,Product_ID,Quantity,From_ID FROM order_detail where To_ID='"+user_data[0]+"' AND Is_delivered='0';  ";
            ResultSet r = st.executeQuery(str);
            
            String master="";
            while(r.next()) {
            	String oid=r.getString(1);
            	String pid=r.getString(2);
            	int qtn=r.getInt(3);
            	String cid=r.getString(4);
            	int up=0;
            	int tot=0;
            	String pname="";
            	String cname="";
            	String isAvailable = "";
            	String tooltip = "";
            	int available_qty=0;
            	
            	
            	Statement st1 = con.createStatement();    			
            	ResultSet tr1=st1.executeQuery("SELECT Product_name, Price FROM product WHERE Product_ID='"+pid+"'; ");
            	while(tr1.next()) {
            		pname=tr1.getString(1);
            		up=tr1.getInt(2);
            	}
            	Statement st2 = con.createStatement();
//            	System.out.println(cid);
            	ResultSet tr2=st2.executeQuery("SELECT First_name, Last_name from profile_details WHERE User_ID='"+cid+"';");
            	while(tr2.next()) {
            		cname=tr2.getString(1)+" "+tr2.getString(2);
            	}
            	
            	Statement st3 = con.createStatement();    			
            	ResultSet tr3=st3.executeQuery("SELECT Quantity FROM inventory WHERE Manufacturer_ID='"+user_data[0]+"' AND Product_ID='"+pid+"'; ");
            	while(tr3.next()) {
            		available_qty = tr3.getInt(1);
            	}
            	System.out.println(available_qty+" "+user_data[0]+" "+pid);
            	
            	if(available_qty<qtn)
        		{
        			isAvailable = "unable";
        			tooltip = "Only "+available_qty+" Available";
        		}
            	
            	
            	tot=up*qtn;
            	master+="<article title='"+tooltip+"'  id='"+oid+"' class='leaderboard_profile "+isAvailable+" '>"
            			+ "    <span class='leaderboard_order_id'>"+oid+"</span>"
            			+ "    <span class='leaderboard_name'>"+pname+"</span>"
            			+ "    <span class='leaderboard_name'>"+cname+"</span>"
            			+ "    <span class='leaderboard_qty'>"+qtn+"</span>"
            			+ "    <span class='leaderboard_amt'>"+tot+"</span>"
            			+ "    <button value='"+oid+"' class='btn btn-success btn-circle btn-circle-xl m-1'><i class='fa fa-check'></i></button>"
            			+ "</article>";
            }
			
			
			
			html = html.replace("{{Name}}",user_data[3]+" "+user_data[4]);
			html = html.replace("<!-- {{Order details}} -->",master);
			
			html = html.replace("//port number","port = '"+prop.dep_port+"';");
			
			String manufacturer_data = "manufacturer_data  = {"+
					"	        		'user_id' : '"+user_data[0]+"',"+
					"	        		'email_id' : '"+user_data[2]+"',"+
					"	        		'first_name' : '"+user_data[3]+"',"+
					"	        		'last_name' : '"+user_data[4]+"',"+
					"	        		'phone_no' : '"+user_data[5]+"',"+
					"	        		'street' : '"+user_data[6]+"',"+
					"	        		'city' : '"+user_data[7]+"',"+
					"	        		'state' : '"+user_data[8]+"',"+
					"	        		'zipcode' : '"+user_data[9]+"'"+
					"	        }";
			html = html.replace("//manufacturer_data",manufacturer_data);
			
			
			String item_data = "item_data = {";
			r = st.executeQuery("SELECT * from inventory WHERE Manufacturer_ID='"+user_data[0]+"';");
			while(r.next())
			{
				item_data += "	'"+r.getString(2)+"' : {'itemID' : '"+r.getString(2)+"',"+
						"	        		'itemName' : '"+r.getString(3)+"',"+
						"	        		'itemQty' : '"+r.getString(4)+"'}, ";
				System.out.println(r.getString(3));
			}
			item_data += "}";
			html = html.replace("//item_data",item_data);
			
//			String categ_data = "categ_data = [";
//			 r = st.executeQuery("SELECT DISTINCT Category FROM `product`; ");
//				while(r.next())
//				{
//					categ_data+="'"+r.getString(1)+"',";
//				}
//			html = html.replace("//categ_data",categ_data+"]");
			
			
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print(html);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		
		try {
			Gson gson = new Gson();
		    BufferedReader reader = req.getReader();
		     
		    
		    ServletContext context = req.getServletContext();
			Reader conf_reader = new FileReader(context.getRealPath("conf.json"));
		    conf_data prop = gson.fromJson(conf_reader, conf_data.class);
		    
			String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
			System.out.println(req.getHeader("Request-Type"));
			
			if(req.getHeader("Request-Type").equals("1"))
			{
				String curr_orderID = req.getHeader("OrderID");
				ResultSet rs =  st.executeQuery("SELECT * FROM `order_detail` WHERE order_id='"+curr_orderID+"';");
				rs.next();
				String consumer_id = rs.getString(2);
				String manufacturer_id = rs.getString(3);
				int	qty = rs.getInt(4);
				String product_id = rs.getString(5);
				int	is_delivered = rs.getInt(6);
				rs=st.executeQuery("SELECT Quantity FROM inventory WHERE Manufacturer_ID='"+manufacturer_id+"' AND Product_ID='"+product_id+"'; ");
            	rs.next();
            	int available_qty = rs.getInt(1);
				
				st.executeUpdate("UPDATE `order_detail` SET `Is_delivered`=1 WHERE Order_ID='"+curr_orderID+"'");
				st.executeUpdate("UPDATE `inventory` SET `Quantity`="+(available_qty-qty)+" WHERE Manufacturer_ID='"+manufacturer_id+"' AND Product_ID='"+product_id+"';");
			}
			else if(req.getHeader("Request-Type").equals("2"))
			{
				item_data[] items = gson.fromJson(reader, item_data[].class);
//				st.executeUpdate("DELETE from inventory WHERE Manufacturer_ID='"+user_data[0]+"';");
//				st.executeUpdate("DELETE from product WHERE User_ID='"+user_data[0]+"';");
				ResultSet rs =  st.executeQuery("select product_id,product_name,user_id from product;");
				List<String> curr_item_names = new ArrayList<>();
				List<String> curr_item_ids = new ArrayList<>();
				List<String> curr_user_ids = new ArrayList<>();
				
				while(rs.next()) 
				{	
					curr_item_ids.add(rs.getString(1));
					curr_item_names.add(rs.getString(2));
					curr_user_ids.add(rs.getString(3));
				}
//							
				for (item_data ite : items) 
				{
					
					if(curr_item_names.contains(ite.itemName))
					{
						int idx;
						for(idx=0;idx<curr_item_names.size();idx++)
						{
							if(curr_item_names.get(idx).equals(ite.itemName))
							{
								ite.itemID = curr_item_ids.get(idx);
								if(curr_user_ids.get(idx).equals(user_data[0]))
								{
									st.executeUpdate("UPDATE `inventory` SET `Quantity`='"+ite.itemQty+"' WHERE `Manufacturer_ID`='"+user_data[0]+"' AND `Product_ID`='"+ite.itemID+"';");
									st.executeUpdate("UPDATE `product` SET `Price`='"+ite.itemPrice+"',`Category`='"+ite.itemCateg+"' WHERE `Product_ID`='"+ite.itemID+"' AND`User_ID`='"+user_data[0]+"';");
									break;
								}							
								
							}
							
						}
						System.out.println(ite.itemID);
						if(idx==curr_item_names.size())
						{
							st.executeUpdate("INSERT INTO `product`(`Product_ID`, `Product_name`, `Price`, `Category`, `User_ID`) VALUES ('"+ite.itemID+"','"+ite.itemName+"','"+ite.itemPrice+"','"+ite.itemCateg+"','"+user_data[0]+"')");
							
							String str = "INSERT INTO `inventory`(`Manufacturer_ID`, `Product_ID`, `Product_name`, `Quantity`) VALUES ('"+user_data[0]+"','"+ite.itemID+"','"+ite.itemName+"','"+ite.itemQty+"');";
							st.executeUpdate(str);
							
						}
//						ite.itemID = curr_item_ids.get();
					}
					else 
					{
						Random rd = new Random();				
    					String rndm_id = Integer.toString(rd.nextInt(10,99));    					
						while(curr_item_ids.contains("P"+rndm_id)){rndm_id = Integer.toString(rd.nextInt(10,99));}
						ite.itemID = "P"+rndm_id;
						st.executeUpdate("INSERT INTO `product`(`Product_ID`, `Product_name`, `Price`, `Category`, `User_ID`) VALUES ('"+ite.itemID+"','"+ite.itemName+"','"+ite.itemPrice+"','"+ite.itemCateg+"','"+user_data[0]+"')");
						
						String str = "INSERT INTO `inventory`(`Manufacturer_ID`, `Product_ID`, `Product_name`, `Quantity`) VALUES ('"+user_data[0]+"','"+ite.itemID+"','"+ite.itemName+"','"+ite.itemQty+"');";
						st.executeUpdate(str);
						
						
						
						
					}
				}		
			}
			doGet(req, res);
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
}