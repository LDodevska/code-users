package com.fri.code.users.lib;

import java.util.List;

public class UserMetadata {

    public Integer ID;
    public String firstName;
    public String lastName;
    public String email;

    // Subject IDs
    public List<Integer> subjects;

    public List<Integer> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Integer> subjects) {
        this.subjects = subjects;
    }

    public void addSubjectId(Integer subjectID){
        this.subjects.add(subjectID);
    }

    public void removeSubjectId(Integer subjectID){
        this.subjects.remove(subjectID);
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
