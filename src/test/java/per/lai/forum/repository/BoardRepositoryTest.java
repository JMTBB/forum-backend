package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.ERole;
import per.lai.forum.pojo.Role;
import per.lai.forum.pojo.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void saveBoard() {
        User user = new User();
        user.setUserName("Manager4B1");
        user.setUserEmail("m1@a.edu");
        user.setUserExp(150);
        user.setUserJob("doctor");
        user.setUserPhone("10086");
        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName(ERole.ROLE_BOARD_MANAGER);
        Board board = new Board();
        board.setBoardName("杂谈");
        board.setBoardDescription("此板块可以讨论任何内容");
        user.getRoles().add(role);
        board.setBoardManager(user);
        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void delete() {
        boardRepository.delete(boardRepository.getOne(1));
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void updateBoard() {
        Board board = boardRepository.getOne(2);
        board.setBoardManager(userRepository.getOne(7));
        boardRepository.save(board);
    }
    @Test
    @Transactional
    @Rollback(false)
    public void addBoard() {
        Board board = new Board(null, "Test Board", "This is the board for unit test",0, userRepository.getOne(7));
        boardRepository.save(board);
    }
}