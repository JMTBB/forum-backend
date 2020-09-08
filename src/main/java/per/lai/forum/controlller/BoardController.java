package per.lai.forum.controlller;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.dto.ReceivedBoard;
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

    @PreAuthorize("permitAll()")
    @GetMapping("/board/count/{id}")
    public Result getBoardSeq(@PathVariable Integer id){
        return boardService.getBoardSeq(id);
    }
    @PreAuthorize("hasRole('BOARD_MANAGER')")
    @GetMapping("/board/admin/{id}")
    public Result getByManger(@PathVariable Integer id){
        return boardService.getByManager(id);
    }
    @PreAuthorize("hasRole('BOARD_MANAGER')")
    @PutMapping("/board")
    public Result updateBoard(@RequestBody Board board) {

        return boardService.update(board);
    }
}
