package com.jnj.configuration.service;

import java.io.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonReaderService {

	/**
	 * Retrieves the values associated with a specified key from a JSON file.
	 *
	 * @param fileName The name of the JSON file
	 * @param key      The key whose associated values are to be retrieved.
	 * @return A list of values associated with the specified key, or an empty list
	 *         if the key is not found.
	 */

	public Object getJsonValues(String fileName, String key) {
		try {
			// Define the file location
			String baseDirectory = System.getProperty("com.polarion.home") + "/../scripts/JSON Configuration/";
			String fileLocation = baseDirectory + fileName + ".json";

			// Read and parse JSON directly from the file using parse method
            FileReader reader = new FileReader(fileLocation);
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement jsonElement = com.google.gson.JsonParser.parseReader(jsonReader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            reader.close();

            // Retrieve values dynamically
           return  getNestedJsonValues(jsonObject, key);
		} catch (Exception e) {
			System.err.println("Error reading JSON: " + e.getMessage());
		}
		return null;
	}
	
	
	
	/**
	 * Retrieves a nested JSON value using dot notation.
	 * 
	 * @param jsonObject The root JSON object from which to retrieve values.
	 * @param key        The dot-separated key path (e.g., "polarion.projects.0.id").
	 * @return The JSON element found at the specified key path, or null if not found.
	 */
	 private Object getNestedJsonValues(JsonObject jsonObject, String key) {
		
			// Check if key matches directly without needing dot notation (flat structure)
			if (jsonObject.has(key)) {
			    JsonElement element = jsonObject.get(key);  
			    System.out.print("Json Element"+ element+"\n");
			    if (element.isJsonPrimitive()) {
			        return element.getAsString();
			    }		  
			}
	
			String[] keys = key.split("\\.");
			JsonElement jsonAttributeValues = jsonObject;

			for (String iterateKey : keys) {
				if (jsonAttributeValues instanceof JsonObject jsonObj && jsonObj.has(iterateKey)) {
					jsonAttributeValues = jsonObj.get(iterateKey);
				} else if (jsonAttributeValues instanceof JsonArray jsonArray) {
					try {
						int index = Integer.parseInt(iterateKey);
						jsonAttributeValues = jsonArray.get(index);
					} catch (NumberFormatException | IndexOutOfBoundsException e) {
						return null; // Invalid index or not an array
					}
				} else {
					return null; // Key not found
				}
			}
			System.out.println("current Json Object is" + jsonAttributeValues+"\n");
		
			 // Handle different JSON value types
	        if (jsonAttributeValues.isJsonPrimitive()) {
	            JsonPrimitive objectType = jsonAttributeValues.getAsJsonPrimitive();
	            
	            if (objectType.isString()) {
	                return objectType.getAsString();
	            } else if (objectType.isNumber()) {
	                return objectType.getAsNumber();
	            } else if (objectType.isBoolean()) {
	                return objectType.getAsBoolean();
	            }
	        } else if (jsonAttributeValues.isJsonObject() || jsonAttributeValues.isJsonArray()) {
	        	System.out.println("Its working");
	        	System.out.println("Json Attribute value"+ jsonAttributeValues.toString());
	            return jsonAttributeValues.toString(); 
	        }

        return jsonAttributeValues;  
	}	
}
