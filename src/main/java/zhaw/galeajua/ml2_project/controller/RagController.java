package zhaw.galeajua.ml2_project.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zhaw.galeajua.ml2_project.dto.ResponseDto;
import zhaw.galeajua.ml2_project.input.UserQuery;
import zhaw.galeajua.ml2_project.service.RagService;

import javax.imageio.IIOException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/")
public class RagController {

    private static final Logger logger = LoggerFactory.getLogger(RagController.class);

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping(value = "prompt")
    @ResponseBody
    public ResponseEntity<Object> getPromptResponse (@RequestBody UserQuery query,
                                             HttpServletRequest request) throws IOException {
        logger.info("endpoint hit! {}", request);
        if (query.getQuery() == null || query.getQuery().isEmpty()) {
            return new ResponseEntity<>("input query must not be null", HttpStatus.BAD_REQUEST);
        }
        ResponseDto responseDto = ragService.retrieve(query.getQuery());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
