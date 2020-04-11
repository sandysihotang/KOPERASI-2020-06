package io.github.sandy.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role_user")
public class RoleUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "user_id")
    private Integer userId;

    public RoleUser() {
    }

    public RoleUser(Integer roleId, Integer userId) {
        this.roleId = roleId;
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
