package per.lai.forum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.lai.forum.security.UserDetailsImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class AvatarUtil {
    private static final Path PATH = Paths.get("D:/JAVA_code/Temp/avatar/");

    public static Path path(){
        return PATH;
    }

    public static String getUUIDName(String originName){
        int index = originName.lastIndexOf(".");    //keep the suffix if exist
        return UUID.randomUUID().toString().replace("-", "").toUpperCase() + (index == -1 ? "":originName.substring(index));
    }

    public static int getCurrentAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return ((UserDetailsImpl) authentication.getPrincipal()).getUserId();
        else
            return -1;
    }

}
