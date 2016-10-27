package org.tonquin.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class PriceService {

	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100").setScale(2);

	/**
	 * 
	 * @param item
	 * @param newPrice
	 */
	public void updateCatalogItemPrice(CatalogItem item, BigDecimal newPrice) {

		if (isPriceIncrease(item, newPrice)) {

			item.cancelPromotion();
			
		} else {
			
			if (item.hasPromotionExpired()) {
				
				if (isDifferenceBetweenFiveandThrityPercent(item.getPrice(), newPrice)) {

					if (item.getPriceSetDate().isBefore(LocalDate.now().minusDays(30))) {

						item.setPromotion(new Promotion(LocalDate.now(), 30, item.getPrice() ) );
					}
				}

			} else {
				
				BigDecimal percentageDifference = item.getPromotion().getOriginalPrice().subtract(newPrice).
						divide(item.getPromotion().getOriginalPrice(), 3, BigDecimal.ROUND_DOWN).multiply(ONE_HUNDRED);
				
				if( percentageDifference.abs().compareTo( new BigDecimal("30") ) > 0){
					
					item.cancelPromotion();
					
				}
				
			}
		}

		item.setPrice(newPrice.setScale(2, RoundingMode.HALF_EVEN));
		item.setPriceSetDate(LocalDate.now());

	}

	/**
	 * 
	 * @param item
	 * @param newPrice
	 * @return
	 */
	protected boolean isPriceIncrease(CatalogItem item, BigDecimal newPrice) {
		return newPrice.compareTo(item.getPrice()) == 1;
	}

	/**
	 * 
	 * @param currentPrice
	 * @param newPrice
	 * @return
	 */
	protected boolean isDifferenceBetweenFiveandThrityPercent(BigDecimal currentPrice, BigDecimal newPrice) {
		
		BigDecimal percentageDifference = currentPrice.subtract(newPrice).
				divide(currentPrice, 3, BigDecimal.ROUND_DOWN).multiply(ONE_HUNDRED);
		
		return percentageDifference.abs().compareTo(
				new BigDecimal("5").setScale(2)) >= 0 && 
					percentageDifference.abs().compareTo(new BigDecimal("30").setScale(2)) <= 0;
	}


}
