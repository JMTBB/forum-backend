package per.lai.forum.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import per.lai.forum.pojo.dto.ReceivedThread;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.service.ThreadService;

import java.util.Map;


@RestController
public class ThreadController {
    private ThreadService threadService;
    @Autowired
    public void setThreadService(ThreadService threadService) {
        this.threadService = threadService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/thread")
    public Result addThread(@RequestBody ReceivedThread thread) {
        if (!thread.integrityCheck())
            return ResultBuilder.buildFailResult(thread.integrityResult());
        return threadService.addThread(thread);
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/thread/{id}")
    public Result getThreadById(@PathVariable int id) {
        return threadService.getThreadById(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/threads/{boardId}/{pageNumber}")
    public Result getThreadsByBoardId(@PathVariable int boardId, @PathVariable int pageNumber) {
        return threadService.getByBoardId(boardId, pageNumber);
    }

    /*
    * Get all user's threads
    * */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/threadinfo/{userId}/{pageNumber}")
    public Result getThreadOfUser(@PathVariable Integer userId, @PathVariable Integer pageNumber){
        return threadService.getUserThread(userId, pageNumber);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/thread/{id}")
    public Result updateThread(@PathVariable Integer id, @RequestBody Map<String, String> payload){
        return threadService.updateThread(payload, id);
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/thread/{id}")
    public Result deleteThread(@PathVariable Integer id){
        return threadService.deleteByThreadId(id);
    }
    @PreAuthorize("hasRole('GLOBAL_MANAGER') or hasRole('BOARD_MANAGER')")
    @PutMapping("/thread/top/{id}")
    public Result setTop(@PathVariable Integer id){
        return threadService.setTop(id);
    }
}
