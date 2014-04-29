package ltg.ns.ambient.updaters;

import java.util.HashMap;
import java.util.Random;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Classrooms;
import ltg.ns.ambient.model.Info;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class WordleUpdater extends AbstractNoteUpdater {
	
	public WordleUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}
	
	
	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		String wordle_text = "";
		for(Note n: sortedNotes){
			wordle_text = wordle_text + n.getBodyDescription();
		}	
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		if(classId.equals("all")) payload.put("type", "all");
		else payload.put("type", classId);
		
		payload.put("school", Classrooms.getSchooForClass(classId))
				.put("class", classId)
				.put("wordle_text", wordle_text);
		return payload;
	}

	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		return gridPayloadBuilder(false);
	}
	
	private synchronized JsonNode gridPayloadBuilder(Boolean func) {
		HashMap<String, Info> map_wordles = Maps.newHashMap();
		for(Note n: sortedNotes){
			if(map_wordles.containsKey(n.getAuthor())){
				Info info = map_wordles.get(n.getAuthor());
				String wordle_text = info.getWordle();
				info.setWordle(wordle_text+n.getBodyDescription());
				map_wordles.put(n.getAuthor(), info);
			}
			else {
				Info info = new Info(n.getAuthor(), n.getClassroom(), n.getSchool(), 0, n.getBodyDescription());				
				map_wordles.put(n.getAuthor(), info);
			}
		}
		
		Random r = new Random();
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		
		if(classId.equals("all")) payload.put("type", "all");
		else payload.put("type", classId);
		
		ArrayNode grid = payload.putArray("grid");
		ImmutableList<String> keys = ImmutableList.copyOf(map_wordles.keySet());
		int grid_size = Math.min(map_wordles.size(), 9);

		for (int i=0; i<grid_size; i++) {
			int rand = r.nextInt(map_wordles.size());
			ObjectNode wordle = JsonNodeFactory.instance.objectNode()
					.put("school", map_wordles.get(keys.get(rand)).getSchool())
					.put("class",  map_wordles.get(keys.get(rand)).getClassroom())
					.put("group", keys.get(rand))
					.put("wordle_text", map_wordles.get(keys.get(rand)).getWordle());
			if (func)
				wordle.put("updated", true);
			grid.add(wordle);
		}
		return payload;
	}

	@Override
	protected synchronized void fullUpdate() {
		eh.generateEvent("wordle_full_update", fullInit(null));
		
	}

	@Override
	protected synchronized void gridUpdate() {
		eh.generateEvent("wordle_grid_update", gridPayloadBuilder(true));		
	}

}
