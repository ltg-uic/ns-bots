package ltg.ns.ambient.updaters;

import java.util.List;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class NotesUpdater extends AbstractNoteUpdater {

	public NotesUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}

	/**
	 * Returns the latest note posted either by the class or all the classes
	 */
	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		Note last_note = sortedNotes.get(0);
		return JsonNodeFactory.instance.objectNode()
				.put("school", last_note.getSchool())
				.put("class", last_note.getClassroom())
				.put("group", last_note.getAuthor())
				.put("note_body", last_note.getBodyDescription());
	}

	/**
	 * Return the latest note posted by each one of the groups either in the class or all the classes
	 */
	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {		
		return gridPayloadBuilder(false);
	}


	private synchronized JsonNode gridPayloadBuilder(Boolean func) {
		// Select new entries in grid
		List<Note> newEntriesInGrid = Lists.newArrayList();
		for (Note n: sortedNotes) {
			if (oldSortedNotes!=null && !oldSortedNotes.isEmpty() && n.equals(oldSortedNotes.get(0)))
				break;
			newEntriesInGrid.add(n);
		}
		// Calculate grid size 
		int grid_size = Math.min(sortedNotes.size(), 9);
		// Assemble object
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<grid_size; i++) {
			Note note = sortedNotes.get(i);
			ObjectNode noteData = JsonNodeFactory.instance.objectNode()
					.put("school", note.getSchool())
					.put("class", note.getClassroom())
					.put("group", note.getAuthor())
					.put("note_body", note.getBodyDescription());
			if (func && newEntriesInGrid.contains(note)) 
				noteData.put("updated", true);
			grid.add(noteData);
		}
		return payload;
	}


	protected synchronized void fullUpdate(){
		eh.generateEvent("notes_full_update", fullInit(null));
	}


	protected synchronized void gridUpdate() {
		eh.generateEvent("notes_grid_update", gridPayloadBuilder(true));
	}
}
