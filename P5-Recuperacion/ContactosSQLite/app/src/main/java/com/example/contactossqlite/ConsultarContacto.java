package com.example.contactossqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultarContacto extends AppCompatActivity {
    EditText edNombre;
    TextView edApellido, edEmail, edTelefono;
    ConexionSQLite conn;
    @Override
    protected void onCreate(Bundle savedInstanciaState) {
        super.onCreate(savedInstanciaState);
        setContentView(R.layout.activity_buscar_contacto);
        conn=new ConexionSQLite(getApplicationContext(),"bd_contactos",null,1);
        final Button btGuardar = (Button) this.findViewById(R.id.btBuscar);
        edNombre = (EditText) this.findViewById(R.id.edNombre);
        edApellido = (TextView) this.findViewById(R.id.edApellido);
        edEmail = (TextView) this.findViewById(R.id.edEmail);
        edTelefono = (TextView) this.findViewById(R.id.edTelefono);



        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             consultar();
            }


        });
    }
    private void consultar() {
        SQLiteDatabase db= conn.getReadableDatabase();
        String[] parametros={edNombre.getText().toString()};
        String[] campos={Utilidades.CAMPO_APELLIDO,Utilidades.CAMPO_EMAIL,Utilidades.CAMPO_TELEFONO};

        try {
            Cursor cursor= db.query(Utilidades.TABLA_CONTACTO,campos,Utilidades.CAMPO_NOMBRE+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            edApellido.setText(cursor.getString(0));
            edEmail.setText(cursor.getString(1));
            edTelefono.setText(cursor.getString(2));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El contacto no existe",Toast.LENGTH_LONG).show();
            limpiar();
        }

    }
    private void limpiar(){
        edNombre.setText("");
        edApellido.setText("");
        edEmail.setText("");
        edTelefono.setText("");
    }


}
