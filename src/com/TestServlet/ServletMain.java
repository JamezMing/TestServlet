package com.TestServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;



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
		Gson gson = new Gson();

			
        PrintWriter writer = response.getWriter();
        
        // build HTML code
        String htmlResponse = "<html>";
        
        if(name == null || pass == null || name.isEmpty() || pass.isEmpty()){
        	url = "/login.html";
        	getServletContext().getRequestDispatcher(url).forward(request, response);;
        	
        	
        }else{
        	
        	HTTPRequest newreq = new HTTPRequest(name, pass);
        	String respStr = new String();
        	try {
        		respStr = newreq.sendGet("https://dts-stg.autodesk.com/rest/api/2/issue/" + index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		JsonObject result = new JsonParser().parse(respStr).getAsJsonObject();
    		String creationTime = result.get("fields").getAsJsonObject().get("created").toString();
    		creationTime = creationTime.substring(1, creationTime.length()-1);
    		String status = result.get("fields").getAsJsonObject().get("status").getAsJsonObject().get("statusCategory").getAsJsonObject().get("name").toString();
    		String resolutiondate = new String();
    		resolutiondate = result.get("fields").getAsJsonObject().get("resolutiondate").toString();
    		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


        	
	        htmlResponse += "<h2>Your username is: " + name + "<br/>";      
	        htmlResponse += "Your password is: " + pass + "<br/>"; 
	        htmlResponse += "The entry you are looking into is: " + index + "<h2/>";
	        htmlResponse += "<p>" + creationTime + "</p>";
	        htmlResponse += "<p>" + status + "</p>";
    		if(!resolutiondate.isEmpty() && resolutiondate!=null){
    			try {
    	    		resolutiondate = resolutiondate.substring(1, resolutiondate.length()-1);
    		        htmlResponse += "<p>" + resolutiondate + "</p>";
					Date start = formater.parse(creationTime);
	    			Date end = formater.parse(resolutiondate);
	    			long diffinmilles = end.getTime() - start.getTime();
	    			//TimeUnit timeUnit = TimeUnit.DAYS;
	    			long diffinhours = TimeUnit.HOURS.convert(diffinmilles, TimeUnit.MILLISECONDS);
	    			htmlResponse += "<p>" + "The issues takes " + diffinhours + " hours to resolve" + "</p>";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else{
		        htmlResponse += "<p>" + "Unresolved" + "</p>";
    		}
	        htmlResponse += "</html>";
	         
	        // return response
	        writer.println(htmlResponse);
        }
	}

}
