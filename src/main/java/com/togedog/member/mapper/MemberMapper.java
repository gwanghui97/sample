package com.togedog.member.mapper;

import com.togedog.member.dto.MemberDto;
import com.togedog.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post post);
    Member memberPatchToMember(MemberDto.Patch patch);
    MemberDto.Response memberToResponseDto(Member member);
    List<MemberDto.Response> membersToResponseDto(List<Member> members);
}