package com.api.machinemonitor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.repositories.MachineRepository;

@Service
public interface MachineService {
	
	public Machine save(Machine machine);

	public List<Machine> findAll();

}
