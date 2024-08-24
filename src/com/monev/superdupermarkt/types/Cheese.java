package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

public class Cheese extends Product {

	public Cheese(String id, double quality, LocalDate expiryDate, double defaultPrice) {
		super(id, quality, expiryDate, defaultPrice);
        if (quality < 30) {
            throw new IllegalArgumentException("Cheese quality must be at least 30 to be put on stands.");
        }
        if (50 >= getDifferenceToExpiryDate() || getDifferenceToExpiryDate() >= 100) {
            throw new IllegalArgumentException("Cheese expiry date must be between 50 and 100 days.");
        }
	}

    @Override
    public void updateQuality() {
        if (quality > 0) {
            quality -= 1; // Cheese loses quality by 1 each day
        }
        
        if(quality <= 30) {
        	System.err.println("This product must to be removed from the stands!");
        }
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpiryDate());
    }

}
