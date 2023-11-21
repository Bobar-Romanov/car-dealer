package com.service.carDealer.controller;

import com.service.carDealer.model.CarModel;
import com.service.carDealer.model.User;
import com.service.carDealer.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/cars/home")
public class HomeController {

    @Autowired
    CarService carService;

    @GetMapping("")
    public String HomePage( Model model){
        model.addAttribute("brands", carService.getAllBrands());
        return "home";
    }

    @GetMapping("/{brand_id}")
    public String BrandPage( @PathVariable("brand_id") Long brandId, Model model){
        model.addAttribute("brand", carService.getBrandById(brandId));
        ArrayList<CarModel> res = carService.getAllModels(brandId);
        if(res.isEmpty()){
            model.addAttribute("Empty", "ТУТ НИКОГО...");
        }
        model.addAttribute("models", carService.getAllModels(brandId));
        return "brandPage";
    }

    @GetMapping("/{brand_id}/{model_id}")
    public String ModelPage( @PathVariable("brand_id") Long brandId,
                             @PathVariable("model_id") Long modelId,
                             Model model){
        model.addAttribute("brand", carService.getBrandById(brandId));
        model.addAttribute("model", carService.getModelById(modelId));
        model.addAttribute("colors",carService.getAllColors());
        model.addAttribute("options", carService.getAllOptions());
        model.addAttribute("date", LocalDate.now().plusDays(30));
        return "modelPage";
    }
    @PostMapping("/{brandId}/{modelId}")
    public String processForm(@AuthenticationPrincipal User user,
                              @PathVariable Long brandId,
                              @PathVariable Long modelId,
                              @RequestParam Long color,
                              @RequestParam(required = false) List<Long> options,
                              @RequestParam Integer price,
                              Model model) {
        log.warn("User {} try to create new order modelId: {}", user.getLogin(), modelId);
        carService.addNewOrder(user.getId(), modelId, color, options, price);
        return "redirect:/cars/home";
    }




}
