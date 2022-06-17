package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Email_SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button btn;
    User user;
    String kategorija,ime,broj,email,password;
    EditText im,br,em,pass;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        mAuth = FirebaseAuth.getInstance();
        im=findViewById(R.id.ime);
        br=findViewById(R.id.broj);
        em=findViewById(R.id.email);
        pass=findViewById(R.id.lozinka);
        btn=findViewById(R.id.registrira_kor);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategorija="korisnik";
                ime=im.getText().toString();
                broj=br.getText().toString();
                email=em.getText().toString();
                password=pass.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = new User(ime, email, password, broj, kategorija);
                            FirebaseUser user_new = mAuth.getCurrentUser();
                            databaseReference.child(user_new.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Email_SignUp.this,"Успешна регистрација!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Email.class));
                                    }
                                    else {
                                        Toast.makeText(Email_SignUp.this,"Неуспешно!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Email_SignUp.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }
}