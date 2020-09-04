package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.Thread;

import java.util.List;

public interface ThreadRepository extends JpaRepository<Thread, Integer>, JpaSpecificationExecutor<Thread> {
    boolean existsByThreadId(int id);

    List<Thread> findThreadsByThreadBoard(Board board);
}
