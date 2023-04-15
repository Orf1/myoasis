package dev.orf1.clevergreen.data.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlantService {
    private float temperature;
    private float humidity;
    private boolean shouldWater;
    private float maxHumidity = 100;
    private float maxTemperature = 100;
    private float minHumidity = 0;
    private float minTemperature = 0;
    private boolean hasNotified = false;

    public static final String ACCOUNT_SID = "redacted";
    public static final String AUTH_TOKEN = "redacted";


    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public float getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(float minHumidity) {
        this.minHumidity = minHumidity;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void checkLimits() {
        if (hasNotified) {
            return;
        }

        String messageText = "";

        if (humidity > maxHumidity) {
           messageText = "Oasis Alert! Your plant has reached the humidity max of " + maxHumidity;
        }

        if (temperature > maxTemperature) {
            messageText = "Oasis Alert! Your plant has reached the temperature max of " + maxHumidity;
        }

        if (humidity < minHumidity) {
            messageText = "Oasis Alert! Your plant has reached the humidity min of " + minHumidity;
        }

        if (temperature < minTemperature) {
            messageText = "Oasis Alert! Your plant has reached the temperature min of " + minTemperature;
        }

        if (messageText.equals("")) {
            return;
        }

        hasNotified = true;

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+18058610195"),
                        new com.twilio.type.PhoneNumber("+15109300767"), messageText)
                .create();
        System.out.println(message.getDateSent());

    }

    public boolean isHasNotified() {
        return hasNotified;
    }

    public void setHasNotified(boolean hasNotified) {
        this.hasNotified = hasNotified;
    }

    public float getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(float maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public boolean isShouldWater() {
        return shouldWater;
    }

    public void setShouldWater(boolean shouldWater) {
        System.out.println("Water pump status updated: " + shouldWater);
        this.shouldWater = shouldWater;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        System.out.println("Temperature recieved from remote device: " + temperature);

    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
        System.out.println("Humidity recieved from remote device: " + humidity);
    }
}
