package com.api.machinemonitor.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.api.machinemonitor.models.MachineRedis;
import com.sun.management.OperatingSystemMXBean;

public class GetMachineInfoUtil {
	public MachineRedis exec() throws UnknownHostException {
	  InetAddress name = InetAddress.getLocalHost();
	  OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	  double cpu = osBean.getCpuLoad() * 1000;
	  String traffic;
	  if (cpu < 35) {
	  	traffic = "green";
	  } else if (cpu < 75) {
	  	traffic = "yellow";
	  } else {
	  	traffic = "red";
	  }
	  var machine = new MachineRedis();
	  machine.setName(name.toString());
	  machine.setCpuUsage(cpu);
	  machine.setTrafficLight(traffic);
	  return machine;
	}
}
