package per.lai.forum.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import per.lai.forum.pojo.User;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.service.UserService;

import java.util.Map;

@RestController
public class UserController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public Result userLogin(@RequestBody Map<String, String> payload) {
        if (StringUtils.isEmpty(payload.get("userEmail")) ||
                StringUtils.isEmpty(payload.get("userPassword"))) { //recheck the integrity of data
            return ResultBuilder.buildFailResult("Email or Password empty");
        }else {
            User user = new User();
            user.setUserEmail(payload.get("userEmail"));
            user.setUserPassword(payload.get("userPassword"));
            return userService.login(user);
        }
    }

    @PostMapping("/user")
    public Result userRegister(@RequestBody Map<String, String> payload) {

        if (StringUtils.isEmpty(payload.get("userEmail")) ||
                StringUtils.isEmpty(payload.get("userName")) ||
                StringUtils.isEmpty(payload.get("userPassword"))) { //recheck the integrity of data
            return ResultBuilder.buildFailResult("data incomplete!");
        }else {
            User user = new User();
            user.setUserEmail(payload.get("userEmail")); //encapsulate the payload
            user.setUserName(payload.get("userName"));
            user.setUserPassword(payload.get("userPassword"));
            return userService.register(user);
        }

    }

}
