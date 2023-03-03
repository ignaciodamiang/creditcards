package com.nacho.creditcards;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class propertiesTest {

	@Test
	public void testLoadProperties() {
	    // Try to load the application.properties file
	    try {
            Resource resource = new ClassPathResource("application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            // Verify that the properties are not null
            assertNotNull(props, "Properties should not be null");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading application.properties file", ex);
        }
	}
}
