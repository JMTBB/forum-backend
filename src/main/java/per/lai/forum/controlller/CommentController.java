package per.lai.forum.controlller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import per.lai.forum.pojo.dto.ReceivedComment;
import per.lai.forum.repository.CommentRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.service.CommentService;

@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/comments/{threadId}")
    public Result getCommentByThreadId(@PathVariable Integer threadId) {
        return commentService.getByThreadId(threadId);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/comments/user/{userId}")
    public Result getCommentsByUserId(@PathVariable Integer userId) {
        return commentService.getByUserId(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/comment")
    public Result addComment(@RequestBody ReceivedComment receivedComment) {
        if (!receivedComment.integrityCheck()) {
            return ResultBuilder.buildFailResult("数据不完整");
        }
        return commentService.addComment(receivedComment);
    }

    @PreAuthorize(("permitAll()"))
    @GetMapping("/comment/counter/{threadId}")
    public Result getCommentNumber(@PathVariable Integer threadId) {
        return commentService.getCounterByThread(threadId);
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/comment/{id}")
    public Result deleteCommentById(@PathVariable Integer id){
        return commentService.deleteComment(id);
    }
}
