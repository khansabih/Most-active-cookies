package com.example.cookies;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

final class CsvLine {
    final String cookie;
    final OffsetDateTime timestamp;

    private CsvLine(String cookie, OffsetDateTime timestamp) {
        this.cookie = cookie;
        this.timestamp = timestamp;
    }

    static CsvLine parse(String line) throws IllegalArgumentException {
        // Trim and quick skip empty/comment lines
        if (line == null) throw new IllegalArgumentException("Null line");
        String t = line.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("Empty line");

        // CSV: cookie,timestamp
        int idx = t.indexOf(',');
        if (idx <= 0 || idx == t.length() - 1)
            throw new IllegalArgumentException("Malformed CSV: " + line);

        String cookie = t.substring(0, idx).trim();
        String ts = t.substring(idx + 1).trim();
        if (cookie.isEmpty() || ts.isEmpty())
            throw new IllegalArgumentException("Missing fields: " + line);

        // Header guard: "cookie,timestamp"
        if (cookie.equalsIgnoreCase("cookie") && ts.equalsIgnoreCase("timestamp")) {
            // Represent as a special header marker via exception type caller can detect;
            // Here we throw with a sentinel message and let caller skip.
            throw new HeaderLine();
        }

        try {
            OffsetDateTime odt = OffsetDateTime.parse(ts);
            // Normalize any offset to UTC to make date extraction unambiguous
            odt = odt.withOffsetSameInstant(ZoneOffset.UTC);
            return new CsvLine(cookie, odt);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Bad timestamp: " + ts, ex);
        }
    }

    // Marker exception to indicate header line
    static final class HeaderLine extends IllegalArgumentException {
        HeaderLine() { super("HEADER"); }
    }
}
