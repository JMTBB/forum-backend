package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import per.lai.forum.pojo.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {
    List<Board> findBoardsByBoardAccessLevelIsLessThanEqual(Integer level);
    boolean existsByBoardId(int id);
}
