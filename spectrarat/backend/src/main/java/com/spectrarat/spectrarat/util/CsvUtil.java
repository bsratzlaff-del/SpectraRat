package com.spectrarat.spectrarat.util;

public class CsvUtil {

    /**
     * Helper to escape characters for CSV format.
     * @param data The string to escape.
     * @return A CSV-safe string.
     */
    public static String escape(String data) {
        if (data == null) {
            return "";
        }
        // If the data contains a comma, quote, or newline, wrap it in double quotes.
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            // Escape existing double quotes by doubling them up.
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }
}

