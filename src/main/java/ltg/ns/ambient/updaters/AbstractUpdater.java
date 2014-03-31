package ltg.ns.ambient.updaters;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

public abstract class AbstractUpdater implements UpdaterInterface {
	
	protected SingleChatLTGEventHandler eventGenerator; 
	
	public AbstractUpdater(SingleChatLTGEventHandler eg) {
		this.eventGenerator = eg;
	}
	
	protected abstract void generateUpdate();

}
