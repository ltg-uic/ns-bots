package ltg.ns.ambient.updaters;

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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class WordleUpdater extends AbstractUpdater {

	private Map<String, String> wordles = Maps.newHashMap();
	private ImmutableSet<Note> all_notes;
	private Random r = new Random();

	public WordleUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
//		@SuppressWarnings("unchecked")
//		Set<Note> notes = (Set<Note>) arg;
//		all_notes = ImmutableSet.copyOf(notes);
		all_notes = Note.generateStubNotesForIcsBenClassroom();
		for (Note n: all_notes) {
			String wordleText = wordles.get(n.getAuthor())==null ? "" : wordles.get(n.getAuthor());
			wordleText += n.getRandomBody() + " ";
			wordles.put(n.getAuthor(), wordleText);
		}
		//generateUpdate();
	}

	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		Note rn = Lists.newArrayList(all_notes).get(r.nextInt(all_notes.size()));
		return JsonNodeFactory.instance.objectNode()
				.put("school", rn.getSchool())
				.put("class", rn.getClassroom())
				.put("group", rn.getAuthor())
				.put("wordle_text", wordles.get(rn.getAuthor()));
	}

	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			Note rn = Lists.newArrayList(all_notes).get(r.nextInt(all_notes.size()));
			ObjectNode note = JsonNodeFactory.instance.objectNode()
					.put("school", rn.getSchool())
					.put("class", rn.getClassroom())
					.put("group", rn.getAuthor())
					.put("wordle_text", wordles.get(rn.getAuthor()));
			grid.add(note);
		}
		return payload;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub

	}

}
