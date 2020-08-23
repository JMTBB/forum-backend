package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import per.lai.forum.pojo.Role;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void addRole(){
        Role role = new Role();
        role.setRoleName("Ordinary user");
        role.setRoleId(3);

        roleRepository.save(role);
    }


}