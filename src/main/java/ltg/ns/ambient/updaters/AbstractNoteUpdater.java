package ltg.ns.ambient.updaters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;
import ltg.ns.comparators.NoteComparator;

public abstract class AbstractNoteUpdater implements UpdaterInterface {

	protected SingleChatLTGEventHandler eh;
	protected String classId;
	protected Set<Note> notes = null;
	protected ArrayList<Note> sortedNotes = null;
	protected ArrayList<Note> lastNineNotes = null;
	protected HashMap<String, Integer> map_note_counts = null;
	
	protected ImmutableMap<String, Integer> group_notes_counts = null;


	public AbstractNoteUpdater(SingleChatLTGEventHandler eh, String classId) {
		this.eh = eh;
		this.classId = classId;
		map_note_counts = new HashMap<String, Integer>();
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
			sortedNotes = Lists.newArrayList(notes);
			Collections.sort(sortedNotes, new NoteComparator());			
			
			for(Note n:notes){
				if(n.getClassroom().equals(classId)){
					if(map_note_counts.containsKey(n.getAuthor())){
						int count = map_note_counts.get(n.getAuthor());
						
						map_note_counts.put(n.getAuthor(), count+1);
					}
					else{
						map_note_counts.put(n.getAuthor(), 1);
					}
				}
			}
			
			group_notes_counts = ImmutableMap.copyOf(map_note_counts);
			
			System.out.println(group_notes_counts);
			System.out.println(notes.size());

			generateUpdate();
	}

	protected abstract void generateUpdate();
}
