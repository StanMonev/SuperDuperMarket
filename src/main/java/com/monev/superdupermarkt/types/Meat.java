package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;
import com.monev.superdupermarkt.util.MeatTypeEnum;

/**
 * Represents a type of meat product in the SuperDuperMarket system.
 * This class extends the Product class and adds specific attributes and behaviors
 * related to meat, such as its type and whether it is vacuum packed.
 */
public class Meat extends Product {
	
    private MeatTypeEnum meatType;
    private boolean isVacuumPacked;
    private final double startingQuality;

    /**
     * Constructs a Meat product with the specified properties.
     * Performs validation to ensure the quality and expiry date are appropriate based on whether
     * the meat is vacuum packed or not.
     *
     * @param id             The product ID
     * @param name           The product name
     * @param quality        The initial quality of the meat (must be at least 50)
     * @param expiryDate     The expiry date of the meat
     * @param defaultPrice   The base price of the meat
     * @param meatType       The type of meat
     * @param isVacuumPacked Whether the meat is vacuum packed
     */
	public Meat(String id, String name, double quality, LocalDate expiryDate, double defaultPrice, MeatTypeEnum meatType, boolean isVacuumPacked) {
		super(id, name, quality, expiryDate, defaultPrice);

        this.meatType = meatType;
        this.isVacuumPacked = isVacuumPacked;
        this.startingQuality = quality;
        
        // Automatically calculate expiration date if not vacuum packed and no expiration date is provided. 
        // Some fresh meat products do not have a set expiration date or it is unknown.
        if(!isVacuumPacked && expiryDate == null) {
        	setExpiryDate(calculateExpiryDate());
        }else if(!isVacuumPacked && expiryDate != null){
            if(getDifferenceToExpiryDate() < 1 || getDifferenceToExpiryDate() > 4) {
                throw new IllegalArgumentException("Meat expiry date must be between 1 and 4 days for fresh meat.");
            }
        }
		
        // Handle restraints
        if (quality < 50) {
            throw new IllegalArgumentException("Meat quality must be at least 50 to be put on stands.");
        }
        
        if(isVacuumPacked && getDifferenceToExpiryDate() < 4 || getDifferenceToExpiryDate() > 10) {
            throw new IllegalArgumentException("Meat expiry date must be between 5 and 10 days for vacuum packed meat.");
        }
	}
	
	public MeatTypeEnum getMeatType() {
		return meatType;
	}

	
	public boolean isVacuumPacked() {
		return isVacuumPacked;
	}

	
    /**
     * Updates the quality of the meat based on its expiry status.
     * Meat loses quality faster after its expiry date.
     */
	@Override
	public void updateQuality() {
        if (isExpired()) {
        	System.out.println("IsExpired QualityModifier: " + getQualityModifier() * 2);
            setQuality(getQuality() - (getQualityModifier() * 2)); // Meat loses quality faster after expiration
        } else {
        	System.out.println("QualityModifier: " + getQualityModifier());
            setQuality(getQuality() - getQualityModifier());
        }

        if (getQuality() < 0) {
            setQuality(0); // Ensure quality doesn't go below 0
        }
	}
    
    @Override
    public String toString() {
    	String desc = super.toString();
    	StringBuilder sb = new StringBuilder(desc);
    	sb.append("Is vacuum packed: ");
    	sb.append(isVacuumPacked ? "Yes" : "No");
		sb.append(System.getProperty("line.separator"));
    	
    	sb.append("Should be removed from shells: ");
    	sb.append((getQuality() <= 50) ? "Yes" : "No");
		sb.append(System.getProperty("line.separator"));
    	return sb.toString();
    }
	
    /**
     * Calculates the expiry date for fresh meat based on the meat type.
     * The expiry date is determined by adding a specific number of days to the current date.
     *
     * @return The calculated expiry date
     */
    private LocalDate calculateExpiryDate() {
        int daysToAdd =  meatType.getFreshDays();
        return LocalDate.now().plusDays(daysToAdd);
    }
    
    private int getQualityModifier() {
    	int daysInFridge = this.isVacuumPacked ? this.meatType.getVacuumedDays() : this.meatType.getFreshDays();
    	System.out.println("Days in fridge: " +daysInFridge);
    	return (int) ((this.startingQuality - 50) / daysInFridge); // 50 is the quality that the meat should drop below
    }

}
