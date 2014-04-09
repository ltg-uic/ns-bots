package ltg.ns.ambient.updaters;

import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import ltg.commons.ltg_event_handler.SingleChatLTGEventHandler;
import ltg.ns.ambient.model.Burst;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class BurstUpdater extends AbstractUpdater {

	private ImmutableSet<Burst> bursts = ImmutableSet.of();

	public BurstUpdater(SingleChatLTGEventHandler eh, String classId) {
		super(eh, classId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void update(Observable o, Object arg) {
		Set<Burst> newBursts = (Set<Burst>) arg; 
		// Filter
		if (!classId.equals("all"))
			newBursts = new HashSet<>(Collections2.filter(ImmutableSet.copyOf(newBursts), new Predicate<Burst>() {
				@Override
				public boolean apply(Burst note) {
					return note.getClassroom().equals(classId);
				}
			}));
			// Calculate the diff and update
			newBursts.removeAll(bursts);
			sendUpdate(ImmutableSet.copyOf(newBursts));
			// Update the bursts to prepare for next update cycle
			bursts = ImmutableSet.<Burst>builder().addAll(bursts).addAll(newBursts).build();
	}

	/**
	 * Returns a random burst among the ones that have been uploaded in the last update cycle
	 */
	public synchronized void sendUpdate(ImmutableSet<Burst> newBurstsOnly) {
		if (newBurstsOnly.isEmpty())
			return;
		Burst b = Lists.newArrayList(newBurstsOnly).get(new Random().nextInt(newBurstsOnly.size()));
		ObjectNode node = JsonNodeFactory.instance.objectNode()
				.put("school", b.getSchool())
				.put("class", b.getClassroom())
				.put("group", b.getAuthor())
				.put("time", b.getCreated_at());
		ArrayNode burst = node.putArray("burst");
		for (String image: b.getImages())
			burst.add(image);
		eh.generateEvent("images_update", node);
	}


	/**
	 * Returns a random burst among the ones that have been posted so far
	 */
	@Override
	public synchronized JsonNode fullInit() {
		Burst b = Lists.newArrayList(bursts).get(new Random().nextInt(bursts.size()));
		ObjectNode node = JsonNodeFactory.instance.objectNode()
				.put("school", b.getSchool())
				.put("class", b.getClassroom())
				.put("group", b.getAuthor())
				.put("time", b.getCreated_at());
		ArrayNode burst = node.putArray("burst");
		for (String image: b.getImages())
			burst.add(image);
		return node;
	}


	/**
	 * Returns a random burst per each group in the classroom/all classrooms
	 */
	@Override
	public synchronized JsonNode gridInit() {
		// TODO we actually need to add the single groups and not 9 random bursts
		ObjectNode payload = JsonNodeFactory.instance.objectNode();
		ArrayNode grid = payload.putArray("grid"); 
		for (int i=0; i<9; i++) {
			Burst b = Lists.newArrayList(bursts).get(new Random().nextInt(bursts.size()));
			ObjectNode node = JsonNodeFactory.instance.objectNode()
					.put("school", b.getSchool())
					.put("class", b.getClassroom())
					.put("group", b.getAuthor())
					.put("time", b.getCreated_at());
			ArrayNode burst = node.putArray("burst");
			for (String image: b.getImages())
				burst.add(image);
			grid.add(node);
		}		
		return payload;
	}



}
