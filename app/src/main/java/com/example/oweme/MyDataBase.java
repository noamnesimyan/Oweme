package com.example.oweme;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(version = 1, entities = {Debt.class, Expense.class}, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase implements ExpenseDao, DebtDao {

    abstract protected DebtDao getDebtDao();
    abstract protected ExpenseDao getExpenseDao();

    private static final String DB_NAME = "MyDataBase.db";
    private static volatile MyDataBase instance;
    private static DebtDao debtDao;
    private static ExpenseDao expenseDao;


    static synchronized MyDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
            debtDao = instance.getDebtDao();
            expenseDao = instance.getExpenseDao();
        }
        return instance;
    }

    public MyDataBase() { }

    private static MyDataBase create(final Context context) {
        return Room.databaseBuilder(context, MyDataBase.class, DB_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void addNewExpense(Expense newExpense) {
        expenseDao.addNewExpense(newExpense);
    }

    @Override
    public void updateDebt(Debt newDebt) {
        debtDao.updateDebt(newDebt);
    }


    public boolean exist(Expense expense) {
        return (expenseDao.count(expense.getExpenseID()) != 0);
    }

    @Override
    public int count(String expenseID) {
        return getExpenseDao().count(expenseID);
    }


    @Override
    public void deleteDebt() {
        getDebtDao().deleteDebt();
    }

    @Override
    public List<Debt> getAllDebts(boolean permanent) {
        return getDebtDao().getAllDebts(permanent);
    }

    @Override
    public Debt getDebtByUid(String uid, boolean permanent) {
        return debtDao.getDebtByUid(uid, permanent);
    }

    @Override
    public void deleteAllDebts(boolean permanent) {
        getDebtDao().deleteAllDebts(permanent);
    }
}
