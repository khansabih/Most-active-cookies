package com.example.cookies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class CookieCounter {

    static Map<String, Integer> countByCookie(InputStream in, LocalDate targetDateUtc, boolean verbose) throws IOException {
        Objects.requireNonNull(in, "input stream");
        Objects.requireNonNull(targetDateUtc, "targetDateUtc");
        Map<String, Integer> counts = new HashMap<>(1024);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                CsvLine parsed;
                try {
                    parsed = CsvLine.parse(line);
                } catch (CsvLine.HeaderLine hl) {
                    // Skip header
                    if (verbose) System.err.println("[info] Skipping header at line " + lineNo);
                    continue;
                } catch (IllegalArgumentException ex) {
                    // Treat any malformed line as a parse error
                    throw new IOException("Parse error at line " + lineNo + ": " + ex.getMessage(), ex);
                }

                // Compare date in UTC
                LocalDate lineDateUtc = parsed.timestamp.toLocalDate();
                if (lineDateUtc.equals(targetDateUtc)) {
                    counts.merge(parsed.cookie, 1, Integer::sum);
                }
            }
        }
        return counts;
    }

    static List<String> mostActive(Map<String, Integer> counts) {
        if (counts.isEmpty()) return List.of();
        int max = counts.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        List<String> winners = new ArrayList<>();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() == max) winners.add(e.getKey());
        }
        // Deterministic output
        Collections.sort(winners);
        return winners;
    }
}
