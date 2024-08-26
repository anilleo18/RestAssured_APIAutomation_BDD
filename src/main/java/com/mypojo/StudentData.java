package com.mypojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * StudentData class to define a POJO payload for Student information.
 */
@Getter
@Setter
public class StudentData {

    private String firstName;
    private String lastName;
    private String email;
    private String program;
    private List<String> courses;
}
