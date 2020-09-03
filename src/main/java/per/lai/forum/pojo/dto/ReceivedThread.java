package per.lai.forum.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedThread {
    private String title;
    private String content;
    private int userId;
    private int boardId;

    public boolean integrityCheck() {
        return (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(content) && userId != 0 && boardId != 0);
    }

    public String integrityResult() {
        String result = "";
        if (StringUtils.isEmpty(title))
            result += "标题为空！";
        if (StringUtils.isEmpty(content))
            result += "内容为空";
        if (userId==0 || boardId==0) {
            result = "";
            result += "上传出错，请重试！";
        }
        return result;
    }

}
