package com.pkg;
import com.google.gson.*;

import java.io.*;

import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	

    
public class consumer extends HttpServlet {
	public class order_data{
		int count;
		String img_name;
		String product_name;
		int total_price;
		int unit_price;
		String product_id;
		String consumer_id;
		String mfg_id;
	}
	
	public class conf_data{
		String dep_port;
		String db_port;
		String db_username;
		String db_password;
		
	}
	
	
	public String getproductHTML(ResultSet r)
	{
		try {
			String html_str = "<div class='col-lg-3 col-md-4 col-sm-6 pb-1' id='"+r.getString(3)+"' name='"+r.getString(1)+"' price='"+r.getInt(2)+"'>"
		+ "                    <div class='product-item bg-light mb-4'>"
		+ "                        <div class='product-img position-relative overflow-hidden'>"
		+ "                            <img class='img-fluid w-100' src='https://shorturl.at/cszPU' alt=''>"		
		+ "                        </div>"
		+ "                        <div class='text-center py-4'>"
		+ "                            <a class='h2 text-decoration-none text-truncate' href=''>"+r.getString(1)+"</a>"
		+ "                            <div class='d-flex align-items-center justify-content-center mt-2'>"
		+ "                                <h3>Rs."+r.getInt(2)+"</h3>"
		+ "                                <h4 class='text-muted ml-2'><del>Rs."+r.getInt(2)+"</del></h4>"
		+ "                            </div>"
		+ "                            <div class='product-action'>"
		+ "                                <a value='"+r.getString(3)+"' class='ATC btn btn-outline-dark btn-square'>"
		+ "                                        <i class='fa fa-shopping-cart'></i></a>"
		+ "                            </div>"
		+ "                        </div>"
		+ "                    </div>"
		+ "      	</div>";
		return html_str;
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
		
	}
	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		
//		if(req.getSession().getAttribute("logout")!=null && req.getSession().getAttribute("logout"))
//		{
//			
//		}
		
		
		ServletContext context = req.getServletContext();
		System.out.println(context.getContextPath()+"/Image/logo.png");
		
//		String curr_file_path = context.getRealPath("").replace("\\","/");
		String curr_file_path = context.getRealPath("");
		Reader reader = new FileReader(context.getRealPath("conf.json"));
	    Gson gson = new Gson();
	    conf_data prop = gson.fromJson(reader, conf_data.class);
	    
	    Object[] user_data = (Object[])req.getSession().getAttribute("user_data");
	    
	    
	    
		
		
		
		String cat1="";
		String cat2="";
		String cat3="";
		String cat4="";
		String cat5="";
		
		
		try {
//			sqldata sqlobj = new sqldata();
//            sqlobj.connect("root");
			String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
			
			
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
			
			String str = "SELECT DISTINCT Product_name,Price,Product_ID from product WHERE Category='Appliances';  ";
            ResultSet r = st.executeQuery(str);
            while (r.next()) {
            		cat1 += getproductHTML(r);
//              System.out.println(r.getString(1));
            } 
            
            str = "SELECT DISTINCT Product_name,Price,Product_ID from product WHERE Category='Clothing';  ";
            r = st.executeQuery(str);
            while (r.next()) {
				cat2 += getproductHTML(r);
//              System.out.println(r.getString(1));
            }
            
            str = "SELECT DISTINCT Product_name,Price,Product_ID from product WHERE Category='Cosmetic';  ";
            r = st.executeQuery(str);
            while (r.next()) {
				cat3 += getproductHTML(r);
//              System.out.println(r.getString(1));
            }
            str = "SELECT DISTINCT Product_name,Price,Product_ID from product WHERE Category='Electronics';  ";
            r = st.executeQuery(str);
            while (r.next()) {
            		cat4 += getproductHTML(r);
//              System.out.println(r.getString(1));
            }
            
            str = "SELECT DISTINCT Product_name,Price,Product_ID from product WHERE Category='Sports';  ";
            r = st.executeQuery(str);
            while (r.next()) {
            		cat5 += getproductHTML(r);
//              System.out.println(r.getString(1));
            }
            
            //manufacturer side code 
            Vector<String> prid = new Vector<String>();
            str="SELECT DISTINCT(Product_ID) FROM product;";
            r=st.executeQuery(str);
            while (r.next()) {
        		prid.add(r.getString(1));
//          System.out.println(r.getString(1));
            }
            
            Vector<Vector<String>> vec_final = new Vector<Vector<String>>();
            String product_mfg_list = "product_mfg_list = {";
            for (String s : prid) {
//                System.out.println(s);
            	
                String tq="SELECT CONCAT(First_name,' ',Last_name) as mfg_name,user_id from profile_details where User_ID in (SELECT User_ID from product WHERE Product_ID='"+s+"');"; 
                String mfg_names = "[";
//                t.add(s);
                r=st.executeQuery(tq);
                while(r.next()) {
                	mfg_names += "['"+r.getString(1)+"','"+r.getString(2)+"'],";
                }
                mfg_names+="]";
                product_mfg_list += "'"+s+"':"+mfg_names+",";
//                vec_final.add(t);
            }
            product_mfg_list+="}";
            
            
            for (int i = 0; i < vec_final.size(); i++) {
                for (int j = 0; j < vec_final.get(i).size(); j++) {
                    System.out.print(vec_final.get(i).get(j) + " ");
                }
                System.out.println();
            }

            con.close();
//            sqlobj.closeConnection();
//            ServletContext context = req.getServletContext();
            String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("consumer.html"))));
            String footer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\footer_css.css"))));
			String skeleton_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\skeleton.css"))));
			String consumer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\consumer.css"))));
			String cart_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\cart.css"))));
			
			
			String skeleton_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\skeleton.js"))));
			String main_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\main.js"))));
			
			html = html.replace("<!--  footer css data -->","<style type=\"text/css\">"+footer_css+"</style>");
			html = html.replace("<!--  skeleton css data -->","<style type=\"text/css\">"+skeleton_css+"</style>");
			html = html.replace("<!--  consumer css data -->","<style type=\"text/css\">"+consumer_css+"</style>");
			html = html.replace("<!--  cart css data -->","<style type=\"text/css\">"+cart_css+"</style>");
            
			html = html.replace("<!--  skeleton js data -->","<script>"+skeleton_js+"</script>");
			html = html.replace("<!--  main js data -->","<script>"+main_js+"</script>");
			html = html.replace("@curr_path\\",curr_file_path);
			
			html = html.replace("//port number","port = '"+prop.dep_port+"';");
			
			String consumer_data = "consumer_data  = {"+
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
			html = html.replace("//consumer_data",consumer_data);
			
			html = html.replace("//product_mfg_list",product_mfg_list);
			
			
			html = html.replace("{{Name}}",user_data[3]+" "+user_data[4]);
			html = html.replace("<!-- {{Appliances card code}} -->",cat1);
			html = html.replace("<!-- {{Clothing card code}} -->",cat2);
			html = html.replace("<!-- {{Cosmetic card code}} -->",cat3);
			html = html.replace("<!-- {{Electronics card code}} -->",cat4);
			html = html.replace("<!-- {{Sports card code}} -->",cat5);
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
		    order_data[] order = gson.fromJson(reader, order_data[].class); 
		    
		    ServletContext context = req.getServletContext();
			Reader conf_reader = new FileReader(context.getRealPath("conf.json"));
		    conf_data prop = gson.fromJson(conf_reader, conf_data.class);
		    
			String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery("select order_id from order_detail");
			
			List<String> curr_ids = new ArrayList<>();
			while(rs.next()) {curr_ids.add(rs.getString(1).substring(1));}
			
			for (order_data ite : order) {
				Random rd = new Random();				
				String rndm_id = Integer.toString(rd.nextInt(10000,99999));
				while(curr_ids.contains(rndm_id))rndm_id = Integer.toString(rd.nextInt(10000,99999));
//				ResultSet r_supplier = st.executeQuery("select user_id from product where product_id='"+ite.product_id+"'");
				String manf_id = ite.mfg_id;
//				if(r_supplier.next()) {manf_id = r_supplier.getString(1);}
				String str = "INSERT INTO `order_detail`(`Order_ID`, `From_ID`, `To_ID`, `Quantity`, `Product_ID`, `Is_delivered`) VALUES ('O"+rndm_id+"','"+ite.consumer_id+"','"+manf_id+"','"+Integer.toString(ite.count)+"','"+ite.product_id+"','0');";
				st.executeUpdate(str);			
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
}