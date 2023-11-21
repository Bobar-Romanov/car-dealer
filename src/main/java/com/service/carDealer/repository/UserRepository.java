package com.service.carDealer.repository;

import com.service.carDealer.model.Role;
import com.service.carDealer.model.User;
import com.service.carDealer.model.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;
    public UserDetails getUserByLogin(String login) {
        List<Object[]> result = entityManager.createNativeQuery("CALL get_user_by_login(:login)")
                .setParameter("login", login)
                .getResultList();
        if (result.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        for (Object[] row : result) {
            user.setId((Long) row[0]);
            user.setLogin((String) row[1]);
            user.setPassword((String) row[2]);
            user.setFullName((String) row[3]);
            user.setPhone((String) row[4]);
            user.setRole(new Role( (Long)row[5],(String)row[6]));
        }
        return user;
    }

    public boolean existByLogin(String login){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("check_user_exists")
                .registerStoredProcedureParameter("p_login", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_exists", Integer.class, ParameterMode.OUT)
                .setParameter("p_login", login);
        query.execute();

        Integer userExists = (Integer) query.getOutputParameterValue("p_exists");
        return userExists == 1;
    }

    public boolean existByPhone(String phone) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("check_user_number_exists")
                .registerStoredProcedureParameter("p_number", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_exists", Integer.class, ParameterMode.OUT)
                .setParameter("p_number", phone);
        query.execute();

        Integer userExists = (Integer) query.getOutputParameterValue("p_exists");
        return userExists == 1;
    }

    public boolean saveUser(String login, String password, String fullName, String phone){
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("add_user")
                    .registerStoredProcedureParameter("p_login", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_fullname", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_role_id", Long.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_success", Boolean.class, ParameterMode.OUT)
                    .setParameter("p_login", login)
                    .setParameter("p_password", password)
                    .setParameter("p_fullname", fullName)
                    .setParameter("p_phone_number", phone)
                    .setParameter("p_role_id", 1);

            query.execute();

            Boolean success = (Boolean) query.getOutputParameterValue("p_success");

            return success;
        }catch (Exception e){
            return false;
        }
    }

    public ArrayList<UserDTO> getAllUsers(){

        List<Object[]> result = entityManager.createNativeQuery("CALL get_all_users_info")
                .getResultList();

        ArrayList<UserDTO> res = new ArrayList<>();
        for (Object[] row : result) {
            res.add(new UserDTO((Long) row[0], (String) row[1], (String) row[2], ((Long)row[3]).intValue()));
        }
        return res;
    }



}
