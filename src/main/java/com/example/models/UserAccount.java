package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

    private String userName;
    private String password;
    private String loginDate;
    private String logoutDate;
}
