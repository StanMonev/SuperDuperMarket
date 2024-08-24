package com.monev.superdupermarkt.types;

import com.monev.superdupermarkt.Product;
import java.time.LocalDate;

public class Wine extends Product {

	public Wine(String id, double quality, LocalDate expiryDate, double defaultPrice) {
        super(id, quality, expiryDate, defaultPrice);
        if (quality < 0) {
            throw new IllegalArgumentException("Wine quality must be at least 0.");
        }
    }

    @Override
    public void updateQuality() {
        LocalDate today = LocalDate.now();
        
        if(today.isAfter(getExpiryDate())) {
        	if(quality < 50 && (Math.abs(getDifferenceToExpiryDate()) % 10 == 0)) {
        		quality++;
        	}
        }
    }

    @Override
    public boolean isExpired() {
        return false; // Wine does not expire
    }

    @Override
    public double calculateDailyPrice() {
        return defaultPrice; // Price does not change after being put on the stands
    }

}
