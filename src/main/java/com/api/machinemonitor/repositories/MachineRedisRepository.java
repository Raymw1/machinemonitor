package com.api.machinemonitor.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.api.machinemonitor.models.MachineRedis;

public interface MachineRedisRepository extends CrudRepository<MachineRedis, UUID>{

}
