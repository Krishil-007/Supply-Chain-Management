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

public class auth2 extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		
        System.out.println("posted in auth");		
    		try 
    		{
    			String url = "jdbc:mysql://localhost:4000/aj";
    			String sql_uname = "root";
    			String sql_pass = "root";
    			Class.forName("com.mysql.cj.jdbc.Driver");
    			Connection con = DriverManager.getConnection(url,sql_uname,sql_pass);			
    			Statement st = con.createStatement();
    			String auth_type = req.getParameter("auth_type");
    			System.out.println("auth type : "+auth_type);
//    			if(auth_type==null)
    			if(auth_type.equals("signup"))
    			{
    				ResultSet rs = st.executeQuery("select * from user_data where email='"+req.getParameter("email")+"'");
    				int line=0;
    				while(rs.next())line++;
    				if(line>0)
    				{
    					System.out.println("Account Already exists with same email ID");
    					throw new Exception("Email ID already registered"); 
    				}
    				else
    				{
    					String user_type = req.getParameter("user_type");
    					rs = st.executeQuery("select user_id from user_data");
    					
    					List<String> curr_ids = new ArrayList<>();
    					while(rs.next()) {curr_ids.add(rs.getString(1).substring(1));}
    					
    					Random rd = new Random();				
    					String rndm_id = Integer.toString(rd.nextInt(10000,99999));
    					while(curr_ids.contains(rndm_id))rndm_id = Integer.toString(rd.nextInt(10000,99999));
    					
    					
    					st.executeUpdate("insert into user_data values('"+user_type.charAt(0)+rndm_id+"','"+req.getParameter("email")+"','"+req.getParameter("firstname")+"','"+req.getParameter("lastname")+"','"+req.getParameter("password")+"')");
    					System.out.println("Data Saved");
    					throw new Exception("Account Registered");
    					
    				}
    			}
    			else if(auth_type.equals("login"))
    			{
    				String username = req.getParameter("username");
    					ResultSet rs = st.executeQuery("select * from user_data where user_id='"+username+"' OR email='"+username+"'");
    					if(rs.next())
    					{
    						if(rs.getString("password").equals(req.getParameter("password")))
    						{    			
    							int numColumns = rs.getMetaData().getColumnCount();
    							Object[] user_data = new Object[numColumns];
    							for (int i = 1; i <= numColumns; i++) {
    					            user_data[i - 1] = rs.getObject(i);
    					        }
    							req.getSession().setAttribute("user_data",user_data);
//    							System.out.println("Success.."+rs.getString("first_name")+" "+rs.getString("last_name"));
    							if(rs.getString(1).charAt(0)=='C')
    								{
//    								RequestDispatcher dispatcher = req.getRequestDispatcher("/consumer");
//    							    dispatcher.forward(req, res);
    							    res.sendRedirect("/Innovative/consumer");
    								}
    							else if(rs.getString(1).charAt(0)=='S')
    								{
//    								RequestDispatcher dispatcher = req.getRequestDispatcher("/supplier");
//    							    dispatcher.forward(req, res);
    							    res.sendRedirect("/Innovative/supplier");
    								}
    							else if(rs.getString(1).charAt(0)=='M') 
    								{
//    								RequestDispatcher dispatcher = req.getRequestDispatcher("/manufacturer");
//    							    dispatcher.forward(req, res);
    							    res.sendRedirect("/Innovative/manufacturer");
    								}
    							else res.sendRedirect("/Innovative/");
    							
    						}
    						else 
    						{
    							throw new Exception("Invalid Password");
    							
    						}
    					}
    					else 
    					{
    						throw new Exception("Invalid Account");    								
    					}    					
    				
    				
    			}
    						
    			st.close();
    			con.close();
    		} catch (Exception e) {
				req.getSession().setAttribute("msg_type",e.getMessage());
				System.out.println(e.getMessage()+" in auth");
				res.sendRedirect("/Innovative/");
			}
        
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		System.out.println("get in auth");
	}
}

