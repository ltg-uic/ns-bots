package ltg.ns.ambient.updaters;

import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Note;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class WordleUpdater extends AbstractUpdater {

	private Map<String, String> wordles = Maps.newHashMap();
	private ImmutableSet<Note> allNotes;
	private Random r = new Random();

	public WordleUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		Set<Note> notes = (Set<Note>) arg;
		allNotes = ImmutableSet.copyOf(notes);
		for (Note n: notes) {
			String wordleText = wordles.get(n.getAuthor());
			wordleText += n.getRandomBody() + " ";
			wordles.put(n.getAuthor(), wordleText);
		}
		generateUpdate();
	}

	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		Note rn = Lists.newArrayList(allNotes).get(r.nextInt(allNotes.size()));
		return JsonNodeFactory.instance.objectNode().put("school", rn.getSchool())
		.put("class", rn.getClassroom())
		.put("group", rn.getAuthor())
		.put("wordle_text", wordles.get(rn.getAuthor()));
	}

	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub

	}

}
