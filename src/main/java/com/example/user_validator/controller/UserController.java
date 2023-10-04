package com.example.user_validator.controller;

import com.example.user_validator.entity.User;
import com.example.user_validator.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("main_page")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> postUser(@RequestBody User user){
        if(user.getBirthDay().isAfter(LocalDate.now())){
            return new ResponseEntity<>("Date of birth isn't correct", HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(user);
        return new ResponseEntity<>("Congratulations, "+user.getName()+"! You have registered", HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody User user){
        if(user.getBirthDay().isAfter(LocalDate.now())){
            return new ResponseEntity<>("Date of birth isn't correct", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.updateAllFields(id, user), HttpStatus.CREATED);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<Object> partialUpdateOfUser(@PathVariable int id, @RequestBody Map<String, Object> fields){
        User user = userService.partialUpdateOfUser(id, fields);
        if(user.getBirthDay().isAfter(LocalDate.now())){
            return new ResponseEntity<>("Date of birth isn't correct", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.partialUpdateOfUser(id, fields), HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
    }
    @GetMapping("/users/birthdate")
    public ResponseEntity<List<User>> findUsersByDateOfBirthRange(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate){
        return new ResponseEntity<>(userService.findUsersByDateOfBirthRange(fromDate, toDate), HttpStatus.OK);
    }
}
