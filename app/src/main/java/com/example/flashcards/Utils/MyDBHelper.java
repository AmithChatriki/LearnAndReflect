package com.example.flashcards.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.flashcards.AccountsModel;
import com.example.flashcards.GratitudesModel;
import com.example.flashcards.Model.CardsModel;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AccountsDB";
    private static final int DB_VERSION = 1;
    private static final String tableAccounts = "accounts";
    private static final String KEY_id = "id";
    private static final String KEY_emailID = "emailID";
    private static final String KEY_password = "password";

    private static final String KEY_iscurrentuser = "isCurrent";

    private static final String tableGrats = "gratitude";
    private static final String KEY_id2 = "id";

    private static final String KEY_title = "title";

    private static final String KEY_content = "content";

    private static final String tableCards = "cards";

    private static final String KEY_id3 = "id";
    private static final String KEY_goal = "goal";
    private static final String KEY_status = "status";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
//        context.deleteDatabase(DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE accounts(id INTEGER PRIMARY KEY AUTOINCREMENT, emailID TEXT, password TEXT)
        db.execSQL("CREATE TABLE "+tableAccounts+"("+KEY_id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_emailID+" TEXT UNIQUE, "+KEY_password+" TEXT, "+KEY_iscurrentuser+" INT DEFAULT 0)");

        db.execSQL("CREATE TABLE "+tableGrats+"("+KEY_id2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_emailID+" TEXT, "+KEY_title+" TEXT, "+KEY_content+" TEXT, FOREIGN KEY ("+KEY_emailID+") REFERENCES "+tableAccounts+"("+KEY_emailID+"))");

        db.execSQL("CREATE TABLE " + tableCards + "(id INTEGER PRIMARY KEY AUTOINCREMENT, goal TEXT, status INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + tableAccounts);
        db.execSQL("DROP TABLE IF EXISTS " + tableGrats);
        db.execSQL("DROP TABLE IF EXISTS " + tableCards);
        onCreate(db);
    }

    public void addAccounts(String emailID, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_emailID, emailID);
        values.put(KEY_password, password);
        values.put(KEY_iscurrentuser, 1);

        db.insert(tableAccounts, null, values);
    }

    public ArrayList<AccountsModel> fetchAccounts() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+tableAccounts, null);

        ArrayList<AccountsModel> arrayAccounts = new ArrayList<>();

        while(cursor.moveToNext()) {
            AccountsModel model = new AccountsModel();
            model.setId(cursor.getInt(0));
            model.setEmailID(cursor.getString(1));
            model.setPassword(cursor.getString(2));

            arrayAccounts.add(model);
        }
        return arrayAccounts;
    }

    public int Login(String username,String password)
    {
        String[] selectionArgs = new String[]{username, password};
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from "+tableAccounts+" where "+KEY_emailID+"=? and "+KEY_password+"=?", selectionArgs);
            c.moveToFirst();
            i = c.getCount();
            Log.d("login", "index = "+i);
            c.close();
            db.execSQL("UPDATE "+tableAccounts+" SET "+KEY_iscurrentuser+" = 1 WHERE "+KEY_emailID+"=? and "+KEY_password+"=?", selectionArgs);
            return i;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public void addGratitudes(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("accounts",
                new String[] {"emailID"},
                "isCurrent" + " = ? ",
                new String[] {"1"},
                null, null, null);
        cursor.moveToFirst();
        int x = cursor.getColumnIndex("emailID");
        String currentusermail = cursor.getString(x);

        ContentValues values = new ContentValues();
        values.put(KEY_emailID, currentusermail);
        values.put(KEY_title, title);
        values.put(KEY_content, content);

        db.insert(tableGrats, null, values);
    }

    public Object getGrats() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("accounts",
                new String[] {"emailID"},
                "isCurrent" + " = ? ",
                new String[] {"1"},
                null, null, null);
        cursor.moveToFirst();
        int x = cursor.getColumnIndex("emailID");
        String currentUserMail = cursor.getString(x);


        List<GratitudesModel> data = new ArrayList<>();


        Cursor cursor3 = db.query(tableGrats,
                new String[] {KEY_emailID, KEY_title, KEY_content},
                KEY_emailID + " = ? ",
                new String[] {currentUserMail},
                null, null, null);

        try {
            while (cursor3.moveToNext()) {

                int y = cursor3.getColumnIndex(KEY_title);
                String title = cursor3.getString(y);
                int y1 = cursor3.getColumnIndex(KEY_content);
                String content = cursor3.getString(y1);

                GratitudesModel row = new GratitudesModel();
                row.setTitle(title);
                row.setContent(content);
                data.add(row);

            }
        } finally {
            cursor.close();
        }


        return data;
    }

    public void deleteGrats(String title) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableGrats, KEY_title+"=?", new String[]{title});

    }

    public void insertGoal(CardsModel model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_goal, model.getGoal());
        values.put(KEY_status, 0);
        db.insert(tableCards, null, values);

    }

    public void updateGoal(int id, String task) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_goal, task);
        db.update(tableCards, values, "id=?", new String[]{String.valueOf(id)});

    }

    public void updateStatus(int id, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_status, status);
        db.update(tableCards, values, "id=?", new String[]{String.valueOf(id)});

    }

    public void deleteGoal(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableCards, "id=?", new String[]{String.valueOf(id)});

    }

    public List<CardsModel> getAllGoals() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<CardsModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(tableCards, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        CardsModel card = new CardsModel();
                        try {
                            int x1 = cursor.getColumnIndex(KEY_id3);
                            card.setId(cursor.getInt(x1));
                            x1 = cursor.getColumnIndex(KEY_goal);
                            card.setGoal(cursor.getString(x1));
                            x1 = cursor.getColumnIndex(KEY_status);
                            card.setStatus(cursor.getInt(x1));
                        } catch(IllegalStateException ex) {
                            Log.d("hell", String.valueOf(ex));
                        }
                        modelList.add(card);

                    }while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }

        return modelList;

    }

}
