package per.lai.forum.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.service.BoardService;

@RestController
public class BoardController {

    private BoardService boardService;

    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }
    /*
    * While create a thread, the choice of board depend on user's level
    * */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/limit/board/{userId}")
    public Result getBoardListByLevel(@PathVariable int userId) {
        return boardService.getBoardListByLevel(userId);
    }
    /*
    * Show all the board name in index page.
    * */
    @PreAuthorize("permitAll()")
    @GetMapping("/board")
    public Result getAllBoard() {
        return boardService.getAllBoard();
    }
}
