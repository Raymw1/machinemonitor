package com.api.machinemonitor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.machinemonitor.models.MachineRedis;
import com.api.machinemonitor.repositories.MachineRedisRepository;
import com.api.machinemonitor.repositories.MachineRepository;
import com.api.machinemonitor.utils.MachineRedisToMachine;


@Service
public class MachineService {
	
	@Autowired
	private MachineRedisRepository machineRedisRepository;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Transactional
	public MachineRedis save(MachineRedis machine) {
		MachineRedis machineRedis = machineRedisRepository.save(machine);
		machineRepository.save(new MachineRedisToMachine().exec(machineRedis));
		return machineRedis;
	}

	public List<MachineRedis> findAll() {
		return (List<MachineRedis>) machineRedisRepository.findAll();
	}
}
