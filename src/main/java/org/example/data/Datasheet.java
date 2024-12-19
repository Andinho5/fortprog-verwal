package org.example.data;

import org.example.transaction.BaseTransaction;

import java.util.List;

/**
 * A datasheet is a class representing a local file that contains information about account actions.
 * Datasheets can either be used to plan transactions by writing them by hand and using them in the program
 * or are created when full account information is requested.
 */
public interface Datasheet {

    /**
     * Appends a transaction to the sheet.
     * @param transaction The transaction to apply to the list.
     */
    void append(BaseTransaction transaction);

    /**
     * Saves any changes made to the corresponding file.
     */
    void save();

    /**
     * Loads any content (if known and this sheet exists locally) into the memory.
     * @return Whether the loading was successful or not.
     */
    boolean load();

    /* -- Getter -- */

    List<BaseTransaction> getTransactionsFor(String receiver);

    List<BaseTransaction> getAll();

    double getLoadedTotalMoney();

}
