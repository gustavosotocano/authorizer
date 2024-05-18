package com.authorizer.adapter.persistence.imp;


import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.domain.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The interface defines an output adapter which is an implementation of the outbound port. It enables the core application to communicate to external dependency such as the database.
 */
@Repository
public class TransactionRepositoryImplementation implements TransactionRepository {

    private static final Map<Integer, Transaction> transactionMap = new HashMap<Integer, Transaction>(0);

    int id = 0;

    public static boolean isDifferenceDifferentFromTwoMinutes(Date date1, Date date2) {
        long differenceInMillis = Math.abs(date2.getTime() - date1.getTime());
        // Convertir la diferencia a minutos
        long differenceInMinutes = differenceInMillis / (60 * 1000);
        // Verificar si la diferencia es diferente a dos minutos
        return (differenceInMinutes > 0 || differenceInMinutes < 2);
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {

        transactionMap.put(id++, transaction);

        return transaction;
    }

    @Override
    public List<Transaction> lastTransactions(Transaction transaction) {

        Date sDate1 =   transaction.getTransaction().getTime();

        Predicate<Date> filterGreater3 = f -> (isDifferenceDifferentFromTwoMinutes(f,sDate1)) || f.equals(sDate1);

        return transactionMap.entrySet()
                .stream()
                .parallel()
                .filter(x -> filterGreater3.test(x.getValue().getTransaction().getTime()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

    }

    @Override
    public List<Transaction> sameLastTransactions(Transaction transaction) {

        Date sDate2 = transaction.getTransaction().getTime();
        Predicate<Date> filterSameTransaction = f ->( isSameDate(f,sDate2) );

         return transactionMap.entrySet()
                .stream()
                .parallel()
                .filter(x -> filterSameTransaction.test(x.getValue().getTransaction().getTime()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

    }

    public static boolean isSameDate(Date date1, Date date2) {

        return (date1.compareTo(date2) == 0);
    }


    @Override
    public void remove() {
        if (null != transactionMap) {
            transactionMap.clear();
        }
    }

}
