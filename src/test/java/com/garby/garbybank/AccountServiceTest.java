package com.garby.garbybank;

import com.garby.garbybank.exception.OverDraftException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.garby.garbybank.exception.AccountNotExistException;
import com.garby.garbybank.exception.SystemException;
import com.garby.garbybank.model.Account;
import com.garby.garbybank.model.TransferRequest;
import com.garby.garbybank.repository.AccountsRepository;
import com.garby.garbybank.service.AccountsService;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	AccountsRepository accRepo;
	
	@InjectMocks
	AccountsService accService;
	
	@Test
	public void testRetrieveBalance() {
		when(accRepo.findByAccountId(1L)).thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
		
		assertEquals(BigDecimal.ONE, accService.retrieveBalances(1L).getBalance());
	}
	
	@Test(expected = AccountNotExistException.class)
	public void testRetrieveBalanceFromInvalidAccount() {
		when(accRepo.findByAccountId(1L)).thenReturn(Optional.empty());
		
		accService.retrieveBalances(1L);
	}
	
	@Test
	public void testTransferBalance() throws Exception, Exception, Exception {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(10);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		
		accService.transferBalances(request);
		
		assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}
	
	@Test(expected = OverDraftException.class)
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException, SystemException {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(20);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		
		accService.transferBalances(request);
	}
}
