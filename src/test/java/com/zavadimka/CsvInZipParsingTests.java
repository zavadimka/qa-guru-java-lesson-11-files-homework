package com.zavadimka;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class СsvInZipParsingTests {

    private final ClassLoader cl = СsvInZipParsingTests.class.getClassLoader();
    String filePath = "files/supercars_csv.zip";
    String[] titlesToCheck = new String[]{
            "Производитель",
            "Модель",
            "Двигатель",
            "Объем",
            "Мощность",
            "Крутящий момент",
            "Трансмиссия",
            "Количество передач",
            "Привод",
            "Разгон до 100 км/ч",
            "Максимальная скорость",
            "Цена",
            "Тираж"};

    String[] bugattiDivo = new String[]{
            "Bugatti",
            "Divo",
            "W16",
            "8.0 л",
            "1500 л.с.",
            "1600 Нм",
            "автоматическая",
            "7",
            "полный",
            "2.4 сек",
            "380 км/ч",
            "5.5 млн евро",
            "40 экземпляров"};

    @DisplayName("Тест на парсинг csv-файлов, запакованных в zip-архив")
    @Test
    void csvInZipParsingTest() throws Exception {

        try (
                InputStream is = cl.getResourceAsStream(filePath);
                ZipInputStream zis = new ZipInputStream(is);
                CSVReader csvReader = new CSVReader(new InputStreamReader(zis))
        ) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    csvParsingFromZipInputStream(zis, csvReader);
                }
            }
        }
    }

    // Метод для парсинга csv-файла из потока ZipInputStream, без создания временного csv-файла
    void csvParsingFromZipInputStream(ZipInputStream zis, CSVReader csvReader) throws Exception {

        List<String[]> content = csvReader.readAll();

        Assertions.assertArrayEquals(
                titlesToCheck, content.get(0)
        );
        Assertions.assertArrayEquals(
                bugattiDivo, content.get(1)
        );
    }

    // Метод для парсинга csv-файла с созданием временного csv-файла
    void csvParsingWithCreatingTempCsv(ZipInputStream zis, ZipEntry entry) throws Exception {

        String fileName = entry.getName();
        String fileNameWithoutExtension = fileName.substring(0, fileName.length() - 4);
        File unzippedTempFile = File.createTempFile(fileNameWithoutExtension, ".csv");
        Files.copy(zis, unzippedTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try (CSVReader csvReader = new CSVReader(new FileReader(unzippedTempFile))) {
            List<String[]> content = csvReader.readAll();

            Assertions.assertArrayEquals(
                    titlesToCheck, content.get(0)
            );
            Assertions.assertArrayEquals(
                    bugattiDivo, content.get(1)
            );
        }
        unzippedTempFile.delete();
    }
}