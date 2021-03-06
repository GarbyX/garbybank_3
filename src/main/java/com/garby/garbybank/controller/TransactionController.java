package com.garby.garbybank.controller;

import javax.validation.Valid;

import com.garby.garbybank.dto.TransferResult;
import com.garby.garbybank.exception.AccountNotExistException;
import com.garby.garbybank.exception.OverDraftException;
import com.garby.garbybank.model.TransferRequest;
import com.garby.garbybank.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garby.garbybank.exception.CheckBalanceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/transaction")
@Api(tags = {"Transaction Controller"}, description = "Provide APIs for transaction related operation")
public class TransactionController {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private AccountsService accountService;

	@PostMapping(consumes = { "application/json" })
	@ApiOperation(value = "API to create transaction", response = TransferResult.class, produces = "application/json")
	public ResponseEntity transferMoney(@RequestBody @Valid TransferRequest request) throws Exception {

		try {
			accountService.transferBalances(request);
			
			TransferResult result = new TransferResult();
			result.setAccountFromId(request.getAccountFromId());
			result.setBalanceAfterTransfer(accountService.checkBalance(request.getAccountFromId()));
			
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | OverDraftException e) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw e;
		} catch (CheckBalanceException cbEx) {
			log.error("Fail to check balances after transfer, please check with system administrator.");
			throw cbEx;
		}
	}
}
