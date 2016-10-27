package org.tonquin.shop;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class PromotionTest {

	@Test
	public void name() throws Exception {


	}

	/**
	 * Promotion Number Of Active Days
	 */
	@Test
	public void testExpired() {
		
		LocalDate startDate =  LocalDate.of(2014, 11, 10);
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
		
		new Promotion( LocalDate.now(), 31, null);
		
	}
		
}
