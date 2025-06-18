package com.sketchers.tripsketch_back.dto.Mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareTripRespDto {
    private int successCount;                // 성공 개수
    private List<String> failedEmails;       // 전송 실패 이메일 목록
}
