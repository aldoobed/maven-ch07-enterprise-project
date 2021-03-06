package com.sonatype.mavenbook.weather;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.sonatype.mavenbook.weather.WeatherService;
import org.sonatype.mavenbook.weather.model.Location;
import org.sonatype.mavenbook.weather.model.Weather;
import org.sonatype.mavenbook.weather.persist.LocationDAO;
import org.sonatype.mavenbook.weather.persist.WeatherDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	private WeatherService weatherService;
	private WeatherDAO weatherDAO;
	private LocationDAO locationDAO;
	
	public static void main(String[] args) throws Exception {
		
		// Configure Log4J
		PropertyConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));
		
		// Read the Woeid Code from the Command-line (if none supplied, use 60202)
		String woeid = "116545";
		try {
			woeid = args[0];
		} catch (Exception e) {
		}

		// Read the Operation from the Command-line (if none supplied, use weather)
		String operation = "weather";
		try {
			operation = args[1];
		} catch (Exception e) {
		}

		// Start the program
		Main main = new Main(woeid);

		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { 
				"classpath:applicationContext-weather.xml",
				"classpath:applicationContext-persist.xml" });
		main.weatherService = (WeatherService) context.getBean("weatherService");
		main.locationDAO = (LocationDAO) context.getBean("locationDAO");
		main.weatherDAO = (WeatherDAO) context.getBean("weatherDAO");
		if( operation.equals("weather")) {
			main.getWeather();
		} else {
			main.getHistory();
		}
	}

	private String woeid;

	public Main(String woeid) {
		this.woeid = woeid;
	}

	public void getWeather() throws Exception {
		Weather weather = weatherService.retrieveForecast(woeid);
		weatherDAO.save( weather );
		System.out.println(new WeatherFormatter().formatWeather(weather));
	}

	public void getHistory() throws Exception {
		Location location = locationDAO.findByWoeid(woeid);
		List<Weather> weathers = weatherDAO.recentForLocation(location);
		System.out.println(new WeatherFormatter().formatHistory(location, weathers));
	}
}
