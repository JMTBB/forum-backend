package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import per.lai.forum.pojo.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
    public User findUserByUserName(String userName);

    public User findUserByUserEmail(String userEmail);

    public User findUserByUserNameOrUserEmail(String userName,String userEmail);

    public boolean existsByUserEmail(String userEmail);

    public boolean existsByUserName(String userName);

}
