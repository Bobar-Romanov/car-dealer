package com.service.carDealer.controller;

import com.service.carDealer.model.OrderCard;
import com.service.carDealer.model.User;
import com.service.carDealer.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;


@Controller
@Slf4j
@RequestMapping("/cars")
public class OrderController {

    @Autowired
    CarService carService;

    @GetMapping("/myorders")
    public String HomePage(@AuthenticationPrincipal User user, Model model){
        ArrayList<OrderCard> res = carService.getOrdersByUserId(user.getId());
        if(res.isEmpty()){
            model.addAttribute("Empty", "Заказов нет...");
        }
        model.addAttribute("orders", res );
        return "ordersPage";
    }
}
