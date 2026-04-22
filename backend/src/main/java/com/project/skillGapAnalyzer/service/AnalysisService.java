package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.model.Analysis;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.AnalysisRepository;
import com.project.skillGapAnalyzer.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;

    public AnalysisService(AnalysisRepository analysisRepository, UserRepository userRepository) {
        this.analysisRepository = analysisRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveAnalysis(SkillGapRequestDTO request,
                             SkillAnalysisResponseDTO result) {


        String userId = getCurrentUserId();

        Analysis analysis = Analysis.builder()
                .userId(userId)
                .targetRole(request.getTargetRole())
                .userSkills(request.getUserSkills())
                .missingSkills(result.getMissingSkills())
                .progress(result.getProgress())
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        analysisRepository.save(analysis);
    }

    public Page<AnalysisResponseDTO> getUserAnalysis(int page, int size) {

        String userId = getCurrentUserId();

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());

        Page<Analysis> analysisPage =
                analysisRepository.findByUserId(userId, pageable);

        return analysisPage.map(a -> new AnalysisResponseDTO(
                a.getId(),
                a.getTargetRole(),
                a.getMissingSkills(),
                a.getProgress(),
                a.getCreatedAt()
        ));
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUserName(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
