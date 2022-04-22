package com.liseinfotech.gorestModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;
}
