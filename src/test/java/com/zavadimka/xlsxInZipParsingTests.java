package com.zavadimka;

import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class xlsxInZipParsingTests {

    private final ClassLoader cl = xlsxInZipParsingTests.class.getClassLoader();
    String filePath = "files/companies.zip";
    String textToCheck = "NovoTech",
            sheetName = "Sheet1";
    int rowNumber = 1,
            cellNUmber = 0;


    @DisplayName("Тест на парсинг xlsx-файлов, запакованных в zip-архив")
    @Test
    void xlsxInZipParsingTest() throws Exception {

        try (
                InputStream is = cl.getResourceAsStream(filePath);
                ZipInputStream zis = new ZipInputStream(is);
        ) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    xlsxParsingFromZipInputStream(zis);
                }
            }
        }
    }


    // Метод для парсинга xlsx-файла из потока ZipInputStream, без создания временного xlsx-файла
    void xlsxParsingFromZipInputStream(ZipInputStream zis) throws Exception {

        XLS xlsx = new XLS(zis);

        Assertions.assertEquals(textToCheck,
                xlsx.excel.getSheet(sheetName)
                        .getRow(rowNumber)
                        .getCell(cellNUmber)
                        .getStringCellValue()
        );
    }

    // Метод для парсинга xlsx-файла с созданием временного xlsx-файла
    void xlsxParsingWithCreatingTempCsv(ZipInputStream zis, ZipEntry entry) throws Exception {

        String fileName = entry.getName();
        String fileNameWithoutExtension = fileName.substring(0, fileName.length() - 5);
        File unzippedTempFile = File.createTempFile(fileNameWithoutExtension, ".xlsx");
        Files.copy(zis, unzippedTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        XLS unzippedXlsx = new XLS(unzippedTempFile);

        Assertions.assertEquals(textToCheck,
                unzippedXlsx.excel.getSheet(sheetName)
                        .getRow(rowNumber)
                        .getCell(cellNUmber)
                        .getStringCellValue()
        );

        unzippedTempFile.delete();
    }
}
