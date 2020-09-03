package per.lai.forum.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @Column(name = "board_name")
    private String boardName;

    @Column(name = "board_description")
    private String boardDescription;

    @Column(name = "board_access_level")
    private Integer boardAccessLevel;
    /*
    * The manager of this board
    * */
    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "board_manager",referencedColumnName = "user_id")
    @JsonManagedReference
    private User boardManager;






}
