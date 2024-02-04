package com.zavadimka.model;

import java.util.List;

public record Car(String manufacturer, String model, CarEngine carEngine, Transmission transmission, String drive,
                  Performance performance, int price, List<String> features) {
}

