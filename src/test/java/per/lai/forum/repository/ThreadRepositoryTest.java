package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import per.lai.forum.pojo.Thread;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ThreadRepositoryTest {
    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void addThread() {
        Thread thread = new Thread();
        thread.setThreadTitle("Test title");
        thread.setThreadContent("this is a thread for unit test");
        thread.setThreadTime(new Date());
        thread.setThreadOwner(userRepository.getOne(7));
        thread.setThreadBoard(boardRepository.getOne(2));
        threadRepository.save(thread);

    }
}