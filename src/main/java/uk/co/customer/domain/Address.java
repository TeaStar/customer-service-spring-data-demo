package uk.co.customer.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Address {

    private String id;
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String county;
    private String postcode;
}
