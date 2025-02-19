package com.jnj.configuration.impl;



import java.util.List;

import com.jnj.configuration.IJNJProjectConfiguration;
import com.jnj.configuration.model.Person;
import com.jnj.configuration.service.JsonReader;

public class JNJProjectConfiguration implements IJNJProjectConfiguration {
	
	private final JsonReader readerService;

    public JNJProjectConfiguration() {
        this.readerService = new JsonReader();
    }

	@Override
	public List<Person> getPersonList() {
		List<Person> persons = readerService.readJsonFile();
        return persons;
	}
}
