package ltg.ns.ambient.pollers;

import java.util.Observable;

public abstract class AbstractPoller extends Observable implements Runnable {
	
	public final static long POLL_INTERVAL = 30_000;

}
