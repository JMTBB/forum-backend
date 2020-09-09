package per.lai.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.User;

import java.util.List;

public interface ThreadRepository extends JpaRepository<Thread, Integer>, JpaSpecificationExecutor<Thread> {
    boolean existsByThreadId(int id);

    Page<Thread> findThreadsByThreadBoard(Board board, Pageable pageable);
    List<Thread> findThreadsByThreadBoard(Board board);

    Page<Thread> findThreadsByThreadOwner(User user, Pageable pageable);

}
