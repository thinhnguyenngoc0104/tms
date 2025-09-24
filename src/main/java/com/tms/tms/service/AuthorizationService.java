package com.tms.tms.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.tms.tms.common.helper.CurrentUserProvider;
import com.tms.tms.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final ProjectRepository projectRepository;
    private final CurrentUserProvider currentUserProvider;

    public boolean isAdmin() {
        return currentUserProvider.hasRole("ADMIN");
    }

    public boolean canAccessProject(Long projectId) {
        if (isAdmin()) {
            return true;
        }
        String currentSub = currentUserProvider.getCurrentUserSub();
        return projectRepository.existsByIdAndUserHasAccess(projectId, currentSub);
    }

    public void requireProjectAccess(Long projectId) {
        if (!canAccessProject(projectId)) {
            throw new AccessDeniedException(
                    "ACCESS DENIED: Owner - Member's privileges required for projectId=" + projectId);
        }
    }
}
