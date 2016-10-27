package org.tonquin.shop;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


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

        return promotion != null;
    }

    public boolean hasPromotionExpired() {

        return promotion == null || promotion.hasPromotionExpired();
    }

    public void cancelPromotion() {

        if (hasPromotion()) {
            promotion.cancelPromotion();
        }

    }


}
