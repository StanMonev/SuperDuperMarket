package com.monev.superdupermarkt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Product implements Cloneable{
	
    protected String id;
    protected double quality;
    protected LocalDate expiryDate;
    protected final double defaultPrice;

    public Product(String id, double quality, LocalDate expiryDate, double defaultPrice) {
        this.id = id;
        this.quality = quality;
        this.expiryDate = expiryDate;
        this.defaultPrice = defaultPrice;
    }

    public String getId() {
        return id;
    }

    public double getQuality() {
        return quality;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }
    
    public long getDifferenceToExpiryDate() {
    	return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

	public double calculateDailyPrice() {
        return  (double) Math.round((defaultPrice + 0.10 * quality) * 100) / 100;
	}

    public abstract void updateQuality();

    public abstract boolean isExpired();

	
	public void simulateQualityChange(long days) {
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(days);

        while (!currentDate.isAfter(endDate)) {
            System.out.println("Date: " + currentDate);
            System.out.println(this);

            // Update product's quality for the day
            this.updateQuality();

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }
	}
	
    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported", e);
        }
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product ID: " + this.getId());
		sb.append(System.getProperty("line.separator"));
		sb.append("Current Quality: " + this.getQuality());
		sb.append(System.getProperty("line.separator"));
		sb.append("Expiry Date: " + this.getExpiryDate());
		sb.append(System.getProperty("line.separator"));
		sb.append("Daily Price:" + this.calculateDailyPrice() + "€");
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

}
