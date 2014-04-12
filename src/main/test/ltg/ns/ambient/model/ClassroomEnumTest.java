package ltg.ns.ambient.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassroomEnumTest {
	
	@Test
	public void TestGetSchooForClass() {
		assertEquals("ics", Classrooms.getSchooForClass("ben"));
	}

}
