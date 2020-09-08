package per.lai.forum.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedBoard {
    private int id;
    private String name;
    private String description;
    private int level;
}
