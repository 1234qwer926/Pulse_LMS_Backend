package com.LMS.Pulse.repository;

import com.LMS.Pulse.model.Jotform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JotformRepository extends JpaRepository<Jotform, Long> {
}