package com.infopulse.infomail.models;

public class ServerInformation {
    private String projectName;

    public ServerInformation(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
