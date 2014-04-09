package ltg.ns.ambient.updaters;

import java.util.Observable;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BurstUpdater extends AbstractUpdater {

	public BurstUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized JsonNode fullInit() {
		ObjectNode payload = JsonNodeFactory.instance.objectNode()
				.put("school", "ics")
				.put("class", "ben")
				.put("time", "") //burst
				.put("group", "cicciopasticcio");
		ArrayNode burst = payload.putArray("burst");
		burst.add("http://pikachu.badger.encorelab.org/1bfz298wrnk.jpg");
		burst.add("http://pikachu.badger.encorelab.org/1jk3e5tut4w.jpg");
		burst.add("http://pikachu.badger.encorelab.org/o7shv2i0ao.jpg");
		return payload;
	}

	@Override
	public synchronized JsonNode gridInit() {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			ObjectNode node = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", "ben")
					.put("time", "") //burst
					.put("group", "cicciopasticcio");
			ArrayNode burst = node.putArray("burst");
			burst.add("http://pikachu.badger.encorelab.org/1bfz298wrnk.jpg");
			burst.add("http://pikachu.badger.encorelab.org/1jk3e5tut4w.jpg");
			burst.add("http://pikachu.badger.encorelab.org/o7shv2i0ao.jpg");
			grid.add(node);
		}		
		return payload;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub

	}


	

}
