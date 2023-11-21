package com.service.carDealer.controller;

import com.service.carDealer.model.CarModel;
import com.service.carDealer.model.OrderCard;
import com.service.carDealer.model.User;
import com.service.carDealer.service.CarService;
import com.service.carDealer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/cars/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CarService carService;


    @GetMapping("/users")
    public String allUsersPage( @AuthenticationPrincipal User user, Model model){

        model.addAttribute("users", userService.getAll(user.getId()));
        return "usersPage";
    }

    @GetMapping("/users/{user_id}")
    public String userPage(@PathVariable("user_id") Long userId, Model model){
        ArrayList<OrderCard> res = carService.getOrdersByUserId(userId);
        if(res.isEmpty()){
            model.addAttribute("Empty", "Заказов нет...");
        }
        model.addAttribute("orders", res );
        return "ordersPage";
    }

    @GetMapping("/add")
    public String creativePage(Model model){
        model.addAttribute("brands", carService.getAllBrands());
        model.addAttribute("wd", carService.getAllWd());
        model.addAttribute("box", carService.getAllBox());
        return "addNew";
    }

    @GetMapping("/report")
    public String reportPage(Model model){
        model.addAttribute("brands", carService.getBrandTop());
        model.addAttribute("models", carService.getModelTop());
        return "statistics";
    }

    @PostMapping("/add/brand")
    public String addNewBrand(@AuthenticationPrincipal User user,
                              @RequestParam String name){
        carService.addNewBrand(name);
        log.warn("Admin {} add new car brand: {} ", user.getLogin(), name );
        return "redirect:/cars/home";
    }

    @PostMapping("/add/option")
    public String addNewOption(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam String price){
        if(carService.addNewOption(name, price)){
            log.warn("Admin {} add new option: {} ", user.getLogin(), name);
            return "redirect:/cars/home";
        }
        return "redirect:/cars/admin/add?option=error";
    }

    @PostMapping("/add/model")
    public String addNewModel(@RequestParam Long brandId,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam String price,
                              @RequestParam String horsepower,
                              @RequestParam Long wdId,
                              @RequestParam Long boxId,
                              @AuthenticationPrincipal User user,
                              Model model){
        if(!carService.addNewModel(brandId, name, description, price, horsepower, wdId, boxId)){
            model.addAttribute("error", "Попробуйте снова");
            model.addAttribute("brands", carService.getAllBrands());
            model.addAttribute("wd", carService.getAllWd());
            model.addAttribute("box", carService.getAllBox());
            model.addAttribute("name", name);
            model.addAttribute("description", description);
            return "addNew";
        }
        log.warn("Admin {} add new model: {} ", user.getLogin(), name);
        return "redirect:/cars/home";
    }
    @PostMapping("/delete/brand/{brand_id}")
    public String deleteBrand(
            @AuthenticationPrincipal User user,
            @PathVariable("brand_id") Long brandId)
    {
        log.warn("Admin {} try to delete brand with id: {} ", user.getLogin(), brandId);
        if(carService.deleteBrand(brandId)){
            log.warn("Admin {} delete brand with id: {} ", user.getLogin(), brandId);
            return "redirect:/cars/home";
        }
        return "redirect:/cars/home";

    }

    @PostMapping("/delete/model/{model_id}")
    public String deleteModel(
            @AuthenticationPrincipal User user,
            @PathVariable("model_id") Long modelId)
    {
        log.warn("Admin {} try to delete model with id: {} ", user.getLogin(), modelId);
        if(carService.deleteModel(modelId)){
            log.warn("Admin {} delete model with id: {} ", user.getLogin(), modelId);
            return "redirect:/cars/home";
        }
        return "redirect:/cars/home";
    }
}
