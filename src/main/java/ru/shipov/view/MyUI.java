package ru.shipov.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import ru.shipov.Vacancy;
import ru.shipov.controller.Controller;
import ru.shipov.model.Model;
import ru.shipov.model.Model.Strategy;

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
    private Model model;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        model = new Model(Strategy.ALL);
        controller = new Controller(model);
        createView();
    }

    private void createView() {
        setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        // TODO: 09.06.2018 добавить приветсвие

        Grid<Vacancy> vacancyGrid = new Grid<>();
        configureGrid(vacancyGrid);

        HorizontalLayout search = new HorizontalLayout();
        TextField textField = new TextField();
        textField.setPlaceholder("Введите город");
        Button button = new Button("Go", event -> vacancyGrid.setItems(controller.getVacancies(textField.getValue())));
        search.setMargin(true);
        search.addComponent(textField);
        search.addComponent(button);

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.setSizeFull();
        RadioButtonGroup<String> radioButton = getRadioButton(vacancyGrid);
        gridLayout.addComponent(vacancyGrid);
        gridLayout.addComponent(radioButton);
        gridLayout.setComponentAlignment(vacancyGrid, Alignment.TOP_RIGHT);
        gridLayout.setComponentAlignment(radioButton, Alignment.TOP_LEFT);
        gridLayout.setExpandRatio(vacancyGrid, 0.8f);
        gridLayout.setExpandRatio(radioButton, 0.2f);

        mainLayout.addComponent(search);
        mainLayout.addComponent(gridLayout);
        mainLayout.setComponentAlignment(search, Alignment.BOTTOM_CENTER);
        mainLayout.setComponentAlignment(gridLayout, Alignment.TOP_CENTER);
        mainLayout.setExpandRatio(search, 0.35f);
        mainLayout.setExpandRatio(gridLayout, 0.65f);
        mainLayout.setMargin(true);
        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private void configureGrid(Grid<Vacancy> vacancyGrid) {
        vacancyGrid.addColumn(Vacancy::getTitle).setCaption("Вакансия");
        vacancyGrid.addColumn(Vacancy::getSalary).setCaption("Зарплата");
        vacancyGrid.addColumn(Vacancy::getCity).setCaption("Город");
        vacancyGrid.addColumn(Vacancy::getCompanyName).setCaption("Компания");
        // TODO: 09.06.2018 добавить кликабельную гиперссылку на вакансию
        vacancyGrid.addColumn(Vacancy::getUrl).setCaption("Ссылка");
        vacancyGrid.setItems();
        vacancyGrid.setWidth(80, Unit.PERCENTAGE);
        vacancyGrid.setHeight(70, Unit.PERCENTAGE);
    }

    private RadioButtonGroup<String> getRadioButton(Grid<Vacancy> vacancyGrid) {
        RadioButtonGroup<String> radioButton = new RadioButtonGroup<>("Где ищем?");
        radioButton.setItems("hh.ru", "moikrug.ru", "all");
        radioButton.setSelectedItem("all");
        radioButton.addValueChangeListener(event -> {
            if (!((ListDataProvider) vacancyGrid.getDataProvider()).getItems().isEmpty()) {

                String radio = radioButton.getSelectedItem().get();
                if (radio.equals("hh.ru"))
                    model.setStrategy(Strategy.HH);
                if (radio.equals("moikrug.ru"))
                    model.setStrategy(Strategy.MOIKRUG);
                if (radio.equals("везде"))
                    model.setStrategy(Strategy.ALL);

                vacancyGrid.setItems(controller.refresh());
                vacancyGrid.getDataProvider().refreshAll();
            }
        });
        return radioButton;
    }
}
