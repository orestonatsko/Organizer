package ua.com.info.data;

/**
 * Створено v.m 01.08.2017.
 */


public class Login {
    private static IdName mUser;

    private static int multiUserMode;

    static final String ADMIN_SETUP = "AdminSetup";
    private static final int SINGLE_USER = 1;
    private static final int MULTI_USER = 2;

//    public static boolean isMultiUserMode() {
//        if (multiUserMode == 0) {
//            String s = DbHelper.getParameter("КодКористувача");
//            if (s == null)
//                multiUserMode = MULTI_USER;
//            else {
//                multiUserMode = SINGLE_USER;
//                mUser = new IDName();
//                mUser.ID = Integer.parseInt(s);
//                mUser.Name = DbHelper.getUserName(mUser.ID);
//            }
//        } else {
//            if (mUser==null){
//                String s = DbHelper.getParameter("КодКористувача");
//                if (s != null){
//                    mUser = new IDName();
//                    mUser.ID = Integer.parseInt(s);
//                    mUser.Name = DbHelper.getUserName(mUser.ID);
//                }
//            }
//        }
//        return multiUserMode == MULTI_USER;
//    }

    public static IdName getUser() {
        if (mUser == null) mUser = new IdName(1, "TestUser");
        return mUser;
    }

    public static int UserID() {
        return mUser.ID;
    }

//    public static void setMultiUserMode(boolean multiUser) {
//        multiUserMode = multiUser;
//        if (Debug.isDebuggerConnected()) {
//            int id = Utils.context.getResources().getInteger(R.integer.userID);
//            setUser(new IDName(id, "Тестовий Користувач"));
//        }
//    }

//    public static void changeUser(final Context context, DialogInterface.OnDismissListener listener) {
//        if (DbHelper.getIntParameter(ADMIN_SETUP, 0) == 1)
//            Message.prompt(context, "Перед використанням програми необхідно завести адміністратора системи", new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    setupAdmin(context);
//                }
//            });
//        else {
//            LoginDialog d = new LoginDialog(context);
//            d.setOnDismissListener(listener);
//            d.show();
//        }
//    }

//    private static void setupAdmin(Context context) {
//        Intent intent = new Intent(context, UserActivity.class);
//        intent.putExtra(ADMIN_SETUP, true);
//        context.startActivity(intent);
//    }

    static void setUser(IdName mUser) {
        Login.mUser = mUser;
    }

    public static boolean Role(int role) {
//        if (mUser==null) return false;
//        if (Utils.Debug) return true;
       boolean ret = false;
//        Cursor cursor = DbHelper.mdb.rawQuery("SELECT Role FROM Rights WHERE ParentID=" + mUser.ID + " AND Role=" + role, null);
//        if (cursor != null) {
//            ret = cursor.moveToFirst();
//            cursor.close();
//        }
        return ret;
    }

    public static void logout() {
        mUser = null;
    }
}
