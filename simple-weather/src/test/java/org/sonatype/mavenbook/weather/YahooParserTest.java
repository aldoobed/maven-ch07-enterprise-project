package org.sonatype.mavenbook.weather;

import java.io.InputStream;

import org.sonatype.mavenbook.weather.model.Weather;

import junit.framework.TestCase;

public class YahooParserTest extends TestCase {

	public YahooParserTest(String name) {
		super(name);
	}
	
	public void testParser() throws Exception {
		InputStream nyData = 
			getClass().getClassLoader().getResourceAsStream("ny-weather.xml");
		Weather weather = new YahooParser().parse( "10002", nyData );
		assertEquals( "New York", weather.getLocation().getCity() );
		assertEquals( "NY", weather.getLocation().getRegion() );
		assertEquals( "US", weather.getLocation().getCountry() );
		assertEquals( "39", weather.getCondition().getTemp() );
		assertEquals( "Fair", weather.getCondition().getText() );
		assertEquals( "39", weather.getWind().getChill() );
		assertEquals( "67", weather.getAtmosphere().getHumidity() );
	}
}
