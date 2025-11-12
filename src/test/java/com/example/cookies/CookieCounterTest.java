package com.example.cookies;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CookieCounterTest {

    @Test
    void countsSingleDayAndFindsWinner() throws Exception {
        String data = String.join("\n",
            "cookie,timestamp",
            "A,2018-12-09T01:00:00+00:00",
            "B,2018-12-09T02:00:00+00:00",
            "A,2018-12-09T03:00:00+00:00",
            "C,2018-12-08T23:00:00+00:00"
        );

        var in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        var counts = CookieCounter.countByCookie(in, LocalDate.parse("2018-12-09"), false);
        assertEquals(Map.of("A", 2, "B", 1), counts);

        var winners = CookieCounter.mostActive(counts);
        assertEquals(List.of("A"), winners);
    }

    @Test
    void tieIsSortedLexAsc() throws Exception {
        String data = String.join("\n",
            "A,2018-12-09T01:00:00+00:00",
            "B,2018-12-09T02:00:00+00:00",
            "C,2018-12-09T03:00:00+00:00"
        );
        var in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        var counts = CookieCounter.countByCookie(in, LocalDate.parse("2018-12-09"), false);
        var winners = CookieCounter.mostActive(counts);
        assertEquals(List.of("A","B","C"), winners);
    }

    @Test
    void emptyOnNoMatches() throws Exception {
        String data = String.join("\n",
            "A,2018-12-08T01:00:00+00:00",
            "B,2018-12-08T02:00:00+00:00"
        );
        var in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        var counts = CookieCounter.countByCookie(in, LocalDate.parse("2018-12-09"), false);
        assertTrue(counts.isEmpty());
        var winners = CookieCounter.mostActive(counts);
        assertTrue(winners.isEmpty());
    }

    @Test
    void rejectsBadLines() {
        String data = "A|2018-12-09T01:00:00+00:00\n";
        var in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        var exception = assertThrows(java.io.IOException.class, () ->
            CookieCounter.countByCookie(in, LocalDate.parse("2018-12-09"), false)
        );
        assertTrue(exception instanceof java.io.IOException);
    }
}
