package ltg.ns.ambient.model;


public enum Classrooms {
	BEN("ics", "ben", "http://drowsy.badger.encorelab.org/safari-ben/notes"),
	AMANDA("ics", "amanda", "http://drowsy.badger.encorelab.org/safari-amanda/notes"),
	BZ_7MS("bzaeds", "7MS", "http://drowsy.badger.encorelab.org/safari-7MS/notes"),
	BZ_7BL("bzaeds", "7BL", "http://drowsy.badger.encorelab.org/safari-7BL/notes"),
	BZ_7DM("bzaeds", "7DM", "http://drowsy.badger.encorelab.org/safari-7DM/notes")
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
	
	public static String getSchooForClass(String clazzroom) {
		String school = "all";
		for(Classrooms t: Classrooms.values())
			if (t.classroom.equals(clazzroom)) {
				school = t.school;
				break;
			}
		return school;
	}
	 
}
