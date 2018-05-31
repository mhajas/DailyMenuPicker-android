package soft.brunhilda.org.dailymenupicker

import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import soft.brunhilda.org.dailymenupicker.fragments.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import soft.brunhilda.org.dailymenupicker.notification.AlarmReceiver
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displaySelectedScreen(R.id.nav_restaurants) // First start TODO

        Hawk.init(this).build() // no idea where to put this

         /*Prevent to create notification again, when user open application via notification in the SAME MINUTE*/
        if(!intent.getBooleanExtra("fromNotification",false)){
            setUpNotification()
        }
    }

    fun displaySelectedScreen(id: Int){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when (id) {
            R.id.nav_restaurants -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, TodayAllRestaurantFragment.getInstance()).commit()
            }
            R.id.nav_today_all_foods -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, TodayAllFoodFragment.getInstance()).commit()
            }
            R.id.nav_favorite_ingredients -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, FavoriteIngredientsFragment.getInstance()).commit()
            }
            R.id.nav_favorite_restaurants -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, FavouriteRestaurantsFragment.getInstance()).commit()
            }
            R.id.nav_agenda -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, AgendaFragment.getInstance()).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Settings will be here",Toast.LENGTH_LONG).show()
                return true
        }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.itemId)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * This function configure every day notification
     * source: https://ptyagicodecamp.github.io/scheduling-repeating-local-notifications-using-alarm-manager.html
     * TODO implement to reaction when device was restarted (^^last section)
     */
    private fun setUpNotification(){
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 46) // TODO set up this values on the settings page

        val intent = Intent(this, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(this, AlarmReceiver.NOTIFICATION_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,  AlarmManager.INTERVAL_DAY, alarmIntent) //better for battery life
    }
}
