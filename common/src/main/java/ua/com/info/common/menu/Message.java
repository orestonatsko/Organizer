package ua.com.info.common.menu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ua.com.info.common.R;

/**
 * Створив VM 12.08.2017.
 */

public class Message extends Dialog implements View.OnClickListener {

    private final String title;
    private final String msg;

    public boolean Confirmed;

    public Message(Context context, String title, String msg) {
        super(context);
        Confirmed = false;
        this.title = title;
        this.msg = msg;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        setTitle(title);
        TextView v = (TextView) findViewById(R.id.vMsg);
        v.setText(msg);
        findViewById(R.id.Ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.Confirmed = true;
        dismiss();
    }

    public static void show(Context context, String title, String msg) {
        new Message(context, title, msg).show();
    }

    public static void error(Context context, String msg) {
        new Message(context, "Помилка", msg).show();
    }

    public static void error(Context context, String msg, OnDismissListener listener) {
        Message m = new Message(context, "Помилка", msg);
        m.setOnDismissListener(listener);
        m.show();
    }

    public static void prompt(Context context, String msg, OnDismissListener listener) {
        Message message = new Message(context, "Повідомлення", msg);
        message.setOnDismissListener(listener);
        message.show();
    }

}