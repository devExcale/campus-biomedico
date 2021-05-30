package it.edu.faraday.campus_biomedico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class CampusBiomedicoApplication {

	public static final DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String encoding;
	private static final Logger logger;

	static {
		encoding = StandardCharsets.UTF_8.toString();
		logger = LoggerFactory.getLogger(CampusBiomedicoApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CampusBiomedicoApplication.class, args);
	}

	public static String encode(String string) {
		String s = "";
		try {
			s = URLEncoder.encode(string, encoding);
		} catch(UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	public static String getEncoding() {
		return encoding;
	}

	public static Logger getLogger() {
		return logger;
	}

}
