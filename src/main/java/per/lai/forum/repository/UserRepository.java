package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import per.lai.forum.pojo.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
    User findUserByUserName(String userName);

    User findUserByUserEmail(String userEmail);

    User findUserByUserNameOrUserEmail(String userName,String userEmail);

   boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);




}
