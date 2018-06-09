package ru.shipov.controller;

import ru.shipov.Vacancy;
import ru.shipov.model.Model;

import java.util.List;

public class Controller {

    private Model model;
    private String city;

    public Controller(Model model) {
        if (model == null)
            throw new IllegalArgumentException();
        this.model = model;
    }

    public List<Vacancy> getVacancies(String cityName) {
        city = cityName;
        return model.getVacancies(cityName);
    }

    public List<Vacancy> refresh() {
       return getVacancies(city);
    }
}
