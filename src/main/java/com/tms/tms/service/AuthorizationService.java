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

    // Check if current user has ADMIN role
    public boolean isAdmin() {
        return currentUserProvider.hasRole("ADMIN");
    }

    /**
     * Check if current user can access a project
     * Admin: all projects
     * User: only projects they are owner or members
     */
    public boolean canAccessProject(Long projectId) {
        if (isAdmin()) {
            return true;
        }

        String currentSub = currentUserProvider.getCurrentUserSub();

        // Check if accessible
        return projectRepository.existsByIdAndUserHasAccess(projectId, currentSub);
    }

    public void requireProjectAccess(Long projectId) {
        if (!canAccessProject(projectId)) {
            throw new AccessDeniedException(
                "ACCESS DENIED: Owner - Member's privileges required for projectId=" + projectId
            );
        }
    }
}

