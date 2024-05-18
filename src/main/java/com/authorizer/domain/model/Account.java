package com.authorizer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account implements  Authorizer{
  private AccountDetail account;
  private List<String> violations = new ArrayList<>();

  public Account(AccountDetail account) {
    this.account = account;
  }
}
