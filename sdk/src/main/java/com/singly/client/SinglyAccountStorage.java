package com.singly.client;

public interface SinglyAccountStorage {

  public void saveAccessToken(String account, String accessToken);
  
  public boolean hasAccessToken(String account);
  
  public String getAccessToken(String account);
  
  public void removeAccessToken(String account);
  
}
