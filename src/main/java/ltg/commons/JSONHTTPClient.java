package ltg.commons;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

public class JSONHTTPClient {


	public static JsonNode GET(String url) {
		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
		JsonNode result = null;
		try {
			HttpResponse res = requestFactory.buildGetRequest(new GenericUrl(url)).execute();
			if (!res.isSuccessStatusCode())
				throw new IOException("Response code is now 200 OK");
			String res_body = res.parseAsString();
			if (res_body==null)
				throw new IOException("Response body is null");
			result = new ObjectMapper().readTree(res_body);
			res.disconnect();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return result;
	}



	public static JsonNode POST(String url, GenericData data) {
		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
		JsonNode result = null;
		try {		
			HttpResponse res = requestFactory.buildPostRequest(new GenericUrl(url), new JsonHttpContent(new JacksonFactory(), data)).execute();
			if (!res.isSuccessStatusCode())
				throw new IOException("Response code is now 200 OK");
			String res_body = res.parseAsString();
			if (res_body==null)
				throw new IOException("Response body is null");
			result = new ObjectMapper().readTree(res_body);
			res.disconnect();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return result;
	}

}
