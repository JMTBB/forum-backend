package per.lai.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.lai.forum.pojo.User;
import per.lai.forum.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!userRepository.existsByUserEmail(username) && !userRepository.existsByUserName(username))
            throw new UsernameNotFoundException("User" + username + "don't exist");
        User user = userRepository.findUserByUserNameOrUserEmail(username, username);
        return UserDetailsImpl.build(user);
    }
}
