package ltg.ns.ambient.updaters;

import java.util.Collection;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class NotesUpdater extends AbstractUpdater {
	
	private String class_id;
	private Collection<Note> class_notes;
	private Random r = new Random();
	
	
	public NotesUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}

	public NotesUpdater(SingleChatLTGEventHandler eg, String class_id) {
		this(eg);
		this.class_id = class_id;
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		Set<Note> notes = (Set<Note>) arg;
		class_notes = Collections2.filter(ImmutableSet.copyOf(notes), new Predicate<Note>() {
			@Override
			public boolean apply(Note note) {
				return note.getClassroom().equals(class_id);
			}
		});
		generateUpdate();
	}

	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		Note rn = Lists.newArrayList(class_notes).get(r.nextInt(class_notes.size()));
		return JsonNodeFactory.instance.objectNode().put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", rn.getAuthor())
				.put("note_body", rn.getBody());
	}

	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected synchronized void generateUpdate() {
		// TODO generate upadate for _full 
		// TODO generate upadate for _grid
		//eventGenerator.generateEvent("notes_update", JsonNodeFactory.instance.objectNode());
	}

}
