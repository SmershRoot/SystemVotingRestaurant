package ru.smv.system.restaurant.security;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityRole  implements GrantedAuthority {

    ADMIN(0),
    USER(2);

    private Integer id;

    SecurityRole(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SecurityRole fromId(int id){
        for(SecurityRole role : values()){
            if(role.getId() == id){
                return role;
            }
        }
        return USER;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
