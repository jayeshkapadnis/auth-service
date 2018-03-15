package com.hashmapinc.haf.entity;

import com.hashmapinc.haf.constants.ModelConstants;
import com.hashmapinc.haf.models.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = ModelConstants.USERS_TABLE)
public class UserEntity implements Serializable{

    @Transient
    private static final long serialVersionUID = 9135513967525611597L;

    @Id
    @Column(name = ModelConstants.ID_PROPERTY)
    private String id;

    @Column(name = ModelConstants.USER_NAME_PROPERTY)
    private String userName;

    @Column(name = ModelConstants.USER_PASSWORD_PROPERTY)
    private String password;

    @Column(name = ModelConstants.TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.USER_ENABLED_PROPERTY)
    private boolean enabled;

    @Column(name = ModelConstants.USER_FIRST_NAME_PROPERTY)
    private String firstName;

    @Column(name = ModelConstants.USER_LAST_NAME_PROPERTY)
    private String lastName;

    @ElementCollection
    @CollectionTable(name = ModelConstants.USER_AUTHORITIES_TABLE, joinColumns = @JoinColumn(name = ModelConstants.ID_PROPERTY))
    @Column(name = ModelConstants.USER_AUTHORITIES_COLUMN)
    private List<String> authorities;


    public UserEntity() {
    }

    public UserEntity(User user) {
        if (user.getId() != null) {
            this.setId(user.getId());
        }
        this.userName = user.getUserName();
        this.enabled = user.isEnabled();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        if (user.getTenantId() != null) {
            this.tenantId = user.getTenantId();
        }
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    protected void setId(String id){
        this.id = id;
    }

    protected String getId(){
        return this.id;
    }

    public User toData() {
        User user = new User(getId());
        //user.setCreatedTime(UUIDs.unixTimestamp(getId()));
        user.setAuthorities(authorities);
        if (tenantId != null) {
            user.setTenantId(tenantId);
        }
        user.setUserName(userName);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;

        UserEntity that = (UserEntity) o;

        if (enabled != that.enabled) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        return authorities != null ? authorities.equals(that.authorities) : that.authorities == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        return result;
    }
}
