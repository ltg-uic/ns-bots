package ltg.ns.ambient.model;

public enum Classrooms {
	BEN("ics", "ben", "http://drowsy.badger.encorelab.org/safari-ben/notes"),
	//AMANDA("ics", "amanda", "http://drowsy.badger.encorelab.org/safari-amanda/notes"),
	BZ_5AG("bzaeds", "bz_5ag", "http://drowsy.badger.encorelab.org/safari-5ag/notes"),
	//BZ_2("bzaeds", "", ""),
	//BZ_3("bzaeds", "", "")
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
