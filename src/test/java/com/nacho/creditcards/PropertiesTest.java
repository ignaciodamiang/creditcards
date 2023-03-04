package com.nacho.creditcards;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesTest {

    @Test
    public void testLoadProperties() throws IOException {
        // Load the application.properties file
        Resource resource = new ClassPathResource("application.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        // Verify that the properties are not null
        assertNotNull(props, "Properties should not be null");
    }
}
