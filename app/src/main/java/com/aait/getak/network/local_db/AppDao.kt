package com.aait.getak.network.local_db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aait.getak.models.countries_model.Country


@Dao
interface AppDao {
    @Query("select * from countries ")
    fun getCountries(): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCountries(countries:List<Country>)


}
