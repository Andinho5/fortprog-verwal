package org.example.transaction;

import java.sql.Timestamp;

/**
 * Base interface of simple transaction functionality.
 */
public interface BaseTransaction {

    int ELEMENTS = BaseTransaction.class.getDeclaredMethods().length; //all elements that need to be serialized.

    String getTransactionId();

    String getReceiverMail();

    double getAmount();

    Timestamp getDate();

    String getPurposeMessage();

}
