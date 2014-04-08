package ltg.ns.ambient.model;

public enum Classrooms {
	BEN("ics", "ben", "http://drowsy.badger.encorelab.org/safari-ben/notes"),
	AMANDA("ics", "amanda", "http://drowsy.badger.encorelab.org/safari-amanda/notes"),
	BZ_7MS("bzaeds", "7ms", "http://drowsy.badger.encorelab.org/safari-7ms/notes"),
	BZ_7BL("bzaeds", "7bl", "http://drowsy.badger.encorelab.org/safari-7bl/notes"),
	BZ_7DM("bzaeds", "7dm", "http://drowsy.badger.encorelab.org/safari-7dm/notes")
	;
	
    private String school;
    private String classroom;
    private String notes_url;

	private Classrooms(String school, String classroom, String url) {
		this.school = school;
		this.classroom = classroom;
		this.notes_url = url;
	}
	
	public String getSchool() {
		return school;
	}
	
	public String getClassroom() {
		return classroom;
	}
	
	public String getNotesURL() {
		return notes_url;
	}
	 
}
