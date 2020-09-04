package per.lai.forum.utils;


import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import per.lai.forum.security.UserDetailsImpl;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

public class AvatarUtil {
    private static final Path PATH = Paths.get("D:/JAVA_code/Temp/avatar/");

    public static Path path(){
        return PATH;
    }

    public static String getUUIDName(String originName){
        int index = originName.lastIndexOf(".");    //keep the suffix if exist
        return getUUID() + (index == -1 ? "":originName.substring(index));
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static int getCurrentAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return ((UserDetailsImpl) authentication.getPrincipal()).getUserId();
        else
            return -1;
    }

    public static UserDetailsImpl getCurrentUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return (UserDetailsImpl) authentication.getPrincipal();
        }else
            return null;
    }

    public static boolean InitUserAvatar(String uuid, String email) {
        String suffix = ".png";
        String md5 = "23ede0af553c51257fdedd0a41cd857d";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(email.getBytes());
            byte[] digest = md.digest();
            md5 = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        String d = "monsterId".toLowerCase();
        String rank = "g";
        int size = 80;
        String url = "http://www.gravatar.com/avatar/"
                + md5 +"?s=" + size + "&d=" + d +"&r=" + rank;
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5*1000);
            InputStream inputStream = connection.getInputStream();
            Files.copy(inputStream, path().resolve(uuid + suffix));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
