package com.service.hydrometrics.models.DTO.user;

import com.service.hydrometrics.models.DB.entity.User;
import com.service.hydrometrics.models.enums.Role;
import com.service.hydrometrics.utils.UtilsMethods;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link User}
 */
@Getter
public class UserReportDTO implements Serializable {
    private final String username;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final Role role;
    private final boolean enabled;
    private final String creation_date;
    private final String modification_date;

    public UserReportDTO(String username, String firstName, String lastName, String email, String role, boolean enabled, Long creationDate, Long modificationDate) {
        this.username = username;
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.role = Role.valueOf(role);
        this.enabled = enabled;
        this.creation_date = UtilsMethods.longTimeStampToFormatString(String.valueOf(creationDate));
        this.modification_date = UtilsMethods.longTimeStampToFormatString(String.valueOf(modificationDate));
    }
}