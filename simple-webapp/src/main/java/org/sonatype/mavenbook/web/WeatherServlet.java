package org.sonatype.mavenbook.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sonatype.mavenbook.weather.WeatherService;

public class WeatherServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String woeid = request.getParameter("woeid");
		WeatherService weatherService = new WeatherService();
		PrintWriter out = response.getWriter();
		try {
			out.println(weatherService.retrieveForecast(woeid));
		} catch (Exception e) {
			out.println("Error Retrieving Forecast: " + e.getMessage());
		}
		out.flush();
		out.close();
	}
}