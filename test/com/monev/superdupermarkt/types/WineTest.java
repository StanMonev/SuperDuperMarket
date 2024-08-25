package com.monev.superdupermarkt.types;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WineTest {
	
    private Wine wine1;
    private Wine wine2;

    @BeforeEach
    public void setUp() {
        // Create a Cheese instance with valid parameters
        wine1 = new Wine("W002", "Chardonnay", 45.0, LocalDate.now().minusDays(10), 10.0);
        wine2 = new Wine("W003", "Chardonnay", 35.0, LocalDate.now().minusDays(7), 10.0);
    }

    @Test
    public void testWineQualityIncrease() {
        wine1.updateQuality();
        wine2.updateQuality();
        assertEquals(46, wine1.getQuality(), "Wine1 quality should increase by 1 every 10 days after expiry.");
        assertEquals(35, wine2.getQuality(), "Wine1 quality should not increase because it hasn't passed the 10 day mark.");
    }

    @Test
    public void testWineMaxQuality() {
        for (int i = 0; i < 10; i++) {
            wine1.updateQuality();
        }
        assertEquals(50, wine1.getQuality(), "Wine quality should not exceed 50.");
    }
}
