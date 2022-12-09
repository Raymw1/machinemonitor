package com.api.machinemonitor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.models.MachineRedis;
import com.api.machinemonitor.repositories.MachineRedisRepository;
import com.api.machinemonitor.repositories.MachineRepository;
import com.api.machinemonitor.utils.MachineRedisToMachine;


@Service
@Caching
public class MachineService {
	
	@Autowired
	private MachineRedisRepository machineRedisRepository;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Transactional
	@CacheEvict(cacheNames = "machines", allEntries = true)
	public MachineRedis save(MachineRedis machine) {
		MachineRedis machineRedis = machineRedisRepository.save(machine);
		machineRepository.save(new MachineRedisToMachine().exec(machineRedis));
		return machineRedis;
	}
	
	@Cacheable(cacheNames = "machines")
	public List<MachineRedis> findAll() {
//		return machineRepository.findAll();
		return (List<MachineRedis>) machineRedisRepository.findAll();
	}
}
