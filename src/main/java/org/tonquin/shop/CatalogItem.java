package org.tonquin.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;

import lombok.*;

import org.joda.time.LocalDate;

@Data
public class CatalogItem {

    static BigDecimal ONE_HUNDRED = new BigDecimal("100").setScale(0);
    private String name;
    private BigDecimal price;
    private LocalDate priceSetDate;
    private Promotion promotion;


    /**
     * Create a new catalog item
     *
     * @param name         item name
     * @param price        the item price
     * @param priceSetDate date the price was effective, if null will be set to current time
     */
    public CatalogItem(String name, BigDecimal price, LocalDate priceSetDate) {
        super();
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.priceSetDate = priceSetDate;
    }


    public boolean hasPromotion() {

        return promotion == null ? false : true;
    }

    public boolean hasPromotionExpired() {

        return promotion == null ? true : promotion.hasPromotionExpired();
    }

    public void cancelPromotion() {

        if (hasPromotion()) {
            promotion.cancelPromotion();
        }

    }


}
