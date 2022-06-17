package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Salon_pregled extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,naslov;
    Button btn;
    private DatabaseReference databaseReference,dr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_pregled);
        String kluc=getIntent().getStringExtra("salon");
        btn=findViewById(R.id.zakazi);
        naslov=findViewById(R.id.im);
        tv1=findViewById(R.id.kat);
        tv2=findViewById(R.id.lok);
        tv3=findViewById(R.id.rab);
        tv4=findViewById(R.id.br);
        tv5=findViewById(R.id.em);
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + kluc);
        dr = FirebaseDatabase.getInstance().getReference("salon/" + kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User val=snapshot.getValue(User.class);
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Salon_user salon=snapshot.getValue(Salon_user.class);
                        naslov.setText(val.getName());
                        tv1.setText(val.getKategorija());
                        tv2.setText(salon.getLokacija());
                        tv3.setText("Работно Време: "+salon.getRabotno());
                        tv4.setText("Телефонски број:"+val.getBroj());
                        tv5.setText(val.getEmail());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Zakazi_termin.class);
                intent.putExtra("salon",kluc);
                startActivity(intent);
            }
        });
    }
}