package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import per.lai.forum.pojo.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
