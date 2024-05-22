package com.service.hydrometrics.services;

import com.service.hydrometrics.models.DB.entity.User;
import com.service.hydrometrics.models.DTO.user.UserReportDTO;

import java.util.List;

public interface IUserService {

    boolean userExist(User user);

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User getUser(String username);

    List<UserReportDTO> getUserReports();
}
