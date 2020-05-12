package com.example.reviewinator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class localHistoryScreen extends AppCompatActivity {
    TextView textView1, textView2, textView3;
    String localHistoryString1, localHistoryString2, localHistoryString3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_history_screen);

        textView1 = findViewById(R.id.localHistoryTextView1);
        localHistoryString1 = Objects.requireNonNull(getIntent().getExtras()).getString("Value");
        assert localHistoryString1 != null;
        if(!localHistoryString1.equals(""))textView1.setText(localHistoryString1);

        textView2 = findViewById(R.id.localHistoryTextView2);
        localHistoryString2 = Objects.requireNonNull(getIntent().getExtras()).getString("Value1");
        assert localHistoryString2 != null;
        if(!localHistoryString2.equals(""))textView2.setText(localHistoryString2);

        textView3 = findViewById(R.id.localHistoryTextView3);
        localHistoryString3 = Objects.requireNonNull(getIntent().getExtras()).getString("Value2");
        assert localHistoryString3 != null;
        if(!localHistoryString3.equals(""))textView3.setText(localHistoryString3);
    }
}
