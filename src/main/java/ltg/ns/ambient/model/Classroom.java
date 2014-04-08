package ltg.ns.ambient.model;

public final class Classroom {
	
	private final int id;
	private final String name;
	private final int schoolId;
	
	public Classroom(int id, String name, int schoolId) {
		this.id = id;
		this.name = name;
		this.schoolId = schoolId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSchoolId() {
		return schoolId;
	}
	
	
	
	

}
