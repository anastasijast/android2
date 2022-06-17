package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Email extends AppCompatActivity {
    Button btn;
    EditText password,email;
    private FirebaseAuth mAuth;
    String pass,em;
    DatabaseReference databaseReference;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        tv=findViewById(R.id.reg_se_kor);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email_k);
        password=findViewById(R.id.pass_k);
        btn=findViewById(R.id.prodolzi);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp_pred.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               najava();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            currentUser.reload();
            FirebaseUser user = mAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User value = snapshot.getValue(User.class);
                    if (value.getKategorija().equals("korisnik")) {
                        Intent intent = new Intent(getApplicationContext(), Welcome.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Salon.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void najava(){
        em=email.getText().toString();
        pass=password.getText().toString();
        mAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Email.this, "Успешно се најавивте!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User value = snapshot.getValue(User.class);
                            if (value.getKategorija().equals("korisnik")) {
                                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Salon.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else{
                    Toast.makeText(Email.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
