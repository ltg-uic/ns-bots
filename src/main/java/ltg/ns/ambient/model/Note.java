package ltg.ns.ambient.model;


public class Note {
	
	private String _id;
	private String author;
	private String classroom;
	private String school;
	private String body;
	private String created_at;
	
	public Note(String _id, String school, String classroom, String author, String body, String created_at) {
		this._id = _id;
		this.classroom = classroom;
		this.author = author;
		this.school = school;
		this.body = body;
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

	public String getBody() {
		return body;
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

}
