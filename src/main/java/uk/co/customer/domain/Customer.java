package uk.co.customer.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Customer {

    @Id
    private String id;
    private String title;

    @NotNull
    private String surname;

    @NotNull
    private String forename;

    @NotNull
    private String nationalInsuranceNumber;

    private String phoneNumber;
    private String mobileNumber;
    private String emailAddress;
    private String mothersMaidenName;

    @OneToOne
    private Address address;

}
