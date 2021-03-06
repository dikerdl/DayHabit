package com.jarimport.dl.dayhabit.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HABIT".
*/
public class HabitDao extends AbstractDao<Habit, Long> {

    public static final String TABLENAME = "HABIT";

    /**
     * Properties of entity Habit.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HabitInfo = new Property(1, String.class, "habitInfo", false, "HABIT_INFO");
        public final static Property AddTime = new Property(2, String.class, "addTime", false, "ADD_TIME");
    }


    public HabitDao(DaoConfig config) {
        super(config);
    }
    
    public HabitDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HABIT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HABIT_INFO\" TEXT UNIQUE ," + // 1: habitInfo
                "\"ADD_TIME\" TEXT UNIQUE );"); // 2: addTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HABIT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Habit entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String habitInfo = entity.getHabitInfo();
        if (habitInfo != null) {
            stmt.bindString(2, habitInfo);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(3, addTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Habit entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String habitInfo = entity.getHabitInfo();
        if (habitInfo != null) {
            stmt.bindString(2, habitInfo);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(3, addTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Habit readEntity(Cursor cursor, int offset) {
        Habit entity = new Habit( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // habitInfo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // addTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Habit entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHabitInfo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAddTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Habit entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Habit entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Habit entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
