package com.TestServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.net.*;


/**
 * Servlet implementation class TestServlet
 */
//@WebServlet("/Main")
public class ServletMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String message;

    /**
     * Default constructor. 
     */
    public ServletMain() {
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    public void init() throws ServletException
    {
        // Do required initialization
        message = "Hello World";
    }

    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String index = request.getParameter("index");
		String url;
			
        PrintWriter writer = response.getWriter();
        
        // build HTML code
        String htmlResponse = "<html>";
        
        if(name == null || pass == null || name.isEmpty() || pass.isEmpty()){
        	url = "/login.html";
        	getServletContext().getRequestDispatcher(url).forward(request, response);;
        	
        	
        }else{
        	
        	Authenticator.setDefault(new Authenticator() {
        		 @Override
        		        protected PasswordAuthentication getPasswordAuthentication() {
        		         return new PasswordAuthentication(
        		   name, pass.toCharArray());
        		        }
        	});
        	
        	HTTPRequest newreq = new HTTPRequest();
        	try {
				newreq.sendGet("http://localhost:8090/jira/rest/api/2/issue/HSP-1");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	
	        htmlResponse += "<h2>Your username is: " + name + "<br/>";      
	        htmlResponse += "Your password is: " + pass + "<br/>"; 
	        htmlResponse += "The entry you are looking into is: " + index + "<h2/>";
	        htmlResponse += "</html>";
	         
	        // return response
	        writer.println(htmlResponse);
        }
	}

}
