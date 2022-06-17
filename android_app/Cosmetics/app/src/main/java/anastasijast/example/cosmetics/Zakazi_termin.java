package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

public class Zakazi_termin extends AppCompatActivity {
    TextView vreme,salon,datum;
    TimePickerDialog picker;
    DatePickerDialog pk;
    Button btn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakazi_termin);
        String kluc=getIntent().getStringExtra("salon");
        vreme=findViewById(R.id.vreme);
        salon=findViewById(R.id.salon_nas);
        btn=findViewById(R.id.zakaza);
        datum=findViewById(R.id.datum);
        FirebaseUser user_new = FirebaseAuth.getInstance().getCurrentUser();
        String user=user_new.getUid();
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int minutes = cldr.get(Calendar.MINUTE);
        int year=cldr.get(Calendar.YEAR);
        int month=cldr.get(Calendar.MONTH);
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             User user=snapshot.getValue(User.class);
             salon.setText(user.getName().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        datum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pk=new DatePickerDialog(Zakazi_termin.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year_k, int month_k, int dayOfMonth) {
                                datum.setText(dayOfMonth+"/"+month_k);
                            }
                        },year,month,day);
                pk.show();
            }
        });
        vreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new TimePickerDialog(Zakazi_termin.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                vreme.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Termin nov=new Termin(vreme.getText().toString(),datum.getText().toString(),user,kluc,0);
                FirebaseDatabase.getInstance().getReference("termin").child(UUID.randomUUID().toString()).setValue(nov).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Zakazi_termin.this,"Успешно!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Welcome.class));
                        }
                        else {
                            Toast.makeText(Zakazi_termin.this,"Неуспешно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}