package com.api.machinemonitor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.repositories.MachineRepository;

@Service
public class MachineService {

	@Autowired
	MachineRepository machineRepository;
	
	public Machine save(Machine machine) {
		return machineRepository.save(machine);
	}

}
