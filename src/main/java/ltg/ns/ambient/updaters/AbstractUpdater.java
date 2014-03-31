package ltg.ns.ambient.updaters;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

public abstract class AbstractUpdater implements UpdaterInterface {
	
	protected SingleChatLTGEventHandler eh; 
	
	public AbstractUpdater(SingleChatLTGEventHandler eh) {
		this.eh = eh;
	}
	
	protected abstract void generateUpdate();

}
