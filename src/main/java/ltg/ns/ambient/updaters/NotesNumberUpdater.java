package ltg.ns.ambient.updaters;

import java.util.Collection;
import java.util.Random;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class NotesNumberUpdater extends AbstractNoteUpdater {
	
		private String class_id;
	private Collection<Note> class_notes;
	private Random r = new Random();
	private ImmutableMap<String, Integer> group_notes_counts;

	
	public NotesNumberUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}


	@Override
	// Displays the overall amount of notes posted for this class
	public synchronized JsonNode fullInit() {
		Note rn = Lists.newArrayList(class_notes).get(r.nextInt(class_notes.size()));
		return JsonNodeFactory.instance.objectNode()
				.put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", "")
				.put("note_number", class_notes.size());
	}


	@Override
	// Displays the overall amount of notes posted by 9 groups in this class
	public synchronized JsonNode gridInit() {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid");
		ImmutableList<String> keys = ImmutableList.copyOf(group_notes_counts.keySet());
		for (int i=0; i<9; i++) {
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", "ics")
					.put("class", class_id)
					.put("group", keys.get(i))
					.put("note_number", group_notes_counts.get(keys.get(i)));
			grid.add(note);
		}
		return payload;
	}


	@Override
	protected void generateUpdate() {
		eh.generateEvent("#_notes_update", JsonNodeFactory.instance.objectNode());
	}

}
