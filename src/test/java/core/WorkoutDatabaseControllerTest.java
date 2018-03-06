package core;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WorkoutDatabaseControllerTest {
	
	WorkoutDatabaseController wdc;
	Workout w1, w2;
	
	@BeforeClass
	public static void beforeClass() {
		CreateDatabase.main(null);

	}
	
	@Before
	public void setup() {
		wdc = new WorkoutDatabaseController();
		w1 = new Workout(new Date(1), 1, 5, 5, "note");
		w2 = new Workout(new Date(2), 2, 6, 6, "note2");
	}

	@Test
	public void testCreateExpectPersistedObject() {
		w1 = new Workout(wdc.create(w1), w1.getTimestamp(), w1.getDuration(), w1.getForm(), w1.getPerformance(), w1.getNote());
		
		Assert.assertNotEquals(-1, w1.getWorkoutID());
	}

}
