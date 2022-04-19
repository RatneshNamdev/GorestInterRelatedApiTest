package com.liseinfotech.gorestApiTest;

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
public class TodosPostData {

    private int id;
    private int user_id;
    private String title;
    private String due_on;
    private String status;
}
