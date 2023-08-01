package com.pkg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Files;

public class consumer1 extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		try {
			ServletContext context = req.getServletContext();
			Object[] user_data = (Object[])req.getSession().getAttribute("user_data");
			System.out.println("In Consumer");
			String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("consumer.html"))));
			
			String footer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\footer_css.css"))));
			String skeleton_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\skeleton.css"))));
			String consumer_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\consumer.css"))));
			
			
			String skeleton_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\skeleton.js"))));
			String main_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\main.js"))));
			
//			html = html.replace("<!--  footer css data -->","<style type=\"text/css\">"+footer_css+"</style>");
//			html = html.replace("<!--  skeleton css data -->","<style type=\"text/css\">"+skeleton_css+"</style>");
//			html = html.replace("<!--  consumer css data -->","<style type=\"text/css\">"+consumer_css+"</style>");
			
//			html = html.replace("<!--  skeleton js data -->","<script type=\"text/javascript\">"+skeleton_js+"</script>");
//			html = html.replace("<!--  main js data -->","<script type=\"text/javascript\">"+main_js+"</script>");
			
			
			
			html = html.replace("{{Name}}",user_data[2]+" "+user_data[3]);
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print(html);		
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
}

