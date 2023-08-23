package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 'RestController' 어노테이션: 이 클래스가 REST API 엔드포인트를 처리하는 컨트롤러임을 나타냄
@RestController
@RequestMapping("/api") // 엔드포인트의 기본 경로 설정
public class MemoController {

    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // HTTP POST 요청 처리
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        // 새 메모 생성
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        // 메모 ID 설정
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        // 메모를 메모 목록에 추가
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto
        // 생성된 메모 정보를 응답
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    // HTTP GET 요청 처리
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map To List
        // 메모 목록을 응답용 DTO 리스트로 변환
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();

        return responseList;
    }

    // HTTP PUT 요청 처리
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        // 주어진 ID로 메모를 찾음
        if(memoList.containsKey(id)) {
            // 해당 메모 가져오기
            Memo memo = memoList.get(id);

            // memo 수정
            // 메모 내용 업데이트
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    // HTTP DELETE 요청 처리
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        // 주어진 ID로 메모를 찾음
        if(memoList.containsKey(id)) {
            // 해당 메모 삭제하기
            // 메모 삭제
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}