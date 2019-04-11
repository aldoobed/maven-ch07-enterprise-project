package org.sonatype.mavenbook.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

public class YahooRetriever {
	private static Logger log = Logger.getLogger(YahooRetriever.class);

	public InputStream retrieve(String woeid) throws Exception {
		log.info("Retrieving Weather Data");
		return getYahooUrl(woeid);
	}
	
	private InputStream getYahooUrl(String woeid) throws ClientProtocolException, IOException {
		
		final String appId = "TJP1CC4i";
        final String consumerKey = "dj0yJmk9ZFgzanRCSlB1d2NKJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTBj";
        final String consumerSecret = "937145041237b84afbb4b82014d4a3742ab60cab";
        final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
        final String format = "xml";

        long timestamp = new Date().getTime() / 1000;
        byte[] nonce = new byte[32];
        Random rand = new Random();
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        parameters.add("woeid=" + woeid);
        parameters.add("u=c");
        // Make sure value is encoded
//        parameters.add("location=" + URLEncoder.encode("sunnyvale,ca", "UTF-8"));
        parameters.add("format="+format);
        Collections.sort(parameters);

        StringBuffer parametersList = new StringBuffer();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append(((i > 0) ? "&" : "") + parameters.get(i));
        }

        log.debug(parametersList);
        String signatureString = "GET&" +
            URLEncoder.encode(url, "UTF-8") + "&" +
            URLEncoder.encode(parametersList.toString(), "UTF-8");
        log.debug(signatureString);
        String signature = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            Base64 encoder = new Base64();
            signature = encoder.encodeToString(rawHMAC);
            log.debug(signature);
        } catch (Exception e) {
            log.error("Unable to append signature");
            System.exit(0);
        }

        String authorizationLine = "OAuth " +
            "oauth_consumer_key=\"" + consumerKey + "\", " +
            "oauth_nonce=\"" + oauthNonce + "\", " +
            "oauth_timestamp=\"" + timestamp + "\", " +
            "oauth_signature_method=\"HMAC-SHA1\", " +
            "oauth_signature=\"" + signature + "\", " +
            "oauth_version=\"1.0\"";
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
//        URI uri = URI.create(url+"?location=sunnyvale,ca&format="+format); 
        URI uri = URI.create(url+"?woeid="+woeid+"&format="+format+"&u=c"); 
		HttpGet request = new HttpGet(uri);
		request.addHeader("Authorization", authorizationLine);
		request.addHeader("X-Yahoo-App-Id", appId);
		request.addHeader("Content-Type", "application/"+format);

		log.debug("REQUEST:"+request.toString());
		log.debug("URI:"+request.getURI());
		log.debug("REQ_LINE:"+request.getRequestLine());
		log.debug("ALLHEADERS:"+request.getAllHeaders().toString());
		
        HttpResponse response = client.execute(request);
        
        log.debug("RESPONSE:"+response.getEntity().toString());
        
        return response.getEntity().getContent();
        
	}
	
	
}