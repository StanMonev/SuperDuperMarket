package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

public class CommonProduct extends Product {

	public CommonProduct(String id, double quality, LocalDate expiryDate, double defaultPrice) {
		super(id, quality, expiryDate, defaultPrice);
	}

	@Override
	public void updateQuality() {
        if (quality > 0) {
            quality -= 0.24;
        }
	}

	@Override
	public boolean isExpired() {
		return LocalDate.now().isAfter(getExpiryDate());
	}

}
