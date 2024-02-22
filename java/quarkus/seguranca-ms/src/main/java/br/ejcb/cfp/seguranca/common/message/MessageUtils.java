package br.ejcb.cfp.seguranca.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageUtils {

	private static final String MESSAGE_FILE = "messages.properties";
    private Properties properties;

    public MessageUtils() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(MESSAGE_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
    	String value = properties.getProperty(key);
        return value != null ? value : key;
    }

}
