package com.zavadimka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zavadimka.model.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class jsonParsingTestWithJackson {
    private final ClassLoader cl = jsonParsingTestWithJackson.class.getClassLoader();
    String filePath = "files/supercar.json";

    String manufacturerToCheck = "Bugatti",
            modelToCheck = "Divo";
    int numberOfCylindersToCheck = 16,
            priceToCheck = 5_500_000;
    String[] featuresToCheck = new String[]{"aggressive design",
            "improved aerodynamics",
            "lightweight construction",
            "improved maneuverability",
            "limited edition of 40"};

    @DisplayName("Тест на парсинг json-файла с использованием библиотеки Jackson")
    @Test
    void jsonParsingTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream(filePath);
             Reader reader = new InputStreamReader(is)) {

            ObjectMapper objectMapper = new ObjectMapper();
            Car supercar = objectMapper.readValue(reader, Car.class);

            Assertions.assertEquals(manufacturerToCheck, supercar.manufacturer());
            Assertions.assertEquals(modelToCheck, supercar.model());
            Assertions.assertEquals(priceToCheck, supercar.price());
            Assertions.assertEquals(numberOfCylindersToCheck, supercar.carEngine().numberOfCylinders());

            Assertions.assertArrayEquals(
                    featuresToCheck,
                    supercar.features().toArray()
            );
        }
    }
}
