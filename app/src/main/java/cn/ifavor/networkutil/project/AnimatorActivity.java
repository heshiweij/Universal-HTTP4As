package cn.ifavor.networkutil.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import cn.ifavor.networkutil.R;

public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnAnimator;
    private View target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        mBtnAnimator = (Button) findViewById(R.id.btn_animator);
        mBtnAnimator.setOnClickListener(this);

        target = findViewById(R.id.tv_test);

    }

    @Override
    public void onClick(View v) {
        baseAnimator();

    }

    /**
     * the basic animator
     */
    private void baseAnimator() {
        /*ObjectAnimator.ofFloat(target, "translationX", 0f, 100f).setDuration(2000).start();
        ObjectAnimator.ofFloat(target, "rotation", 0f, 350f).setDuration(1500).start();*/


        /*PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat( "translationX", 0f, 100f);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("rotation", 0f, 350);

        ObjectAnimator.ofPropertyValuesHolder(target, holder1,holder2).setDuration(1000).start();*/

        /*ObjectAnimator animator1 = ObjectAnimator.ofFloat(target, "translationX", 0f, 100f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,  "rotation", 0f, 350f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(target,  "alpha", 0f, 1f);*/

        //        AnimatorSet set = new AnimatorSet();
        //        set.playTogether(animator1, animator2); // 同时播放
        //        set.playSequentially(animator1, animator2); // 依次播放

        // 自定义顺序
        //        set.play(animator1).with(animator2); // 先同时执行 1、2
        //        set.play(animator3); // 再执行3
        //        set.start();


        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 350f);

        // 不使用类适配器，方法多
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                System.out.println("onAnimationStart end");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                System.out.println("onAnimationEnd end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                System.out.println("onAnimationCancel end");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                System.out.println("onAnimationRepeat end");
            }
        });

        // 使用类适配器的方式，简化监听接口
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                System.out.println("animation execute end");
            }
        });


        animator.setDuration(2000);
        // 设置差值器
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();


        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        //自定义估值器
        valueAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int startInt = startValue;
                return (int) (startInt + fraction * (endValue - startInt));
            }
        });

        // 使用系统提供的估值器
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setDuration(2000);
        valueAnimator.start();


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction(); // 获取百分数因子（无关ofInt(0,100);）
                Object value = animation.getAnimatedValue(); // 获取值（有关ofInt(0,100);）

                System.out.println(fraction);
            }
        });


    }
}
