package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

/**
 * This class is the default extension of the product class.
 * The quality change of the CommonProduct is quality - 24 (for each hour of the day) * 0.01
 */
public class CommonProduct extends Product {

	public CommonProduct(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
		super(id, name, quality, expiryDate, defaultPrice);
	}

	@Override
	public void updateQuality() {
        if (getQuality() > 0) {
        	setQuality(getQuality() - 0.24);
        }
	}

	@Override
	public boolean isExpired() {
		return LocalDate.now().isAfter(getExpiryDate());
	}

}
