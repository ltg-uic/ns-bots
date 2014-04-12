package ltg.ns.ambient.updaters;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Classrooms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class NotesNumberUpdater extends AbstractNoteUpdater {
	
	private ImmutableMap<String, Integer> group_notes_counts;

	public NotesNumberUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}


	/**
	 * Displays the overall amount of notes posted by this/all classes since the beginning of the unit
	 * OR is it the notes for a random group? not interesting...
	 * TODO define the semantics of each of the channels
	 */
	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		return JsonNodeFactory.instance.objectNode()
				.put("school", Classrooms.getSchooForClass(classId))
				.put("class", classId)
				.put("#_note", notes.size());
	}


	/**
	 * Displays the overall amount of notes posted by all the groups in this/all classes since the beginning of the unit
	 * TODO need to put groups in there! 
	 */
	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid");
		ImmutableList<String> keys = ImmutableList.copyOf(group_notes_counts.keySet());
		for (int i=0; i<9; i++) {
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", classId)
					.put("group", keys.get(i))
					.put("#_note", group_notes_counts.get(keys.get(i)));
			grid.add(note);
		}
		return payload;
	}

	/**
	 * Just send the updated counts for a random group 
	 * AND the whole class
	 */
	@Override
	protected void generateUpdate() {
		eh.generateEvent("#_notes_update", JsonNodeFactory.instance.objectNode());
	}

}
