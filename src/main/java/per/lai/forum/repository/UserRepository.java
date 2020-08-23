package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import per.lai.forum.pojo.User;

public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
    public User findUserByUserName(String userName);

    public User findUserByUserEmail(String userEmail);

}
