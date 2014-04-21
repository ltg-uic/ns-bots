package ltg.ns.ambient.updaters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;
import ltg.ns.comparators.NoteComparator;

public abstract class AbstractNoteUpdater implements UpdaterInterface {
	
	protected SingleChatLTGEventHandler eh;
	protected String classId;
	protected Set<Note> notes = null;
	protected ArrayList<Note> notesToSort = null;
	
	public AbstractNoteUpdater(SingleChatLTGEventHandler eh, String classId) {
		this.eh = eh;
		this.classId = classId;
	}


	@SuppressWarnings("unchecked")
	@Override
	public synchronized void update(Observable o, Object arg) {
		this.notes = (Set<Note>) arg;
		// Filter
		if (!classId.equals("all"))
			notes = new HashSet<>(Collections2.filter(ImmutableSet.copyOf(notes), new Predicate<Note>() {
				@Override
				public boolean apply(Note note) {
					return note.getClassroom().equals(classId);
				}
			}));
		//select last 9 notes and store
			notesToSort = Lists.newArrayList(notes);
			Collections.sort(notesToSort, new NoteComparator());
			generateUpdate();
	}
	
	protected abstract void generateUpdate();
}
