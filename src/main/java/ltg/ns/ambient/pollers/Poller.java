package ltg.ns.ambient.pollers;

import java.util.Observable;

public abstract class Poller extends Observable {
	
	public final static long POLL_INTERVAL = 20_000;

}
