package srsen.martin.infinum.co.hw3_and_on.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import java.util.Random;

import srsen.martin.infinum.co.hw3_and_on.R;

public class CustomButton extends AppCompatButton {

    private double increaseRate = 1.;
    private double desreaseRate = 1.;

    boolean init;
    private int startingArea;
    Random random = new Random();

    private static final int BASE_COLOR = Color.YELLOW;
    private static final int Color1 = Color.RED;
    private static final int Color2 = Color.BLUE;
    private static final int Color3 = Color.GREEN;

    public CustomButton(Context context) {
        super(context);
        init(null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        setBackgroundColor(BASE_COLOR);
        setText("1.0");
        if(attrs == null)   return;

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        increaseRate = a.getFloat(R.styleable.CustomButton_increaseRate, 1.06f);
        desreaseRate = a.getFloat(R.styleable.CustomButton_decreaseRate, 0.96f);

        setOnClickListener(view -> {
            increase();
        });
    }

    private void increase(){
        if(!init){
            startingArea = getWidth() * getHeight();
            init = true;
        }

        setWidth((int) (getWidth()*increaseRate));
        setHeight((int) (getHeight() * increaseRate));
        refreshText();

        getHandler().postDelayed(() -> {
            setWidth((int) (getWidth()*desreaseRate));
            setHeight((int) (getHeight() * desreaseRate));
            refreshText();
        }, random.nextInt() % 4000);
    }

    private void refreshText(){
        double change = ((double)getWidth() * getHeight()) / startingArea;
        setText(String.format("%.3f", change));
        setColor();
    }

    private void setColor(){
        double scaling = Double.parseDouble(getText().toString());

        if(scaling < 4.0){
            setBackgroundColor(BASE_COLOR);
        }else if(scaling < 10.0){
            setBackgroundColor(Color1);
        }else if(scaling < 40.0){
            setBackgroundColor(Color2);
        }else {
            setBackgroundColor(Color3);
        }
    }
}
