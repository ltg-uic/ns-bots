package ltg.ns.ambient.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Note {
	
	private String _id;
	private String author;
	private String body;
	private String created_at;
	
	public Note(String _id, String author, String body, String created_at) {
		this._id = _id;
		this.author = author;
		this.body = body;
		this.created_at = created_at;
	}

	public String get_id() {
		return _id;
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
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
