package cl.tomas.naivepay;

import cl.tomas.naivepay.domain.*;
import cl.tomas.naivepay.repository.TransactionStateRepository;
import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.account.AccountService;
import cl.tomas.naivepay.service.customer.CustomerService;
import cl.tomas.naivepay.service.transaction.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Preload {

	@Bean
	CommandLineRunner run(CustomerService customerService, AccessService accessService, AccountService accountService,
                          TransactionService transactionService, TransactionStateRepository transactionStateRepository) {
		return args -> {

			Customer admin = new Customer();
			admin.setCusName("admin");

			Customer customer = new Customer();
			customer.setCusName("customer");

			Access adminAccess = new Access();
			adminAccess.setAccName("admin");
			adminAccess.setAccPassword("admin");
			adminAccess.setAccRole(1);

			Access customerAccess = new Access();
			customerAccess.setAccName("customer");
			customerAccess.setAccPassword("customer");
			customerAccess.setAccRole(0);

			adminAccess = accessService.create(adminAccess);
			customerAccess = accessService.create(customerAccess);

			admin.setCusAccess(adminAccess);
			customer.setCusAccess(customerAccess);
			admin = customerService.createCustomer(admin);
			customer = customerService.createCustomer(customer);

			Account account1 = new Account();
			account1.setAccId((long)1  );
			account1.setAccAmount(53152);
			account1.setAccCustomer(customer);
			account1.setAccNum( 11121431);
			account1.setAccCvv(341);

			Account account2 = new Account();
			account2.setAccId((long)2);
			account2.setAccAmount(93452);
			account2.setAccCustomer(admin);
			account2.setAccNum( 12451431);
			account2.setAccCvv(521);

			accountService.createAccount(account1);
			accountService.createAccount(account2);

			TransactionState state = new TransactionState();
			state.setTrsName("a");
			state.setTrsId(1L);
			transactionStateRepository.save(state);
			Transaction transaction = new Transaction();
			transaction.setTraTransactionState(state);
			transaction.setTraAmount(15000);
			transaction.setTraOriginAccount("1");
			transaction.setTraAccount(account1);
			transaction.setTraDestinationAccount("2");

			transactionService.transferAmount(transaction);


		};
	}
}
