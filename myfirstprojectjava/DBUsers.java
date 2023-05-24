package com.example.myfirstprojectjava;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBUsers{

    private static final String DATABASE_NAME = "UsersDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "loginsTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_MONEY = "money";
    private static final String COLUMN_MS = "MS";
    private static final String COLUMN_RELOADTIME = "RELOADTIME";
    private static final String COLUMN_BONUSREWARD = "BONUSREWARD";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LOGIN = 1;
    private static final int NUM_COLUMN_PASSWORD = 2;
    private static final int NUM_COLUMN_MONEY = 3;
    private static final int NUM_COLUMN_MS = 4;
    private static final int NUM_COLUMN_RELOADTIME = 5;
    private static final int NUM_COLUMN_BONUSREWARD = 6;

    private SQLiteDatabase mDataBase;
    Context context;

    public DBUsers(Context context) {
        this.context = context;
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
        int tableCount = getTableCount(mDataBase);
        System.out.println(tableCount);
    }

    public int getTableCount(SQLiteDatabase database) {
        int tableCount = 0;
        Cursor cursor =  database.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%'", null);
        if (cursor.moveToFirst()) {
            tableCount = cursor.getInt(0);
        }
        cursor.close();
        return tableCount;
    }

    @SuppressLint("Recycle")
    public long insert(User user) {
        mDataBase.execSQL("INSERT INTO loginsTable (login, password, money, MS, RELOADTIME, BONUSREWARD) " +
                "VALUES (?, ?, ?, ?, ?, ?)", new String[]{
                user.login,
                user.password,
                String.valueOf(user.money),
                String.valueOf(user.toSend[0]),
                String.valueOf(user.toSend[1]),
                String.valueOf(user.toSend[2])
        });
        return 0;
    }

    public User select(String login) {
        Cursor mCursor = mDataBase.rawQuery("SELECT * FROM loginsTable where login = ?", new String[]{login});
        if (mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            String password = mCursor.getString(NUM_COLUMN_PASSWORD);
            int id = mCursor.getInt(NUM_COLUMN_ID);
            int money = mCursor.getInt(NUM_COLUMN_MONEY);
            int[] ts = new int[]{mCursor.getInt(NUM_COLUMN_MS), mCursor.getInt(NUM_COLUMN_RELOADTIME), mCursor.getInt(NUM_COLUMN_BONUSREWARD)};
            mCursor.close();

            return new User(id, ts, money, login, password);
        }
        return null;
    }

    public int update(User user) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOGIN, user.login);
        cv.put(COLUMN_PASSWORD, user.password);
        cv.put(COLUMN_MONEY, user.money);
        cv.put(COLUMN_MS, user.toSend[0]);
        cv.put(COLUMN_RELOADTIME, user.toSend[1]);
        cv.put(COLUMN_BONUSREWARD, user.toSend[2]);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_LOGIN + " = ?", new String[]{user.login});
    }

    class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("ASD");
            String query = "CREATE TABLE IF NOT EXISTS loginsTable (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "login STRING UNIQUE NOT NULL, " +
                    "password STRING, " +
                    "money INTEGER, " +
                    "MS INTEGER DEFAULT (0), " +
                    "RELOADTIME INTEGER DEFAULT (0), " +
                    "BONUSREWARD INTEGER DEFAULT (0));";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }
}