package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisDetailResponseDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.exceptions.UnauthorizedException;
import com.project.skillGapAnalyzer.mapper.AnalysisDetailMapper;
import com.project.skillGapAnalyzer.mapper.AnalysisMapper;
import com.project.skillGapAnalyzer.model.Analysis;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.AnalysisRepository;
import com.project.skillGapAnalyzer.repository.UserRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AnalysisService {

    private final static Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;
    private final AnalysisMapper analysisMapper;
    private final AnalysisDetailMapper analysisDetailMapper;

    public AnalysisService(AnalysisRepository analysisRepository, UserRepository userRepository, AnalysisMapper analysisMapper, AnalysisDetailMapper analysisDetailMapper) {
        this.analysisRepository = analysisRepository;
        this.userRepository = userRepository;
        this.analysisMapper = analysisMapper;
        this.analysisDetailMapper = analysisDetailMapper;
    }

    @Transactional
    public void saveAnalysis(SkillGapRequestDTO request,
                             SkillAnalysisResponseDTO result) {

        String userId = getCurrentUserId();
        logger.info("Saving analysis for user: {}", userId);

        Analysis analysis = Analysis.builder()
                .userId(userId)
                .targetRole(StringNormalizer.normalize(request.getTargetRole()))
                .userSkills(StringNormalizer.normalizeList(request.getUserSkills()))
                .missingSkills(result.getMissingSkills())
                .progress(result.getProgress())
                .createdAt(Instant.now())
                .build();
        analysisRepository.save(analysis);
    }

    public Page<AnalysisResponseDTO> getUserAnalysis(int page, int size) {

        String userId = getCurrentUserId();
        logger.debug("Fetching analysis for user: {}", userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return analysisRepository.findByUserId(userId, pageable)
                .map(analysisMapper::toDTO);
    }

    public AnalysisDetailResponseDTO getAnalysisById(String id) {

        String userId = getCurrentUserId();
        logger.debug("Fetching analysis with id: {}", id);

        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found"));

        if (!analysis.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Analysis not found");
        }

        return analysisDetailMapper.toDTO(analysis);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthenticated access");
        }

        String username = StringNormalizer.normalize(authentication.getName());

        return userRepository.findByUserNameIgnoreCase(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
