package ltg.ns.ambient.pollers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ltg.commons.JSONHTTPClient;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Builds a wordle per group off of the notes they wrote.
 * Updates are simply the updated wordle. 
 */
public class NotesPoller extends Poller implements Runnable {

	private Set <Note> notes = Collections.synchronizedSet(new HashSet<Note>());


	@Override
	public void run() {
		while (!Thread.interrupted()) {
			Set<Note> notes = new HashSet<>();
			// Fetch all notes
			for (NotesURLs_Enum url: NotesURLs_Enum.values()) {
				notes.addAll(parseNotes(JSONHTTPClient.GET(url.getURL())));
			}
			// Check if anything new happened, update notes and notify observers
			if (!newAndOldNotesAreTheSame(notes)) {
				updateNotes(notes);
				// Notify observers
				this.setChanged();
				this.notifyObservers(notes);
			}
			// Sleep...
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				return;
			}
		}
	}


	private Set<Note> parseNotes(JsonNode get) {
		Set<Note> notes = new HashSet<>();
		for (JsonNode o: get)
			notes.add( new Note( o.get("_id").get("$oid").textValue(), 
					o.get("author").textValue(), 
					o.get("body").textValue(), 
					o.get("created_at").get("$date").textValue()));
		return notes;
	}


	private boolean newAndOldNotesAreTheSame(Set<Note> new_notes) {
		return notes.containsAll(new_notes);
	}


	private void updateNotes(Set<Note> new_notes) {
		notes.addAll(new_notes);
	}


}
