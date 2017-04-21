package com.bjtu.bean;



import java.util.Collection;
import java.util.Vector;

/**
 * Created by Gimling on 2017/4/13.
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails{

    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    public UserDetails(UserBean ub) {
        this.id = ub.getId();
        this.username = ub.getUsername();
        this.password = ub.getPassword();
        this.enabled = ub.getEnabled();
        this.authorities.add(new GrantedAuthority(ub.getRole()));
    }

    private Collection<GrantedAuthority> authorities = new Vector<GrantedAuthority>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId(){
        return this.id;
    }
}
