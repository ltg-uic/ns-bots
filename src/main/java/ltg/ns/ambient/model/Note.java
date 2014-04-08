package ltg.ns.ambient.model;

import com.google.common.collect.ImmutableSet;


public class Note {

	public enum Type {
		planning, photo_set, open, cross_cutting;
	} 

	private String id;
	private String author;
	private String classroom;
	private String school; 
	private Type type;
	private String body_title;
	private String body_description;
	private String body_question;
	private String body_hypothesis;
	private String body_explanation;
	private String body_evidence;
	private String created_at;

	private Note(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getSchool() {
		return school;
	}

	public String getClassroom() {
		return classroom;
	}

	public String getAuthor() {
		return author;
	}

	public Type getNoteType() {
		return type;
	}

	public String getBodyTitle() {
		return body_title;
	}

	public String getBodyDescription() {
		return body_description;
	}

	public String getBodyQuestion() {
		return body_question;
	}

	public String getBodyHypothesis() {
		return body_hypothesis;
	}

	public String getBodyExplanation() {
		return body_explanation;
	}

	public String getBodyEvidence() {
		return body_evidence;
	}

	public String getCreated_at() {
		return created_at;
	}

	@Override
	public boolean equals(Object obj) {
		return id.equals(((Note)obj).getId());
	}


	@Override
	public String toString() {	
		return getRandomBody();
	}

	public String getRandomBody() {
		if (body_evidence!=null)
			return body_evidence;
		if (body_hypothesis!=null)
			return body_hypothesis;
		if (body_question!=null)
			return body_question;
		throw new RuntimeException("This note has no body... like... WAT!");
	}


	public static Note buildDebugNote(String group, String note) {
		return buildNoteWithId("id").school("ics").classroom("ben").author(group).withOpenBody("a title", note);
	}

	public static Note buildNoteWithId(String id) {
		return new Note(id);
	}

	
	// Fluent interface for notes creation
	public Note school(String string) {
		this.school = string;
		return this;
	}

	public Note classroom(String string) {
		this.classroom = string;
		return this;
	}

	public Note author(String string) {
		this.author = string;
		return this;
	}

	public Note createdAt(String string) {
		this.created_at = string;
		return this;
	}

	public Note withType(Type t) {
		this.type = t;
		return this;
	}

	public Note withOpenBody(String title, String description) {
		this.body_title = title;
		this.body_description = description;
		return this;
	}

	public Note withPlanningBody(String title, String description, String hypothesis, String explanation, String evidence) {
		withOpenBody(title, description);
		this.body_hypothesis = hypothesis;
		this.body_explanation = explanation;
		this.body_evidence = evidence;
		return this;
	}

	public Note withPhotoSetBody(String title, String description, String question, String explanation) {
		withOpenBody(title, description);
		this.body_question = question;
		this.body_explanation = explanation;
		return this;
	}

	public Note withCrussCuttingBody(String title, String description, String explanation) {
		withOpenBody(title, description);
		this.body_explanation = explanation;
		return this;
	}


	public static ImmutableSet<Note> generateStubNotesForIcsBenClassroom() {
		return ImmutableSet.of(
				Note.buildDebugNote("group-1", "The raccoon, sometimes spelled racoon, also known as the common raccoon, North American raccoon,[4] northern raccoon[5] and colloquially as coon,[6] is a medium-sized mammal native to North America. The raccoon is the largest of the procyonid family, having a body length of 40 to 70 cm (16 to 28 in) and a body weight of 3.5 to 9 kg (8 to 20 lb)."),
				Note.buildDebugNote("group-2", "Its grayish coat mostly consists of dense underfur which insulates against cold weather. Two of the raccoon's most distinctive features are its extremely dexterous front paws and its facial mask, which are themes in the mythology of several Native American tribes. "),
				Note.buildDebugNote("group-3", "Raccoons are noted for their intelligence, with studies showing that they are able to remember the solution to tasks for up to three years.[7] The diet of the omnivorous raccoon, which is usually nocturnal, consists of about 40% invertebrates, 33% plant foods, and 27% vertebrates."),
				Note.buildDebugNote("group-4", "The original habitats of the raccoon are deciduous and mixed forests, but due to their adaptability they have extended their range to mountainous areas, coastal marshes, and urban areas, where some homeowners consider them to be pests"),
				Note.buildDebugNote("group-5", "As a result of escapes and deliberate introductions in the mid-20th century, raccoons are now also distributed across the European mainland, the Caucasus region and Japan."),
				Note.buildDebugNote("group-6", "Though previously thought to be solitary, there is now evidence that raccoons engage in gender-specific social behavior."),
				Note.buildDebugNote("group-7", "Related females often share a common area, while unrelated males live together in groups of up to four animals to maintain their positions against foreign males during the mating season, and other potential invaders"),
				Note.buildDebugNote("group-8", "Home range sizes vary anywhere from 3 hectares (7 acres) for females in cities to 50 km2 (20 sq mi) for males in prairies."),
				Note.buildDebugNote("group-9", "After a gestation period of about 65 days, two to five young, known as \"kits\", are born in spring."),
				Note.buildDebugNote("group-10", "This Latin word was borrowed from the Ancient Greek word σκίουρος, skiouros, which means shadow-tailed, referring to the bushy appendage possessed by many of its members"),
				Note.buildDebugNote("group-11", "The native Old English word for the squirrel, ācweorna, survived only into Middle English (as aquerne) before being replaced"),
				Note.buildDebugNote("group-2", "The kits are subsequently raised by their mother until dispersion in late fall. Although captive raccoons have been known to live over 20 years, their average life expectancy in the wild is only 1.8 to 3.1 years. "),
				Note.buildDebugNote("group-2", "In many areas, hunting and vehicular injury are the two most common causes of death."),
				Note.buildDebugNote("group-2", "Squirrels belong to family Sciuridae of small or medium-size rodents."),
				Note.buildDebugNote("group-2", "The family includes tree squirrels, ground squirrels, chipmunks, marmots (including woodchucks), flying squirrels, and prairie dogs."),
				Note.buildDebugNote("group-6", "Squirrels are indigenous to the Americas, Eurasia, and Africa, and have been introduced to Australia"),
				Note.buildDebugNote("group-6", "The earliest known squirrels date from the Eocene and are most closely related to the mountain beaver and to the dormouse among living rodent families."),
				Note.buildDebugNote("group-7", "The word 'squirrel', first specified in 1327, comes from Anglo-Norman esquirel from the Old French escurel, the reflex of a Latin word sciurus.")
				);
	}	

}
