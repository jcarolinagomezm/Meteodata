package com.service.hydrometrics.services.Impl;

import com.service.hydrometrics.models.DB.entity.User;
import com.service.hydrometrics.models.DTO.user.UserReportDTO;
import com.service.hydrometrics.models.enums.ActionLog;
import com.service.hydrometrics.repository.UserRepository;
import com.service.hydrometrics.services.IUserService;
import com.service.hydrometrics.utils.UtilsMethods;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;


    @Transactional
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var userSaved = repo.save(user);
        UtilsMethods.generatePersistentLogger("User", ActionLog.CREATE);
        return userSaved;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User userOld = this.getUser(user.getUsername());
        user.setPassword(userOld.getPassword());
        user.setId(userOld.getId());
        var userSaved = repo.save(user);
        UtilsMethods.generatePersistentLogger("User", ActionLog.UPDATE);
        return userSaved;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userExist(User user) {
        return repo.existsByEmail(user.getEmail()) || repo.existsByUsername(user.getUsername());
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public List<UserReportDTO> getUserReports() {
        String queryStr = "SELECT u.username, u.first_name, u.last_name, u.email, u.role, u.enabled, (SELECT ar.timestamp FROM user_aud au INNER JOIN audit_revision ar ON ar.id = au.rev WHERE au.username = u.username AND au.revtype = 0 ORDER BY ar.timestamp ASC LIMIT 1)  AS creation_date, COALESCE((SELECT ar.timestamp FROM user_aud au INNER JOIN audit_revision ar ON ar.id = au.rev WHERE au.username = u.username AND au.revtype = 1 ORDER BY ar.timestamp ASC LIMIT 1), 0) AS modification_date FROM user u";
        Query query = entityManager.createNativeQuery(queryStr);
        List<Object[]> results = query.getResultList();
        List<UserReportDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            String username = (String) result[0];
            String firstName = (String) result[1];
            String lastName = (String) result[2];
            String email = (String) result[3];
            String role = (String) result[4];
            boolean enabled = (boolean) result[5];
            Long creationDate = (Long) result[6];
            Long modificationDate = (Long) result[7];
            UserReportDTO dto = new UserReportDTO(username, firstName, lastName, email, role, enabled, creationDate, modificationDate);
            dtoList.add(dto);
        }
        return dtoList;
    }


}
