package com.singly.client;

import java.util.HashMap;
import java.util.Map;

public class InMemorySinglyAccountStorage
  implements SinglyAccountStorage {
  
  private Map<String, String> accounts = new HashMap<String, String>();

  @Override
  public void saveAccessToken(String account, String accessToken) {
    accounts.put(account, accessToken);
  }

  @Override
  public boolean hasAccessToken(String account) {
    return accounts.containsKey(account);    
  }

  @Override
  public String getAccessToken(String account) {
    return accounts.get(account);
  }

  @Override
  public void removeAccessToken(String account) {
    accounts.remove(account);    
  }

}
