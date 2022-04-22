package com.liseinfotech.gorestModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.liseinfotech.gorestModels.UserData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@NoArgsConstructor
@ToString
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataForGet {

    private ArrayList<UserData> data;
}
