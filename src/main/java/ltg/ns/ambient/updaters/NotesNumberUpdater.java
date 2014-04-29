package ltg.ns.ambient.updaters;
import java.util.Random;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Classrooms;
import ltg.ns.ambient.model.Note;

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
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		if(classId.equals("all")) payload.put("type", "all");
		else payload.put("type", classId);
		
		payload.put("school", Classrooms.getSchooForClass(classId))
		.put("class", classId)
		.put("#_note", notes.size());
		return payload;
	}

	/**
	 * Displays the overall amount of notes posted by all the groups in this/all classes since the beginning of the unit
	 */
	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		return gridPayloadBuilder(false);
	}


	private synchronized JsonNode gridPayloadBuilder(Boolean func) {
		Random r = new Random();
		ObjectNode payload = JsonNodeFactory.instance.objectNode();

		if(classId.equals("all")) payload.put("type", "all");
		else payload.put("type", classId);

		ArrayNode grid = payload.putArray("grid");
		ImmutableList<String> keys = ImmutableList.copyOf(groupNotesCounts.keySet());

		int grid_size = Math.min(groupNotesCounts.size(), 9);
		for (int i=0; i<grid_size; i++) {
			int rand = r.nextInt(groupNotesCounts.size());
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", groupNotesCounts.get(keys.get(rand)).getSchool())
					.put("class", groupNotesCounts.get(keys.get(rand)).getClassroom())
					.put("group", keys.get(rand))
					.put("#_notes", groupNotesCounts.get(keys.get(rand)).getCount()); //?
			if (func)
				note.put("updated", true);
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
		eh.generateEvent("#_notes_grid_update", gridPayloadBuilder(true));
	}
}

