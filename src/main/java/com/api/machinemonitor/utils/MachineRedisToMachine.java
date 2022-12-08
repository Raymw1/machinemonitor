package com.api.machinemonitor.utils;

import org.springframework.beans.BeanUtils;

import com.api.machinemonitor.models.Machine;
import com.api.machinemonitor.models.MachineRedis;

public class MachineRedisToMachine {
	public Machine exec(MachineRedis machineRedis) {
		Machine machine = new Machine();
		BeanUtils.copyProperties(machineRedis, machine);
		return machine;
	}
}
