package com.pkg;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pkg.auth.conf_data;

public class supplier extends HttpServlet {
	public class conf_data{
		String dep_port;
		String db_port;
		String db_username;
		String db_password;
		
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		try 
		{
			ServletContext context = req.getServletContext();
			Reader reader = new FileReader(context.getRealPath("conf.json"));
		    Gson gson = new Gson();
		    conf_data prop = gson.fromJson(reader, conf_data.class);
		    
		    
			Object[] user_data = (Object[])req.getSession().getAttribute("user_data");
			System.out.println("In Supplier");
			String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("supplier.html"))));
			
			String footer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\footer_css.css"))));
			String skeleton_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\skeleton.css"))));
			String supplier_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\supplier.css"))));
			String Tables_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\Tables.css"))));			
			
			
			String skeleton_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\skeleton.js"))));
			
			html = html.replace("<!--  footer css data -->","<style type=\"text/css\">"+footer_css+"</style>");
			html = html.replace("<!--  skeleton css data -->","<style type=\"text/css\">"+skeleton_css+"</style>");
			html = html.replace("<!--  supplier css data -->","<style type=\"text/css\">"+supplier_css+"</style>");
			html = html.replace("<!--  Tables css data -->","<style type=\"text/css\">"+Tables_css+"</style>");
			
			
			html = html.replace("<!--  skeleton js data -->","<script type=\"text/javascript\">"+skeleton_js+"</script>");
			
			html = html.replace("{{Name}}",user_data[3]+" "+user_data[4]);
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print(html);
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
	}
}
