package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;
import com.monev.superdupermarkt.util.MeatType;

public class Meat extends Product {
	
    private MeatType meatType;
    private boolean isVacuumPacked;

	public Meat(String id, String name, double quality, LocalDate expiryDate, double defaultPrice, MeatType meatType, boolean isVacuumPacked) {
		super(id, name, quality, expiryDate, defaultPrice);
		
        if (quality < 50) {
            throw new IllegalArgumentException("Meat quality must be at least 50 to be put on stands.");
        }
        
        if (!isVacuumPacked && (getDifferenceToExpiryDate() < 2 || getDifferenceToExpiryDate() > 5)) {
            throw new IllegalArgumentException("Meat expiry date must be between 2 and 5 days for fresh meat.");
        } else if(isVacuumPacked && getDifferenceToExpiryDate() < 8 || getDifferenceToExpiryDate() > 10) {
            throw new IllegalArgumentException("Meat expiry date must be between 8 and 10 days for vacuum packed meat.");
        }

        this.meatType = meatType;
        this.isVacuumPacked = isVacuumPacked;
        
        if(!isVacuumPacked && expiryDate == null) {
        	setExpiryDate(calculateExpiryDate());
        }
	}
	
	public MeatType getMeatType() {
		return meatType;
	}

	
	public boolean isVacuumPacked() {
		return isVacuumPacked;
	}

	@Override
	public void updateQuality() {
        if (isExpired()) {
            setQuality(getQuality() - 2); // Meat loses quality faster after expiration
        } else {
            setQuality(getQuality() - 1); // Meat loses 1 quality per day before expiration
        }

        if (getQuality() < 0) {
            setQuality(0); // Ensure quality doesn't go below 0
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
    	sb.append((getQuality() <= 50) ? "Yes" : "No");
		sb.append(System.getProperty("line.separator"));
    	return sb.toString();
    }
	
    private LocalDate calculateExpiryDate() {
        int daysToAdd =  meatType.getFreshDays();
        return LocalDate.now().plusDays(daysToAdd);
    }

}
