package com.controlledthinking.dropwizard;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TwilioPhoneNumberConfiguration extends Configuration {
    @Valid
    private DataSourceFactory database;
    @NotNull
    private String authSalt;
    @NotNull
    private String authHeader;
    private BananaAwsConfiguration awsConfiguration;
    
    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public String getAuthSalt() {
        return authSalt;
    }

    public void setAuthSalt(String authSalt) {
        this.authSalt = authSalt;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }
    
    @JsonProperty("aws")
    public void setAwsConfiguration(BananaAwsConfiguration bAws) {
        this.awsConfiguration = bAws;
    }
    
    public BananaAwsConfiguration getAwsConfiguration() {
        return this.awsConfiguration;
    }
}
