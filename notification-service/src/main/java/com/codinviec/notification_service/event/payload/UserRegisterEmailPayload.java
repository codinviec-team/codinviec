package com.codinviec.notification_service.event.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterEmailPayload {
    private String email;
    private String firstName;
    private String lastName;
}
