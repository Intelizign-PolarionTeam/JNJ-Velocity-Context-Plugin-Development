package com.jnj.configuration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnj.configuration.model.Person;

import java.io.*;
import java.util.List;


public class JsonReader {

	public List<Person> readJsonFile() {
		String filePath = "C:\\Users\\T82218\\Downloads\\com.jnj.configuration\\com.jnj.configuration\\src\\sample.json";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
		
			Person response = objectMapper.readValue(new File(filePath), Person.class);

			System.out.println("Status: " + response.getStatus());
			System.out.println("Message: " + response.getMessage());
			System.out.println("Persons:");

			List<Person> persons = response.getData();
			for (Person person : persons) {
				System.out.println(person);
			}
		  return persons;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
