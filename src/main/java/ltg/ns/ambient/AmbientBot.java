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
import ltg.ns.ambient.updaters.ImageUpdater;
import ltg.ns.ambient.updaters.NotesNumberUpdater;
import ltg.ns.ambient.updaters.NotesUpdater;
import ltg.ns.ambient.updaters.ScoreboardUpdater;
import ltg.ns.ambient.updaters.UpdaterInterface;
import ltg.ns.ambient.updaters.WordleUpdater;

public class AmbientBot implements Observer {
	// XMPP event handler
	private SingleChatLTGEventHandler eh;
	// Pollers
	private NotesPoller np = new NotesPoller();
	private ImagePoller ip = new ImagePoller();
	// Updaters
	private UpdaterInterface wordleU;
	private UpdaterInterface imageU; 
	private UpdaterInterface scoreU;
	private UpdaterInterface notesNumberU;
	private UpdaterInterface notesU;
	// Flags
	private boolean isNotesPollerAlive = false;
	private boolean isImagesPollerAlive = false;
	private boolean isDataValid = false;


	public AmbientBot(String class_id) {
		eh = new SingleChatLTGEventHandler("ns-bot-1@ltg.evl.uic.edu", "ns-bot-1", "nh-test@conference.ltg.evl.uic.edu");
		wordleU = new WordleUpdater(eh);
		imageU = new ImageUpdater(eh);
		scoreU = new ScoreboardUpdater(eh);
		notesNumberU = new NotesNumberUpdater(eh, class_id);
		notesU = new NotesUpdater(eh, class_id);
	}


	public static void main(String[] args) {
		System.out.println("Started Neighborhood Safari Ambient Display bot");
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
		np.addObserver(wordleU);
		ip.addObserver(imageU);
		np.addObserver(scoreU);
		np.addObserver(notesNumberU);
		np.addObserver(notesU);	
	}

	private void startPolling() {
		new Thread(np).start();
		new Thread(ip).start();
	}
	
	private void registerListeners() {
		// Process init events
		eh.registerHandler("\\w+_init", new SingleChatLTGEventListener() {
			public void processEvent(LTGEvent e) {
				// If there is no valid data coming from the pollers this request can't be satisfied
				if (!isDataValid)
					return;
				Matcher m = Pattern.compile("(.+)_init").matcher(e.getEventType());
				m.matches();
				switch (m.group(1)) {
				case "wordle_full":
					eh.generateEvent(m.group(0).toString()+"_r", wordleU.fullInit(e));
					break;
				case "wordle_grid":
					eh.generateEvent(m.group(0).toString()+"_r", wordleU.gridInit(e));
					break;
				case "images_full":
					eh.generateEvent(m.group(0).toString()+"_r", imageU.fullInit(e));
					break;
				case "images_grid":
					eh.generateEvent(m.group(0).toString()+"_r", imageU.fullInit(e));
					break;
				case "score_full":
					eh.generateEvent(m.group(0).toString()+"_r", scoreU.fullInit(e));
					break;
				case "score_grid":
					eh.generateEvent(m.group(0).toString()+"_r", scoreU.fullInit(e));
					break;
				case "#_notes_full":
					eh.generateEvent(m.group(0).toString()+"_r", notesNumberU.fullInit(e));
					break;
				case "#_notes_grid":
					eh.generateEvent(m.group(0).toString()+"_r", notesNumberU.fullInit(e));
					break;
				case "notes_full":
					eh.generateEvent(m.group(0).toString()+"_r", notesU.fullInit(e));
					break;
				case "notes_grid":
					eh.generateEvent(m.group(0).toString()+"_r", notesU.fullInit(e));
					break;
				default:
					throw new RuntimeException("Unknown init message!");
				}
			}
		});
		eh.runSynchronously();
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

}
