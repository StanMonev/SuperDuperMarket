package com.monev.superdupermarkt.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.monev.superdupermarkt.util.MeatTypeEnum;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MeatTest {

    private Meat meat;
    private Meat vacuumPackedMeat;
    private LocalDate validExpiryDate;
    private LocalDate validVacuumPackedExpiryDate;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();
        validExpiryDate = today.plusDays(3); // Valid for fresh meat (2-5 days)
        validVacuumPackedExpiryDate = today.plusDays(9); // Valid for vacuum-packed meat (8-10 days)
        
        meat = new Meat("MT001", "Beef Steak", 60, validExpiryDate, 15.00, MeatTypeEnum.BEEF, false);
        vacuumPackedMeat = new Meat("MT002", "Lamb Chops", 62, validVacuumPackedExpiryDate, 20.00, MeatTypeEnum.LAMB, true);
    }

    @Test
    void testValidMeatCreation() {
        assertNotNull(meat);
        assertEquals("Beef Steak", meat.getName());
        assertEquals(60, meat.getQuality());
        assertEquals(validExpiryDate, meat.getExpiryDate());
    }

    @Test
    void testInvalidMeatQualityBelowThreshold() {
        LocalDate today = LocalDate.now();
        LocalDate validDate = today.plusDays(3);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Meat("MT003", "Chicken Breast", 49, validDate, 8.00, MeatTypeEnum.POULTRY, false)
        );

        assertEquals("Meat quality must be at least 50 to be put on stands.", exception.getMessage());
    }

    @Test
    void testInvalidFreshMeatExpiryDate() {
        LocalDate invalidExpiryDate = LocalDate.now().plusDays(6); // Invalid for fresh meat (should be 2-5 days)
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Meat("MT004", "Pork Loin", 55, invalidExpiryDate, 10.00, MeatTypeEnum.PORK, false)
        );

        assertEquals("Meat expiry date must be between 2 and 5 days for fresh meat.", exception.getMessage());
    }

    @Test
    void testInvalidVacuumPackedMeatExpiryDate() {
        LocalDate invalidExpiryDate = LocalDate.now().plusDays(11); // Invalid for vacuum-packed meat (should be 8-10 days)
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Meat("MT005", "Venison", 58, invalidExpiryDate, 18.00, MeatTypeEnum.VENISON, true)
        );

        assertEquals("Meat expiry date must be between 8 and 10 days for vacuum packed meat.", exception.getMessage());
    }

    @Test
    void testMeatQualityDecrease() {
        meat.updateQuality();
        assertEquals(59, meat.getQuality());

        vacuumPackedMeat.updateQuality();
        assertEquals(61, vacuumPackedMeat.getQuality());
    }

    @Test
    void testExpiredMeatQualityDecrease() {
        Meat expiredMeat = new Meat("MT006", "Expired Pork", 55, LocalDate.now().plusDays(3), 10.00, MeatTypeEnum.PORK, false);
        expiredMeat.setExpiryDate(LocalDate.now().minusDays(3));
        expiredMeat.updateQuality();
        assertEquals(53, expiredMeat.getQuality());
    }

    @Test
    void testToString() {    	
		StringBuilder sb = new StringBuilder();
		sb.append("Product ID: " + meat.getId());
		sb.append(System.getProperty("line.separator"));
		sb.append("Product Name: " + meat.getName());
		sb.append(System.getProperty("line.separator"));
		sb.append("Current Quality: " + meat.getQuality());
		sb.append(System.getProperty("line.separator"));
		sb.append("Expiry Date: " + meat.getExpiryDate());
		sb.append(System.getProperty("line.separator"));
		sb.append("Daily Price: " + meat.calculateDailyPrice() + "€");
		sb.append(System.getProperty("line.separator"));
    	sb.append("Should be removed from shells: ");
    	sb.append((meat.getQuality() <= 50) ? "Yes" : "No");
		sb.append(System.getProperty("line.separator"));
		
        assertEquals(sb.toString(), meat.toString());
    }
}

