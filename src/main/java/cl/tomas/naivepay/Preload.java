package cl.tomas.naivepay;

import cl.tomas.naivepay.domain.entities.AccessEntity;
import cl.tomas.naivepay.domain.entities.AccountEntity;
import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.infrastructure.models.*;
import cl.tomas.naivepay.infrastructure.repository.AccountRepository;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionStateRepository;
import cl.tomas.naivepay.service.account.AccountService;
import cl.tomas.naivepay.service.customer.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Preload {
	private static final File accountsFile = Paths
			.get("src", "main", "resources", "transactions.json").toFile();

	@Bean
	CommandLineRunner run(CustomerService customerService, CustomerRepository customerRepository, AccountService accountService,
						  TransactionStateRepository transactionStateRepository,
						  TransactionRepository transactionRepository, AccountRepository accountRepository) {
		return args -> {
			List<Transaction> transactions = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());

			try {
				transactions = mapper.readValue(accountsFile, new TypeReference<List<Transaction>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			CustomerEntity admin = new CustomerEntity();
			admin.setCusName("admin");

			CustomerEntity customer = new CustomerEntity();
			customer.setCusName("customer");

			AccessEntity adminAccess = new AccessEntity();
			adminAccess.setAccName("admin");
			adminAccess.setAccPassword("admin");
			adminAccess.setAccRole(1);
			adminAccess.setEnabled(true);

			AccessEntity customerAccess = new AccessEntity();
			customerAccess.setAccName("customer");
			customerAccess.setAccPassword("customer");
			customerAccess.setAccRole(0);
			customerAccess.setEnabled(true);

			admin.setCusAccess(adminAccess);
			customer.setCusAccess(customerAccess);
			admin.setCusMail("tomasrodcas@gmail.com");
			customer.setCusMail("t.rodriguez07@ufromail.cl");
			customerService.createCustomer(admin);
			customerService.createCustomer(customer);

			AccountEntity account1 = new AccountEntity();
			account1.setAccId((long)1  );
			account1.setAccAmount(53152);
			account1.setAccCustomer(customerRepository.findById(2L).orElseThrow().toEntity());
			account1.setAccNum( 11121431);
			account1.setAccCvv(341);

			AccountEntity account2 = new AccountEntity();
			account2.setAccId((long)2);
			account2.setAccAmount(93452);
			account2.setAccCustomer(customerRepository.findById(1L).orElseThrow().toEntity());
			account2.setAccNum( 12451431);
			account2.setAccCvv(521);

			accountService.createAccount(account1);
			accountService.createAccount(account2);

			TransactionState state = new TransactionState();
			state.setTrsName("a");
			state.setTrsId(1L);
			transactionStateRepository.save(state);

			String sDate1="31/12/2021";
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
			for(Transaction transaction : transactions){
				Date randomDate = new Date(ThreadLocalRandom.current()
						.nextLong(date1.getTime(), System.currentTimeMillis()));
				transaction.setTraAccount(accountRepository.findById(1L).orElseThrow());
				transaction.setTraDate(randomDate);
				transaction.setTraTransactionState(state);
				transactionRepository.save(transaction);
			}


		};
	}
}
