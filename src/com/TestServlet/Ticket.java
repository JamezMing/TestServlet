package com.TestServlet;

import java.io.Serializable;

public class Ticket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imlevel;
	private String type;
	private String description;
	
	public Ticket(){
		imlevel = "";
		type = "";
		description = "";
	}
	
	public Ticket(String level, String ty, String des){
		imlevel = level;
		type = ty;
		description = des;
	}
	
	public String getImportanceLevel(){
		return this.imlevel;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setImportanceLevel(String level){
		imlevel = level;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setDescription(String des){
		description = des;
	}
}
