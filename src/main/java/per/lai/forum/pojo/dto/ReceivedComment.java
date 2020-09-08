package per.lai.forum.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedComment {
    private Integer threadId;
    private String content;
    private Integer nestedId;

    public boolean integrityCheck() {
        return threadId != null && content !=null && nestedId != null ;
    }
}
