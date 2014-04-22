package ltg.ns.ambient.pollers;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ltg.ns.ambient.model.Note;
import ltg.ns.ambient.model.Note.Type;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class MockNotesPoller extends AbstractPoller {

	private ImmutableSet<Note> allNotes = RandomNoteBuilder.buildInitialSetOfNotes(15);
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Create a new note just for fun
			ImmutableSet<Note> allNewNotes = 
					ImmutableSet.<Note>builder()
					.addAll(allNotes)
					.add(RandomNoteBuilder.buildNote())
					.build();
			if (!allNotes.containsAll(allNewNotes)) {
				allNotes = ImmutableSet.copyOf(allNewNotes);
				// Notify observers
				this.setChanged();
				this.notifyObservers(allNotes);
			}
			// Sleep...
			try {
				Thread.sleep(15_000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}



	private static class RandomNoteBuilder {
		
		static private ImmutableSet<String> NOTE_TEXTS = 
				ImmutableSet.of(
						"The raccoon, sometimes spelled racoon, also known as the common raccoon, North American raccoon,[4] northern raccoon[5] and colloquially as coon,[6] is a medium-sized mammal native to North America. The raccoon is the largest of the procyonid family, having a body length of 40 to 70 cm (16 to 28 in) and a body weight of 3.5 to 9 kg (8 to 20 lb).",
						"Its grayish coat mostly consists of dense underfur which insulates against cold weather. Two of the raccoon's most distinctive features are its extremely dexterous front paws and its facial mask, which are themes in the mythology of several Native American tribes. ",
						"Raccoons are noted for their intelligence, with studies showing that they are able to remember the solution to tasks for up to three years.[7] The diet of the omnivorous raccoon, which is usually nocturnal, consists of about 40% invertebrates, 33% plant foods, and 27% vertebrates.",
						"The original habitats of the raccoon are deciduous and mixed forests, but due to their adaptability they have extended their range to mountainous areas, coastal marshes, and urban areas, where some homeowners consider them to be pests",
						"As a result of escapes and deliberate introductions in the mid-20th century, raccoons are now also distributed across the European mainland, the Caucasus region and Japan.",
						"Though previously thought to be solitary, there is now evidence that raccoons engage in gender-specific social behavior.",
						"Related females often share a common area, while unrelated males live together in groups of up to four animals to maintain their positions against foreign males during the mating season, and other potential invaders",
						"Home range sizes vary anywhere from 3 hectares (7 acres) for females in cities to 50 km2 (20 sq mi) for males in prairies.",
						"After a gestation period of about 65 days, two to five young, known as \"kits\", are born in spring.",
						"This Latin word was borrowed from the Ancient Greek word σκίουρος, skiouros, which means shadow-tailed, referring to the bushy appendage possessed by many of its members",
						"The native Old English word for the squirrel, ācweorna, survived only into Middle English (as aquerne) before being replaced",
						"The kits are subsequently raised by their mother until dispersion in late fall. Although captive raccoons have been known to live over 20 years, their average life expectancy in the wild is only 1.8 to 3.1 years. ",
						"In many areas, hunting and vehicular injury are the two most common causes of death.",
						"Squirrels belong to family Sciuridae of small or medium-size rodents.",
						"The family includes tree squirrels, ground squirrels, chipmunks, marmots (including woodchucks), flying squirrels, and prairie dogs.",
						"Squirrels are indigenous to the Americas, Eurasia, and Africa, and have been introduced to Australia",
						"The earliest known squirrels date from the Eocene and are most closely related to the mountain beaver and to the dormouse among living rodent families.",
						"The word 'squirrel', first specified in 1327, comes from Anglo-Norman esquirel from the Old French escurel, the reflex of a Latin word sciurus."
						);
		
		private static ImmutableSet<String> NOTE_TITLES = 
				ImmutableSet.of(
						"This is a short title",
						"This instead is more of a long long long title",
						"yet another title and that's enough"
						);
		
		private static ImmutableSet<String> NOTE_CLASSROOM = 
				ImmutableSet.of("ben","amanda", "7BL", "7MS", "7DM");
		
		private static ImmutableMap<String, ImmutableSet<String>> NOTE_AUTHOR = 
				ImmutableMap.of(
						"ben", ImmutableSet.of("robford","saladplusmonkey", "violet", "rofo", "malabalicious", "ridonculous", "mistermonacle", "meatball", "galaxy", "pickles", "pickles2"),
						"amanda", ImmutableSet.of("ribbit","froginators", "aloneraccoon", "mysloth", "snowflake", "prettiestprincesses", "catbugheat", "unicornprincesses", "bobcats", "turtellyawesome"),
						"7BL", ImmutableSet.of("eam","mmn", "mjr", "jjs"),
						"7MS", ImmutableSet.of("beja","ihj", "sjhr", "smmo", "ids","eam"),
						"7DM", ImmutableSet.of("mng","joc", "gsg", "rss", "lcg", "frillyfurries")
						);
		
		private static Random rnd = new Random();
		private static int id = 0;
		
		private static String randomIn(ImmutableSet<String> set) {
			return set.asList().get(rnd.nextInt(set.size()));
		}
		
		public static ImmutableSet<Note> buildInitialSetOfNotes(int n) {
			Set<Note> notes = new HashSet<>();
			for (int i=0; i < n; i++) {
				notes.add(buildNote());
			}
			return ImmutableSet.copyOf(notes);
		}
		
		public static Note buildNote () {
			Note n  = Note.buildNoteWithId(Integer.toString(id++))
					.school("ics")
					.classroom(randomIn(NOTE_CLASSROOM))
					.author(randomIn(NOTE_AUTHOR.get(randomIn(NOTE_CLASSROOM))))
					.createdAt(ISODateTimeFormat.dateTime().print(new DateTime()));
			// Note type
			Type noteType = Type.values()[rnd.nextInt(Type.values().length)];
			n.withType(noteType);
			switch (noteType) {
			case cross_cutting:
				n.withCrussCuttingBody(randomIn(NOTE_TITLES), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS));
				break;
			case open:
				n.withOpenBody(randomIn(NOTE_TITLES), randomIn(NOTE_TEXTS));
				break;
			case photo_set:
				n.withPhotoSetBody(randomIn(NOTE_TITLES), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS));
				break;
			case planning:
				n.withPlanningBody(randomIn(NOTE_TITLES), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS), randomIn(NOTE_TEXTS));
				break;
			default:
				new RuntimeException("Unknown note type");
			}
			return n;
		}
	}

}
