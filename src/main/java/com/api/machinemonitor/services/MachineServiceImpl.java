package com.api.machinemonitor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.repositories.MachineRepository;

@Service
@CacheConfig(cacheNames = "machineCache")
public class MachineServiceImpl implements MachineService {

	@Autowired
	MachineRepository machineRepository;
	
	@CacheEvict(cacheNames = "machines", allEntries = true)
	public Machine save(Machine machine) {
		return machineRepository.save(machine);
	}
//
	@Cacheable(cacheNames = "machines")
	@Override
	public List<Machine> findAll() {
//		waitSomeTime();
		return machineRepository.findAll();
	}

	private void waitSomeTime() {
		System.out.println("Long Wait Begin");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Long Wait End");
	}
}
