package com.example.places.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.places.model.Place

@Database(entities = [Place:: class], version = 1, exportSchema = false)

abstract class PlaceDatabase : RoomDatabase(){

    abstract fun placeDao(): PlaceDao

    companion object{
        @Volatile
        private var INSTANCE : PlaceDatabase? = null

        //Patron de dise;o singleton -> se crea solo una vez el objeto y se reutiliza


        fun getDatabase(context: android.content.Context) :
                PlaceDatabase{
            val temp = INSTANCE;
            if (temp != null){
                return temp;
            }
            //
            synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaceDatabase :: class.java,
                    "place_database"
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}