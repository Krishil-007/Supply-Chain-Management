package com.pkg;

import java.io.*;

import javax.servlet.http.HttpServlet;

import com.google.gson.JsonObject;

import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;
import java.nio.file.Paths;
import java.nio.file.Files;

public class index extends HttpServlet {
	public class conf_data{
		String dep_port;
		String db_port;
		String db_username;
		String db_password;
		
	}
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException
	{
		try {
			ServletContext context = req.getServletContext();
			Reader reader = new FileReader(context.getRealPath("conf.json"));
			String curr_file_path = context.getRealPath("").replace("\\","/");
		    Gson gson = new Gson();
		    conf_data prop = gson.fromJson(reader, conf_data.class);
		    
//		    System.out.println("get in index");
		    
            
			String html = new String(Files.readAllBytes(Paths.get(context.getRealPath("index.html"))));
			
			String login_css = new String(Files.readAllBytes(Paths.get(context.getRealPath("css\\Login_css.css"))));
			String login_js = new String(Files.readAllBytes(Paths.get(context.getRealPath("Javascript\\Login_js.js"))));
			
			
			html = html.replace("<!-- login css data -->","<style type=\"text/css\">"+login_css+"</style>");
			html = html.replace("<!-- login js data -->","<script type=\"text/javascript\">"+login_js+"</script>");
			
			Object msg_type = req.getSession().getAttribute("msg_type");
			req.getSession().setAttribute("msg_type","");
				System.out.println(msg_type);
				
				if(msg_type!=null && msg_type.toString().length()>0)
				{
					System.out.println(msg_type+" in index get");
					if(msg_type.equals("Email ID already registered"))
					{
						html = html.replace("<!-- {{Signup_error}} -->","<div class='signupError'><p>"+msg_type.toString()+"</p></div>");
					}					
					else if(msg_type.equals("Account Registered"))
					{						
						html = html.replace("<!-- {{Signup_complete}} -->","<div class='signupComplete'><p>"+msg_type.toString()+"</p></div>");						
					}
					else 
					{
						html = html.replace("<!-- {{Login_error}} -->","<div class='loginError'><p>"+msg_type.toString()+"</p></div>");
					}
					
					
				}
			
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.print(html);
			out.close();			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException
	{
		try {
//			System.out.println("post in index");
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}

