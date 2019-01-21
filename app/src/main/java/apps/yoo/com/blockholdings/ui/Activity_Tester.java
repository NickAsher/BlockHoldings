package apps.yoo.com.blockholdings.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.Objects.Object_Coin;

public class Activity_Tester extends AppCompatActivity {
    Context context ;
    TextView displayTextView ;
    AppDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;

        setupSaveButton() ;
        setupRefreshButton() ;
    }

    private void setupSaveButton(){
        displayTextView = findViewById(R.id.activityTester_TextView_Display) ;


        Button saveButton = findViewById(R.id.activityTester_Button_1) ;
        final EditText editTextId = findViewById(R.id.activityTester_TextView_EditText1) ;
        final EditText editTextSymbol = findViewById(R.id.activityTester_TextView_EditText2) ;
        final EditText editTextName = findViewById(R.id.activityTester_TextView_EditText3) ;
        final EditText editTextImageLogo = findViewById(R.id.activityTester_TextView_EditText4) ;
        final EditText editTextTwitter = findViewById(R.id.activityTester_TextView_EditText5) ;


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.coinDao().insertCoin(new Object_Coin(
                        editTextId.getText().toString(),
                        editTextSymbol.getText().toString(),
                        editTextName.getText().toString(),
                        editTextImageLogo.getText().toString(),
                        editTextTwitter.getText().toString()
                ));


            }
        });



    }

    private void setupRefreshButton(){
        Button refreshButton = findViewById(R.id.activityTester_Button_2) ;
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTextView.setText("");
                List<Object_Coin> listOfCoins = db.coinDao().getListOfAllCoins() ;
                for (Object_Coin coin:listOfCoins) {
                    displayTextView.append(
                            coin.getId() + " - " +  coin.getSymbol() + " - " + coin.getName() + " - " + coin.getImageLogoLink() + " - " + coin.getTwitterLink()  + " \n"
                    );

                }

            }
        });
    }
}
