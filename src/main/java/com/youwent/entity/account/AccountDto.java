package com.youwent.entity.account;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// dto == form class in django
@Data
public class AccountDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    @NotBlank
    @Pattern(regexp = "[0-9]{10,11}")
    private String phone;
}
