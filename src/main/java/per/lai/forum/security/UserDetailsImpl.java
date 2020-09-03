package per.lai.forum.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import per.lai.forum.pojo.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class UserDetailsImpl implements UserDetails{
    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private Collection<? extends GrantedAuthority> authorities;

    private int exp;
    private int level;

    public int getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(int userId, String userName, String userEmail, String userPassword, Collection<? extends GrantedAuthority> authorities, int exp) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.authorities = authorities;
        this.exp = exp;
        this.level = expToLevel(exp);
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPassword(),
                authorities,
                user.getUserExp()
                );
    }

    /*
    * User would level 1 while register
    * */
    private int expToLevel(int exp) {
        return exp/100+1;
    }

    public int getExp() {
        return exp;
    }


    public int getLevel() {
        return level;
    }



    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
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
}
