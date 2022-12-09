package com.api.machinemonitor.models;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "machine", timeToLive = 3600*2)
public class MachineRedis {	
	@Id
	@Indexed
	private UUID id;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	
	@Column(nullable = false)
	private double cpuUsage;
	
	@Column(nullable = false)
	private String trafficLight;
}
