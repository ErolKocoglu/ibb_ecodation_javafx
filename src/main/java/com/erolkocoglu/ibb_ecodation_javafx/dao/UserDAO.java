package com.erolkocoglu.ibb_ecodation_javafx.dao;

import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;

import java.util.List;
import java.util.Optional;

// UserDAO
public class UserDAO implements IDaoImplements<UserDTO> {

    // Field


    // CREATE
    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
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
