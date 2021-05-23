package it.edu.faraday.campus_biomedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class CampusBiomedicoApplication {

	public static final String encoding = StandardCharsets.UTF_8.toString();

	public static void main(String[] args) {
		SpringApplication.run(CampusBiomedicoApplication.class, args);
	}

	public static String encode(String string) {
		String s = "";
		try {
			return URLEncoder.encode(string, encoding);
		} catch(UnsupportedEncodingException ignored) {
		}
		return s;
	}

}
