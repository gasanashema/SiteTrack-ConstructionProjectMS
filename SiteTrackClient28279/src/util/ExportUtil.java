package util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExportUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // --- CSV EXPORT ---

    public static String generateCSV(List<?> dataList) {
        if (dataList == null || dataList.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = dataList.get(0).getClass();
        List<String> fieldNames = getFieldNames(clazz);

        // Header
        for (int i = 0; i < fieldNames.size(); i++) {
            sb.append(escapeCSV(fieldNames.get(i)));
            if (i < fieldNames.size() - 1) sb.append(",");
        }
        sb.append("\n");

        // Data
        for (Object obj : dataList) {
            List<Object> values = getFieldValues(obj);
            for (int i = 0; i < values.size(); i++) {
                Object val = values.get(i);
                String strVal = "";
                if (val != null) {
                    if (val instanceof BigDecimal) {
                        strVal = val.toString(); // Unformatted for CSV
                    } else if (val instanceof LocalDate) {
                        strVal = formatDate((LocalDate) val);
                    } else if (val instanceof LocalDateTime) {
                        strVal = formatTimestamp((LocalDateTime) val);
                    } else {
                        strVal = val.toString();
                    }
                }
                sb.append(escapeCSV(strVal));
                if (i < values.size() - 1) sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void writeCSVFile(String csvData, String filePath, String fileName) throws IOException {
        String fullPath = getFullPath(filePath, fileName, ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write(csvData);
        }
    }

    // --- EXCEL EXPORT (Apache POI) ---

    public static void writeExcelFile(List<?> dataList, String filePath, String fileName, String sheetName) throws IOException {
        String fullPath = getFullPath(filePath, fileName, ".xlsx");
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            headerStyle.setFont(font);

            // Data Style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            if (dataList == null || dataList.isEmpty()) {
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                cell.setCellValue("No data available");
                try (FileOutputStream outputStream = new FileOutputStream(fullPath)) {
                    workbook.write(outputStream);
                }
                return;
            }

            Class<?> clazz = dataList.get(0).getClass();
            List<String> fieldNames = getFieldNames(clazz);

            // Header Row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fieldNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fieldNames.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;
            for (Object obj : dataList) {
                Row row = sheet.createRow(rowNum++);
                List<Object> values = getFieldValues(obj);
                for (int i = 0; i < values.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(dataStyle);
                    
                    Object val = values.get(i);
                    if (val != null) {
                        if (val instanceof Number) {
                            cell.setCellValue(((Number) val).doubleValue());
                        } else if (val instanceof LocalDate) {
                            cell.setCellValue(formatDate((LocalDate) val));
                        } else if (val instanceof LocalDateTime) {
                            cell.setCellValue(formatTimestamp((LocalDateTime) val));
                        } else {
                            cell.setCellValue(val.toString());
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < fieldNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(fullPath)) {
                workbook.write(outputStream);
            }
        }
    }

    // --- PDF EXPORT (iText 5) ---

    public static void writePDFFile(List<?> dataList, String filePath, String fileName, String titleStr, String subtitleStr) throws IOException {
        String fullPath = getFullPath(filePath, fileName, ".pdf");
        
        Document document = new Document(PageSize.A4.rotate()); // Landscape
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();

            // Header Section
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(27, 58, 107)); // Navy
            Paragraph title = new Paragraph(titleStr, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            if (subtitleStr != null && !subtitleStr.isEmpty()) {
                Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
                Paragraph subtitle = new Paragraph(subtitleStr, subtitleFont);
                subtitle.setAlignment(Element.ALIGN_CENTER);
                document.add(subtitle);
            }
            
            document.add(new Paragraph("Generated at: " + formatTimestamp(LocalDateTime.now())));
            document.add(new Paragraph(" ")); // Spacer

            if (dataList == null || dataList.isEmpty()) {
                document.add(new Paragraph("No data available for this report."));
                document.close();
                return;
            }

            Class<?> clazz = dataList.get(0).getClass();
            List<String> fieldNames = getFieldNames(clazz);

            // Table
            PdfPTable table = new PdfPTable(fieldNames.size());
            table.setWidthPercentage(100);

            // Header Row
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerBg = new BaseColor(27, 58, 107);
            
            for (String header : fieldNames) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Data Rows
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            for (Object obj : dataList) {
                List<Object> values = getFieldValues(obj);
                for (Object val : values) {
                    String strVal = "";
                    if (val != null) {
                        if (val instanceof BigDecimal) {
                            strVal = formatCurrency((BigDecimal) val);
                        } else if (val instanceof LocalDate) {
                            strVal = formatDate((LocalDate) val);
                        } else if (val instanceof LocalDateTime) {
                            strVal = formatTimestamp((LocalDateTime) val);
                        } else {
                            strVal = val.toString();
                        }
                    }
                    PdfPCell cell = new PdfPCell(new Phrase(strVal, dataFont));
                    cell.setPadding(4);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();
            
        } catch (DocumentException e) {
            throw new IOException("Failed to generate PDF document", e);
        }
    }

    // --- HELPERS ---

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) return "RWF 0.00";
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
        return format.format(amount);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMAT);
    }

    public static String formatTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(TIMESTAMP_FORMAT);
    }

    public static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static List<String> getFieldNames(Class<?> clazz) {
        List<String> names = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            String name = f.getName();
            // Capitalize and insert spaces (e.g., "fullName" -> "Full Name")
            name = name.replaceAll("([a-z])([A-Z]+)", "$1 $2");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            names.add(name);
        }
        return names;
    }

    public static List<Object> getFieldValues(Object obj) {
        List<Object> values = new ArrayList<>();
        if (obj == null) return values;
        for (Field f : obj.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            try {
                f.setAccessible(true);
                values.add(f.get(obj));
            } catch (IllegalAccessException e) {
                values.add("ERROR");
            }
        }
        return values;
    }

    private static String getFullPath(String filePath, String fileName, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String cleanFileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String fullPath = filePath;
        if (!fullPath.endsWith(File.separator)) {
            fullPath += File.separator;
        }
        fullPath += cleanFileName + "_" + timestamp + extension;
        return fullPath;
    }
}
