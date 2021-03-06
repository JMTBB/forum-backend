package per.lai.forum.result.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String jwt;
    private Integer id;
    private String username;
    private String email;
    private int exp;
    private int level;
    private List<String> roles;
}
