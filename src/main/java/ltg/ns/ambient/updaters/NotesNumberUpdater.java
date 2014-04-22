package ltg.ns.ambient.updaters;
import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Classrooms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;

public class NotesNumberUpdater extends AbstractNoteUpdater {


	public NotesNumberUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}
	
	/**
	 * Displays the overall amount of notes posted by this/all classes since the beginning of the unit
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
	 */
	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid");
		ImmutableList<String> keys = ImmutableList.copyOf(group_notes_counts.keySet());
		int grid_size = Math.min(group_notes_counts.size(), 9);
		for (int i=0; i<grid_size; i++) {
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", classId)
					.put("group", keys.get(i))
					.put("#_notes", group_notes_counts.get(keys.get(i)));
			grid.add(note);
		}
		return payload;
	}


	/**
	 * Send the updated counts for the whole class 
	 */
	protected synchronized void fullUpdate(){
		eh.generateEvent("#_notes_full_update", fullInit(null));
	}

	/**
	 * Send the updated counts for each group in the class
	 */
	protected synchronized void gridUpdate() {
		eh.generateEvent("#_notes_grid_update", gridInit(null));
	}
}

