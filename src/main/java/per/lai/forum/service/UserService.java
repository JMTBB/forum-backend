package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;
import per.lai.forum.repository.RoleRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.result.ResultCode;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Result login(User user) {
        User user1 = userRepository.findUserByUserEmail(user.getUserEmail());
        if(user1 != null) {
            if (user.getUserPassword().equals(user1.getUserPassword())){
                //determine the user type
                String returnMessage = "success";
                /*
                * Status Code:
                *   200: Ordinary User
                *   201: Global Manager
                *   202: Board Manager
                * */
                return ResultBuilder.buildResult(ResultCode.SUCCESS,
                        returnMessage,
                        user1.getUserEmail()
                );

            }else {
                return ResultBuilder.buildFailResult("Wrong Password!");
            }
        }else {
            return ResultBuilder.buildFailResult("User isn't exist!");
        }
        //try catch return code 500
    }

    public Result register(User user) {
        User userByUserEmail = userRepository.findUserByUserEmail(user.getUserEmail());
        if(userByUserEmail != null) {   //if query success, then register failed
            return ResultBuilder.buildFailResult("User already exist");
        }else {
            System.err.println(user);
            Role role = roleRepository.getOne(3);
            System.err.println(role);
            //user.setUserRole(role);     //default user type would be ordinary user
            System.err.println(user);
            user.setUserExp(0); //init exp is 0.
            User save = userRepository.save(user);
            System.err.println(save);
            return ResultBuilder.buildSuccessResult(save);
        }
    }
}
