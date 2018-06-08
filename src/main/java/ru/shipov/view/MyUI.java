package ru.shipov.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import ru.shipov.Vacancy;
import ru.shipov.controller.Controller;
import ru.shipov.model.HHStrategy;
import ru.shipov.model.Model;
import ru.shipov.model.MoikrugStrategy;
import ru.shipov.model.Provider;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private Controller controller;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Provider hhProvider = new Provider(new HHStrategy());
        Provider moikrugProvider = new Provider(new MoikrugStrategy());
        Model model = new Model(hhProvider, moikrugProvider);
        controller = new Controller(model);
        createView();
    }

    private void createView() {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        Grid<Vacancy> vacancyGrid = new Grid<>();

        HorizontalLayout search = new HorizontalLayout();
        TextField textField = new TextField();
        textField.setPlaceholder("Введите город");
        Button button = new Button("Go", event -> vacancyGrid.setItems(controller.getVacancies(textField.getValue())));
        search.setMargin(true);
        search.addComponent(textField);
        search.addComponent(button);

        vacancyGrid.addColumn(Vacancy::getTitle).setCaption("Вакансия");
        vacancyGrid.addColumn(Vacancy::getSalary).setCaption("Зарплата");
        vacancyGrid.addColumn(Vacancy::getCity).setCaption("Город");
        vacancyGrid.addColumn(Vacancy::getCompanyName).setCaption("Компания");
        vacancyGrid.addColumn(Vacancy::getSiteName).setCaption("Сайт");
        vacancyGrid.addColumn(Vacancy::getUrl).setCaption("Ссылка");
        vacancyGrid.setItems();
        vacancyGrid.setWidth(70, Unit.PERCENTAGE);
        vacancyGrid.setHeight(60, Unit.PERCENTAGE);

        layout.addComponent(search);
        layout.addComponent(vacancyGrid);
        layout.setComponentAlignment(search, Alignment.BOTTOM_CENTER);
        layout.setComponentAlignment(vacancyGrid, Alignment.TOP_CENTER);
        layout.setExpandRatio(search, 0.4f);
        layout.setExpandRatio(vacancyGrid, 0.6f);
        layout.setMargin(true);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
