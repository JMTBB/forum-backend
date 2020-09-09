package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;
import per.lai.forum.pojo.dto.ReceivedUser;
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

    public Result getInfo(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResultBuilder.buildFailResult("user doesn't exist");
        return ResultBuilder.buildSuccessResult(user);
    }
    public Result updateInfo(int id, Map<String, String> payload) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResultBuilder.buildFailResult("user doesn't exist");
        if (StringUtils.hasText(payload.get("userPhone")))
            user.setUserPhone(payload.get("userPhone"));
        if (StringUtils.hasText(payload.get("userJob")))
            user.setUserJob(payload.get("userJob"));
        if (StringUtils.hasText(payload.get("userAddress")))
            user.setUserAddress(payload.get("userAddress"));
        userRepository.save(user);
        return ResultBuilder.buildSuccessResult(user.getUserId());
    }

    public Result getAll() {
        List<User> all = userRepository.findAll();
        return ResultBuilder.buildSuccessResult(all);
    }

    public boolean setManager(int id, boolean global) {
        User user = userRepository.findById(id).orElse(null);
        if(user ==null)
            return false;
        user.getRoles().add(roleRepository.getOne(2));
        if (global)
            user.getRoles().add(roleRepository.getOne(1));
        userRepository.save(user);
        return true;
    }

    public boolean removeManager(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return  false;
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getOne(3));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }


    public Result getUserExp(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResultBuilder.buildFailResult("don't exist");
        return ResultBuilder.buildSuccessResult(user.getUserExp());
    }

    public Result getUsersWithPageNumber(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        return ResultBuilder.buildSuccessResult(userRepository.findAll(pageable));
    }

    public Result updateUserByAdmin(ReceivedUser receivedUser) {
        User user = userRepository.getOne(receivedUser.getId());
        user.setUserName(receivedUser.getName());
        user.setUserAddress(receivedUser.getAddress());
        user.setUserJob(receivedUser.getJob());
        user.setUserPhone(receivedUser.getPhone());
        userRepository.save(user);
        return ResultBuilder.buildSuccessResult(user.getUserId());
    }
}
