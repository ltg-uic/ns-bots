package ltg.ns.ambient.updaters;

import java.util.Observer;

import ltg.commons.ltg_event_handler.LTGEvent;

import com.fasterxml.jackson.databind.JsonNode;

public interface UpdaterInterface extends Observer {
	
	public JsonNode fullInit(LTGEvent e);
	public JsonNode gridInit(LTGEvent e);

}
