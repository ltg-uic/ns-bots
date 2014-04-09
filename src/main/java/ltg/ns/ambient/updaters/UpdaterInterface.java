package ltg.ns.ambient.updaters;

import java.util.Observer;

import com.fasterxml.jackson.databind.JsonNode;

public interface UpdaterInterface extends Observer {
	
	public JsonNode fullInit();
	public JsonNode gridInit();

}
