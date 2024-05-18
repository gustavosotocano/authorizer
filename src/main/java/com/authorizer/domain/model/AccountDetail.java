package com.authorizer.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDetail{

    @JsonIgnore
    private int accountId;
    @JsonProperty("active-card")
    private Boolean activeCard;
    @JsonProperty("available-limit")
    private Integer availableLimit;






}

