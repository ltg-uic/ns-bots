package ltg.ns.ambient.updaters;

import java.util.Random;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;

public class ScoreboardUpdater extends AbstractNoteUpdater {
	
		private ImmutableListMultimap<String, Integer> global_scoreboard;
	private ImmutableList<ImmutableListMultimap<String, Integer>> group_scoreboard;
	
	
	public ScoreboardUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}

	@Override
	// Returns the tags scoreboard for all the classes
	public synchronized JsonNode fullInit() {
		ObjectNode payload = JsonNodeFactory.instance.objectNode()
				.put("school", "ics")
				.put("class", "ben")
				.put("group", "");
		ArrayNode scoreboard = payload.putArray("tag_scoreboard");
		for (String s: global_scoreboard.keySet()) {
			ObjectNode line = JsonNodeFactory.instance.objectNode();
			ArrayNode scores = line.putArray(s);
			for (Integer i: global_scoreboard.get(s))
				scores.add(i);
			scoreboard.add(line);
		}
		return payload;
	}

	@Override
	// Returns 9 scoreboards of 9 groups among all the groups in the run
	public synchronized JsonNode gridInit() {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			ObjectNode node = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", "ben")
					.put("group", "group-"+(i+1));
			ArrayNode scoreboard = node.putArray("tag_scoreboard");
			for (String s: group_scoreboard.get(i).keySet()) {
				ObjectNode line = JsonNodeFactory.instance.objectNode();
				ArrayNode scores = line.putArray(s);
				for (Integer sc: global_scoreboard.get(s))
					scores.add(sc);
				scoreboard.add(line);
			}
			grid.add(node);
		}		
		return payload;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub
		
	}
	
	
	static public ImmutableListMultimap<String, Integer> buildGroupScoreboard() {
		Random r = new Random();
		return new ImmutableListMultimap.Builder<String, Integer>()
				.putAll("squirrel", r.nextInt(15), r.nextInt(25), r.nextInt(35))
				.putAll("raccoon", r.nextInt(6), r.nextInt(16), r.nextInt(26))
				.putAll("bird", r.nextInt(13), r.nextInt(23), r.nextInt(33))
				.putAll("deer", r.nextInt(2), r.nextInt(12), r.nextInt(22))
				.build();
	}

}
