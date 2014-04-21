package ltg.ns.ambient.updaters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;
import ltg.ns.comparators.NoteComparator;

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
		// TODO this is not random... We need to sort the notes in chronological order and select the most recent
		
		//no sorted
		Note rn = Lists.newArrayList(notes).get(new Random().nextInt(notes.size()));
		
		return JsonNodeFactory.instance.objectNode()
				.put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", rn.getAuthor())
				.put("note_body", rn.getBodyDescription());
	}

	/**
	 * Return the latest note posted by each one of the groups either in the class or all the classes
	 */
	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			// TODO radom needs to go!
			Note rn = Lists.newArrayList(notes).get(new Random().nextInt(notes.size()));
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", rn.getSchool())
					.put("class", rn.getClassroom())
					.put("group", rn.getAuthor())
					.put("note_body", rn.getBodyDescription());
			grid.add(note);
		}
		return payload;
	}

	@Override
	protected void generateUpdate() {
		/* TODO this might send out two notes. 
		 * What happens if in the last update period two groups updated notes?
		 * If I only send the most recent then one will be lost forever.
		 * So I will have to send more than one update, one per 
		 * 
		 * But if we send out two events, then the single note viz doesn't know what to do...
		 * So we need two updates... that is the truth... 
		 * - One update message for the single with the latest in the refresh cycle
		 * - One update message for the grid with the latest notes from all groups that updated in the latest 
		 * 	 refresh cycle
		 */
		
		//sort the list
		//save the last 9 notes
		
		//full pick the last one
		//grid picke the 9 lasts ones
		
		
		Note rn = Lists.newArrayList(notes).get(new Random().nextInt(notes.size()));
		eh.generateEvent("notes_update", JsonNodeFactory.instance.objectNode()
				.put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", rn.getAuthor())
				.put("note_body", rn.getBodyDescription()));
	}

}
