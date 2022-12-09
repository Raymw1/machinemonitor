package com.api.machinemonitor.controllers;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.models.MachineRedis;
import com.api.machinemonitor.services.MachineService;
import com.api.machinemonitor.utils.GetMachineInfoUtil;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/machine")
public class MachineController {
	@Autowired
	private MachineService machineService;
	
	
	@GetMapping
	public ResponseEntity<Object> getMachinesInfo() {
		List<MachineRedis> machineList = machineService.findAll();
//		machineList.removeAll(Collections.singleton(null));
		return ResponseEntity.ok(machineList);
	}
	
	@GetMapping("/post")
	public ResponseEntity<Object> storeMachineInfo() {
		try {
			MachineRedis machine = new GetMachineInfoUtil().exec();
		  return ResponseEntity.ok(machineService.save(machine));
		} catch (UnknownHostException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

}
