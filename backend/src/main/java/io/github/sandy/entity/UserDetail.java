package io.github.sandy.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_detail")
public class UserDetail extends BaseIdEntity {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "no_telepon")
    private String noTelepon;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserDetail(String firstName, String lastName, String address, String noTelepon, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.noTelepon = noTelepon;
        this.user = user;
    }

    public UserDetail() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
