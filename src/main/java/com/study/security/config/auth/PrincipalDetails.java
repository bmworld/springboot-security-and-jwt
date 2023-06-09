package com.study.security.config.auth;

import com.study.security.domain.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h2>Security Session & User 정보</h2>
 * <pre>
 * Security =>  "/login" Request를 Intercept => login 시킴
 *
 * How?
 * 로그인 시, session 만들어서 넣어줌. ( => SecurityContextHolder)
 * : Security Session => Authentication => UserDetails
 * </pre>
 *
 *
 * ----------------------------------------------------------------
 * <h2>Security의 `UserDetails` & `OAuth2`를 모두 상속받아서 편리하게 사용하자</h2>
 *
 */
@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    // #################################################################
    // Oauth2User implements Methods
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getName() {
        return (String) attributes.get("name"); // 강사님은 null 처리함
    }

    // #################################################################

    /**
     * @return 해당 유저의 권한을 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(r -> {
            authorities.add(() -> "ROLE_" + r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료가 되지 않은 계정인가?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠기지 않은 계정인가?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호사용기간이 너무 오래되지 않은것은 아니지?
    }

    @Override
    public boolean isEnabled() {
        // 사이트에서 1년 동안 로그인하지 않았다면
        return true; // 활성화된 계정인가?
    }


}
