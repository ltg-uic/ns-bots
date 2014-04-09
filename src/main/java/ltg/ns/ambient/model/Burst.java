package ltg.ns.ambient.model;

import com.google.common.collect.ImmutableList;

public class Burst {

	private int id;
	private String author;
	private String classroom;
	private String school;
	private String created_at;
	private ImmutableList<String> images;

	// private constructor to prevent creation without builder
	private Burst(int id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Burst)obj).id ;
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}

	
	// Fluent interface for notes creation
	public static Burst buildBurstWithId(int id) {
		return new Burst(id);
	}

	public Burst school(String string) {
		this.school = string;
		return this;
	}

	public Burst classroom(String string) {
		this.classroom = string;
		return this;
	}

	public Burst author(String string) {
		this.author = string;
		return this;
	}
	
	public Burst createdAt(String string) {
		this.created_at = string;
		return this;
	}
	
	public Burst images(ImmutableList<String> images) {
		this.images = ImmutableList.copyOf(images);
		return this;
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getClassroom() {
		return classroom;
	}

	public String getSchool() {
		return school;
	}

	public String getCreated_at() {
		return created_at;
	}

	public ImmutableList<String> getImages() {
		return images;
	}
	
	


}
