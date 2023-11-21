package com.service.carDealer.service;

import com.service.carDealer.model.*;
import com.service.carDealer.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public ArrayList<CarBrand> getAllBrands(){
        return carRepository.getAllBrands();
    }

    public ArrayList<CarModel> getAllModels(Long branId){
        return carRepository.getAllModels(branId);
    }

    public CarBrand getBrandById(Long brandId){
        return carRepository.getBrandById(brandId);
    }

    public CarModel getModelById(Long modelId){
        return carRepository.getModelById(modelId);
    }

    public ArrayList<Color> getAllColors(){
        return carRepository.getAllColors();
    }

    public ArrayList<Option> getAllOptions(){
        return carRepository.getAllOptions();
    }

    public boolean addNewOrder(Long userId, Long modelId, Long colorId, List<Long> options, Integer price){
        String date = LocalDate.now().plusDays(30).toString();
        String strOptions = options.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return carRepository.addNewOrder(userId, modelId, colorId, date, price, strOptions);
    }
     public ArrayList<OrderCard> getOrdersByUserId(Long userId){
        return carRepository.getOrdersByUserId(userId);
     }

     public ArrayList<Box> getAllBox(){ return carRepository.getAllBox(); }

    public  ArrayList<Wd> getAllWd(){ return  carRepository.getAllWd(); }

    public boolean addNewBrand(String name) {
        return carRepository.addNewCarBrand(name);
    }

    public boolean addNewOption(String name, String price){
        try {
            int intPrice = Integer.parseInt(price);

            return intPrice < 0 ? false :  carRepository.addNewOption(name,intPrice);

        }catch (NumberFormatException e){
            return false;
        }

    }

    public boolean addNewModel(Long brandId, String name, String description, String price, String horsepower, Long wd_id, Long box_id){
        try {
            int intPrice = Integer.parseInt(price);
            int intHorsepower = Integer.parseInt(horsepower);
            if(intPrice < 0 || intHorsepower < 0){
                return false;
            }
            return carRepository.addNewModel(brandId, name, description, intPrice, intHorsepower, wd_id, box_id);

        }catch (NumberFormatException e){
            return false;
        }
    }

    public boolean deleteBrand(Long id){
        return carRepository.deleteBrand(id);
    }

    public boolean deleteModel(Long id){
        return carRepository.deleteModel(id);
    }

    public ArrayList<ReportDTO> getBrandTop(){
        return carRepository.getBrandTop();
    }

    public ArrayList<ReportDTO> getModelTop(){
        return carRepository.getModelTop();
    }

}
