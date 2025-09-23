package com.tms.tms.service;

import org.springframework.stereotype.Service;

import com.tms.tms.common.helper.CurrentUserProvider;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.repository.ProjectRepository;

import jakarta.persistence.EntityNotFoundException;
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
     * User: only projects they are members
     */
    public boolean canAccessProject(Long projectId) {
        if (isAdmin()) {
            return true;
        }

        String currentSub = currentUserProvider.getCurrentUserSub();

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        // Check if user is owner
        if (project.getOwner() != null && project.getOwner().getSub().equals(currentSub)) {
            return true;
        }
        // Check if user is member
        return project.getMembers().stream()
                .anyMatch(member -> member.getSub().equals(currentSub));
    }

    public void requireProjectAccess(Long projectId) {
        if (!canAccessProject(projectId)) {
            throw new SecurityException("ACCESS DENIED: Owner - Member's privileges required");
        }
    }

    public void requireProjectPermission() {
        if (!isAdmin()) {
            throw new SecurityException("ACCESS DENIED: Admin's privilege required");
        }
    }
}
