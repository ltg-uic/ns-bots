package ltg.ns.ambient.model;

public final class School {
	
	private final int id;
	private final String name;
	
	public School(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
