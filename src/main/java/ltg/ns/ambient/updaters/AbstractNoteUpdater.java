package ltg.ns.ambient.updaters;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

public abstract class AbstractNoteUpdater implements UpdaterInterface {
	
	protected SingleChatLTGEventHandler eh;
	protected String classId;
	protected Set<Note> notes = null;
	
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
			generateUpdate();
	}
	
	protected abstract void generateUpdate();
}
