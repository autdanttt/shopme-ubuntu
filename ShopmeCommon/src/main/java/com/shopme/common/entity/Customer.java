package com.shopme.common.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer extends AbstractAddressWithCountry{

    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "verification_code", length = 45)
    private String verificationCode;
    private boolean enabled;
    @Column(name = "created_time")
    private Date createdTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;
    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    public Customer() {
    }

    public Customer(Integer id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateTime() {
        return createdTime;
    }

    public void setCreateTime(Date createdTime) {
        this.createdTime = createdTime;
    }




    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }


    public String getFullName(){
        return firstName+" "+ lastName;
    }

}
