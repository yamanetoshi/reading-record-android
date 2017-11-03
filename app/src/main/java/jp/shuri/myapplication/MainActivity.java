package jp.shuri.myapplication;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private String mComma = "、、、、、、、、、、、、、、、、";

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText)findViewById(R.id.sfenstring);

        Button btn = (Button)findViewById(R.id.execbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechText();
            }
        });

        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.shutdown();
        }
    }

    private boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String convertKoma(String str, boolean isNari) {
        String ret = "";
        String tmp = str.toLowerCase();

        if (tmp.equals("p")) {
            if (isNari) {
                ret = "と";
            } else {
                ret = "ふ";
            }
        } else if (tmp.equals("l")) {
            if (isNari) {
                ret = "なりきょう";
            } else {
                ret = "きょう";
            }
        } else if (tmp.equals("n")) {
            if (isNari) {
                ret = "なりけい";
            } else {
                ret = "けい";
            }
        } else if (tmp.equals("s")) {
            if (isNari) {
                ret = "なりぎん";
            } else {
                ret = "ぎん";
            }
        } else if (tmp.equals("g")) {
            ret = "きん";
        } else if (tmp.equals("k")) {
            ret = "ぎょく";
        } else if (tmp.equals("b")) {
            if (isNari) {
                ret = "うま";
            } else {
                ret = "かく";
            }
        } else if (tmp.equals("r")) {
            if (isNari) {
                ret = "りゅう";
            } else {
                ret = "飛車";
            }
        }

        return ret;
    }

    private String getBlank() {
        return mComma;
    }

    private String getPositionNum(int num) {
        String ret = "";

        if (num == 1) {
            ret = "いち";
        } else if (num == 2) {
            ret = "に";
        } else if (num == 3) {
            ret = "さん";
        } else if (num == 4) {
            ret = "よん";
        } else if (num == 5) {
            ret = "ご";
        } else if (num == 6) {
            ret = "ろく";
        } else if (num == 7) {
            ret = "なな";
        } else if (num == 8) {
            ret = "はち";
        } else if (num == 9) {
            ret = "きゅう";
        }

        return ret;
    }

    private String[] sfenSplit;

    private String[] mochigoma;
    private String[] haichi;
    private String speechStr;

    private String sfen;
    private boolean isNari;

    private void speechPosition(boolean isGyokuKata) {

        int row = 1;
        int column = 9;
        int sfenIndex = 0;
        speechStr = "";

        for (; sfenIndex < sfenSplit[0].length(); sfenIndex++) {

            if (haichi[sfenIndex].equals("")) {
                continue;
            }

            if (haichi[sfenIndex].equals("/")) {
                row++;
                column = 9;
                continue;
            }

            if (isNumber(haichi[sfenIndex])) {
                int num = Integer.parseInt(haichi[sfenIndex]);
                column -= num;
            }

            if (haichi[sfenIndex].equals("+")) {
                isNari = true;
            }

            Character tmp = haichi[sfenIndex].charAt(0);

            if (Character.isUpperCase(haichi[sfenIndex].charAt(0))) {
                if (!isGyokuKata) {
                    speechStr += getPositionNum(column);
                    speechStr += getBlank();
                    speechStr += getPositionNum(row);
                    speechStr += getBlank();
                    speechStr += convertKoma(haichi[sfenIndex], isNari);
                    speechStr += getBlank();
                    speechStr += getBlank();

                    tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);
                    speechStr = "";
                }

                isNari = false;
                column--;
            }

            if (Character.isLowerCase(haichi[sfenIndex].charAt(0))) {
                if (isGyokuKata) {
                    speechStr += getPositionNum(column);
                    speechStr += getBlank();
                    speechStr += getPositionNum(row);
                    speechStr += getBlank();
                    speechStr += convertKoma(haichi[sfenIndex], isNari);
                    speechStr += getBlank();
                    speechStr += getBlank();

                    tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);
                    speechStr = "";
                }

                isNari = false;
                column--;
            }
        }

    }

    private void speechMochigoma() {
        int sfenIndex = 0;
        int num = 0;
        speechStr = "";

        for (; sfenIndex < sfenSplit[2].length(); sfenIndex++) {
            if (mochigoma[sfenIndex].equals("")) {
                continue;
            }

            if (isNumber(mochigoma[sfenIndex])) {
                num = Integer.parseInt(mochigoma[sfenIndex]);
            }

            if (Character.isUpperCase(mochigoma[sfenIndex].charAt(0))) {
                speechStr = convertKoma(mochigoma[sfenIndex], false);
                if (num != 0) {
                    speechStr += getPositionNum(num);
                    num = 0;
                }

                tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);
                speechStr = "";
            }
        }
    }

    private void speechKoma() {

        isNari = false;

        if (sfen.equals("")) {
//        sfen = "6sks/9/7S1/9/9/9/9/9/9 w SP2r2b4g4n4l17p 1";
            sfen = "8n/6S2/8k/5Br2/8p/9/9/9/9 w RG2Lb3g3s3n2l17p 1";
        }

        sfenSplit = sfen.split(" ");
        haichi = sfenSplit[0].split("");
        mochigoma = sfenSplit[2].split("");
        speechStr = "ぎょくかた";

        tts.speak(speechStr, TextToSpeech.QUEUE_FLUSH, null, speechStr);

        speechPosition(true);

        speechStr = "攻め方";

        tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);

        speechPosition(false);

        speechStr = "持ち駒は";

        tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);

        speechMochigoma();

        speechStr = "です";

        tts.speak(speechStr, TextToSpeech.QUEUE_ADD, null, speechStr);
    }

    private void speechText() {
/*
        if (mEditText.getText().toString().equals("")) {
            Toast.makeText(this, "SFEN 未入力", Toast.LENGTH_LONG).show();
            return;
        }
*/
        sfen = mEditText.getText().toString();

        if (tts.isSpeaking()) {
            tts.stop();
        }
        speechKoma();
    }

    @Override
    public void onInit(int i) {
        if (TextToSpeech.SUCCESS == i) {
            Locale locale = Locale.JAPAN;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
            } else {
                Log.d("", "Error SetLocale");
            }
        } else {
            Log.d("", "Error Init");
        }
    }
}
