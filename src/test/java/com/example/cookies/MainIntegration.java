package com.example.cookies;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainIntegrationTest {

    @Test
    void integrationWithHeader() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/sample_with_header.csv")) {
            var counts = CookieCounter.countByCookie(in, LocalDate.parse("2018-12-09"), false);
            var winners = CookieCounter.mostActive(counts);
            assertEquals(List.of("AtY0laUfhglK3lC7"), winners);
        }
    }

    @Test
    void integrationNoHeader() throws Exception {
        // For 2018-12-08 in sample_no_header.csv there is a 3-way tie (each occurs once).
        try (InputStream in = getClass().getResourceAsStream("/sample_no_header.csv")) {
            var counts = CookieCounter.countByCookie(in, LocalDate.parse("2018-12-08"), false);
            var winners = CookieCounter.mostActive(counts);
            assertEquals(
                List.of("4sMM2LxV07bPJzwf", "SAZuXPGUrfbcn5UA", "fbcn5UAVanZf6UtG"),
                winners
            );
        }
    }
}
