package io.microsamples.gatlingrunner.load;

public enum GatlingContext {
    INSTANCE;

    public String payload;
    public String baseUrl;
    public String endpoint;

    public int constantUsersPerSecond;
    public int constantUsersPerSecondDuration;
}
