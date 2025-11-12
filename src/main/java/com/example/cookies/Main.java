package com.example.cookies;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int code = run(args);
        if (code != 0) System.exit(code);
    }

    static int run(String[] args) {
        try {
            Cli.Options opt = Cli.parse(args, new java.io.PrintWriter(System.err, true));
            if (opt == null) return 2;
            if (opt.help) { Cli.printUsage(new java.io.PrintWriter(System.out, true)); return 0; }
            if (opt.version) { System.out.println(Cli.VERSION); return 0; }

            if (opt.verbose) {
                System.err.printf("[info] version=%s%n", Cli.VERSION);
                System.err.printf("[info] file=%s date=%s%n", opt.file, opt.dateUtc);
            }

            try (InputStream in = open(opt.file)) {
                var counts = CookieCounter.countByCookie(in, opt.dateUtc, opt.verbose);
                List<String> winners = CookieCounter.mostActive(counts);
                for (String w : winners) {
                    System.out.println(w);
                }
                if (opt.verbose) {
                    if (winners.isEmpty()) System.err.println("[info] No cookies found for date.");
                    else System.err.printf("[info] winners=%s%n", String.join(",", winners));
                }
            } catch (java.io.FileNotFoundException fnf) {
                System.err.println("ERROR: file not found: " + opt.file);
                return 3;
            } catch (java.io.IOException ioe) {
                System.err.println("ERROR: " + ioe.getMessage());
                return 4;
            }

            return 0;
        } catch (Throwable t) {
            System.err.println("FATAL: " + t);
            return 5;
        }
    }

    private static InputStream open(String file) throws java.io.IOException {
        if ("-".equals(file)) {
            return System.in;
        }
        return new FileInputStream(file);
    }
}
