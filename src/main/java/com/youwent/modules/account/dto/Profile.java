package com.youwent.modules.account.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Profile {
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    @NotBlank
    @Pattern(regexp = "[0-9]{10,11}")
    private String phone;
}
