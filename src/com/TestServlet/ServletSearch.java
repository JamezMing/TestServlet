package com.TestServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class ServletSearch
 */
@WebServlet({ "/ServletSearch", "/search" })
public class ServletSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String username = new String();
	private String password = new String();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    PrintWriter out = response.getWriter();
		String index = request.getParameter("index");
		String param = request.getParameter("type");
		



		if (session.isNew()){
			out.println("<h2> Invalid Session </h2>");
		}else{
			String pass = (String) session.getAttribute("username");
			String uname = (String) session.getAttribute("password");
        	HTTPRequest newreq = new HTTPRequest(uname, pass);
        	String respStr = new String();
			if (param.equals("issue")){
	        	try {
	        		respStr = newreq.sendGet("https://dts-stg.autodesk.com/rest/api/2/issue/" + index);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		JsonObject result = new JsonParser().parse(respStr).getAsJsonObject();
    		String creationTime = result.get("fields").getAsJsonObject().get("created").toString();
    		creationTime = creationTime.substring(1, creationTime.length()-1);
    		String status = result.get("fields").getAsJsonObject().get("status").getAsJsonObject().get("statusCategory").getAsJsonObject().get("name").toString();
    		String statusdes = result.get("fields").getAsJsonObject().get("status").getAsJsonObject().get("name").toString();
    		statusdes = statusdes.substring(1, statusdes.length()-1);
    		boolean isEscalated; 
    		if (statusdes.equalsIgnoreCase("Escalated to Autodesk") || statusdes.equalsIgnoreCase("escalated")){
    			isEscalated = true;
    		}
    		else{
    			isEscalated = false;
    		}

    		String resolutiondate = new String();
    		String htmlResponse = new String();
    		resolutiondate = result.get("fields").getAsJsonObject().get("resolutiondate").toString();
    		System.out.println(resolutiondate);
    		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


  	        htmlResponse += "<h2>The entry you are looking into is: " + index + "<h2/>";
	        htmlResponse += "<p>" + creationTime + "</p>";
	        htmlResponse += "<p>" + status + "</p>";
	        htmlResponse += "<p>" + "Is escalated: " + isEscalated + "</p>";

    		if(!resolutiondate.isEmpty() && !resolutiondate.equals("null")){
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
	        out.println(htmlResponse);
			
		}
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
