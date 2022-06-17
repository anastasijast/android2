package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {
    Spinner sp;
    TextView pod;
    Button btn,poraki,usluga,preb;
    String userID,kat;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp=findViewById(R.id.spinner_welcome);
        poraki=findViewById(R.id.poraki);
        usluga=findViewById(R.id.usluga);
        preb=findViewById(R.id.prebaraj);
        FirebaseUser user_new = FirebaseAuth.getInstance().getCurrentUser();
        sp.setPrompt("Изберете салон за:");
        pod=findViewById(R.id.podatok);
        btn=findViewById(R.id.odjavi_k);
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user_new.getUid());
        userID=user_new.getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value=snapshot.getValue(User.class);
                String source="<b> Име: </b>"+value.getName()+"<br><b>Емаил:</b> "+value.getEmail()+
                        "<br><b>Телефонски број: </b>"+value.getBroj();
                pod.setText(Html.fromHtml(source));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        poraki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),Poraki.class));
            }
        });
        usluga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.isShown()){
                    sp.setVisibility(View.INVISIBLE);
                    preb.setVisibility(View.INVISIBLE);
                }
                else{
                    sp.setVisibility(View.VISIBLE);
                    preb.setVisibility(View.VISIBLE);
                }
            }
        });
        preb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kat=(String)sp.getSelectedItem();
                Intent intent=new Intent(getApplicationContext(),Saloni.class);
                intent.putExtra("zaKat", kat);
                startActivity(intent);
            }
        });
    }
}