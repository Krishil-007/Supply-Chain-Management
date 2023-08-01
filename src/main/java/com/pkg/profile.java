package com.pkg;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pkg.consumer.conf_data;

public class profile extends HttpServlet {
	Object[] user_data;
	public class conf_data{
		String dep_port;
		String db_port;
		String db_username;
		String db_password;
		
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		try {
			System.out.println(req.getSession().getAttribute("requestType"));
			
			ServletContext context = req.getServletContext();
			Reader reader = new FileReader(context.getRealPath("conf.json"));
		    Gson gson = new Gson();
		    conf_data prop = gson.fromJson(reader, conf_data.class);
		    
		    
			user_data = (Object[])req.getSession().getAttribute("user_data");
			System.out.println("In profile");
			String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("profile.html"))));
			
			String footer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\footer_css.css"))));
			String skeleton_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\skeleton.css"))));
			String profile_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\profile.css"))));
			
			String skeleton_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\skeleton.js"))));
			String main_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\main.js"))));
			
			
			html = html.replace("<!-- footer css data -->","<style type=\"text/css\">"+footer_css+"</style>");
			html = html.replace("<!-- skeleton css data -->","<style type=\"text/css\">"+skeleton_css+"</style>");
			html = html.replace("<!-- profile css data -->","<style type=\"text/css\">"+profile_css+"</style>");
			html = html.replace("<!--  skeleton js data -->","<script type=\"text/javascript\">"+skeleton_js+"</script>");
			html = html.replace("<!--  main js data -->","<script type=\"text/javascript\">"+main_js+"</script>");
			
			String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
			String uid=(String)user_data[0];
			String str="SELECT First_name,Last_name,Email_ID,Phone_no,Street,City,State,Zipcode from profile_details WHERE User_ID='"+uid+"'; ";
			ResultSet r=st.executeQuery(str);
			String fname="",lname="",email="",street="",city="",state="";
			long  phno=0,zpcd=0;
			while(r.next()) {
				fname=r.getString(1);
				lname=r.getString(2);
				email=r.getString(3);
				street=r.getString(5);
				city=r.getString(6);
				state=r.getString(7);
				phno=r.getLong(4);
				zpcd=r.getLong(8);
			}
			con.close();
			String inpstr1="<input type='text' class='form-control' id='fullName' name='first_name' value='"+fname+"' placeholder='Enter First name' required>";
			String inpstr2="<input type='text' class='form-control' id='fullName' name='last_name' value='"+lname+"' placeholder='Enter Last name' required>";
			String inpstr3="<input type='email' class='form-control' id='eMail' name='u_email' value='"+email+"' placeholder='Enter email ID' disabled>";
			String inpstr4="<input type='text' class='form-control' id='phone' name='phno' value='"+phno+"' placeholder='Enter phone number' required>";
			String inpstr5="<input type='name' class='form-control' id='Street' name='street' value='"+street+"'  placeholder='Enter Street' required>";
			String inpstr6="<input type='name' class='form-control' id='ciTy' name='city' value='"+city+"'  placeholder='Enter City' required>";
			String inpstr7="<input type='text' class='form-control' id='sTate' name='state' value='"+state+"'  placeholder='Enter State' required>";
			String inpstr8="<input type='text' class='form-control' id='zIp' name='zpcd' value='"+zpcd+"'  placeholder='Zip Code' required>";
			String user_type = "";
			if(user_data[0].toString().charAt(0)=='C') {user_type="consumer";}
			else if(user_data[0].toString().charAt(0)=='M') {user_type="manufacturer";}
			else if(user_data[0].toString().charAt(0)=='S') {user_type="supplier";}
			String home_btn = "<a href='/Innovative/"+user_type+"'><button type='button' id='submit' name='submit' class='btn btn-secondary btn-lg'>Home</button></a>";
			html = html.replace("{{Uname}}",fname+" "+lname);
			html = html.replace("{{Uemail}}",email);
			if(street.length()>0 && city.length()>0 && state.length()>0 && zpcd>0)
			{
				html = html.replace("{{Uaddress}}",street+", "+city+", "+state+" - "+zpcd);
			}
			else 
			{
				html = html.replace("{{Uaddress}}","-");
			}
			
			html = html.replace("<!-- inpstr1 -->",inpstr1);
			html = html.replace("<!-- inpstr2 -->",inpstr2);
			html = html.replace("<!-- inpstr3 -->",inpstr3);
			html = html.replace("<!-- inpstr4 -->",inpstr4);
			html = html.replace("<!-- inpstr5 -->",inpstr5);
			html = html.replace("<!-- inpstr6 -->",inpstr6);
			html = html.replace("<!-- inpstr7 -->",inpstr7);
			html = html.replace("<!-- inpstr8 -->",inpstr8);
			html = html.replace("<!-- Home button -->",home_btn);
			if(req.getHeader("requestType")!=null &&  req.getHeader("requestType").equals("warning"))
			{
				String warningPopup = "<div id='warningBox' class='alert alert-warning' role='alert'>"
						+ "            Please Provide Address Details"
						+ "        </div>";
				html = html.replace("<!-- Warning Popup -->",warningPopup);
				
			}
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print(html);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException{
		try {
			if(req.getHeader("requestType")!=null && req.getHeader("requestType").equals("warning"))
			{
				doGet(req, res);
			}
			
			ServletContext context = req.getServletContext();
			Reader reader = new FileReader(context.getRealPath("conf.json"));
		    Gson gson = new Gson();
		    conf_data prop = gson.fromJson(reader, conf_data.class);
		    
		    String fname=req.getParameter("first_name").toString();
			String lname=req.getParameter("last_name").toString();
//			String email=req.getPara("email").toString();
			String street=req.getParameter("street").toString();
			String city=req.getParameter("city").toString();
			String state=req.getParameter("state").toString();
			String uid=user_data[0].toString();
			String phno=req.getParameter("phno").toString();
			String zpcd=req.getParameter("zpcd").toString();
			
			String str="UPDATE `profile_details` SET `First_name` = '"+fname+"', `Last_name` = '"+lname+"', `Phone_no` = '"+phno+"', `Street` = '"+street+"', `City` = '"+city+"', `State` = '"+state+"', `Zipcode` = '"+zpcd+"' WHERE `profile_details`.`User_ID` = '"+uid+"'; ";
			String url = "jdbc:mysql://localhost:"+prop.db_port+"/innovative_aj";
			String sql_uname = prop.db_username;
			String sql_pass = prop.db_password;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);
			Statement st = con.createStatement();
			st.executeUpdate(str);
			con.close();
			doGet(req,res);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
}