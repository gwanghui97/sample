package com.togedog.board.mapper;

import com.togedog.board.dto.BoardDto;
import com.togedog.board.entity.*;
import com.togedog.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface BoardMapper {
    default Board boardDtoPostToBoard(BoardDto.Post requestBody){
        Member member = new Member();
        Board board;
        if (requestBody.getBoardType() == BoardType.REVIEW) {
            board = new BoardReview();
        } else if (requestBody.getBoardType() == BoardType.BOAST) {
            board = new BoardBoast();
        } else if (requestBody.getBoardType() == BoardType.INQUIRY) {
            board = new BoardInquiry();
        } else if (requestBody.getBoardType() == BoardType.ANNOUNCEMENT){
            board = new BoardAnnouncement();
        } else {
            board = new Board();
        }
        board.setTitle(requestBody.getTitle());
        board.setContent(requestBody.getContent());
        board.setContentImg(requestBody.getContentImg());
        board.setBoardType(requestBody.getBoardType());
        board.setMember(member);
        return board;
    }

    Board boardDtoPatchToBoard(BoardDto.Patch requestBody);
    BoardDto.Response boardToBoardDtoResponse(Board board);
    default List<BoardDto.Response> boardToBoardDtoResponses(List<Board> boards) {
        return boards
                .stream()
                .filter(status -> !status.getBoardStatus().equals(Board.BoardStatus.BOARD_DELETED))
                .map(board -> BoardDto.Response
                        .builder()
                        .title(board.getTitle())
                        .content(board.getContent())
                        .boardType(board.getBoardType().getBoardDescription())
                        .boardStatus(board.getBoardStatus().getStatusDescription())
                        .viewCount(board.getViewCount())
                        .build())
                .collect(Collectors.toList());
    }

}
