package com.monev.superdupermarkt.types;

import com.monev.superdupermarkt.Product;
import java.time.LocalDate;

public class Wine extends Product {

	public Wine(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
        super(id, name, quality, expiryDate, defaultPrice);
        if (quality < 0) {
            throw new IllegalArgumentException("Wine quality must be at least 0.");
        }
    }

    @Override
    public void updateQuality() {
        LocalDate today = LocalDate.now();
        
        if(today.isAfter(getExpiryDate())) {
        	if(getQuality() < 50 && (Math.abs(getDifferenceToExpiryDate()) % 10 == 0)) {
        		setQuality(getQuality() + 1);
        	}
        }
    }

    @Override
    public boolean isExpired() {
        return false; // Wine does not expire
    }

    @Override
    public double calculateDailyPrice() {
        return getDefaultPrice(); // Price does not change after being put on the stands
    }

}
