package ru.shipov.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.shipov.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {

    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
    private static final String REFERRER = "https://moikrug.ru/";
    private static final String VACANCY = "[class=job]";
    private static final String VACANCY_MARKED = "[class = job marked]";
    private static final String TITLE = "[class=title]";
    // TODO: 09.06.2018 проверить тег - location, сейчас путаница с городами.
    private static final String CITY = "[class=location]";
    private static final String COMPANY_NAME = "[class=company_name]";
    private static final String SALARY = "[class=count]";
    private static final String SITE = "https://moikrug.ru";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document document = getDocument(searchString, page++);
                Elements elements = document.select(VACANCY);
                Elements elements2 = document.select(VACANCY_MARKED);
                if (elements.isEmpty())
                    break;

                elements.stream().map(this::getVacancy).forEach(vacancies::add);
                elements2.stream().map(this::getVacancy).forEach(vacancies::add);
            }
        } catch (IOException ignored) {
        }
        return vacancies;
    }

    private Vacancy getVacancy(Element element) {
        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(element.select(TITLE).text());
        String city = element.select(CITY).text();
        vacancy.setCity(city != null ? city : "");
        vacancy.setCompanyName(element.select(COMPANY_NAME).text());
        String salary = element.select(SALARY).text();
        vacancy.setSalary(salary != null ? salary : "");
        vacancy.setUrl("https://moikrug.ru" + element.select("a[class=job_icon]").attr("href"));
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
