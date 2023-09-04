package org.gachi.repository;

import org.gachi.model.Cooldown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooldownRepository extends JpaRepository<Cooldown, Long> {
}
