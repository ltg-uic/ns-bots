package ltg.ns.ambient.updaters;

import java.util.Observable;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class NotesNumberUpdater extends AbstractUpdater {
	
	private String class_id;

	public NotesNumberUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}


	public NotesNumberUpdater(SingleChatLTGEventHandler eg, String class_id) {
		this(eg);
		this.class_id = class_id;
	}


	@Override
	public synchronized void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}


	@Override
	public synchronized JsonNode fullInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public synchronized JsonNode gridInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void generateUpdate() {
		eh.generateEvent("#_notes_update", JsonNodeFactory.instance.objectNode());
	}

}
