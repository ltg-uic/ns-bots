package ltg.ns.ambient.model;



public class Note {
	
	private String _id;
	private String author;
	private String classroom;
	private String school; 
	private String body_open;
	private String body_hypothesis;
	private String body_question;
	private String created_at;
	
	public Note(String _id, String school, String classroom, String author, String body_open, String body_hypothesis, String body_question, String created_at) {
		this._id = _id;
		this.classroom = classroom;
		this.author = author;
		this.school = school;
		this.body_open = body_open;
		this.body_hypothesis = body_hypothesis;
		this.body_question = body_question;
		this.created_at = created_at;
	}

	public String get_id() {
		return _id;
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

	public String getBodyOpen() {
		return body_open;
	}
	
	public String getBodyHypothesis() {
		return body_hypothesis;
	}
	
	public String getBodyQuestion() {
		return body_question;
	}

	public String getCreated_at() {
		return created_at;
	}
	
	@Override
	public boolean equals(Object obj) {
		return _id.equals(((Note)obj).get_id());
	}
	
	
	@Override
	public String toString() {	
		return author;
	}

	public String getRandomBody() {
		if (body_open!=null)
			return body_open;
		if (body_hypothesis!=null)
			return body_hypothesis;
		if (body_question!=null)
			return body_question;
		throw new RuntimeException("This note has no body... like... WAT!");
	}

}
