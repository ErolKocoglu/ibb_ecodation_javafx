package com.erolkocoglu.ibb_ecodation_javafx.dao;

import com.erolkocoglu.ibb_ecodation_javafx.database.SingletonDBConnection;
import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDaoImplements<T> extends ICrud<T>,IGenericsMethod<T>,ILogin<T> {


    /// ////////////////////////////////////////////////////////////////
    // Gövdeli Method
    default Connection iDaoImplementsDatabaseConnection() {
        // Singleton DB
        return SingletonDBConnection.getInstance().getConnection();

        // Singleton Config
        //return SingletonPropertiesDBConnection.getInstance().getConnection();
    }

    // CRUD
    // CREATE
    //Optional<UserDTO> create(UserDTO userDTO);
}
