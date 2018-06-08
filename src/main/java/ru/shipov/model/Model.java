package ru.shipov.model;

import ru.shipov.Vacancy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Model {

    private Provider[] providers;

    public Model(Provider... providers) {
        if (providers == null || providers.length == 0)
            throw new IllegalArgumentException();
        this.providers = providers;
    }

    public List<Vacancy> getVacancies(String city) {
        return Arrays.stream(providers)
                .map(provider -> provider.getJavaVacancies(city))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
