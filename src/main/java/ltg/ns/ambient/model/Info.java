package ltg.ns.ambient.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Info {
	
	String author = "";
	String classroom = "";
	String school = "";
	Integer number = 0;
	String wordle = "";

	public Info(){
		
	}
	
	public Info(String a, String c, String s, Integer n, String w){
		author =a;
		classroom = c;
		school = s;
		number = n;
		wordle = w;
	}

	public int getCount() {
		return number;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getClassroom(){
		return classroom;
	}
	
	public String getSchool() {
		return school;
	}
	
	public String getWordle(){
		return wordle;
	}
	
	public void setCount(Integer n) {
		number = n;
	}
	
	public void setAuthor(String a){
		author = a;
	}
	
	public void setClassroom(String c){
		classroom = c;
	}
	
	public void setWordle(String w){
		wordle = w;
	}

	
}
