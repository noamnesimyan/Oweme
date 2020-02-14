package com.example.oweme;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(version = 1, entities = {Depth.class, Expense.class}, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase implements ExpenseDao, DepthDao {

    abstract protected DepthDao getDepthDao();
    abstract protected ExpenseDao getExpenseDao();

    private static final String DB_NAME = "MyDataBase.db";
    private static volatile MyDataBase instance;

    static synchronized MyDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public MyDataBase() { }

    private static MyDataBase create(final Context context) {
        return Room.databaseBuilder(context, MyDataBase.class, DB_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void addNewExpense(Expense newExpense) {
        getExpenseDao().addNewExpense(newExpense);
    }

    @Override
    public int count(String expenseID) {
        return getExpenseDao().count(expenseID);
    }

    @Override
    public void addNewDepth(Depth newDepth) {
        getDepthDao().addNewDepth(newDepth);
    }

    @Override
    public void deleteDepth() {
        getDepthDao().deleteDepth();
    }

    @Override
    public List<Depth> getAllDepths() {
        return getDepthDao().getAllDepths();
    }
}