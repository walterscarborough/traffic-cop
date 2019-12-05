package io.microsamples.gatlingrunner.load;

public enum GatlingContext {
    INSTANCE;

    public String payload;
    public String baseUrl;
    public String endpoint;
    public HttpMethod httpMethod;

    public int constantUsersPerSecond;
    public int constantUsersPerSecondDuration;

    public int rampUsersPerSecondMinimum;
    public int rampUsersPerSecondMaximum;
    public int rampUsersPerSecondDuration;
}
