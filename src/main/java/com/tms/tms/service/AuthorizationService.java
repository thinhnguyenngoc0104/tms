package com.tms.tms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.tms.common.util.AuthUtil;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.repository.ProjectRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthUtil authUtil;
    private final ProjectRepository projectRepository;

    // Check if current user has ADMIN role
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ADMIN"));
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

        UserEntity currentUser = authUtil.getCurrentUser();
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        // Check if user is owner
        if (project.getOwner() != null && project.getOwner().getId().equals(currentUser.getId())) {
            return true;
        }
        // Check if user is member
        return project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));
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
