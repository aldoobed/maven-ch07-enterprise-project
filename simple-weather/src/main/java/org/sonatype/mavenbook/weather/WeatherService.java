package org.sonatype.mavenbook.weather;

import java.io.InputStream;

public class WeatherService {

	public WeatherService() {
	}

	public String retrieveForecast(String woid) throws Exception {
		// Retrieve Data
		InputStream dataIn = new YahooRetriever().retrieve(woid);
		// Parse Data
		Weather weather = new YahooParser().parse(dataIn);
		// Format (Print) Data
		return new WeatherFormatter().format(weather);

	}
}