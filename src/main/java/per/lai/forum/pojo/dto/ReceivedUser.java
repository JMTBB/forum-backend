package per.lai.forum.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedUser {
    private int id;
    private String name;
    private String job;
    private String phone;
    private String address;
    private int exp;


    boolean checkIntegrity() {
        return id != 0 && StringUtils.hasText(name);
    }
}
