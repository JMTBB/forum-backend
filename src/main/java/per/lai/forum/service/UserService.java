package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;
import per.lai.forum.repository.RoleRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.result.payload.response.JwtResponse;
import per.lai.forum.utils.AvatarUtil;
import per.lai.forum.utils.JwtUtils;
import per.lai.forum.security.UserDetailsImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
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
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Result login(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName() == null ? user.getUserEmail() : user.getUserName(), user.getUserPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResultBuilder.buildSuccessResult(new JwtResponse(
                jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getUserEmail(),
                userDetails.getExp(),
                userDetails.getLevel(),
                roles
        ));
    }

    public Result register(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail()))
            return ResultBuilder.buildFailResult("Email already exist");
        if (userRepository.existsByUserName(user.getUserName()))
            return ResultBuilder.buildFailResult("User already exist");


        user.setRoles(new HashSet<>() {{add(roleRepository.getOne(3));}});     //default user type would be ordinary user
        user.setUserExp(0);             //init exp is 0.
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));           //Encode the password
        String uuid = AvatarUtil.getUUID();
        if (AvatarUtil.InitUserAvatar(uuid, user.getUserEmail())) {
            user.setUserAvatar(uuid + ".png");
        }
        User save = userRepository.save(user);
        System.err.println(save);
        return ResultBuilder.buildSuccessResult(save.getUserName() + " " + save.getUserEmail());

    }


    public Result setUserAvatar(MultipartFile file, Integer id) {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return ResultBuilder.buildFailResult("Something wrong with filename");
        }
        if (id != AvatarUtil.getCurrentAuthenticatedUserId()){
            return ResultBuilder.buildFailResult("Access deny");
        }
        String UUIDName = AvatarUtil.getUUIDName(originalName);
        try {
            Files.copy(file.getInputStream(), AvatarUtil.path().resolve(UUIDName));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBuilder.buildFailResult("Save file failed, please retry");
        }
        User user = userRepository.getOne(id);
        user.setUserAvatar(UUIDName);
        userRepository.save(user);
        return ResultBuilder.buildSuccessResult("Updated avatar");
    }

    public Result getAvatar(int id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.getOne(id);
            String avatar = user.getUserAvatar() == null ?
                    "3F87E430EBAE4C88A54B13C7FCD7B374.png" :
                    user.getUserAvatar();
            return ResultBuilder.buildSuccessResult(avatar);
        }else
            return ResultBuilder.buildFailResult("error");
    }
}
