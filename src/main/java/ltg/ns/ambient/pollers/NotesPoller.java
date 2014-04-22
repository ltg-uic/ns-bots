package ltg.ns.ambient.pollers;

import java.util.HashSet;
import java.util.Set;

import ltg.commons.JSONHTTPClient;
import ltg.ns.ambient.model.Classrooms;
import ltg.ns.ambient.model.Note;
import ltg.ns.ambient.model.Note.Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;

public class NotesPoller extends AbstractPoller {

	private ImmutableSet<Note> allNotes = ImmutableSet.of();


	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Fetch all new notes
			Set<Note> allNewNotes = new HashSet<>();
			for (Classrooms c: Classrooms.values()) {
				allNewNotes.addAll(parseNotes(c, JSONHTTPClient.GET(c.getNotesURL())));
			}
			if (allNotes.size() != allNewNotes.size()) {
				allNotes = ImmutableSet.copyOf(allNewNotes);
				// Notify observers
				this.setChanged();
				this.notifyObservers(allNotes);
			}
			// Sleep...
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				return;
			}
		}
	}


	private ImmutableSet<Note> parseNotes(Classrooms url, JsonNode get) {
		Set<Note> notes = new HashSet<>();
		for (JsonNode o: get)
			if (o.get("published")!=null && o.get("published").asBoolean())
				notes.add(parseNote(url, o));
		return ImmutableSet.copyOf(notes);
	}


	private Note parseNote(Classrooms url, JsonNode o) {
		// Parse content common to all notes
		Note n  = Note.buildNoteWithId(o.get("_id").get("$oid").textValue())
				.school(url.getSchool())
				.classroom(url.getClassroom())
				.author(o.get("author").textValue())
				.createdAt(o.get("created_at").get("$date").textValue());
		// Parse type
		Note.Type type = null;
		switch (o.get("type").textValue()) {
		case "planning":
			type = Type.planning;
			break;
		case "photo_set":
			type = Type.photo_set;
			break;
		case "open":
			type = Type.open;
			break;
		case "cross_cutting":
			type = Type.cross_cutting;
			break;
		}
		// Parse body based on type
		String b_title = o.get("body").get("title")!=null ? o.get("body").get("title").textValue() : null;
		String b_description = o.get("body").get("question")!=null ? o.get("body").get("question").textValue() : null;
		String b_question = o.get("body").get("hypothesis")!=null ? o.get("body").get("hypothesis").textValue() : null;
		String b_hypothesis = o.get("body").get("explanation")!=null ? o.get("body").get("explanation").textValue() : null;
		String b_explanation = o.get("body").get("description")!=null ? o.get("body").get("description").textValue() : null;
		String b_evidence = o.get("body").get("evidence")!=null ? o.get("body").get("evidence").textValue() : null;
		switch (type) {
		case cross_cutting:
			n.withCrussCuttingBody(b_title, b_description, b_explanation);
			break;
		case open:
			n.withOpenBody(b_title, b_description);
			break;
		case photo_set:
			n.withPhotoSetBody(b_title, b_description, b_question, b_explanation);
			break;
		case planning:
			n.withPlanningBody(b_title, b_description, b_hypothesis, b_explanation, b_evidence);
			break;
		default:
			new RuntimeException("Unknown note type");
		}
		return n;
	}
}
