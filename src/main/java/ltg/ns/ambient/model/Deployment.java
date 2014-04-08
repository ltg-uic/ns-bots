package ltg.ns.ambient.model;

public final class Deployment {
	
	private final int id;
	private final int classroomId;
	private final String author;
	
	public Deployment(int id, int classroomId, String author) {
		this.id = id;
		this.classroomId = classroomId;
		this.author = author;
	}
	
	public int getId() {
		return id;
	}
		
	public int getClassroomId() {
		return classroomId;
	}
	
	public String getAuthor() {
		return author;
	}
}
