/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package car.location;


import car.Car;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarLocationController {

	private final CarRepository repository;


	public CarLocationController(CarRepository repository) {
		this.repository = repository;
	}

	/**
	 * From a terminal:
	 * <pre>
	 * curl -v http://localhost:8081/cars --> JSON array
	 * curl -v -H "Accept:application/stream+json" ... --> Stream of JSON objects
	 * </pre>
	 *
	 * Access the data: .doOnNext, .map, etc<br>
	 * Log backpressure: .log()<br>
	 * Simulate delay: .delayElements<br>
	 *
	 * <p>Switch from Spring WebFlux to Spring MVC (pom.xml) and see impact on backpressure:
	 * spring-boot-starter-webflux -> spring-boot-starter-web
	 */

	@GetMapping("/cars")
	public Flux<Car> getCars() {
		return this.repository.findAll().log();
	}

	@PostMapping(path="/cars", consumes="application/stream+json")
	public Mono<Void> loadCars(@RequestBody Flux<Car> cars) {
		return this.repository.insert(cars).then();
	}

}