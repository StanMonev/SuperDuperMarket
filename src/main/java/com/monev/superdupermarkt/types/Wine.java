package com.monev.superdupermarkt.types;

import com.monev.superdupermarkt.Product;
import java.time.LocalDate;


/**
 * Represents a type of wine product in the SuperDuperMarket system.
 * This class extends the Product class and adds specific behaviors related to wine,
 * such as the fact that wine quality can improve over time and it does not expire.
 */
public class Wine extends Product {

    /**
     * Constructs a Wine product with the specified properties.
     *
     * @param id            The product ID
     * @param name          The product name
     * @param quality       The initial quality of the wine (must be at least 0)
     * @param expiryDate    The expiry date of the wine, though wine does not actually expire
     * @param defaultPrice  The base price of the wine
     */
	public Wine(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
        super(id, name, quality, expiryDate, defaultPrice);
        if (quality < 0) {
            throw new IllegalArgumentException("Wine quality must be at least 0.");
        }
        
        //if the expiration date is not set -> set it to today.
        if(expiryDate == null) {
        	setExpiryDate(LocalDate.now());
        }
    }
	
    /**
     * Updates the quality of the wine based on its age.
     * Wine quality increases over time after its nominal expiry date, but only
     * if the quality is below 50. The quality improves by 1 point every 10 days.
     */
    @Override
    public void updateQuality() {        
        if(isExpired()) {
        	if(getQuality() < 50 && (Math.abs(getDifferenceToExpiryDate()) % 10 == 0)) {
        		setQuality(getQuality() + 1);
        	}
        }
    }

    @Override
    public double calculateDailyPrice() {
        return getDefaultPrice(); // Price does not change after being put on the stands
    }

}
