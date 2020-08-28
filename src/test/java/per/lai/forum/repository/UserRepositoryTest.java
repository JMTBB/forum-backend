package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;

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
        role.setRoleName("global manager");
        user.setUserRole(role);
        userRepository.save(user);
    }
    @Autowired
    private RoleRepository roleRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void addUser() {
        User user = new User();
        user.setUserName("passwd");
        user.setUserEmail("passwd@a.edu");
        user.setUserJob("nurse");
        user.setUserExp(308);
        user.setUserPassword("abcdefg");
        user.setUserRole(roleRepository.getOne(3));
        userRepository.save(user);
    }
    @Test
    @Transactional
    @Rollback(false)
    public void updateUser() {
        User user = userRepository.findUserByUserName("Manager4Test");
        user.setUserRole(roleRepository.getOne(3));
        userRepository.save(user);
    }
    @Test
    @Transactional
    @Rollback(false)
    public  void deleteUser() {
        userRepository.deleteById(10);

    }



}