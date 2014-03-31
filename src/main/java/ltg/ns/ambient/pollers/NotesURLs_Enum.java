package ltg.ns.ambient.pollers;

public enum NotesURLs_Enum {
	BEN("http://drowsy.badger.encorelab.org/safari-ben/notes"),
	//AMANDA("http://drowsy.badger.encorelab.org/safari-amanda/notes"),
	BZ_5AG("http://drowsy.badger.encorelab.org/safari-5ag/notes"),
	//BZ_2("http://drowsy.badger.encorelab.org/safari-ben/notes"),
	//BZ_3("http://drowsy.badger.encorelab.org/safari-ben/notes")
	;
	
    private String url;

	private NotesURLs_Enum(String url) {
		this.url = url;
	}
	
	public String getURL() {
		return url;
	}
	 
}
