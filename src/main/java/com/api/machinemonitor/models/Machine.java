package com.api.machinemonitor.models;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "machines")
@RedisHash("machine")
public class Machine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	
	@Column(nullable = false)
	private double cpuUsage;
	
	@Column(nullable = false)
	private String trafficLight;
}
