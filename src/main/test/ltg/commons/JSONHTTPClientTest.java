package ltg.commons;

import static org.junit.Assert.assertNotNull;
import ltg.commons.JSONHTTPClient;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.GenericData;

public class JSONHTTPClientTest {

	@Test
	public void testGET() {
		JsonNode result = JSONHTTPClient.GET("http://localhost:9292/test/test");
		assertNotNull(result);
	}
	
	
	@Test
	public void testPOST() {
		GenericData o = new GenericData();
		o.put("just a", "test");
		JsonNode result = JSONHTTPClient.POST("http://localhost:9292/test/test", o);
		assertNotNull(result);
	}

}
