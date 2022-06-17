package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Rating extends AppCompatActivity {

    String kluc;
    String[] niza;
    TextView salon;
    RatingBar bar;
    Button button;
    Float rejting;
    String userID;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        kluc=getIntent().getStringExtra("zaRating");
        niza=kluc.split("/");
        kluc=niza[1];
        salon=findViewById(R.id.salonn);
        salon.setText(niza[0]);
        bar=findViewById(R.id.ratingBar);
        button=findViewById(R.id.buttonr);
        userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("termin/"+niza[2]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Termin value = snapshot.getValue(Termin.class);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rejting = bar.getRating();
                        Izvestaj izvestaj = new Izvestaj(userID, kluc, rejting);
                        FirebaseDatabase.getInstance().getReference("rating").child(UUID.randomUUID().toString()).setValue(izvestaj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child("status").setValue(3);
                                    Toast.makeText(Rating.this,"Успешнo испратено!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Welcome.class));
                                } else {
                                    Toast.makeText(Rating.this, "Неуспешно испраќање!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}