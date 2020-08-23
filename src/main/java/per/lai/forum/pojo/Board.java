package per.lai.forum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /*
    * The manager of this board
    * */
    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "board_manager",referencedColumnName = "user_id")
    private User boardManager;






}
