package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;
import per.lai.forum.pojo.ERole;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
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
        for (int j = 9; j <=16 ; j++) {
            User user = userRepository.getOne(j);
            Set<Role> roles = new HashSet<>();
            for (int i = 1; i <= 1; i++) {
                roles.add(roleRepository.getOne(i));
            }
            user.setRoles(roles);
            userRepository.save(user);
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



}