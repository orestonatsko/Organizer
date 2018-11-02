package ua.com.info.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Створив VM 01.08.2017.
 */

public class PageSelector extends RadioGroup implements ViewPager.OnPageChangeListener, RadioButton.OnCheckedChangeListener{

    public PageSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        if (isInEditMode()) createButtons(3);
    }

    private ViewPager pager;

    public void setPager(ViewPager pager) {
        this.pager = pager;
        pager.addOnPageChangeListener(this);
        int pageCount = pager.getAdapter().getCount();
        if (pageCount > 1) {
            createButtons(pageCount);
            onPageSelected(pager.getCurrentItem());
            for (int i = 0; i < getChildCount(); i++) {
                ((RadioButton) getChildAt(i)).setOnCheckedChangeListener(this);
            }
        }
    }

    private void createButtons(int count) {
        for (int i = 0; i < count; i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.page_indicator_button, this, true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private boolean checking;
    @Override
    public void onPageSelected(int position) {
        checking = true;
        ((RadioButton) getChildAt(position)).setChecked(true);
        checking = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (checking)return;
        if (isChecked)
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) == buttonView) pager.setCurrentItem(i, true);
            }
    }
}
