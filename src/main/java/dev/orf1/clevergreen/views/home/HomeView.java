package dev.orf1.clevergreen.views.home;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.orf1.clevergreen.Application;
import dev.orf1.clevergreen.data.service.PlantService;
import dev.orf1.clevergreen.views.MainLayout;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {
    PlantService plantService = Application.getPlantService();
    int counter = 0;

    public HomeView() {
        setSpacing(false);

        Image img = new Image("images/oasis_logo.png", "placeholder plant");
        img.setWidth("200px");


        Span pending1 = new Span("Temperature: " + plantService.getTemperature() + " °C");
        Span pending2 = new Span("Humidity: " + plantService.getHumidity() + " %");

        pending1.getElement().getThemeList().add("badge");
        pending2.getElement().getThemeList().add("badge");



        Chart temperatureChart = new Chart(ChartType.LINE);
        Chart humidityChart = new Chart(ChartType.LINE);

        // Add temperature and humidity data to the chart
        int temperature = (int) plantService.getTemperature();
        int humidity = (int) plantService.getHumidity();

        DataSeries temperatureSeries = new DataSeries("Temperature (°C)");
        temperatureSeries.add(new DataSeriesItem(counter, temperature));

        DataSeries humiditySeries = new DataSeries("Humidity (%)");
        humiditySeries.add(new DataSeriesItem(counter, humidity));

        temperatureChart.getConfiguration().addSeries(temperatureSeries);
        humidityChart.getConfiguration().addSeries(humiditySeries);

        Button b = new Button("Update");
        b.addClickListener(buttonClickEvent -> {
            counter++;
            pending1.setText("Temperature: " + plantService.getTemperature() + " °C");
            pending2.setText("Humidity: " + plantService.getHumidity() + " %");
            // Get the updated temperature and humidity data
            int t = (int) plantService.getTemperature();
            int h = (int) plantService.getHumidity();

            // Add the updated data to the data series
            temperatureSeries.add(new DataSeriesItem(counter, t));
            humiditySeries.add(new DataSeriesItem(counter, h));
            if (temperatureSeries.size() > 10 || temperatureSeries.size() > 10) {
                temperatureSeries.remove(temperatureSeries.get(0));
                humiditySeries.remove(humiditySeries.get(0));
            }
        });

        add(img);
        add(pending1);
        add(pending2);
        add(new Paragraph(" "));
        add(humidityChart);
        add(b);
        add(new Paragraph(""));
        createLimitsView();
        add(new Paragraph(""));
        makeWaterButton();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

    }

    private void createLimitsView() {
        NumberField maxHumidityField = new NumberField("Max Humidity %");
        maxHumidityField.setMin(0);
        maxHumidityField.setMax(100);
        maxHumidityField.setStep(0.1);
        maxHumidityField.setValue((double) plantService.getMaxHumidity());

        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> {
            plantService.setMaxHumidity((Float.parseFloat(String.valueOf(maxHumidityField.getValue()))));
            plantService.setHasNotified(false);
            Notification.show("Max humidity set to: " + plantService.getMaxHumidity() + "%");
        });

        HorizontalLayout hz = new HorizontalLayout();
        hz.setAlignItems(Alignment.BASELINE);
        hz.add(maxHumidityField, saveButton);

        add(hz);

    }

    private void makeWaterButton() {
        Button button = new Button("Activate Water Pump");

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Activate Water Pump");
        dialog.setText(
                "Are you sure you want to activate the water pump? Current cycle is set to 10 seconds.");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {

        });


        dialog.setConfirmText("Activate");
        dialog.addConfirmListener(event -> {
            Notification notification = Notification
                    .show("Water Pump Activated!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            plantService.setShouldWater(true);
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                plantService.setShouldWater(false);
            }).start();
        });

        button.addClickListener(event -> {
            dialog.open();
        });



        add(button);
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
