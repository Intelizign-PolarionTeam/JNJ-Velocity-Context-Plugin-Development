package com.jnj.configuration.service;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		
			return processJsonElement(jsonAttributeValues);  
	}	
	 /**
	  * Processes a given JsonElement and returns an appropriate value based on its type.
	  * It checks if the element is a JsonArray, JsonObject, or a primitive value and processes accordingly.
	  *
	  * @param element The JsonElement to process.
	  * @return The processed value based on the type of the JsonElement.
	  */
	 private static Object processJsonElement(JsonElement element) {
	     if (element.isJsonArray()) {
	         return iterateJsonArray(element.getAsJsonArray());
	     }
	     else if (element.isJsonObject()) {
	         return iterateJsonObject(element.getAsJsonObject());
	     }
	     else if (element.isJsonPrimitive()) {
	         return getPrimitiveValue(element);
	     }
	     return null;
	 }

	 /**
	  * Iterates over a JsonArray and processes each element by calling processJsonElement() method.
	  *
	  * @param jsonArray The JsonArray to iterate over.
	  * @return A list of processed values corresponding to each element in the JsonArray.
	  */
	 private static List<Object> iterateJsonArray(JsonArray jsonArray) {
	     List<Object> processedList = new ArrayList<>();
	     for (JsonElement jsonElement : jsonArray) {
	         processedList.add(processJsonElement(jsonElement));
	     }
	     System.out.println("Processed List" + processedList + "\n");
	     return processedList;
	 }

	 /**
	  * Iterates over a JsonObject and processes each entry by calling processJsonElement() method.
	  *
	  * @param jsonObject The JsonObject to iterate over.
	  * @return A map with keys as the field names and values as the processed values of each field.
	  */
	 private static Map<String, Object> iterateJsonObject(JsonObject jsonObject) {
	     Map<String, Object> processedMap = new LinkedHashMap<>();
	     for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
	         processedMap.put(entry.getKey(), processJsonElement(entry.getValue()));
	     }
	     System.out.println("Processed Map" + processedMap + "\n");
	     return processedMap;
	 }

	 /**
	  * Extracts the primitive value from a JsonElement, handling String, Number, and Boolean types.
	  *
	  * @param element The JsonElement to extract the primitive value from.
	  * @return The primitive value as an Object (String, Number, or Boolean), or null if not supported.
	  */
	 private static Object getPrimitiveValue(JsonElement element) {
	     JsonPrimitive primitive = element.getAsJsonPrimitive();
	     if (primitive.isString()) {
	         return primitive.getAsString();
	     }
	     else if (primitive.isNumber()) {
	         return primitive.getAsNumber();
	     }
	     else if (primitive.isBoolean()) {
	         return primitive.getAsBoolean();
	     }
	     return null;
	 }

}