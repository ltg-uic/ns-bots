package ltg.ns.ambient.pollers;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ltg.ns.ambient.model.Burst;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class MockBurstsPoller extends AbstractPoller implements Runnable {
	
	private ImmutableSet<Burst> allBursts = RandomBurstBuilder.buildInitialSetOfBursts(5);
	

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Fetch all bursts
			ImmutableSet<Burst> allNewBursts = 
					ImmutableSet.<Burst>builder()
					.addAll(allBursts)
					.add(RandomBurstBuilder.buildBurst())
					.build();
			if (!allBursts.containsAll(allNewBursts)) {
				allBursts = ImmutableSet.copyOf(allNewBursts);
				// Notify observers
				this.setChanged();
				this.notifyObservers(allBursts);
				System.out.println("Added a burst");
			}
			// Sleep...
			try {
				Thread.sleep(POLL_INTERVAL/4);
			} catch (InterruptedException e) {
				return;
			}
		}
	}


	
private static class RandomBurstBuilder {
		
		private static ImmutableList<String> BURST_URLS = 
				ImmutableList.of(
						"http://pikachu.badger.encorelab.org/1bfz298wrnk.jpg",
						"http://pikachu.badger.encorelab.org/1jk3e5tut4w.jpg",
						"http://pikachu.badger.encorelab.org/o7shv2i0ao.jpg"
						);
		
		private static ImmutableSet<String> BURST_CLASSROOM = 
				ImmutableSet.of("ben","amanda");
		
		private static ImmutableSet<String> BURST_AUTHOR = 
				ImmutableSet.of("awesomers","boogiers", "cranberries", "diracdeltas", "eminentemmersons", "frillyfurries");
		
		private static Random rnd = new Random();
		private static int id = 0;
		
		private static String randomIn(ImmutableSet<String> set) {
			return set.asList().get(rnd.nextInt(set.size()));
		}
		
		public static ImmutableSet<Burst> buildInitialSetOfBursts(int n) {
			Set<Burst> notes = new HashSet<>();
			for (int i=0; i < n; i++) {
				notes.add(buildBurst());
			}
			return ImmutableSet.copyOf(notes);
		}
		
		public static Burst buildBurst () {
			Burst b = Burst.buildBurstWithId(id++)
					.school("ics")
					.classroom(randomIn(BURST_CLASSROOM))
					.author(randomIn(BURST_AUTHOR))
					.createdAt(ISODateTimeFormat.dateTime().print(new DateTime()))
					.images(BURST_URLS);
			return b;
		}
	}
	
	

}
