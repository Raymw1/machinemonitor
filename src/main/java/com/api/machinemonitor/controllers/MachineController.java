package com.api.machinemonitor.controllers;


import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.services.MachineService;
import com.api.machinemonitor.utils.GetMachineInfoUtil;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/machine")
public class MachineController {
	
	@Autowired
	MachineService machineService;
	
	@GetMapping
	public ResponseEntity<Object> getMachinesInfo() {
		return ResponseEntity.ok(machineService.findAll());
	}
	
	@GetMapping("/post")
	public ResponseEntity<Object> storeMachineInfo() {
		try {
			Machine machine = new GetMachineInfoUtil().exec();
		  return ResponseEntity.ok(machineService.save(machine));
		} catch (UnknownHostException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
