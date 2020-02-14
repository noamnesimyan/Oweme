package com.example.oweme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addNewExpense(Expense newExpense);

    @Query("SELECT count(expenseID) FROM myExpenses where expenseID = :expenseID")
    public int count(String expenseID);

}
