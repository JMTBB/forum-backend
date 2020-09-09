package per.lai.forum.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedBoard {
    private String name;
    private String description;
    private int level;
    private int owner;

    public boolean checkIntegrity() {
        return owner != 0 && !StringUtils.isEmpty(name);
    }
}
