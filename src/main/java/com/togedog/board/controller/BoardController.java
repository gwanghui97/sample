package com.togedog.board.controller;

import com.togedog.board.dto.BoardDto;
import com.togedog.board.entity.Board;
import com.togedog.board.entity.BoardType;
import com.togedog.board.mapper.BoardMapper;
import com.togedog.board.service.BoardService;
import com.togedog.dto.MultiResponseDto;
import com.togedog.dto.SingleResponseDto;
import com.togedog.likes.dto.LikesDto;
import com.togedog.likes.mapper.LikesMapper;
import com.togedog.member.entity.Member;
import com.togedog.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/boards")
@Validated
@RequiredArgsConstructor
public class BoardController {
    private final static String BOARD_DEF_URL = "/boards";
    private final BoardMapper mapper;
    private final BoardService service;

    @PostMapping("/{board-type}")
    public ResponseEntity<Void> postBoard(@PathVariable("board-type") String boardType,
                                          @Valid @RequestBody BoardDto.Post requestBody,
                                          Authentication authentication) {
        // URL에서 받아온 boardType을 대문자로 변환하여 BoardType으로 변환
        BoardType boardTypeEnum = Arrays.stream(BoardType.values())
                .filter(type -> type.name().equalsIgnoreCase(boardType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid board type: " + boardType));
        // URL에서 변환된 BoardType을 requestBody에 설정 (JSON에서 온 값은 무시)
        requestBody.setBoardType(boardTypeEnum);

        // 서비스 로직을 호출하여 게시글을 생성
        Board createBoard = service.createBoard(mapper.boardDtoPostToBoard(requestBody), authentication);
        // 리소스의 위치 URI를 생성
        URI location = UriCreator.createUri(BOARD_DEF_URL, createBoard.getBoardId());
        // 생성된 리소스에 대한 응답 반환
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id")
                                   @Positive long boardId) {
        Board findBoard = service.getBoard(service.findVerifiedBoard(boardId));
        if (findBoard.getBoardStatus() == Board.BoardStatus.BOARD_DELETED){
            return new ResponseEntity<>(new SingleResponseDto<>(null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.boardToBoardDtoResponse(findBoard)),
                HttpStatus.OK);
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive long boardId
            , @Valid @RequestBody BoardDto.Patch requestBody){
        requestBody.setBoardId(boardId);
        Board patchBoard = service.patchBoard(mapper.boardDtoPatchToBoard(requestBody));
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.boardToBoardDtoResponse(patchBoard)),
                HttpStatus.OK);
    }

    @DeleteMapping ("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive long boardId) {
        service.deleteBoard(boardId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size){
        Page<Board> pageBoards = service.findBoards(page-1,size);
        List<Board> boards = pageBoards.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.boardToBoardDtoResponses(boards),pageBoards),
                HttpStatus.OK);
    }
}
