package uk.co.customer.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
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

    private Address address;

    private Boolean allowContactConsent;
    private Boolean customerAtCareAddress;
    private Boolean receivingTwentyFourHourCare;

    private Boolean receiveLifestyleMagazine;
    private Boolean receiveCharitableFundRaisingInfo;



}
