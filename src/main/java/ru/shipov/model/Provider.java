package ru.shipov.model;

import ru.shipov.Vacancy;

import java.util.List;

public class Provider {
    private Strategy strategy = new HHStrategy();

    public List<Vacancy> getJavaVacancies(String searchString) {
        return strategy.getVacancies(searchString);
    }

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


}
