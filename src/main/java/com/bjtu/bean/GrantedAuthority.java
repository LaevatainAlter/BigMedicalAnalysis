package com.bjtu.bean;

/**
 * Created by Gimling on 2017/4/13.
 */
public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority{

    String authority;

    public GrantedAuthority(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
