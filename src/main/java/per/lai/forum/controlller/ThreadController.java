package per.lai.forum.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import per.lai.forum.pojo.dto.ReceivedThread;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.service.ThreadService;


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
    @GetMapping("/threads/{boardId}")
    public Result getThreadsByBoardId(@PathVariable int boardId) {
        return threadService.getByBoardId(boardId);
    }
}
