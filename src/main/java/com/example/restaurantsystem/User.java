package com.example.restaurantsystem;

public class User {
    int id;
    String username;
    String password;
    String role;

    int getId(){
        return id;
    }
    String getUsername(){
        return username;
    }
    // maybe not do this
    String getPassword(){
        return password;
    }
    String getRole(){
        return role;
    }
    void setId(int id){
        this.id = id;
    }
    void setUsername(String username){
        this.username = username;
    }
    void setRole(String role){
        this.role = role;
    }
    boolean isAdmin(){
        if (role == "admin"){
            return true;
        }
        else{
            return false;
        }
    }
}
