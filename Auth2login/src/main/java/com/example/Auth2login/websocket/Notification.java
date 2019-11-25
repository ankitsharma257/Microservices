package com.example.Auth2login.websocket;

public class Notification {

  private String message;

  public Notification (String content) {
    this.message = content;
  }

  public String getContent() {
    return message;
  }

}