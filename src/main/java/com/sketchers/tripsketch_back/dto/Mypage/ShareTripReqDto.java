package com.sketchers.tripsketch_back.dto.Mypage;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Data
public class ShareTripReqDto {
    @NotEmpty(message = "이메일 리스트는 비어 있을 수 없습니다.")
    private List<@NotEmpty(message = "이메일 리스트는 비어 있을 수 없습니다.") String> emails; // 공유 대상 이메일 리스트
    private String message;      // 선택 메시지 (이메일 본문 등)
}
