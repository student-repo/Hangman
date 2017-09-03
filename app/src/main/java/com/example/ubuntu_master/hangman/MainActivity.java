package com.example.ubuntu_master.hangman;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] wordSet = {"rankless", "sonnetising", "noninertial", "untheatrical", "minister", "undurable", "severeness", "repeatability", "archegonium", "southwestwards",
            "yankeefying", "subsidiary", "lingulate", "condoner", "frei", "creel", "ladyfy", "amphioxi", "deathless", "isochronized", "thermotherapy", "premire",
            "rancidity", "synchronizer", "australasian", "cabasset", "maralina", "yehudi", "upturn", "comines", "affinal", "electrocardiogram", "bala", "vee", "fordizing", "habenula",
            "diploid", "gorgon", "knotgrass", "nosewing", "antispiritualistic", "untenderly", "interinsurance", "miasmata", "pentasyllabism", "dolly", "cobaltine", "antinomianism",
            "macumba", "pale",};
    private String currentPassword;
    private int missNumber = 0;
    private int missLimit = 11;
    private int guessNumber = 0;
    private ArrayList<Character> checkedLetters = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawCurrentPassword();
        setPasswordFieds();
        setAlphabetRow('A', 'G', (LinearLayout)findViewById(R.id.alphabet_letters_row1));
        setAlphabetRow('H', 'M', (LinearLayout)findViewById(R.id.alphabet_letters_row2));
        setAlphabetRow('N', 'T', (LinearLayout)findViewById(R.id.alphabet_letters_row3));
        setAlphabetRow('U', 'Z', (LinearLayout)findViewById(R.id.alphabet_letters_row4));

        System.out.println(currentPassword);



    }

    private void drawCurrentPassword() {
        currentPassword = getRandomWord();
    }

    private void setAlphabetRow(final char firstLetter, char lastLetter, LinearLayout l) {
        TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, 1.0f);
        AutoResizeTextView aa;

        for(char alphabet = firstLetter; alphabet <= lastLetter;alphabet++) {
            aa = new AutoResizeTextView(this);
            aa.setLayoutParams(paramsExample);
            aa.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            aa.setTextSize(30);
            aa.setText(String.valueOf(alphabet));
            aa.setId(alphabet);
            aa.setOnClickListener(
                    new AutoResizeTextView.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            handleOnClickLetter(v);
                        }
                    }
            );
            l.addView(aa);
        }
    }

    private void handleOnClickLetter(View v) {
        AutoResizeTextView a = (AutoResizeTextView)findViewById(v.getId());
        a.setTextColor(Color.DKGRAY);
        ArrayList<Integer> lettersToShow = getCharIndexesFromPassword(Character.toLowerCase((char)v.getId()));
        if(checkedLetters.contains((char)v.getId())){
            ;
        }
        else if(!lettersToShow.isEmpty()){
            updatePasswordField(lettersToShow);
            if(guessNumber >= currentPassword.length()){
                LinearLayout ll = (LinearLayout)findViewById(R.id.main_linear_layout);
                LinearLayout lll = (LinearLayout)findViewById(R.id.round_finish_linear_layout);
                AutoResizeTextView aaaa = (AutoResizeTextView)findViewById(R.id.finish_text_view);
                aaaa.setText(getString(R.string.congratulations_you_won) + " " +currentPassword);

                ll.setVisibility(View.GONE);
                lll.setVisibility(View.VISIBLE);
            }
        }
        else{
            handleMiss();
        }
        checkedLetters.add((char)v.getId());
    }

    private void handleMiss() {
        missNumber ++;
        ImageView hangmanImage = (ImageView)findViewById(R.id.hangman);

        String uri = "@drawable/step" + missNumber;

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getDrawable(imageResource);
        hangmanImage.setImageDrawable(res);

        if(missNumber >= missLimit){
            LinearLayout ll = (LinearLayout)findViewById(R.id.main_linear_layout);
            LinearLayout lll = (LinearLayout)findViewById(R.id.round_finish_linear_layout);
            AutoResizeTextView aaaa = (AutoResizeTextView)findViewById(R.id.finish_text_view);
            aaaa.setText(getString(R.string.you_lose) + " " +currentPassword);

            ll.setVisibility(View.GONE);
            lll.setVisibility(View.VISIBLE);
        }
    }

    private void updatePasswordField(ArrayList<Integer> lettersToShow) {
        for(int i = 0; i < lettersToShow.size(); i++){
            guessNumber++;
            AutoResizeTextView aa = (AutoResizeTextView)findViewById(lettersToShow.get(i));
            aa.setText(String.valueOf(Character.toUpperCase(currentPassword.charAt(lettersToShow.get(i)))));
        }
    }

    private void setPasswordFieds() {
        LinearLayout lPassword = (LinearLayout)findViewById(R.id.password);
        TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, 1.0f);
        AutoResizeTextView aa;
        for(int i = 0; i < currentPassword.length(); i++){
            aa = new AutoResizeTextView(this);
            aa.setLayoutParams(paramsExample);
            aa.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            aa.setTextSize(30);
            aa.setText("_");
            aa.setId(i);
            lPassword.addView(aa);
        }
    }

    private String getRandomWord() {
        int rnd = new Random().nextInt(wordSet.length);
        return wordSet[rnd];
    }
    
    private ArrayList<Integer> getCharIndexesFromPassword(char letter){
        ArrayList<Integer> a = new ArrayList<>();
        int index = currentPassword.indexOf(letter);
        while (index >= 0) {
            a.add(index);
            index = currentPassword.indexOf(letter, index + 1);
        }
        return a;
    }

    public void handlePlayAgainButton(View view) {
        LinearLayout ll = (LinearLayout)findViewById(R.id.main_linear_layout);
        LinearLayout lll = (LinearLayout)findViewById(R.id.round_finish_linear_layout);
        LinearLayout lPassword = (LinearLayout)findViewById(R.id.password);
        lPassword.removeAllViews();

        ll.setVisibility(View.VISIBLE);
        lll.setVisibility(View.GONE);

        drawCurrentPassword();
        setPasswordFieds();
        resetKeyboard('A', 'G');
        resetKeyboard('H', 'M');
        resetKeyboard('N', 'T');
        resetKeyboard('U', 'Z');

        missNumber = 0;
        missLimit = 11;
        checkedLetters = new ArrayList<Character>();
        guessNumber = 0;
        ImageView hangmanImage = (ImageView)findViewById(R.id.hangman);
        hangmanImage.setImageResource(R.drawable.dark_background);
        System.out.println(currentPassword);

    }

    private void resetKeyboard(char firstLetter, char lastLetter){
        AutoResizeTextView aa;

        for(char alphabet = firstLetter; alphabet <= lastLetter;alphabet++) {
            aa = (AutoResizeTextView)findViewById(alphabet);
            aa.setTextColor(Color.WHITE);
        }
    }

    public void handleQuitButton(View view) {
        System.exit(1);
    }
}
