package com.mypojo;

import lombok.Getter;
import lombok.Setter;

/**
 * PhonePayProductPojo class to define a POJO payload for PhonePay product details.
 */
@Getter
@Setter
public class PhonePayProductData {

    private String name;
    private String description;
    private String type;
    private String category;
    private String imageUrl;
    private String homeUrl;

}
