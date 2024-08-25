package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

public class Cheese extends Product {

	public Cheese(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
		super(id, name, quality, expiryDate, defaultPrice);
        if (quality < 30) {
            throw new IllegalArgumentException("Cheese quality must be at least 30 to be put on stands.");
        }
        if (50 >= getDifferenceToExpiryDate() || getDifferenceToExpiryDate() >= 100) {
            throw new IllegalArgumentException("Cheese expiry date must be between 50 and 100 days.");
        }
	}

    @Override
    public void updateQuality() {
        if (getQuality() > 0) {
        	setQuality(getQuality() - 1); // Cheese loses quality by 1 each day
        }
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpiryDate());
    }
    
    @Override
    public String toString() {
    	String desc = super.toString();
    	StringBuilder sb = new StringBuilder(desc);
    	sb.append("Should be removed from shells: ");
    	sb.append((getQuality() <= 30) ? "Yes" : "No");
		sb.append(System.getProperty("line.separator"));
    	return sb.toString();
    }

}
