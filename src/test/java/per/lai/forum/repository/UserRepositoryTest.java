package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;
import per.lai.forum.pojo.ERole;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;
import per.lai.forum.service.UserService;
import per.lai.forum.utils.AvatarUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        User user = new User();
        user.setUserName("管理员");
        user.setUserEmail("admin@a.edu");
        user.setUserJob("manager");
        user.setUserExp(101);
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName(ERole.ROLE_GLOBAL_MANAGER);
        user.getRoles().add(role);
        userRepository.save(user);
    }
    @Autowired
    private RoleRepository roleRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void addUser() {
        User user = new User();
        user.setUserName("roletest");
        user.setUserEmail("rolewd@a.edu");
        user.setUserJob("ordi");
        user.setUserExp(108);
        user.setUserPassword("abcdefg");
        userRepository.save(user);
    }
    @Test
    @Transactional
    @Rollback(false)
    public void updateUser() {
        for (int j = 5; j <=26; j++) {

            System.out.println(userService.removeManager(j));
        }
    }
    @Test
    @Transactional
    @Rollback(false)
    public  void deleteUser() {
        userRepository.deleteById(10);

    }

    @Test
    @Transactional
    public void getUser() {
        System.out.println(userRepository.getOne(11));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void initAvatar() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            String uuid = AvatarUtil.getUUID();
            System.out.println(user.getUserEmail());
            if (AvatarUtil.InitUserAvatar(uuid, user.getUserEmail())) {
                user.setUserAvatar(uuid + ".png");
            }
            userRepository.save(user);
        }
    }
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    @Transactional
    @Rollback(false)
    public void setPassword() {
        User user = userRepository.getOne(29);
        userService.setManager(29,false);
        user.setUserPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }

}