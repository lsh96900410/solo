package jpabook.jpashop.config.auth;

import jpabook.jpashop.domain.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private final Member member;
    private final Map<String,Object> attributes ;

    public PrincipalDetails(Member member) {
        this.member=member;
        this.attributes=new HashMap<>();
    }
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member=member;
        this.attributes=new HashMap<>(attributes);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                System.out.println("getAuthority : " + member.getRole());
                return member.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
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
        return true;
    }

    /*== OAuth2 ==*/
    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
