package com.LMS.Pulse.Dto;

public class CourseResponseDto {

    private String courseName;
    private String learningJotformName;
    private String assignmentJotformName;
    private String imageFileName;
    private String groupName;

    // Constructors
    public CourseResponseDto() {
    }

    public CourseResponseDto(String courseName, String learningJotformName, String assignmentJotformName, String imageFileName, String groupName) {
        this.courseName = courseName;
        this.learningJotformName = learningJotformName;
        this.assignmentJotformName = assignmentJotformName;
        this.imageFileName = imageFileName;
        this.groupName = groupName;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLearningJotformName() {
        return learningJotformName;
    }

    public void setLearningJotformName(String learningJotformName) {
        this.learningJotformName = learningJotformName;
    }

    public String getAssignmentJotformName() {
        return assignmentJotformName;
    }

    public void setAssignmentJotformName(String assignmentJotformName) {
        this.assignmentJotformName = assignmentJotformName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
