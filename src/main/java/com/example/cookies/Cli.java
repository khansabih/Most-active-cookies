package com.example.cookies;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

final class Cli {
    static final String VERSION = "1.0.0";

    static final class Options {
        String file;           // path or "-" for stdin
        LocalDate dateUtc;     // parsed UTC date
        boolean verbose = false;
        boolean quiet = true;
        boolean help = false;
        boolean version = false;
    }

    static void printUsage(PrintWriter out) {
        out.println("Usage: most-active-cookie -f <file|-> -d <YYYY-MM-DD> [--verbose|--quiet] [--version] [--help]");
        out.println();
        out.println("  -f, --file      Input file path or '-' for STDIN");
        out.println("  -d, --date      Target date in UTC (YYYY-MM-DD)");
        out.println("      --verbose   Show diagnostics on stderr");
        out.println("      --quiet     Suppress diagnostics (default)");
        out.println("      --version   Print version and exit");
        out.println("      --help      Print this help and exit");
    }

    static Options parse(String[] args, PrintWriter err) {
        Options opt = new Options();
        if (args == null || args.length == 0) {
            opt.help = true;
            return opt;
        }
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            switch (a) {
                case "-f":
                case "--file":
                    if (i + 1 >= args.length) {
                        err.println("ERROR: Missing value for " + a);
                        return null;
                    }
                    opt.file = args[++i];
                    break;
                case "-d":
                case "--date":
                    if (i + 1 >= args.length) {
                        err.println("ERROR: Missing value for " + a);
                        return null;
                    }
                    String dateStr = args[++i];
                    try {
                        opt.dateUtc = LocalDate.parse(dateStr);
                    } catch (DateTimeParseException ex) {
                        err.println("ERROR: Invalid date format for -d/--date, expected YYYY-MM-DD: " + dateStr);
                        return null;
                    }
                    break;
                case "--verbose":
                    opt.verbose = true; opt. quiet = false;
                    break;
                case "--quiet":
                    opt.verbose = false; opt.quiet = true;
                    break;
                case "--help":
                    opt.help = true;
                    break;
                case "--version":
                    opt.version = true;
                    break;
                default:
                    err.println("ERROR: Unknown argument: " + a);
                    return null;
            }
        }
        if (opt.help || opt.version) return opt;
        if (isBlank(opt.file) || opt.dateUtc == null) {
            err.println("ERROR: -f/--file and -d/--date are required.");
            return null;
        }
        return opt;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
