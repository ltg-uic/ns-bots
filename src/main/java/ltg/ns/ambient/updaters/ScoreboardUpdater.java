package ltg.ns.ambient.updaters;

import java.util.Observable;
import java.util.Observer;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

import com.fasterxml.jackson.databind.JsonNode;

public class ScoreboardUpdater extends AbstractUpdater {

	public ScoreboardUpdater(SingleChatLTGEventHandler eg) {
		super(eg);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public JsonNode fullInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonNode gridInit(LTGEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateUpdate() {
		// TODO Auto-generated method stub
		
	}

}
