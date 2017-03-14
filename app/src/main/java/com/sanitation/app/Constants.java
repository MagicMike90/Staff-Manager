package com.sanitation.app;

public class Constants {
  private Constants() {
  }
  private static final String LOCAL_HOST = "192.168.1.84";
  private static final String LOCAL_HOST_PORT = "3000";
  public static final String METEOR_SERVER_SOCKET = "ws://"+LOCAL_HOST + LOCAL_HOST_PORT +"/websocket";
  public static final String METEOR_LOCATION_ENDPOINT = "http://" +LOCAL_HOST + LOCAL_HOST_PORT + "/api/location";
}
