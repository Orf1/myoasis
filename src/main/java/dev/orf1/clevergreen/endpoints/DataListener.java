package dev.orf1.clevergreen.endpoints;

import dev.orf1.clevergreen.Application;
import dev.orf1.clevergreen.data.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataListener {
    private PlantService plantService;

    @GetMapping("/upload")
    public String upload(@RequestParam(value = "t") String t, @RequestParam(value = "h") String h) {
        this.plantService = Application.getPlantService();
        plantService.setTemperature(Float.parseFloat(t));
        plantService.setHumidity(Float.parseFloat(h));
        plantService.checkLimits();
        return "ok";
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        if (plantService.isShouldWater()) {
            return new ResponseEntity<>("yes", HttpStatus.CONTINUE);
        }

        return new ResponseEntity<>("no", HttpStatus.OK);
    }
}