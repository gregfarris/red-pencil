package org.tonquin.shop;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Promotion {

	private LocalDate start;
	private LocalDate end;
	private BigDecimal originalPrice;
	private LocalDate canceled;


    /**
	 * 
	 * @param start date for promotion
	 * @param lengthOfPromotion
	 */
	public Promotion(LocalDate start, int lengthOfPromotion, BigDecimal originalPrice) {
		super();
		this.start = start;
		this.end = start.plusDays(lengthOfPromotion);
		this.originalPrice = originalPrice;

		
		if(  lengthOfPromotion  > 30 ){
			throw new IllegalArgumentException( "Promotion can not be long than 30 days," +
		       " attempted to set promotion length to " + lengthOfPromotion);
		}
	}

    public void cancelPromotion(){
        canceled = LocalDate.now();
    }
    
	public boolean hasPromotionExpired(){

		return canceled != null || LocalDate.now().isAfter(end);

	}
}
