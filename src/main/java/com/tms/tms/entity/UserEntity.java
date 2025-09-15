package com.tms.tms.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String role;

    @OneToMany(mappedBy = "owner")
    private List<ProjectEntity> ownProjects;

    @OneToMany(mappedBy = "assignee")
    private List<TaskEntity> assignedTasks;

    @ManyToMany(mappedBy = "members")
    private List<ProjectEntity> joinedProjects;

    // public enum Role {
    //     ROLE_USER
    // }

}
