package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Project findByName(String name);
}