package com.fri.code.users.models.entities;

import com.fri.code.users.lib.SubjectMetadata;
import com.fri.code.users.lib.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_entity")
@NamedQueries(
        value = {
                @NamedQuery(name = "UserMetadataEntity.getAll", query = " SELECT user from UserMetadataEntity user"),
                @NamedQuery(name = "UserMetadataEntity.getUserById", query = "SELECT user from UserMetadataEntity user WHERE user.ID = ?1")
        }
)
public class UserMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    //Subject IDs
    @ElementCollection
    private List<Integer> subjects;

    public List<Integer> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Integer> subjects) {
        this.subjects = subjects;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
