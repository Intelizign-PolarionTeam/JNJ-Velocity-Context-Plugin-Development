package com.jnj.configuration.impl;

import com.jnj.configuration.IJNJProjectConfiguration;
import com.jnj.configuration.service.JsonReaderService;



public class JNJProjectConfiguration implements IJNJProjectConfiguration {
	
	private JsonReaderService jsonReaderService;
	
	public JNJProjectConfiguration() {
		this.jsonReaderService = new JsonReaderService();
	}

	@Override
	public Object getJsonData(String fileName, String key) {
		Object jsonValue = jsonReaderService.getJsonValues(fileName, key);
		return jsonValue;
	}



 

    

    
}