package core;

import java.io.IOException;
import java.sql.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controllers.WorkoutDatabaseController;
import data.DatabaseHandler;

public class WorkoutDatabaseControllerTest {
	
	WorkoutDatabaseController wdc;
	Workout w1, w2;
	
	@BeforeClass
	public static void beforeClass() {
		try {
			DatabaseHandler.database(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	@Test
	public void testRetrieveExpectPersistedObject() {
		w1 = new Workout(wdc.create(w1), w1.getTimestamp(), w1.getDuration(), w1.getForm(), w1.getPerformance(), w1.getNote());
		w2 = wdc.retrieve(w1.getWorkoutID());
		
		Assert.assertEquals(w2.getWorkoutID(), w1.getWorkoutID());
		Assert.assertEquals(w2.getTimestamp(), w1.getTimestamp());
		Assert.assertEquals(w2.getDuration(), w1.getDuration());
		Assert.assertEquals(w2.getForm(), w1.getForm());
		Assert.assertEquals(w2.getPerformance(), w1.getPerformance());
		Assert.assertEquals(w2.getNote(), w1.getNote());
	}
	
	@Test
	public void testUpdateExpectPersistedObjet() {
		w1 = new Workout(wdc.create(w1), w1.getTimestamp(), w1.getDuration(), w1.getForm(), w1.getPerformance(), w1.getNote());
		w2 = new Workout(w1.getWorkoutID(), w2.getTimestamp(), w2.getDuration(), w2.getForm(), w2.getPerformance(), w2.getNote());
		wdc.update(w2);
		w2 = wdc.retrieve(w1.getWorkoutID());
		
		Assert.assertEquals(w1.getWorkoutID(), w2.getWorkoutID());
		Assert.assertNotEquals(w2.getNote(), w1.getNote());
	}
	
	@Test
	public void testDeleteExpectNull() {
		w1 = new Workout(wdc.create(w1), w1.getTimestamp(), w1.getDuration(), w1.getForm(), w1.getPerformance(), w1.getNote());
		wdc.delete(w1.getWorkoutID());
		w2 = wdc.retrieve(w1.getWorkoutID());
		
		Assert.assertEquals(null, w2);
	}
	
	@AfterClass
	public static void wipeDatabase() {
		try {
			DatabaseHandler.database(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
