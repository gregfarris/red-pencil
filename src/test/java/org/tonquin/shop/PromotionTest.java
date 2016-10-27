package org.tonquin.shop;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.LocalDate;
import org.junit.Test;

public class PromotionTest {

	/**
	 * Promotion Number Of Active Days
	 */
	@Test
	public void testExpired() {
		
		LocalDate  startDate = new LocalDate(2014, 11, 10);
		Promotion aPromotion = new Promotion( startDate, 10, null);
		assertEquals("should have expired", true, aPromotion.hasPromotionExpired());
		
		startDate = LocalDate.now();
		aPromotion = new Promotion( startDate, 10, null);
		assertEquals("should have expired", false, aPromotion.hasPromotionExpired());
		
		
		
	}
	
	/** 
	 * A red pencil promotion lasts 30 days as the maximum length.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testOverThrityDays() {
		
		new Promotion( new LocalDate(), 31, null);
		
	}
		
}
