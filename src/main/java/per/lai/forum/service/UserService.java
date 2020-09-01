package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;
import per.lai.forum.repository.RoleRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.result.ResultCode;
import per.lai.forum.result.payload.response.JwtResponse;
import per.lai.forum.security.JwtUtils;
import per.lai.forum.security.UserDetailsImpl;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Result login(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName() == null ? user.getUserEmail() : user.getUserName(), user.getUserPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResultBuilder.buildSuccessResult(new JwtResponse(
                jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getUserEmail(),
                roles
        ));
    }

    public Result register(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail()))
            return ResultBuilder.buildFailResult("Email already exist");
        if (userRepository.existsByUserName(user.getUserName()))
            return ResultBuilder.buildFailResult("User already exist");


        user.setRoles(new HashSet<Role>() {{add(roleRepository.getOne(3));}});     //default user type would be ordinary user
        user.setUserExp(0);             //init exp is 0.
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));           //Encode the password
        User save = userRepository.save(user);
        System.err.println(save);
        return ResultBuilder.buildSuccessResult(save.getUserName() + " " + save.getUserEmail());

    }
}
