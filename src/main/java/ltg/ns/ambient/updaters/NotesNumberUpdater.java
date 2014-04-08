package ltg.ns.ambient.updaters;

import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class NotesNumberUpdater extends AbstractUpdater {
	
	private String class_id;
	private Collection<Note> class_notes;
	private Random r = new Random();
	private ImmutableMap<String, Integer> group_notes_counts;

	public NotesNumberUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}


	public NotesNumberUpdater(SingleChatLTGEventHandler eg, String class_id) {
		this(eg);
		this.class_id = class_id;
	}


	@Override
	
	public synchronized void update(Observable o, Object arg) {
		//class_notes = filterThisClassNotes(arg);
		class_notes = Note.generateStubNotesForIcsBenClassroom();
		Map<String, Integer> gnc = Maps.newHashMap();
		for (Note n: class_notes)
			gnc.put(n.getAuthor(),gnc.get(n.getAuthor())==null ? 1 : gnc.get(n.getAuthor()) + 1);  
		group_notes_counts = ImmutableMap.copyOf(gnc);
	}


	@Override
	// Displays the overall amount of notes posted for this class
	public synchronized JsonNode fullInit(LTGEvent e) {
		Note rn = Lists.newArrayList(class_notes).get(r.nextInt(class_notes.size()));
		return JsonNodeFactory.instance.objectNode()
				.put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", "")
				.put("note_number", class_notes.size());
	}


	@Override
	// Displays the overall amount of notes posted by 9 groups in this class
	public synchronized JsonNode gridInit(LTGEvent e) {
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
	
	
	private Collection<Note> filterThisClassNotes(Object arg) {
		@SuppressWarnings("unchecked")
		Set<Note> notes = (Set<Note>) arg;
		return Collections2.filter(ImmutableSet.copyOf(notes), new Predicate<Note>() {
			@Override
			public boolean apply(Note note) {
				return note.getClassroom().equals(class_id);
			}
		});
	}

}
