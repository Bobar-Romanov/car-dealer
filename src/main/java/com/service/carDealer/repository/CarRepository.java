package com.service.carDealer.repository;

import com.service.carDealer.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ArrayList<CarBrand> getAllBrands(){
        List<Object[]> resultList = entityManager.createNativeQuery("CALL get_all_car_brands")
                .getResultList();
        ArrayList<CarBrand> res = new ArrayList<>();
        for (Object[] row : resultList) {
            res.add(new CarBrand((Long) row[0], (String) row[1]));
        }
        return res;
    }

    public  ArrayList<CarModel> getAllModels(Long brandId){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_models_by_brand_id(:brand_id)")
                .setParameter("brand_id", brandId) // Замени 1 на конкретный brand_id
                .getResultList();

        ArrayList<CarModel> res = new ArrayList<>();

        for (Object[] row : result) {
            Long modelId = (Long) row[0];
            String modelName = (String) row[1];
            String modelDescription = (String) row[2];
            Integer modelPrice = ((BigDecimal) row[3]).intValue();
            Integer modelHorsepower = (Integer) row[4];
            String wdType = (String) row[5];
            String boxType = (String) row[6];
            CarModel car = new CarModel(modelId, modelName, modelDescription, modelPrice,modelHorsepower,wdType,boxType);
            res.add(car);
        }
        return res;
    }

    public CarBrand getBrandById(Long brandId){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_car_brand_by_id(:car_brand_id)")
                .setParameter("car_brand_id", brandId)
                .getResultList();
        CarBrand brand = new CarBrand();
        for (Object[] row : result) {
            brand.setId((Long) row[0]);
            brand.setName((String) row[1]);
        }
        return brand;
    }

    public CarModel getModelById(Long modelId){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_model_by_id(:model_id)")
                .setParameter("model_id", modelId)
                .getResultList();
        CarModel car = new CarModel();
        for (Object[] row : result) {
            car.setId((Long) row[0]);
            car.setName((String) row[1]);
            car.setDescription((String) row[2]);
            car.setPrice(((BigDecimal) row[3]).intValue());
            car.setHorsepower((Integer) row[4]);
            car.setWdType((String) row[5]);
            car.setBoxType((String) row[6]);
        }
        return car;
    }

    public ArrayList<Color> getAllColors(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_all_colors")
                .getResultList();
        ArrayList<Color> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new Color((Long) row[0], (String) row[1]));
        }
       return res;
    }
    public ArrayList<Option> getAllOptions(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_all_options")
                .getResultList();
        ArrayList<Option> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new Option((Long) row[0], (String) row[1], ((BigDecimal) row[2]).intValue()));
        }
        return res;
    }

    @Transactional
    public boolean addNewOrder(Long userId, Long modelId, Long colorId, String date, Integer price, String options){
            try{
                entityManager.createNativeQuery(
                                "CALL insert_order(:userId, :modelId, :colorId, :date, :totalPrice, :options)")
                        .setParameter("userId", userId)
                        .setParameter("modelId", modelId)
                        .setParameter("colorId", colorId)
                        .setParameter("date", date)
                        .setParameter("totalPrice", price)
                        .setParameter("options", options)
                        .executeUpdate();
            }catch (Exception e){
                return false;
            }
            return true;
    }


    public ArrayList<OrderCard> getOrdersByUserId(Long userId) {
        List<Object[]> result = entityManager.createNativeQuery(
                        "CALL get_orders_with_details_by_user_id(:userId)")
                .setParameter("userId", userId)
                .getResultList();
        ArrayList<OrderCard> res = new ArrayList<>();
        for (Object[] row : result) {
            Long id = (Long) row[0];
            String name = (String) row[1];
            String description = (String) row[2];
            Integer horsepower = (Integer) row[3];
            String wdType = (String) row[4];
            String boxType = (String) row[5];
            String color = (String) row[6];
            String date = row[7].toString();
            Integer price = ((BigDecimal) row[8]).intValue();
            res.add(new OrderCard(id,name, description,horsepower,wdType, boxType, color, date, price, this.getOptionsByOrderId(id)));
        }
        return res;
    }

    private ArrayList<Option> getOptionsByOrderId(Long orderId){

        List<Object[]> result = entityManager.createNativeQuery("CALL get_options_by_order_id(:order_id)")
                .setParameter("order_id", orderId)
                .getResultList();
        ArrayList<Option> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new Option((Long) row[0], (String) row[1], ((BigDecimal) row[2]).intValue()));
        }
        return res;
    }

    public ArrayList<Box> getAllBox(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_all_box")
                .getResultList();

        ArrayList<Box> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new Box((Long) row[0], (String) row[1]));
        }
        return res;
    }

    public ArrayList<Wd> getAllWd(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_all_wd")
                .getResultList();

        ArrayList<Wd> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new Wd((Long) row[0], (String) row[1]));
        }
        return res;
    }

    @Transactional
    public boolean addNewCarBrand(String name){
        try{
            entityManager.createNativeQuery(
                            "CALL add_car_brand(:name)")
                    .setParameter("name", name)
                    .executeUpdate();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean addNewOption(String name, Integer price){
        try{
            entityManager.createNativeQuery(
                            "CALL add_option(:name, :price)")
                    .setParameter("name", name)
                    .setParameter("price", price)
                    .executeUpdate();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean addNewModel(Long brandId, String name, String description, Integer price, Integer horsepower, Long wd_id, Long box_id){
        try{
            entityManager.createNativeQuery(
                            "CALL create_new_model(:brand_id, :name, :description, :price, :horsepower, :wd_id, :box_id )")
                    .setParameter("brand_id", brandId)
                    .setParameter("name", name)
                    .setParameter("description", description)
                    .setParameter("price", price)
                    .setParameter("horsepower", horsepower)
                    .setParameter("wd_id", wd_id)
                    .setParameter("box_id", box_id)
                    .executeUpdate();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteBrand(Long brandId){
        try{
            entityManager.createNativeQuery(
                            "CALL delete_car_brand_by_id(:brand_id)")
                    .setParameter("brand_id", brandId)
                    .executeUpdate();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteModel(Long modelId){
        try{
            entityManager.createNativeQuery(
                            "CALL delete_model_by_id(:model_id)")
                    .setParameter("model_id", modelId)
                    .executeUpdate();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public ArrayList<ReportDTO> getBrandTop(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_order_count_by_brand")
                .getResultList();

        ArrayList<ReportDTO> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new ReportDTO((String) row[0], ((Long)row[1]).intValue()));
        }
        return res;
    }

    public ArrayList<ReportDTO> getModelTop(){
        List<Object[]> result = entityManager.createNativeQuery("CALL get_order_count_by_model")
                .getResultList();

        ArrayList<ReportDTO> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new ReportDTO((String) row[0], ((Long)row[1]).intValue()));
        }
        return res;
    }

}
