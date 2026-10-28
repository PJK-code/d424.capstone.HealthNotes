package com.example.d424capstone.utils;

import com.example.d424capstone.entities.HealthNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthReportGenerator {

    // report of health notes organized by year
    public static String generateReportText(List<HealthNote> allNotes) {
        if (allNotes == null || allNotes.isEmpty()) {
            return null;
        }

        // group notes by year
        Map<Integer, List<HealthNote>> notesByYear = new TreeMap<>(Collections.reverseOrder());
        for (HealthNote note : allNotes) {
            int year = extractYear(note.getDate());
            notesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(note);
        }

        StringBuilder reportBuilder = new StringBuilder();
        String genDateStr = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());

        reportBuilder.append("HEALTH NOTES REPORT\n");
        reportBuilder.append("Generated on: ").append(genDateStr).append("\n");
        reportBuilder.append("Organized by Year\n");
        reportBuilder.append("========================================\n\n");

        for (Map.Entry<Integer, List<HealthNote>> entry : notesByYear.entrySet()) {
            int year = entry.getKey();
            List<HealthNote> yearNotes = entry.getValue();

            reportBuilder.append("=== Year: ").append(year).append(" ===\n\n");

            for (HealthNote note : yearNotes) {
                String title = note.getTitle() != null && !note.getTitle().isEmpty() ? note.getTitle() : "Untitled";
                String noteType = note.getNoteType() != null ? note.getNoteType() : "Illness";
                String bodyLoc = note.getBodyLocation() != null && !note.getBodyLocation().isEmpty() ? note.getBodyLocation() : "N/A";
                String date = note.getDate() != null ? note.getDate() : "N/A";
                int severity = note.getSeverity();
                String content = note.getContent() != null && !note.getContent().isEmpty() ? note.getContent() : "N/A";

                reportBuilder.append("• ").append(title).append("\n");
                reportBuilder.append("  Type: ").append(noteType)
                        .append(" | Location: ").append(bodyLoc)
                        .append(" | Date: ").append(date)
                        .append(" | Severity: ").append(severity).append("/5\n");
                reportBuilder.append("  Details: ").append(content).append("\n\n");
            }
        }

        return reportBuilder.toString();
    }

    private static int extractYear(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return Calendar.getInstance().get(Calendar.YEAR);
        }
        Pattern pattern = Pattern.compile("(20\\d{2}|19\\d{2})");
        Matcher matcher = pattern.matcher(dateStr);
        if (matcher.find()) {
            String group1 = matcher.group(1);
            if (group1 != null) {
                try {
                    return Integer.parseInt(group1);
                } catch (NumberFormatException ignored) {}
            }
        }
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
