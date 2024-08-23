package com.togedog.board.dto;

import com.togedog.board.entity.Board;
import com.togedog.board.entity.BoardType;
import com.togedog.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class BoardDto{
    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Post {
//        public Board.BoardType getBoardType;
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s]+$", message = "제목은 영문자,숫자,공백,한글만 허용됩니다")
        private String title;

        @NotBlank
        @Size(min = 1, max = 5000, message = "1자에서 5000자 이내로 작성 가능합니다")
        private String content;

        private String contentImg;

        private BoardType boardType;
    }

    @Getter
    @Setter
    public static class Patch {
        private long boardId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private String contentImg;

        @NotBlank
        private BoardType boardType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String title;
        private String content;
        private String contentImg;
        private String boardType;
        private String boardStatus;
        private int likesCount;
        private int viewCount;
    }
}
