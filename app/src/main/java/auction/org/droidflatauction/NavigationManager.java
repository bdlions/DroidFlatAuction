package auction.org.droidflatauction;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by Nazmul on 8/25/2017.
 */

public class NavigationManager {
    // Context
    Context _context;
    SessionManager session;

    public NavigationManager(Context context)
    {
        this._context = context;
        session = new SessionManager(context);
    }

    public void navigateTo(int id)
    {
        if (id == R.id.nav_dashboard) {
            Intent member_bashboard_intent = new Intent(_context, MemberDashboard.class);
            member_bashboard_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_bashboard_intent);
        } else if (id == R.id.nav_manage_advert) {
            Intent member_manage_advert_intent = new Intent(_context, ManageAdvertDashboard.class);
            member_manage_advert_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_manage_advert_intent);
        } else if (id == R.id.nav_message) {
            Intent member_message_intent = new Intent(_context, MessageDashboard.class);
            member_message_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_message_intent);
        } else if (id == R.id.nav_profile) {
            Intent member_bashboard_intent = new Intent(_context, ProfileDashboard.class);
            member_bashboard_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_bashboard_intent);
        }else if (id == R.id.nav_account_settings) {
            Intent member_account_settings_intent = new Intent(_context, AccountSettingsDashboard.class);
            member_account_settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_account_settings_intent);
        }else if (id == R.id.nav_search) {
            Intent member_account_settings_intent = new Intent(_context, MemberPropertySearch.class);
            member_account_settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(member_account_settings_intent);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }
    }
}
