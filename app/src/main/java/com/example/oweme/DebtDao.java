package com.example.oweme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DebtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void updateDebt(Debt newDebt);

    @Query("DELETE FROM MyDebts WHERE amount = 0")
    public void deleteDebt();

    @Query("DELETE FROM MyDebts WHERE permanent=:permanent")
    public void deleteAllDebts(boolean permanent);

    @Query("SELECT * FROM MyDebts WHERE permanent=:permanent")
    public List<Debt> getAllDebts(boolean permanent);

    @Query("SELECT * FROM MyDebts where userID=:uID and permanent=:permanent")
    public Debt getDebtByUid(String uID, boolean permanent);

}
