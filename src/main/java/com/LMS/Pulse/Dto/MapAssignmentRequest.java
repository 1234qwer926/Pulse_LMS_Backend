package com.LMS.Pulse.Dto;

public class MapAssignmentRequest {
    private String courseName;
    private String jotformName;
    private String group;

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getJotformName() {
        return jotformName;
    }
    public void setJotformName(String jotformName) {
        this.jotformName = jotformName;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
}