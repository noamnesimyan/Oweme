package com.example.oweme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DepthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addNewDepth(Depth newDepth);

    @Query("DELETE FROM MyDepths WHERE amount = 0")
    public void deleteDepth();

    @Query("SELECT * FROM MyDepths")
    public List<Depth> getAllDepths();

}
