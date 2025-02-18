package com.example.woddy.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private String oauthId;
    private String oauthProvider;


}
