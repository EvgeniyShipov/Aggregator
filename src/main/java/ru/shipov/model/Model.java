package ru.shipov.model;

import ru.shipov.Vacancy;

import java.util.*;
import java.util.stream.Collectors;

public class Model {

    private Strategy strategy;

    public Model(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> getVacancies(String city) {
        return strategy.getProviders().stream()
                .map(provider -> provider.getJavaVacancies(city))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public enum Strategy {
        HH(new Provider(new HHStrategy())),
        MOIKRUG(new Provider(new MoikrugStrategy())),
        ALL(new Provider(new HHStrategy()), new Provider(new MoikrugStrategy()));

        final private Set<Provider> providers = new HashSet<>();

        Strategy(Provider... provider) {
            providers.addAll(Arrays.asList(provider));
        }

        public Set<Provider> getProviders() {
            return providers;
        }
    }
}
