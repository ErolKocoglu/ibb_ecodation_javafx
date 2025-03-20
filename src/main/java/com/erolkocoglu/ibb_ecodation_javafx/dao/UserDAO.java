package com.erolkocoglu.ibb_ecodation_javafx.dao;

import com.erolkocoglu.ibb_ecodation_javafx.database.SingletonDBConnection;
import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

// UserDAO
public class UserDAO implements IDaoImplements<UserDTO> {

    // Injection
    private Connection connection;

    // Parametresiz Constructor
    public UserDAO() {
        this.connection = SingletonDBConnection.getInstance().getConnection();
    }

    /// ////////////////////////////////////////////////////////////////////
    // CRUD
    // CREATE
    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
        String sql = "INSERT INTO users (username,password,email) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, userDTO.getPassword());
            preparedStatement.setString(3, userDTO.getEmail());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    // LIST
    @Override
    public List<UserDTO> list() {
        return List.of();
    }

    // FIND BY NAME
    @Override
    public Optional<UserDTO> findByName(String name) {
        return Optional.empty();
    }

    // FIND BY ID
    @Override
    public Optional<UserDTO> findById(int id) {
        return Optional.empty();
    }

    // UPDATE
    @Override
    public Optional<UserDTO> update(int id, UserDTO entity) {
        return Optional.empty();
    }

    // DELETE
    @Override
    public Optional<UserDTO> delete(int id) {
        return Optional.empty();
    }
} //end class
