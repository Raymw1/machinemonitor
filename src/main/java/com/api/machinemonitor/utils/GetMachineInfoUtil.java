package com.api.machinemonitor.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.api.machinemonitor.models.Machine;
import com.sun.management.OperatingSystemMXBean;

public class GetMachineInfoUtil {
	public Machine exec() throws UnknownHostException {
	  InetAddress name = InetAddress.getLocalHost();
	  OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	  double cpu = osBean.getCpuLoad() * 100;
	  String traffic;
	  if (cpu < 35) {
	  	traffic = "green";
	  } else if (cpu < 75) {
	  	traffic = "yellow";
	  } else {
	  	traffic = "red";
	  }
	  var machine = new Machine();
	  machine.setName(name.toString());
	  machine.setCpu_usage(cpu);
	  machine.setTraffic_light(traffic);
	  return machine;
	}
}
