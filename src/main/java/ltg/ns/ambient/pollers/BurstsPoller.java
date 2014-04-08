package ltg.ns.ambient.pollers;

import java.util.HashSet;
import java.util.Set;

import ltg.commons.JSONHTTPClient;
import ltg.ns.ambient.model.Burst;
import ltg.ns.ambient.model.Classroom;
import ltg.ns.ambient.model.Deployment;
import ltg.ns.ambient.model.School;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableSet;

public class BurstsPoller extends AbstractPoller implements Runnable {
	
	private ImmutableSet<Burst> allBursts = ImmutableSet.of();
	private ImmutableSet<Deployment> allDeployments;
	private ImmutableSet<Classroom> allClasses;
	private ImmutableSet<School> allSchools;
	

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Fetch supporting collections
			allDeployments = parseDeployments(JSONHTTPClient.GET("http://trap.euclidsoftware.com/deployment"));
			allClasses = parseClasses(JSONHTTPClient.GET("http://trap.euclidsoftware.com/class"));
			allSchools = parseSchools(JSONHTTPClient.GET("http://trap.euclidsoftware.com/school"));
			// Fetch all bursts
			ImmutableSet<Burst> newBursts = parseImages(JSONHTTPClient.GET("http://trap.euclidsoftware.com/burst"));
			if (!allBursts.containsAll(newBursts)) {
				allBursts = newBursts;
				// Notify observers
				this.setChanged();
				this.notifyObservers(newBursts);
			}
			// Sleep...
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				return;
			}
		}
	}


	private ImmutableSet<Burst> parseImages(JsonNode get) {
		ArrayNode data = (ArrayNode) get.get("burst");
		Set<Burst> bursts = new HashSet<>();
		for (JsonNode o: data)
			bursts.add(parseBurst(o));
		return ImmutableSet.copyOf(bursts);
	}

	// http://trap.euclidsoftware.com/file/image/78  first valid image
	private Burst parseBurst(JsonNode b) {
			Burst burst = Burst.buildBurstWithId(b.get("id").asInt())
					.createdAt(b.get("burst_date").textValue());
			b.get("deployment_id").asInt();
		return burst;
	}
	
	
	private ImmutableSet<Deployment> parseDeployments(JsonNode get) {
		ArrayNode data = (ArrayNode) get.get("deployment");
		Set<Deployment> deployments = new HashSet<>();
		for (JsonNode o: data)
			deployments.add(parseDeployment(o));
		return ImmutableSet.copyOf(deployments);
	}

	private Deployment parseDeployment(JsonNode o) {
		return new Deployment(
				o.get("id").asInt(), 
				o.get("person").get("class_id").asInt(), 
				o.get("person").get("first_name").textValue() 
				);
	}


	private ImmutableSet<Classroom> parseClasses(JsonNode get) {
		ArrayNode data = (ArrayNode) get.get("class");
		Set<Classroom> classrooms = new HashSet<>();
		for (JsonNode o: data)
			classrooms.add(parseClassroom(o));
		return ImmutableSet.copyOf(classrooms);
	}
	
	private Classroom parseClassroom(JsonNode o) {
		return new Classroom(
				o.get("id").asInt(), 
				o.get("name").textValue(), 
				o.get("school_id").asInt()
				);
	}


	private ImmutableSet<School> parseSchools(JsonNode get) {
		ArrayNode data = (ArrayNode) get.get("school");
		Set<School> schools = new HashSet<>();
		for (JsonNode o: data)
			schools.add(parseSchool(o));
		return ImmutableSet.copyOf(schools);
	}

	private School parseSchool(JsonNode o) {
		return new School(
				o.get("id").asInt(), 
				o.get("name").textValue()
				);
	}

}
