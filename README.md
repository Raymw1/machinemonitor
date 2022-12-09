# Spring Data Redis

## Requisitos

- Ter um projeto Spring Boot inicializado.
- Ter o Redis instalado em sua máquina.

---

1. O primeiro passo é adicionar o Spring Data Redis e o Cliente em seu projeto. Para isso, adicione eles em seu `pom.xl`
    
    ```xml
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
    	<groupId>redis.clients</groupId>
    	<artifactId>jedis</artifactId>
    </dependency>
    ```
    
2. Insira suas configurações do Redis em `src/main/resources/application.properties` junto com suas outras configurações, assim como o exemplo abaixo
    
    ```xml
    spring.data.redis.host=localhost // Usar o host no qual o Redis está executando.
    spring.data.redis.port=6379 // Usar a porta na qual o Redis está executando.
    spring.cache.type=redis // Usar o Redis como cache do spring boot.
    spring.cache.redis.time-to-live=60000 // TTL - Tempo para expirar o spring cache.
    spring.cache.redis.cache-null-values=false // Permitir ou não valores nulos no cache do spring.
    spring.cache.redis.use-key-prefix=true // Usar ou não um perfixo para o cache do spring.
    spring.cache.redis.key-prefix=MyCache // Caso use, inserir o prefixo do cache.
    ```
    


3. Crie sua entidade como você cria normalmente para suas aplicações, por exemplo:
    
    ```java
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
    ```
    
4. Agora, você deve criar uma nova entidade para cada entidade que você e deseja utilizar no Redis. Por exemplo, se você possui uma entidade `Machine.java`, crie uma entidade `MachineRedis.java` e utilize os mesmos campos.
    
    ```java
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
    @RedisHash(value = "machine", timeToLive = 10) // Usar entidade no Redis
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
    ```
    

> Em `@RedisHash`, no parâmetro `value`, insira o prefixo que você quer utilizar para a chave desta entidade. No parâmetro `timeToLive`, é informado o tempo de expiração em segundos após o dado ser inserido no Redis.
> 

> Certifique-se de importar corretamente o `@Id` com `org.springframework.data.annotation.Id`.
> 
5. Crie um repositório para sua entidade criada no passo 3 que não utiliza o Redis.
    
    ```java
    package com.api.machinemonitor.repositories;
    
    import java.util.UUID;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    
    import com.api.machinemonitor.models.Machine;
    
    public interface MachineRepository extends JpaRepository<Machine, UUID>{
    
    }
    ```
    
6. Crie um repositório para a entidade criada no passo 4. Exemplo: `MachineRedisRepository.java`
    
    ```java
    package com.api.machinemonitor.repositories;
    
    import java.util.UUID;
    
    import org.springframework.data.repository.CrudRepository;
    
    import com.api.machinemonitor.models.MachineRedis;
    
    public interface MachineRedisRepository extends CrudRepository<MachineRedis, UUID>{
    
    }
    ```
    
7. Crie um controller com os métodos que você deseja para suas entidades. (Utilize as entidades que você fez para o Redis no passo 4).
    
    ```java
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
    ```
    
8. Crie um service que você vai utilizar em seu controller. Ao invés de usar apenas o repositório antigo, iremos usar também o repositório com respeito ao Redis, portanto importe ambos.
    
    ```java
    package com.api.machinemonitor.services;
    
    import java.util.List;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    
    import com.api.machinemonitor.models.MachineRedis;
    import com.api.machinemonitor.repositories.MachineRedisRepository;
    import com.api.machinemonitor.repositories.MachineRepository;
    import com.api.machinemonitor.utils.MachineRedisToMachine;
    
    @Service
    public class MachineService {
    	
    	@Autowired
    	private MachineRedisRepository machineRedisRepository;
    	
    	@Autowired
    	private MachineRepository machineRepository;
    	
    	@Transactional
    	public MachineRedis save(MachineRedis machineRedisData) {
    		MachineRedis machineRedis = machineRedisRepository.save(machineRedisData);
    		Machine machine = new Machine();
    		BeanUtils.copyProperties(machineRedis, machine);
    		machineRepository.save(machine);
    		return machineRedis;
    	}
    
    	public List<MachineRedis> findAll() {
    		return (List<MachineRedis>) machineRedisRepository.findAll();
    	}
    }
    ```
    
9. Note que nos métodos de escrita, é recomendável adicionar a notação `import org.springframework.transaction.annotation.Transactional`.
10. Note também que nos métodos de escrita, você irá escrever no banco Redis e posteriormente no banco que você já utiliza. 

11. Já nos métodos de leitura, utilize apenas o repositório do Redis.

12. Nesse momento, você já está utilizando o Redis, como um de seus bancos. Agora, é interessante utilizarmos o Cache do Spring, para performar ainda mais na leitura de dados com pouca imutabilidade.
13. Insira `spring.main.allow-circular-references=true` em seu `application.properties`
14. Crie um package `configs` na sua aplicação e então, dentro dela uma classe `RedisConfig.java`
    
    ```java
    package com.api.machinemonitor.configs;
    
    import java.io.Serializable;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.autoconfigure.AutoConfigureAfter;
    import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
    import org.springframework.cache.CacheManager;
    import org.springframework.cache.annotation.EnableCaching;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.data.redis.cache.RedisCacheConfiguration;
    import org.springframework.data.redis.cache.RedisCacheManager;
    import org.springframework.data.redis.connection.RedisConnectionFactory;
    import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
    import org.springframework.data.redis.serializer.RedisSerializationContext;
    import org.springframework.data.redis.serializer.StringRedisSerializer;
    
    import jakarta.annotation.PostConstruct;
    import redis.clients.jedis.Jedis;
    
    @Configuration
    @AutoConfigureAfter(RedisAutoConfiguration.class)
    @EnableCaching
    public class RedisConfig {
    	
    	@Autowired
    	CacheManager cacheManager;
    	
    	// Capturar variáveis de ambiente
    	@Value("${spring.data.redis.host}")
    	private String redisHost;
    	
    	@Value("${spring.data.redis.port}")
    	private int redisPort;
    
    	// Configurar template para escrever o cache
    	@Bean
    	public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
    		RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    		template.setKeySerializer(new StringRedisSerializer());
    		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    		template.setConnectionFactory(redisConnectionFactory);
    		return template;
    	}
    	
    	// Configurar o gerenciador de cache para utilizar o template
    	@Bean
    	public CacheManager cacheManager(RedisConnectionFactory factory) {
    		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
    		RedisCacheConfiguration redisCacheConfiguration = config
    			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    		RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
    		return redisCacheManager;
    	}
    	
    //	// Apagar dados do Redis quando a aplicação inicializar 
    //	@PostConstruct
    //	public void clearCache() {
    //		Jedis jedis = new Jedis(redisHost, redisPort, 60*6);
    //		jedis.flushAll();
    //		jedis.close();
    //	}
    }
    ```
    
15. Adicione a notação `@org.springframework.cache.annotation.Cacheable(cacheNames = "referencialAqui")` e em `cacheNames` insira o referencial dos dados que serão usados no método de leitura. 
Nos métodos de escrita use a `@org.springframework.cache.annotation.CacheEvict` e especificamente nos de edição, `@org.springframework.cache.annotation.CachePut` Exemplo:
    
    ```java
    @Service
    public class MachineService {
    	..............
    
    	@Transactional
    	@CacheEvict(cacheNames = "machines", allEntries = true)
    	public MachineRedis save(MachineRedis machine) {
    		MachineRedis machineRedis = machineRedisRepository.save(machine);
    		machineRepository.save(new MachineRedisToMachine().exec(machineRedis));
    		return machineRedis;
    	}
    	
    	@Cacheable(cacheNames = "machines")
    	public List<MachineRedis> findAll() {
    		return (List<MachineRedis>) machineRedisRepository.findAll();
    	}
    }
    ```
