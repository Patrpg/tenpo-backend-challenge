package cl.tenpo.challenge.controller;

import cl.tenpo.challenge.service.PercentageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DynamicPercentageController {

    @Autowired
    private PercentageService percentageService;

    @PostMapping("/dynamicPercentage")
    public double dynamicPercentage(@RequestParam double num1, @RequestParam double num2) {
        double percentage = percentageService.getPercentage();
        return (num1 + num2) + (num1 + num2) * (percentage / 100);
    }
}