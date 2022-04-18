package com.nutrifinder.com.nutrifinder.eclipse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;


public class App 
{
	
	Map<String, String> queryResult = new HashMap<String, String>();
	Input input = new Input();
	boolean quit = false;
	
	// per '5 best practices api key storage'
	// https://developers.amadeus.com/blog/best-practices-api-key-storage
	// i use a free key but still
	final String API_KEY = System.getenv().get("API_KEY"); 
	final String APP_ID = System.getenv().get("APP_ID");
	
	HttpClient client = HttpClient.newHttpClient();	
	
	//singleton for no raisin
	// actually, the free api key only allows max two instances
	private static App instance;
	private App() {
		System.out.println("Welcome to nutrifinder! Powered by nutritionix.com. All reights reservered.");
	}
	public static App getInstance() {
		
		if (instance == null) {
			instance = new App();
		}
		return instance;
	}
	
	
    public void run()
    {
    	
    	//intitialze
    	//while running
    		//make calls
    		//display calls
    		//next
    
    	
    	String query = input.Get();
    	
    	if (query.compareToIgnoreCase("quit") == 0) {
    		quit = true;
    		return;
    	}
    	
    	
    	try {
    		
			var nutritionRequest = HttpRequest.newBuilder()
					.uri(URI.create("https://trackapi.nutritionix.com/v2/natural/nutrients"))
					.header("Content-Type", "application/json")
					.header("x-app-id", APP_ID)
					.header("x-app-key", API_KEY)
					.header("x-remote-user-id", "0")
					.POST(HttpRequest.BodyPublishers.ofString("{ \"query\":\"" + query + "\" }"))
					.build();
														//"{ \"query\":\"i had three eggs for breakfast\\n1 bacon\" }"
			var nutriResponse = client.send(nutritionRequest, BodyHandlers.ofInputStream());
			
			JsonParser parser = Json.createParser(nutriResponse.body());
			
			while (parser.hasNext()) {
				
				Event e = parser.next();
				if (e == Event.KEY_NAME) {
					
					switch (parser.getString()) {
					
						case "food_name":
							parser.next();
							queryResult.put("name", parser.getString());
							break;
							
						case "serving_qty":
							parser.next();
							queryResult.put("quantity", parser.getString());
							break;
							
						case "nf_calories":
							parser.next();
							queryResult.put("cal", parser.getString());
							break;
					}
				}
			}
			
			System.out.printf("%s %s contains %s calories\n", queryResult.get("quantity"),
													   		queryResult.get("name"),
													   		queryResult.get("cal"));
			queryResult.clear();
			
			//investigate some data structures
			
			/*
			
			Map<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
			
			results.put("name", new ArrayList<String>());
			results.get("name").add("Hi!");
			results.get("name").add("Bye");
			
			for (String thing : results.get("name")) {
				System.out.println(thing);
			}
			
			results.put("blue", new ArrayList<String>());
			results.get("blue").add("zig");
			results.get("blue").add("zag");
			
			for (String key : results.keySet()) {
				for (String thing : results.get(key)) {
					System.out.println(thing);
				} //sort keys first
			}
			*/

			
		}
    	
		catch(Exception e) {
			System.out.println("oof");
			System.out.println(e.toString());
		}
    }
}
