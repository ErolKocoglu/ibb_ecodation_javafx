package com.erolkocoglu.ibb_ecodation_javafx.controller;

import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;


// I use this class to keep information of the user that has currently logged in
public class UserSession {
    private static UserDTO currentUser;

    public static UserDTO getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDTO currentUser) {
        UserSession.currentUser = currentUser;
    }
}
