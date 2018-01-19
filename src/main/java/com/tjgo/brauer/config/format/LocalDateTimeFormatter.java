package com.tjgo.brauer.config.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeFormatter extends TemporalFormatter<LocalDateTime> {
	
	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		return env.getProperty("localdate.format-" + locale, "dd/MM/YYYY HH:mm");
	}

	@Override
	public LocalDateTime parse(String text, DateTimeFormatter formatador){
		return LocalDateTime.parse(text, formatador);
	}

}
