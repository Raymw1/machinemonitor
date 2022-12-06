package com.api.machinemonitor.controllers;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.management.MBeanServerConnection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/machine")
public class MachineController {
	
	@PostMapping
	public ResponseEntity<Object> storeMachineInfo() {
		try {
		  InetAddress name = InetAddress.getLocalHost();
		  MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();

		  OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
		  mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		  double cpu = osMBean.();		  
		  return ResponseEntity.ok(cpu);
		} catch (UnknownHostException e) {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
}
