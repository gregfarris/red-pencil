package org.tonquin.shop;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.tonquin.shop.CatalogItem;

public class CatalogItemTest {

	static BigDecimal FIVE = new BigDecimal("5.00").setScale(2, BigDecimal.ROUND_HALF_UP);
	static BigDecimal FOUR_FIFTY = new BigDecimal("4.50").setScale(2, BigDecimal.ROUND_HALF_UP);
	static BigDecimal FOUR_SEVENTY_FIVE = new BigDecimal("4.75").setScale(2, BigDecimal.ROUND_HALF_UP);
	static BigDecimal TWO_FIFTY = new BigDecimal("2.50").setScale(2, BigDecimal.ROUND_HALF_UP);
	
	public PriceService priceService = new PriceService();	
	 
	public LocalDate thrityOneDaysAgo =  LocalDate.now().minusDays(31);
	public LocalDate thrityDaysAgo = LocalDate.now().minusDays(30);
	public LocalDate twentyNineDaysAgo = LocalDate.now().minusDays(29);
	public LocalDate rightNow = LocalDate.now();


	@Test
	public void setupTest(){


	}
	
	/**
	 * A red pencil promotion starts due to a price reduction. 
	 * The price has to be reduced by at least 5% and 
	 * the previous price had to be stable for at least 30 days.
	 */
	@Test
	public void testPromationFivePercent() {

		CatalogItem  redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.75") );
		assertTrue( "should have a promotion price reduced by 5%", redPencil.hasPromotion() );
		
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.76") );
		assertFalse( "should not have a promotion price reduced by less than 5%", redPencil.hasPromotion() );
		
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), twentyNineDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.75") );
		assertFalse( "should not have a promotion price reduced price not stable", redPencil.hasPromotion() );
		
		
	}
	/**
	 * A red pencil promotion starts due to a price reduction. The price has to be reduced by 
	 * at most be 30% and the previous price had to be stable for at least 30 days.
	 */
	@Test
	public void testPromationThrityPercent() {

		CatalogItem redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.00") );
		assertTrue( "should have promotion 20% reduction", redPencil.hasPromotion() );
		
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("3.50") );
		assertTrue( "should have promotion 30% reduction", redPencil.hasPromotion() );
		
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("3.49") );
		assertFalse( "should not have promotion over 30% reduction", redPencil.hasPromotion() );
				
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("4.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("5.00") );
		assertFalse( "should not have promotion increase price", redPencil.hasPromotion() );
				
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), twentyNineDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("3.50") );
		assertFalse( "should not have a promotion price reduced price not stable", redPencil.hasPromotion() );
		
	}
	/**
	 *  If the price is further reduced during the red pencil promotion the promotion 
	 *  will not be prolonged by that reduction.
	 */
	@Test
	public void testCurrentPromotion(){
		
		CatalogItem redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), twentyNineDaysAgo);
		Promotion promotion = new Promotion(twentyNineDaysAgo,3, new BigDecimal("5.00"));
		redPencil.setPromotion( promotion );	
		
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.00") );
		
		assertTrue( "should have promotion 20% reduction", redPencil.hasPromotion() );
		redPencil.getPromotion().getStart().isEqual( twentyNineDaysAgo );

		
	}
	
	/**
	 *  if reduced during the red pencil promotion so that the overall reduction is more 
	 *  than 30% with regard to the original price, the promotion is ended immediately.
	 */
	@Test
	public void testPriceReductionOverThrity(){

		CatalogItem redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.00") );
		assertTrue( "should have promotion 20% reduction", redPencil.hasPromotion() );
		
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("3.40") );
		
		assertTrue( "should not have a promotion price total reduction over 30%", redPencil.hasPromotionExpired() );
		
		
		redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), thrityOneDaysAgo);
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("4.00") );
		assertTrue( "should have promotion 20% reduction", redPencil.hasPromotion() );
		
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("3.50") );
		
		assertTrue( "should have a promotion total reduction is 30%", redPencil.hasPromotion() );

		
	}

	/**
	 *  If the price is increased during the red pencil promotion the promotion will be ended immediately.
	 */
	@Test
	public void testPriceIncrease(){
		
		CatalogItem redPencil = new CatalogItem("Red Pencil", new BigDecimal("5.00"), twentyNineDaysAgo);
		Promotion promotion = new Promotion(twentyNineDaysAgo,3,new BigDecimal("5.00"));
		redPencil.setPromotion( promotion );	
		
		priceService.updateCatalogItemPrice(redPencil,  new BigDecimal("6.00") );
		
		assertTrue( "increase should end promotion", redPencil.hasPromotionExpired() );


		
	}

    /**
     * After a red pencil promotion is ended additional red pencil promotions may follow – as long as the start condition is valid:
     * the price was stable for 30 days and these 30 days don’t intersect with a previous red pencil promotion.
     */
    @Test
    public void testPriceIncreaseOver30Percent(){

    }

	/**
	 * Test percent difference calculation works
	 */			
	@Test
	public void testPrecentagePriceDiffrence() {
		
		PriceService priceService = new PriceService();
		
		assertFalse("0% change",
				priceService.isDifferenceBetweenFiveandThrityPercent(
						new BigDecimal("5.00"),
						new BigDecimal("5.00") ) );
		
		assertTrue("5% change",
				priceService.isDifferenceBetweenFiveandThrityPercent(
						new BigDecimal("5.00"),
						new BigDecimal("4.75") ) );
		
		assertTrue("20% change",
				priceService.isDifferenceBetweenFiveandThrityPercent(
						new BigDecimal("5.00"),
						new BigDecimal("4.00") ) );
		
		assertTrue("30% change",
				priceService.isDifferenceBetweenFiveandThrityPercent(
						new BigDecimal("5.00"),
						new BigDecimal("3.50") ) );
		
		assertFalse("30% change",
				priceService.isDifferenceBetweenFiveandThrityPercent(
						new BigDecimal("5.00"),
						new BigDecimal("3.49") ) );
		

	}
	

}
