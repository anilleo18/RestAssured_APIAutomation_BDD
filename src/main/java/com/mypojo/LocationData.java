package com.mypojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * LocationPojo class to define a POJO payload for Location details.
 */
@Getter
@Setter
public class LocationData {

    private int accuracy;
    private String name;
    private String phoneNumber;
    private String address;
    private String website;
    private String language;
    private Coordinates location;
    private List<String> types;

}

@Getter
@Setter
class Coordinates {

    private Double latitude;
    private Double longitude;
}
