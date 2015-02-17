package soget.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import soget.model.Customer;
import soget.model.Greeting;
import soget.repository.CustomerRepository;

@EnableAutoConfiguration
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
	private CustomerRepository repository;
	
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	
    	repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", name));
		repository.save(new Customer("Bob", name));
		
		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
				for (Customer customer : repository.findAll()) {
					System.out.println(customer);
				}
				System.out.println();

				// fetch an individual customer
				System.out.println("Customer found with findByFirstName('Alice'):");
				System.out.println("--------------------------------");
				System.out.println(repository.findByFirstName("Alice"));

				System.out.println("Customers found with findByLastName('Smith'):");
				System.out.println("--------------------------------");
				for (Customer customer : repository.findByLastName("Smith")) {
					System.out.println(customer);
				}
				
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
