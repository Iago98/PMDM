package com.example.contactossqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {




    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{db.beginTransaction();
            db.execSQL(Utilidades.CREAR_TABLA_CONTACTO);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
    db.execSQL("DROP TABLE IF EXISTS contactos");
    onCreate(db);
    }
    public Cursor getContacto()
    {

        return getReadableDatabase().query( Utilidades.TABLA_CONTACTO,
                null, null, null, null, null, null );
    }
}
