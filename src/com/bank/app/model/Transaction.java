package com.bank.app.model;
import java.time.LocalDateTime;
import com.bank.app.enums.TransactionType;
import com.bank.app.util.DateUtil;
import com.bank.app.util.GenerateId;

public class Transaction {
	private String transactionId;
	private TransactionType transactionType;
	private double amount;
	private LocalDateTime timestamp;
	private boolean success;
	private String failureReason;
	private String relatedTransationId;
	
	public Transaction(String transactionId, TransactionType transactionType, double amount,
			LocalDateTime timestamp, boolean success, String  failureReason, String relatedTransactionId){
		
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.failureReason = failureReason;
		this.relatedTransationId = relatedTransactionId;
		this.timestamp = timestamp;
		this.success = success;
	}
	
	
	public static Transaction success(TransactionType type, double amount) {
        return new Transaction(
                GenerateId.generateTransactionId(),
                type,
                amount,
                DateUtil.now(),
                true,
                null,
                null
        );
    }

	
    public static Transaction failure(TransactionType type, double amount, String reason) {
        return new Transaction(
        		GenerateId.generateTransactionId(),
                type,
                amount,
                DateUtil.now(),
                false,
                reason,
                null
        );
    }
	
	
	public boolean isDebit() {
		return transactionType == TransactionType.DEBIT;
	}
	
	public boolean isCredit() {
		return transactionType == TransactionType.CREDIT;
	}
	
	public boolean isReversal() {
		return transactionType == TransactionType.REVERSAL;
	}

	public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getRelatedTransactionId() {
        return relatedTransationId;
    }	
	
}
