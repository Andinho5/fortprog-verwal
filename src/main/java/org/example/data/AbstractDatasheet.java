package org.example.data;

import org.example.transaction.BaseTransaction;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public abstract class AbstractDatasheet implements Datasheet {

    private static final Function<String, List<BaseTransaction>> INNER_MAPPER = s -> new ArrayList<>();

    protected final ArrayList<BaseTransaction> transactions = new ArrayList<>();
    protected final HashMap<String, List<BaseTransaction>> pointingTransactions = new HashMap<>();
    protected final File _file;
    public AbstractDatasheet(File file) {
        this._file = file;
    }

    @Override
    public void append(BaseTransaction transaction){
        transactions.add(transaction);
        List<BaseTransaction> inner = pointingTransactions.computeIfAbsent(transaction.getReceiverMail(), INNER_MAPPER);
        inner.add(transaction);
    }

    @Override
    public List<BaseTransaction> getTransactionsFor(String receiverId) {
        return pointingTransactions.getOrDefault(receiverId, Collections.emptyList());
    }

    @Override
    public List<BaseTransaction> getAll() {
        return transactions;
    }

    static class SimpleTransaction implements BaseTransaction{
        private final String transactionId;
        private final String receiverMail;
        private final double amount;
        private final Timestamp date;
        private final String purposeMessage;
        public SimpleTransaction(String transactionId, String receiverMail, double amount, Timestamp date, String purposeMessage) {
            this.transactionId = transactionId;
            this.receiverMail = receiverMail;
            this.amount = amount;
            this.date = date;
            this.purposeMessage = purposeMessage;
        }
        public SimpleTransaction(String receiverMail, double amount, String purposeMessage){
            this(UUID.randomUUID().toString(), receiverMail, amount, Timestamp.valueOf(LocalDateTime.now()), purposeMessage);
        }


        @Override
        public String getTransactionId() {
            return transactionId;
        }

        @Override
        public String getReceiverMail() {
            return receiverMail;
        }

        @Override
        public double getAmount() {
            return amount;
        }

        @Override
        public Timestamp getDate() {
            return date;
        }

        @Override
        public String getPurposeMessage() {
            return purposeMessage;
        }
    }

}
