package ltg.ns.ambient.updaters;

import java.util.List;
import java.util.Observable;
import java.util.Random;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class ScoreboardUpdater extends AbstractUpdater {
	
	private ImmutableMap<String, Integer> global_scoreboard;
	private ImmutableList<ImmutableMap<String, Integer>> group_scoreboard;

	public ScoreboardUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		global_scoreboard = buildGroupScoreboard();
		List<ImmutableMap<String, Integer>> scores = Lists.newArrayList();
		for (int i=0; i<9; i++)
			scores.add(buildGroupScoreboard());
		group_scoreboard = ImmutableList.copyOf(scores);
	}

	@Override
	// Returns the tags scoreboard for all the classes
	public synchronized JsonNode fullInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode()
				.put("school", "ics")
				.put("class", "ben");
		ArrayNode scoreboard = payload.putArray("tag_scoreboard");
		for (String s: global_scoreboard.keySet()) {
			scoreboard.add(JsonNodeFactory.instance.objectNode().put(s, global_scoreboard.get(s)));
		}
		return payload;
	}

	@Override
	// Returns 9 scoreboards of 9 groups among all the groups in the run
	public synchronized JsonNode gridInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			ObjectNode node = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", "ben")
					.put("group", "group-"+(i+1));
			ArrayNode scoreboard = node.putArray("tag_scoreboard");
			for (String s: group_scoreboard.get(i).keySet()) {
				scoreboard.add(JsonNodeFactory.instance.objectNode().put(s, global_scoreboard.get(s)));
			}
			grid.add(node);
		}		
		return payload;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub
		
	}
	
	
	static public ImmutableMap<String, Integer> buildGroupScoreboard() {
		Random r = new Random();
		return ImmutableMap.of("squirrel", r.nextInt(35), "raccoon", r.nextInt(6), "bird", r.nextInt(13), "deer", r.nextInt(2), "leves", r.nextInt(19));
	}

}
