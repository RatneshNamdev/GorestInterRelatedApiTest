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
public class CommentsPostData {

    private int id;
    private int post_id;
    private String name;
    private String email;
    private String body;
}
