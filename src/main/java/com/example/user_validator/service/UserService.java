package com.example.user_validator.service;

import com.example.user_validator.entity.User;
import com.example.user_validator.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user){
        if (user.getBirthDay().plusYears(18).isBefore(LocalDate.now())) {
            userRepository.save(user);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date input");
        }
    }

    public User updateAllFields(int id, User user){
        Optional<User> optionalUser = userRepository.findById(id);
        User userTemp;
        if (optionalUser.isPresent()){
            userTemp = optionalUser.get();

            userTemp.setName(user.getName());
            userTemp.setLastName(user.getLastName());
            userTemp.setEmail(user.getEmail());
            userTemp.setAddress(user.getAddress());
            userTemp.setBirthDay(user.getBirthDay());
            userTemp.setPhoneNumber(user.getPhoneNumber());

            userRepository.save(userTemp);
            return userTemp;

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User partialUpdateOfUser(int id, Map<String, Object> fields) {
        Optional<User>optionalUser = userRepository.findById(id);
        User existingUser;
        if (optionalUser.isPresent()){
            existingUser = optionalUser.get();
            fields.forEach((property, value)->{
                Field field = ReflectionUtils.findField(User.class, property);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingUser, value);
            });
            userRepository.save(existingUser);
            return existingUser;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public void deleteUser(int id) {
        Optional<User>optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public List<User> findUsersByDateOfBirthRange(LocalDate fromDate, LocalDate toDate){
        if (fromDate.compareTo(toDate)>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date input");
        }
        List<User> listOfUsers = userRepository.findUserByBirthDayBetween(fromDate, toDate);
        return listOfUsers;

    }

}
