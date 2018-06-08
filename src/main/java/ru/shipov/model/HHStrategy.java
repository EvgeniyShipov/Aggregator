package ru.shipov.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.shipov.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {

    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
    private static final String REFERRER = "no-referrer-when-downgrade";
    private static final String VACANCY = "[data-qa=vacancy-serp__vacancy]";
    private static final String TITLE = "[data-qa=vacancy-serp__vacancy-title]";
    private static final String CITY = "[data-qa=vacancy-serp__vacancy-address]";
    private static final String COMPANY_NAME = "[data-qa=vacancy-serp__vacancy-employer]";
    private static final String SALARY = "[data-qa=vacancy-serp__vacancy-compensation]";
    private static final String SITE = "http://hh.ua";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document document = getDocument(searchString, page++);
                Elements elements = document.select(VACANCY);
                if (elements.isEmpty())
                    break;

                elements.stream().map(this::getVacancy).forEach(vacancies::add);
            }
        } catch (IOException ignored) {
        }
        return vacancies;
    }

    private Vacancy getVacancy(Element element) {
        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(element.select(TITLE).text());
        vacancy.setCity(element.select(CITY).text());
        vacancy.setCompanyName(element.select(COMPANY_NAME).text());
        String salary = element.select(SALARY).text();
        vacancy.setSalary(salary != null ? salary : "");
        vacancy.setUrl(element.select(TITLE).attr("href"));
        vacancy.setSiteName(SITE);
        return vacancy;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .get();
    }
}
