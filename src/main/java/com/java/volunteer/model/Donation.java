package com.java.volunteer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for donations
 */
public class Donation {
    private int donationId;
    private Integer donorId;  // Can be anonymous (null)
    private BigDecimal amount;
    private LocalDateTime donationDate;
    private String paymentMethod;
    private String transactionId;
    private boolean isAnonymous;
    private String message;
    
    // Reference for convenience
    private User donor;

    public Donation() {
    }
    
    // Constructor for anonymous donations
    public Donation(BigDecimal amount, String paymentMethod, String transactionId, String message) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.isAnonymous = true;
        this.message = message;
    }

    // Constructor for donations with donor
    public Donation(Integer donorId, BigDecimal amount, String paymentMethod, 
                  String transactionId, boolean isAnonymous, String message) {
        this.donorId = donorId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.isAnonymous = isAnonymous;
        this.message = message;
    }

    // Getters and Setters
    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public Integer getDonorId() {
        return donorId;
    }

    public void setDonorId(Integer donorId) {
        this.donorId = donorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    @Override
    public String toString() {
        String donorName;
        if (isAnonymous) {
            donorName = "Anonymous";
        } else {
            donorName = (donor != null) ? donor.getUsername() : "Unknown";
        }
        
        return "Donation{" +
                "donationId=" + donationId +
                ", amount=" + amount +
                ", donationDate=" + donationDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", donor=" + donorName +
                ", message='" + (message != null ? message : "") + '\'' +
                '}';
    }
}