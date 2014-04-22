package ltg.ns.ambient.updaters;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;
import ltg.ns.comparators.NoteComparator;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class AbstractNoteUpdater implements UpdaterInterface {

	protected SingleChatLTGEventHandler eh;
	protected String classId;
	protected ImmutableSet<Note> notes = null;
	protected ImmutableSet<Note> oldNotes = null;
	protected ImmutableList<Note> sortedNotes = null;
	protected ImmutableList<Note> oldSortedNotes = null;
	protected ImmutableMap<String, Integer> group_notes_counts = null;


	public AbstractNoteUpdater(SingleChatLTGEventHandler eh, String classId) {
		this.eh = eh;
		this.classId = classId;
	}


	@SuppressWarnings("unchecked")
	@Override
	public synchronized void update(Observable o, Object arg) {
		// Filter
		if (!classId.equals("all")) {
			oldNotes = notes;
			notes = ImmutableSet.copyOf(Collections2.filter(ImmutableSet.copyOf((ImmutableSet<Note>) arg), new Predicate<Note>() {
				@Override
				public boolean apply(Note note) {
					return note.getClassroom().equals(classId);
				}
			}));
		}
		
		// Sort notes chronologically
		List<Note> notes_list =  Lists.newArrayList(notes);
		Collections.sort(notes_list, new NoteComparator());	
		oldSortedNotes = sortedNotes;
		sortedNotes = ImmutableList.copyOf(notes_list);
		
		// Count how many notes each group posted
		HashMap<String, Integer> map_note_counts = Maps.newHashMap();
		for(Note n: notes){
			if(map_note_counts.containsKey(n.getAuthor())){
				int count = map_note_counts.get(n.getAuthor());
				map_note_counts.put(n.getAuthor(), count+1);
			}
			else {
				map_note_counts.put(n.getAuthor(), 1);
			}
		}
		group_notes_counts = ImmutableMap.copyOf(map_note_counts);
			
		// Generate updates only if, after filtering, there are notes that changed
		if (oldNotes==null || oldNotes.size() != notes.size())
			generateUpdate();
	}

	protected abstract void generateUpdate();
}
