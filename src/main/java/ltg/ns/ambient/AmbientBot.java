package ltg.ns.ambient;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.commons.ltg_event_handler.SingleChatLTGEventListener;
import ltg.ns.ambient.pollers.ImagePoller;
import ltg.ns.ambient.pollers.NotesPoller;

public class AmbientBot implements Observer {
	private String class_id;
	// XMPP event handler
	private SingleChatLTGEventHandler eh = new SingleChatLTGEventHandler("ns-bot-1@ltg.evl.uic.edu", "ns-bot-1", "nh-test@conference.ltg.evl.uic.edu");
	// Pollers
	private NotesPoller np = new NotesPoller();
	private ImagePoller ip = new ImagePoller();
	// Updaters
	private WordleUpdater wordle = new WordleUpdater();
	// Flags
	private boolean isNotesPollerAlive = false;
	private boolean isImagesPollerAlive = false;
	private boolean isDataValid = false;
	
	public AmbientBot(String class_id) {
		this.class_id = class_id;
	}

	
	public static void main(String[] args) {
		if (args.length != 1 || 
				args[0]==null || args[0].isEmpty()) {
			System.out.println("Need to specify the classroom");
			System.exit(0);
		}
		AmbientBot ab = new AmbientBot(args[0]);
		ab.registerObservers();
		ab.startPolling();
		ab.registerListeners();
	}
	
	private void registerObservers() {
		np.addObserver(this);
		ip.addObserver(this);
		np.addObserver(wordle);
	}

	private void startPolling() {
		new Thread(np).start();
		new Thread(ip).start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof NotesPoller)
			isNotesPollerAlive = true;
		if (o instanceof ImagePoller)
			isImagesPollerAlive = true;
		if (isImagesPollerAlive && isNotesPollerAlive) {
			np.deleteObserver(this);
			ip.deleteObserver(this);
			isDataValid = true;
		}
	}


	private void registerListeners() {
		// Process init events
		eh.registerHandler("\\w+_init", new SingleChatLTGEventListener() {
			public void processEvent(LTGEvent e) {
				// If there is no valid data coming from the pollers this request can't be satisfied
				if (!isDataValid)
					return;
				Matcher m = Pattern.compile("(\\w+)(_init)").matcher(e.getEventType());
				switch (m.group(1).toString()) {
				case "wordle_full":
					//eh.generateEvent(m.group(0).toString()+"r", wordle.fullInit(e));
					break;
				case "wordle_grid":
					//eh.generateEvent(m.group(0).toString()+"r", wordle.gridInit(e));
					break;
				case "images_full":
					break;
				case "images_grid":
					break;
				case "score_full":
					break;
				case "score_grid":
					break;
				case "#_notes_full":
					break;
				case "#_notes_grid":
					break;
				case "notes_full":
					break;
				case "notes_grid":
					break;
				default:
					throw new RuntimeException("Unknown init message!");
				}
			}
		});
		eh.runSynchronously();
	}

}
