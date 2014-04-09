package ltg.ns.ambient.updaters;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;

public abstract class AbstractUpdater implements UpdaterInterface {
	
	protected SingleChatLTGEventHandler eh;
	protected String classId;
	
	public AbstractUpdater(SingleChatLTGEventHandler eh, String classId) {
		this.eh = eh;
		this.classId = classId;
	}
}
