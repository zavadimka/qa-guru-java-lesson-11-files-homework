package com.zavadimka;

import com.codeborne.pdftest.PDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class pdfInZipParsingTests {

    private final ClassLoader cl = pdfInZipParsingTests.class.getClassLoader();
    String filePath = "files/supercars_pdf.zip";
    String textToCheck = "t e c h n i c a l  s p e c i f i c a t i o n s";

    @DisplayName("Тест на парсинг pdf-файлов, запакованных в zip-архив")
    @Test
    void pdfInZipParsingTest() throws Exception {

        try (
                InputStream is = cl.getResourceAsStream(filePath);
                ZipInputStream zis = new ZipInputStream(is)
        ) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    pdfParsingFromZipInputStream(zis);
                }
            }
        }
    }

    // Метод для парсинга pdf-файла из потока ZipInputStream, без создания временного pdf-файла
    void pdfParsingFromZipInputStream(ZipInputStream zis) throws Exception {

        PDF unzippedPdf = new PDF(zis);

        Assertions.assertTrue(unzippedPdf.text.contains(textToCheck));
    }

    // Метод для парсинга pdf-файла с созданием временного pdf-файла
    void pdfParsingWithCreatingTempPdf(ZipInputStream zis, ZipEntry entry) throws Exception {

        String fileName = entry.getName();
        String fileNameWithoutExtension = fileName.substring(0, fileName.length() - 4);
        File unzippedTempFile = File.createTempFile(fileNameWithoutExtension, ".pdf");
        Files.copy(zis, unzippedTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        PDF unzippedPdf = new PDF(unzippedTempFile);

        Assertions.assertTrue(unzippedPdf.text.contains(textToCheck));

        unzippedTempFile.delete();
    }
}
