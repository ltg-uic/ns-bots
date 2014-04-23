package ltg.ns.ambient.model;




public class Note {

	public enum Type {
		planning, photo_set, open, cross_cutting;
	} 

	private String id;
	private String author;
	private String classroom;
	private String school; 
	private Type type;
	// TODO fix body and note type
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
		return id;
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

}
