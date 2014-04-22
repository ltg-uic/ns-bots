package ltg.ns.ambient;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ltg.commons.ltg_event_handler.LTGEvent;
import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.commons.ltg_event_handler.SingleChatLTGEventListener;
import ltg.ns.ambient.pollers.AbstractPoller;
import ltg.ns.ambient.pollers.MockNotesPoller;
import ltg.ns.ambient.pollers.NotesPoller;
import ltg.ns.ambient.updaters.NotesNumberUpdater;
import ltg.ns.ambient.updaters.NotesUpdater;
import ltg.ns.ambient.updaters.UpdaterInterface;
import ltg.ns.ambient.updaters.WordleUpdater;

public class AmbientBot implements Observer {
	// XMPP event handler
	private SingleChatLTGEventHandler eh;
	// Class id
	private String classId;
	// Pollers
	private AbstractPoller np = new MockNotesPoller();
	//private AbstractPoller np = new NotesPoller();
	//private AbstractPoller ip = new MockBurstsPoller();
	//private AbstractPoller tp = new TagPoller();
	// Updaters
	private UpdaterInterface wordleU;
	private UpdaterInterface notesNumberU;
	private UpdaterInterface notesU;
//	private UpdaterInterface imageU; 
//	private UpdaterInterface scoreU;
	// Flags
	private boolean isNotesPollerAlive = false;
//	private boolean isImagesPollerAlive = false;
	private boolean isDataValid = false;


	public AmbientBot(String login, String class_id) {
		eh = new SingleChatLTGEventHandler(login+"@ltg.evl.uic.edu", login, "nh-test@conference.ltg.evl.uic.edu");
		classId = class_id;
		wordleU = new WordleUpdater(eh, class_id);
		notesNumberU = new NotesNumberUpdater(eh, class_id);
		notesU = new NotesUpdater(eh, class_id);
//		imageU = new BurstUpdater(eh, class_id);
//		scoreU = new TagsScoreboardUpdater(eh, class_id);
	}


	public static void main(String[] args) {
		System.out.println("Started Neighborhood Safari Ambient Display bot");
		if (args.length != 2 || 
				args[0]==null || args[0].isEmpty() ||
				args[1]==null || args[1].isEmpty()) {
			System.out.println("Need to specify the login name (e.g. ns-bot-#) and classroom (e.g. ben)");
			System.exit(0);
		}
		try {
			AmbientBot ab = new AmbientBot(args[0], args[1]);
			ab.registerObservers();
			ab.startPolling();
			ab.registerListeners();
		} catch (Exception e) {
			// Catch the exception, write it out and move on
			e.printStackTrace();
		}

	}

	private void registerObservers() {
		np.addObserver(this);
//		ip.addObserver(this);
		
		// Updaters
		np.addObserver(notesU);
		np.addObserver(notesNumberU);
		np.addObserver(wordleU);
//		np.addObserver(scoreU);
//		ip.addObserver(imageU);
	}

	private void startPolling() {
		new Thread(np).start();
//		new Thread(ip).start();
	}

	private void registerListeners() {
		// Process init events
		eh.registerHandler("(.+)_init", new SingleChatLTGEventListener() {
			public void processEvent(LTGEvent e) {
				// If there is no valid data coming from the pollers, ditch the request
				if (!isDataValid)
					return;
				// If classroom is not the one specified when starting the bot, this is not the right 
				// bot for this request so discard
				if (!e.getPayload().get("class").textValue().equals(classId))
					return;
				Matcher m = Pattern.compile("(.+)_init").matcher(e.getEventType());
				m.matches();
				switch (m.group(1)) {
				case "notes_full":
					eh.generateEvent(m.group(0).toString()+"_r", notesU.fullInit(e));
					break;
				case "notes_grid":
					eh.generateEvent(m.group(0).toString()+"_r", notesU.gridInit(e));
					break;
				case "#_notes_full":
					eh.generateEvent(m.group(0).toString()+"_r", notesNumberU.fullInit(e));
					break;
				case "#_notes_grid":
					eh.generateEvent(m.group(0).toString()+"_r", notesNumberU.gridInit(e));
					break;
				case "wordle_full":
					eh.generateEvent(m.group(0).toString()+"_r", wordleU.fullInit(e));
					break;
				case "wordle_grid":
					eh.generateEvent(m.group(0).toString()+"_r", wordleU.gridInit(e));
					break;
//				case "score_full":
//					eh.generateEvent(m.group(0).toString()+"_r", scoreU.fullInit(e));
//					break;
//				case "score_grid":
//					eh.generateEvent(m.group(0).toString()+"_r", scoreU.gridInit(e));
//					break;
//				case "images_full":
//					eh.generateEvent(m.group(0).toString()+"_r", imageU.fullInit(e));
//					break;
//				case "images_grid":
//					eh.generateEvent(m.group(0).toString()+"_r", imageU.gridInit(e));
//					break;
				default:
					//throw new RuntimeException("Unknown init message!");
					System.out.println("Unknown init event: " + e.getEventType());
				}
			}
		});
		/*
		 * Listens for:
		 * {
    		"event": "displays_init",
    		"payload": {
        		"class": "ben"
    		}
		   }
		 */
		eh.registerHandler("displays_init", new SingleChatLTGEventListener() {	
			@Override
			public void processEvent(LTGEvent e) {
				// If there is no valid data coming from the pollers, ditch the request
				if (!isDataValid)
					return;
				// If classroom is not the one specified when starting the bot, 
				// this is not the right bot for this request, discard
				if (!e.getPayload().get("class").textValue().equals(classId))
					return;
				Matcher m = Pattern.compile("(.+)_init").matcher(e.getEventType());
				m.matches();
				eh.generateEvent("notes_full_init_r", notesU.fullInit(e));
				eh.generateEvent("notes_grid_init_r", notesU.gridInit(e));
				eh.generateEvent("#_notes_full_init_r", notesNumberU.fullInit(e));
				eh.generateEvent("#_notes_grid_init_r", notesNumberU.gridInit(e));
				eh.generateEvent("wordle_full_init_r", wordleU.fullInit(e));
				eh.generateEvent("wordle_grid_init_r", wordleU.gridInit(e));
//				eh.generateEvent("score_full_init_r", scoreU.fullInit(e));
//				eh.generateEvent("score_grid_init_r", scoreU.gridInit(e));
//				eh.generateEvent("images_full_init_r", imageU.fullInit(e));
//				eh.generateEvent("images_grid_init_r", imageU.gridInit(e));
			}
		});

		eh.runSynchronously();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof MockNotesPoller) // TODO change this into simple poller
			isNotesPollerAlive = true;
//		if (o instanceof MockBurstsPoller)    
//			isImagesPollerAlive = true;
		if (isNotesPollerAlive) {
			np.deleteObserver(this);
//			ip.deleteObserver(this);
			isDataValid = true;
			System.out.println("Listening for events");
		}
	}

}
