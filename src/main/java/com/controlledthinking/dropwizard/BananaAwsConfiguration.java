/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard;

import java.util.Map;

/**
 *
 * @author brintoul
 */
public class BananaAwsConfiguration {
    
    private String accessKey;
    private String secretKey;
    private String username;
    private String sqsRegionUrl;
    private String sqsRegion;
    private Map<String, String> queueInfo;

    public Map<String, String> getQueueInfo() {
        return queueInfo;
    }

    public void setQueueInfo(Map<String, String> queueInfo) {
        this.queueInfo = queueInfo;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSqsRegionUrl() {
        return sqsRegionUrl;
    }

    public void setSqsRegionUrl(String sqsRegionUrl) {
        this.sqsRegionUrl = sqsRegionUrl;
    }

    public String getSqsRegion() {
        return sqsRegion;
    }

    public void setSqsRegion(String sqsRegion) {
        this.sqsRegion = sqsRegion;
    }
}
