package com.bank.app.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateId {
	
	private static final AtomicLong CUSTOMER_SEQ = new AtomicLong(1000);
	private static final AtomicLong ACCOUNT_SEQ = new AtomicLong(1000);
	private static final AtomicLong TRANSACTION_SEQ = new AtomicLong(1000);
	
	private GenerateId() {
		
	}
	
	
	public static String generateCustomerId() {
		return "BANK00C" + CUSTOMER_SEQ.getAndIncrement(); 
	}
	
	public static String generateAccountId() {
		return "BANK00A" + ACCOUNT_SEQ.getAndIncrement();
	}
	public static String generateTransactionId() {
		return "BANK00T" + TRANSACTION_SEQ.getAndIncrement();
	}
	
	
	public static String generateRandomId() {
		return UUID.randomUUID().toString();
	}
	
}
