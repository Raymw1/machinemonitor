package com.api.machinemonitor.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.machinemonitor.models.Machine;

public interface MachineRepository extends JpaRepository<Machine, UUID>{

}
